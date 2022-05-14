package com.davidm1a2.afraidofthedark.common.item.starmetal

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.item.core.AOTDShovelItem
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.block.BlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World

class StarMetalShovelItem : AOTDShovelItem("star_metal_shovel", ModToolMaterials.STAR_METAL, 1.5f, -3f, Properties()) {
    override fun canAttackBlock(blockState: BlockState, world: World, blockPos: BlockPos, player: PlayerEntity): Boolean {
        return if (player.getResearch().isResearched(ModResearches.STAR_METAL)) {
            super.canAttackBlock(blockState, world, blockPos, player)
        } else {
            if (!world.isClientSide) {
                player.sendMessage(TranslationTextComponent(LocalizationConstants.DONT_UNDERSTAND))
            }
            false
        }
    }

    override fun canBeDepleted(): Boolean {
        return false
    }

    override fun isEnchantable(itemStack: ItemStack): Boolean {
        return true
    }

    override fun appendHoverText(itemStack: ItemStack, world: World?, tooltip: MutableList<ITextComponent>, iTooltipFlag: ITooltipFlag) {
        val player = Minecraft.getInstance().player

        if (player != null && player.getResearch().isResearched(ModResearches.STAR_METAL)) {
            tooltip.add(TranslationTextComponent(LocalizationConstants.TOOLTIP_MAGIC_ITEM_NEVER_BREAK))
        } else {
            tooltip.add(TranslationTextComponent(LocalizationConstants.TOOLTIP_DONT_KNOW_HOW_TO_USE))
        }
    }
}