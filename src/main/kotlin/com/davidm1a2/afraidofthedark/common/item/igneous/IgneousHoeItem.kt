package com.davidm1a2.afraidofthedark.common.item.igneous

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.item.core.AOTDHoeItem
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUseContext
import net.minecraft.util.ActionResultType
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World

class IgneousHoeItem : AOTDHoeItem("igneous_hoe", ModToolMaterials.IGNEOUS, -8, 0.0f, Properties()) {
    override fun useOn(context: ItemUseContext): ActionResultType {
        val player = context.player
        if (player != null) {
            if (player.getResearch().isResearched(ModResearches.IGNEOUS)) {
                return super.useOn(context)
            } else {
                if (!context.level.isClientSide) {
                    player.sendMessage(TranslationTextComponent(LocalizationConstants.DONT_UNDERSTAND))
                }
            }
        }
        return ActionResultType.FAIL
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
        } else {
            tooltip.add(TranslationTextComponent(LocalizationConstants.TOOLTIP_DONT_KNOW_HOW_TO_USE))
        }
    }
}