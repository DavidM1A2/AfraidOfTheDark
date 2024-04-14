package com.davidm1a2.afraidofthedark.common.item.igneous

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.item.core.AOTDResearchRequiredPickaxeItem
import com.davidm1a2.afraidofthedark.common.utility.damagesource.AOTDDamageType
import com.davidm1a2.afraidofthedark.common.utility.damagesource.IDoesAOTDDamageType
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class IgneousPickaxeItem : AOTDResearchRequiredPickaxeItem("igneous_pickaxe", ModToolMaterials.IGNEOUS, 1, -2.8f, ModResearches.IGNEOUS, Properties()), IDoesAOTDDamageType {
    override val type: AOTDDamageType = AOTDDamageType.SILVER

    override fun canBeDepleted(): Boolean {
        return false
    }

    override fun isEnchantable(itemStack: ItemStack): Boolean {
        return true
    }

    override fun appendHoverText(itemStack: ItemStack, world: Level?, tooltip: MutableList<Component>, iTooltipFlag: TooltipFlag) {
        super.appendHoverText(itemStack, world, tooltip, iTooltipFlag)

        val player = Minecraft.getInstance().player

        if (player != null && player.getResearch().isResearched(requiredResearch)) {
            tooltip.add(TranslatableComponent(LocalizationConstants.TOOLTIP_MAGIC_ITEM_NEVER_BREAK))
            tooltip.add(TranslatableComponent("tooltip.afraidofthedark.igneous_tool.autosmelt"))
        }
    }
}