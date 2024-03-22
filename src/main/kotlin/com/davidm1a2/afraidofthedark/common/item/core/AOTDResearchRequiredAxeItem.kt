package com.davidm1a2.afraidofthedark.common.item.core

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.research.Research
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

abstract class AOTDResearchRequiredAxeItem(
    baseName: String,
    toolMaterial: Tier,
    baseDamage: Float,
    attackSpeedMultiplier: Float,
    protected val requiredResearch: Research,
    properties: Properties,
    displayInCreative: Boolean = true
) : AOTDAxeItem(baseName, toolMaterial, baseDamage, attackSpeedMultiplier, properties, displayInCreative) {
    override fun useOn(context: UseOnContext): InteractionResult {
        val player = context.player
        if (player != null) {
            if (player.getResearch().isResearched(requiredResearch)) {
                return super.useOn(context)
            } else {
                if (!context.level.isClientSide) {
                    player.sendMessage(TranslatableComponent(LocalizationConstants.DONT_UNDERSTAND))
                }
            }
        }
        return InteractionResult.FAIL
    }

    override fun canAttackBlock(blockState: BlockState, world: Level, blockPos: BlockPos, player: Player): Boolean {
        return if (player.getResearch().isResearched(requiredResearch)) {
            super.canAttackBlock(blockState, world, blockPos, player)
        } else {
            if (!world.isClientSide) {
                player.sendMessage(TranslatableComponent(LocalizationConstants.DONT_UNDERSTAND))
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

    override fun appendHoverText(itemStack: ItemStack, world: Level?, tooltip: MutableList<Component>, iTooltipFlag: TooltipFlag) {
        val player = Minecraft.getInstance().player

        if (player != null && !player.getResearch().isResearched(requiredResearch)) {
            tooltip.add(TranslatableComponent(LocalizationConstants.TOOLTIP_DONT_KNOW_HOW_TO_USE))
        }
    }
}