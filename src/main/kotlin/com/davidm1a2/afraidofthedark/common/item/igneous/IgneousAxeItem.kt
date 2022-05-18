package com.davidm1a2.afraidofthedark.common.item.igneous

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.item.core.AOTDAxeItem
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.block.BlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUseContext
import net.minecraft.util.ActionResultType
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World

class IgneousAxeItem : AOTDAxeItem("igneous_axe", ModToolMaterials.IGNEOUS, 5.0f, -3.0f, Properties()) {
    override fun useOn(context: ItemUseContext): ActionResultType {
        val player = context.player
        if (player != null) {
            if (player.getResearch().isResearched(ModResearches.SILVER_SLAYER)) {
                return super.useOn(context)
            } else {
                if (!context.level.isClientSide) {
                    player.sendMessage(TranslationTextComponent(LocalizationConstants.DONT_UNDERSTAND))
                }
            }
        }
        return ActionResultType.FAIL
    }

    override fun canAttackBlock(blockState: BlockState, world: World, blockPos: BlockPos, player: PlayerEntity): Boolean {
        return if (player.getResearch().isResearched(ModResearches.IGNEOUS)) {
            super.canAttackBlock(blockState, world, blockPos, player)
        } else {
            if (!world.isClientSide) {
                player.sendMessage(TranslationTextComponent(LocalizationConstants.DONT_UNDERSTAND))
            }
            false
        }
    }

    override fun onLeftClickEntity(stack: ItemStack, player: PlayerEntity, target: Entity): Boolean {
        if (player.getResearch().isResearched(ModResearches.IGNEOUS)) {
            println(attackDamage)
            target.hurt(ModDamageSources.getSilverDamage(player), attackDamage)
        } else {
            return true
        }

        return super.onLeftClickEntity(stack, player, target)
    }

    override fun canBeDepleted(): Boolean {
        return false
    }

    override fun isEnchantable(itemStack: ItemStack): Boolean {
        return true
    }

    override fun appendHoverText(itemStack: ItemStack, world: World?, tooltip: MutableList<ITextComponent>, iTooltipFlag: ITooltipFlag) {
        val player = Minecraft.getInstance().player

        if (player != null && player.getResearch().isResearched(ModResearches.IGNEOUS)) {
            tooltip.add(TranslationTextComponent(LocalizationConstants.TOOLTIP_MAGIC_ITEM_NEVER_BREAK))
            tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.igneous_tool.autosmelt"))
        } else {
            tooltip.add(TranslationTextComponent(LocalizationConstants.TOOLTIP_DONT_KNOW_HOW_TO_USE))
        }
    }
}