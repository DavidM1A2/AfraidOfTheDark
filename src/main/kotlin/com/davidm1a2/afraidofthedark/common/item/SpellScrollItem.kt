package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.capabilities.hasStartedAOTD
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

class SpellScrollItem : AOTDItem("spell_scroll", Properties().stacksTo(1)) {
    override fun use(world: World, playerEntity: PlayerEntity, hand: Hand): ActionResult<ItemStack> {
        val itemStack = playerEntity.getItemInHand(hand)

        if (!playerEntity.hasStartedAOTD()) {
            if (!world.isClientSide) {
                playerEntity.sendMessage(TranslationTextComponent(LocalizationConstants.DONT_UNDERSTAND))
            }
            return ActionResult.fail(itemStack)
        }

        val spell = getSpell(itemStack)
        if (spell == null) {
            if (!world.isClientSide) {
                playerEntity.sendMessage(TranslationTextComponent("message.afraidofthedark.spell_scroll.empty"))
            }
            return ActionResult.pass(itemStack)
        }

        val uses = getUses(itemStack)
        if (uses < 1) {
            if (!world.isClientSide) {
                playerEntity.sendMessage(TranslationTextComponent("message.afraidofthedark.spell_scroll.no_uses_left"))
            }
            itemStack.shrink(1)
            return ActionResult.success(itemStack)
        }

        val newUses = if (playerEntity.isCreative) {
            uses
        } else {
            uses - 1
        }
        setUses(itemStack, newUses)
        if (!world.isClientSide) {
            if (newUses == 0) {
                itemStack.shrink(1)
            }
            spell.attemptToCast(playerEntity, isSpellScroll = true)
        }

        return ActionResult.success(itemStack)
    }

    override fun getDurabilityForDisplay(stack: ItemStack): Double {
        return if (isEmpty(stack)) {
            0.0
        } else {
            val uses = getUses(stack)
            val maxUses = getMaxUses(stack)
            if (maxUses == 0 || uses > maxUses || uses < 0) {
                0.0
            } else {
                1 - (uses.toDouble() / maxUses.toDouble())
            }
        }
    }

    override fun isFoil(stack: ItemStack): Boolean {
        return !isEmpty(stack)
    }

    override fun showDurabilityBar(stack: ItemStack): Boolean {
        return !isEmpty(stack)
    }

    fun setSpell(itemStack: ItemStack, spell: Spell) {
        NBTHelper.setCompound(itemStack, NBT_SPELL, spell.serializeNBT())
        itemStack.hoverName = TranslationTextComponent("tooltip.afraidofthedark.spell_scroll.spell_name", spell.name)
    }

    fun getSpell(itemStack: ItemStack): Spell? {
        return NBTHelper.getCompound(itemStack, NBT_SPELL)?.let { Spell(it) }
    }

    fun isEmpty(itemStack: ItemStack): Boolean {
        return !NBTHelper.hasTag(itemStack, NBT_SPELL) || !NBTHelper.hasTag(itemStack, NBT_USES) || !NBTHelper.hasTag(itemStack, NBT_MAX_USES)
    }

    fun setUses(itemStack: ItemStack, uses: Int) {
        NBTHelper.setInteger(itemStack, NBT_USES, uses)
    }

    fun getUses(itemStack: ItemStack): Int {
        return NBTHelper.getInteger(itemStack, NBT_USES) ?: 0
    }

    fun setMaxUses(itemStack: ItemStack, uses: Int) {
        NBTHelper.setInteger(itemStack, NBT_MAX_USES, uses)
    }

    fun getMaxUses(itemStack: ItemStack): Int {
        return NBTHelper.getInteger(itemStack, NBT_MAX_USES) ?: 1
    }

    override fun appendHoverText(itemStack: ItemStack, world: World?, tooltip: MutableList<ITextComponent>, iTooltipFlag: ITooltipFlag) {
        val player = Minecraft.getInstance().player

        if (player != null && player.hasStartedAOTD()) {
            val spell = getSpell(itemStack)
            if (spell == null) {
                tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.spell_scroll.empty"))
            } else {
                tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.spell_scroll.spell_name", spell.name))
                tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.spell_scroll.uses_remaining", getUses(itemStack)))
                for ((index, spellStage) in spell.spellStages.withIndex()) {
                    tooltip.add(
                        TranslationTextComponent(
                            "tooltip.afraidofthedark.spell_scroll.spell_stage",
                            index + 1,
                            spellStage.deliveryInstance?.component?.getName() ?: "-",
                            spellStage.effects[0]?.component?.getName() ?: "-",
                            spellStage.effects[1]?.component?.getName() ?: "-",
                            spellStage.effects[2]?.component?.getName() ?: "-",
                            spellStage.effects[3]?.component?.getName() ?: "-"
                        )
                    )
                }
            }
        } else {
            tooltip.add(TranslationTextComponent(LocalizationConstants.DONT_UNDERSTAND))
        }
    }

    companion object {
        private const val NBT_SPELL = "spell"
        private const val NBT_USES = "uses"
        private const val NBT_MAX_USES = "max_uses"
    }
}