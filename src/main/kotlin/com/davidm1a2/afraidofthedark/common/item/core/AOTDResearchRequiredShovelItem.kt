package com.davidm1a2.afraidofthedark.common.item.core

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.research.Research
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.block.BlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.IItemTier
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World

abstract class AOTDResearchRequiredShovelItem(
    baseName: String,
    toolMaterial: IItemTier,
    baseDamage: Float,
    attackSpeedMultiplier: Float,
    protected val requiredResearch: Research,
    properties: Properties,
    displayInCreative: Boolean = true
) : AOTDShovelItem(baseName, toolMaterial, baseDamage, attackSpeedMultiplier, properties, displayInCreative) {
    override fun canAttackBlock(blockState: BlockState, world: World, blockPos: BlockPos, player: PlayerEntity): Boolean {
        return if (player.getResearch().isResearched(requiredResearch)) {
            super.canAttackBlock(blockState, world, blockPos, player)
        } else {
            if (!world.isClientSide) {
                player.sendMessage(TranslationTextComponent(LocalizationConstants.DONT_UNDERSTAND))
            }
            false
        }
    }

    override fun onLeftClickEntity(stack: ItemStack, player: PlayerEntity, target: Entity): Boolean {
        if (!player.getResearch().isResearched(requiredResearch)) {
            return true
        }

        return super.onLeftClickEntity(stack, player, target)
    }

    override fun appendHoverText(itemStack: ItemStack, world: World?, tooltip: MutableList<ITextComponent>, iTooltipFlag: ITooltipFlag) {
        val player = Minecraft.getInstance().player

        if (player != null && !player.getResearch().isResearched(requiredResearch)) {
            tooltip.add(TranslationTextComponent(LocalizationConstants.TOOLTIP_DONT_KNOW_HOW_TO_USE))
        }
    }
}