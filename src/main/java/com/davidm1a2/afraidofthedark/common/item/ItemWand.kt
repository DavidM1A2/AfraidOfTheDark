package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellManager
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.*

/**
 * Item representing a wand used to cast spells with an item
 *
 * @constructor sets up item properties
 */
class ItemWand : AOTDItem("wand")
{
    init
    {
        // They don't stack
        setMaxStackSize(1)
    }

    /**
     * Called when the item is right clicked with a hand
     *
     * @param world  The world the item was right clicked in
     * @param player The player that right clicked
     * @param hand   The hand the item is in
     * @return The result of the right click
     */
    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack>
    {
        // Grab the held itemstack
        val heldItem = player.getHeldItem(hand)
        // Grab the player's spell manager
        val spellManager = player.getSpellManager()

        // If the player is sneaking switch spells, otherwise cast the spell
        if (player.isSneaking)
        {
            // If the item has no spell ID yet grab the player's first spell id and bind it
            if (!hasSpellId(heldItem))
            {
                // Set the spell id to the first player's spell
                setSpellToFirstAvailable(player, heldItem)
            }
            else
            {
                // Grab the current spell ID
                val spellId = getSpellId(heldItem)
                // Grab the spell bound to this ID
                val currentSpell = spellManager.getSpellById(spellId!!)
                // If the spell was deleted set the spell to the player's first available spell
                if (currentSpell == null)
                {
                    setSpellToFirstAvailable(player, heldItem)
                }
                else
                {
                    // Grab the spell list
                    val spells = spellManager.getSpells()
                    val currentSpellIndex = spells.indexOfFirst { it.id == spellId }

                    // If our list has another spell after ours store that one
                    if (currentSpellIndex + 1 < spells.size)
                    {
                        val next = spells[currentSpellIndex + 1]
                        setSpellId(heldItem, next.id)
                        // Send the message server side
                        if (!world.isRemote)
                        {
                            player.sendMessage(TextComponentTranslation("aotd.wand.spell_set", next.name))
                        }
                    }
                    else
                    {
                        setSpellToFirstAvailable(player, heldItem)
                    }
                }
            }
        }
        else
        {
            // Server side processing only
            if (!world.isRemote)
            {
                // Grab the spell that's on the item
                val spellId = getSpellId(heldItem)
                if (spellId != null)
                {
                    val toCast = spellManager.getSpellById(spellId)
                    // If the spell is null print an error, otherwise try and cast the spell
                    if (toCast != null)
                    {
                        toCast.attemptToCast(player)
                    }
                    else
                    {
                        player.sendMessage(TextComponentTranslation("aotd.wand.invalid_spell"))
                    }
                }
                else
                {
                    player.sendMessage(TextComponentTranslation("aotd.wand.no_bound_spell"))
                }
            }
        }
        return ActionResult(EnumActionResult.SUCCESS, heldItem)
    }

    /**
     * Sets the itemstacks spell UUID to the first one the player has
     *
     * @param entityPlayer The player who has spells
     * @param itemStack    The itemstack to set the first spell on
     */
    private fun setSpellToFirstAvailable(entityPlayer: EntityPlayer, itemStack: ItemStack)
    {
        // Grab the player's spell manager
        val spellManager = entityPlayer.getSpellManager()

        // If they have at least one spell grab it
        if (spellManager.getSpells().isNotEmpty())
        {
            val first = spellManager.getSpells()[0]

            // Server side sending only, tell the player the spell was updated
            if (!entityPlayer.world.isRemote)
            {
                entityPlayer.sendMessage(TextComponentTranslation("aotd.wand.spell_set", first.name))
            }

            // Set the NBT spell ID
            setSpellId(itemStack, first.id)
        }
        else
        {
            // Server side sending only, tell the player he/she has no spells to bind to the wand yet
            if (!entityPlayer.world.isRemote)
            {
                entityPlayer.sendMessage(TextComponentTranslation("aotd.wand.no_spells"))
            }
        }
    }

    /**
     * True if the itemstack has a spell ID NBT tag, false otherwise
     *
     * @param itemStack The itemstack to test
     * @return True if the itemstack has a spell ID, false otherwise
     */
    private fun hasSpellId(itemStack: ItemStack): Boolean
    {
        return NBTHelper.hasTag(itemStack, NBT_SPELL_ID)
    }

    /**
     * Sets the spell ID of the itemstack
     *
     * @param itemStack The itemstack to set the spell id on
     * @param spellId   The new spell id to USE
     */
    private fun setSpellId(itemStack: ItemStack, spellId: UUID)
    {
        NBTHelper.setCompound(itemStack, NBT_SPELL_ID, NBTUtil.createUUIDTag(spellId))
    }

    /**
     * Gets the spell id off of the itemstack or null if it does not exist
     *
     * @param itemStack The itemstack to get the spell id from
     * @return The spell id on the itemstack or null if it doesn't exist
     */
    private fun getSpellId(itemStack: ItemStack): UUID?
    {
        val uuidNBT = NBTHelper.getCompound(itemStack, NBT_SPELL_ID)
        return uuidNBT?.let { NBTUtil.getUUIDFromTag(it) }
    }

    /**
     * Called to add a tooltip to the journal.
     *
     * @param stack   The itemstack to add information about
     * @param world The world that the item was hovered over in
     * @param tooltip The tooltip that we need to fill out
     * @param flag  The flag telling us if we should show advanced or normal tooltips
     */
    @SideOnly(Side.CLIENT)
    override fun addInformation(stack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag)
    {
        val player = Minecraft.getMinecraft().player

        // Need to test if player is null during client init
        if (player != null)
        {
            // Test if the wand has a spell ID
            if (hasSpellId(stack))
            {
                // Grab the spell by ID
                val spell = player.getSpellManager().getSpellById(getSpellId(stack)!!)

                // If the spell is non-null show the spell's stats
                if (spell != null)
                {
                    tooltip.add("Spell: ${spell.name}")
                    tooltip.add("Cost: ${spell.getCost()}")
                }
                else
                {
                    tooltip.add("Spell on wand is invalid.")
                }
            }
            else
            {
                tooltip.add("Wand does not have a spell bound yet.")
                tooltip.add("Do so with crouch & right click.")
            }
        }
    }

    companion object
    {
        // NBT compound for spell id
        private const val NBT_SPELL_ID = "spell_id"
    }
}