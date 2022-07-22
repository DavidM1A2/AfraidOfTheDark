package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellManager
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World
import kotlin.math.ceil

/**
 * Item representing a wand used to cast spells with an item
 *
 * @constructor sets up item properties
 */
class WandItem : AOTDItem("wand", Properties().stacksTo(1)) {
    /**
     * Called when the item is right clicked with a hand
     *
     * @param world  The world the item was right clicked in
     * @param player The player that right clicked
     * @param hand   The hand the item is in
     * @return The result of the right click
     */
    override fun use(world: World, player: PlayerEntity, hand: Hand): ActionResult<ItemStack> {
        // Grab the held itemstack
        val heldItem = player.getItemInHand(hand)
        // Grab the player's spell manager
        val spellManager = player.getSpellManager()

        // If the player is sneaking switch spells, otherwise cast the spell
        if (player.isCrouching) {
            // Grab the spell bound to this wand
            val currentSpell = getSpell(heldItem)
            if (currentSpell == null) {
                // Set the spell to the player's first spell
                setSpellToFirstAvailable(player, heldItem)
            } else {
                // Grab the spell list
                val spells = spellManager.getSpells()
                // TODO: Names are not unique, but it sorta kinda works for now. Fix this once we have a better system
                val currentSpellIndex = spells.indexOfFirst { it.name == currentSpell.name }

                // If our list has another spell after ours store that one
                if (currentSpellIndex + 1 < spells.size) {
                    val next = spells[currentSpellIndex + 1]
                    setSpell(heldItem, next)
                    // Send the message server side
                    if (!world.isClientSide) {
                        player.sendMessage(
                            TranslationTextComponent(
                                "message.afraidofthedark:wand.spell_set",
                                next.name
                            )
                        )
                    }
                } else {
                    setSpellToFirstAvailable(player, heldItem)
                }
            }
        } else {
            // Server side processing only
            if (!world.isClientSide) {
                // Grab the spell that's on the item
                val toCast = getSpell(heldItem)
                if (toCast != null) {
                    toCast.attemptToCast(player)
                } else {
                    player.sendMessage(TranslationTextComponent("message.afraidofthedark.wand.no_bound_spell"))
                }
            }
        }

        // Fail the result so that
        return ActionResult.fail(heldItem)
    }

    /**
     * Sets the itemstacks spell UUID to the first one the player has
     *
     * @param entityPlayer The player who has spells
     * @param itemStack    The itemstack to set the first spell on
     */
    private fun setSpellToFirstAvailable(entityPlayer: PlayerEntity, itemStack: ItemStack) {
        // Grab the player's spell manager
        val spellManager = entityPlayer.getSpellManager()

        // If they have at least one spell grab it
        if (spellManager.getSpells().isNotEmpty()) {
            val first = spellManager.getSpells()[0]

            // Server side sending only, tell the player the spell was updated
            if (!entityPlayer.level.isClientSide) {
                entityPlayer.sendMessage(TranslationTextComponent("message.afraidofthedark.wand.spell_set", first.name))
            }

            // Set the NBT spell
            setSpell(itemStack, first)
        } else {
            // Server side sending only, tell the player he/she has no spells to bind to the wand yet
            if (!entityPlayer.level.isClientSide) {
                entityPlayer.sendMessage(TranslationTextComponent("message.afraidofthedark.wand.no_spells"))
            }
        }
    }

    /**
     * True if the itemstack has a spell NBT tag, false otherwise
     *
     * @param itemStack The itemstack to test
     * @return True if the itemstack has a spell, false otherwise
     */
    private fun hasSpell(itemStack: ItemStack): Boolean {
        return NBTHelper.hasTag(itemStack, NBT_SPELL)
    }

    /**
     * Sets the spell of the itemstack
     *
     * @param itemStack The itemstack to set the spell on
     * @param spell The new spell to store
     */
    private fun setSpell(itemStack: ItemStack, spell: Spell) {
        NBTHelper.setCompound(itemStack, NBT_SPELL, spell.serializeNBT())
    }

    /**
     * Gets the spell off of the itemstack or null if it does not exist
     *
     * @param itemStack The itemstack to get the spell from
     * @return The spell on the itemstack or null if it doesn't exist
     */
    private fun getSpell(itemStack: ItemStack): Spell? {
        return NBTHelper.getCompound(itemStack, NBT_SPELL)?.let { Spell(it) }
    }

    /**
     * Called to add a tooltip to the journal.
     *
     * @param stack   The itemstack to add information about
     * @param world The world that the item was hovered over in
     * @param tooltip The tooltip that we need to fill out
     * @param flag  The flag telling us if we should show advanced or normal tooltips
     */
    override fun appendHoverText(stack: ItemStack, world: World?, tooltip: MutableList<ITextComponent>, flag: ITooltipFlag) {
        val player = Minecraft.getInstance().player

        // Need to test if player is null during client init
        if (player != null) {
            // Test if the wand has a spell ID
            if (hasSpell(stack)) {
                // Grab the spell by ID
                val spell = getSpell(stack)

                // If the spell is non-null show the spell's stats
                if (spell != null) {
                    tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.wand.spell_name", spell.name))
                    tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.wand.spell_cost", ceil(spell.getCost())))
                } else {
                    tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.wand.spell_invalid"))
                }
            } else {
                tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.wand.no_spell"))
                tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.wand.set_spell"))
            }
        } else {
            tooltip.add(TranslationTextComponent(LocalizationConstants.TOOLTIP_DONT_KNOW_HOW_TO_USE))
        }
    }

    companion object {
        // NBT compound for spell
        private const val NBT_SPELL = "spell"
    }
}