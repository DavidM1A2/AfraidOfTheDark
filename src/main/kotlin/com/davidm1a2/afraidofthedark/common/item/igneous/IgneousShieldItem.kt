package com.davidm1a2.afraidofthedark.common.item.igneous

import com.davidm1a2.afraidofthedark.client.item.IgneousShieldItemStackRenderer
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.core.AOTDShieldItem
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World
import java.util.concurrent.Callable

class IgneousShieldItem : AOTDShieldItem("igneous_shield", Properties().setISTER { Callable { IgneousShieldItemStackRenderer() } }) {
    override fun canBeDepleted(): Boolean {
        return false
    }

    override fun use(world: World, playerEntity: PlayerEntity, hand: Hand): ActionResult<ItemStack> {
        return if (playerEntity.getResearch().isResearched(ModResearches.IGNEOUS)) {
            super.use(world, playerEntity, hand)
        } else {
            ActionResult.fail(playerEntity.getItemInHand(hand))
        }
    }

    override fun appendHoverText(itemStack: ItemStack, world: World?, tooltip: MutableList<ITextComponent>, iTooltipFlag: ITooltipFlag) {
        super.appendHoverText(itemStack, world, tooltip, iTooltipFlag)

        val player = Minecraft.getInstance().player

        if (player != null && player.getResearch().isResearched(ModResearches.IGNEOUS)) {
            tooltip.add(TranslationTextComponent(LocalizationConstants.TOOLTIP_MAGIC_ITEM_NEVER_BREAK))
        } else {
            tooltip.add(TranslationTextComponent(LocalizationConstants.DONT_UNDERSTAND))
        }
    }
}