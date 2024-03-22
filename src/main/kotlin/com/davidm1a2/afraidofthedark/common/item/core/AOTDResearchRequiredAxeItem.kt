package com.davidm1a2.afraidofthedark.common.item.core

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.research.Research
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.block.BlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.item.IItemTier
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUseContext
import net.minecraft.util.ActionResultType
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.level.Level

abstract class AOTDResearchRequiredAxeItem(
    baseName: String,
    toolMaterial: IItemTier,
    baseDamage: Float,
    attackSpeedMultiplier: Float,
    protected val requiredResearch: Research,
    properties: Properties,
    displayInCreative: Boolean = true
) : AOTDAxeItem(baseName, toolMaterial, baseDamage, attackSpeedMultiplier, properties, displayInCreative) {
    override fun useOn(context: ItemUseContext): ActionResultType {
        val player = context.player
        if (player != null) {
            if (player.getResearch().isResearched(requiredResearch)) {
                return super.useOn(context)
            } else {
                if (!context.level.isClientSide) {
                    player.sendMessage(TranslationTextComponent(LocalizationConstants.DONT_UNDERSTAND))
                }
            }
        }
        return ActionResultType.FAIL
    }

    override fun canAttackBlock(blockState: BlockState, world: Level, blockPos: BlockPos, player: Player): Boolean {
        return if (player.getResearch().isResearched(requiredResearch)) {
            super.canAttackBlock(blockState, world, blockPos, player)
        } else {
            if (!world.isClientSide) {
                player.sendMessage(TranslationTextComponent(LocalizationConstants.DONT_UNDERSTAND))
            }
            false
        }
    }

    override fun onLeftClickEntity(stack: ItemStack, player: Player, target: Entity): Boolean {
        if (!player.getResearch().isResearched(requiredResearch)) {
            return true
        }

        return super.onLeftClickEntity(stack, player, target)
    }

    override fun appendHoverText(itemStack: ItemStack, world: Level?, tooltip: MutableList<ITextComponent>, iTooltipFlag: ITooltipFlag) {
        val player = Minecraft.getInstance().player

        if (player != null && !player.getResearch().isResearched(requiredResearch)) {
            tooltip.add(TranslationTextComponent(LocalizationConstants.TOOLTIP_DONT_KNOW_HOW_TO_USE))
        }
    }
}