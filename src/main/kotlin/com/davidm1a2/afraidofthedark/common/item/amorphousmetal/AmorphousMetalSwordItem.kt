package com.davidm1a2.afraidofthedark.common.item.amorphousmetal

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.item.core.AOTDResearchRequiredSwordItem
import com.davidm1a2.afraidofthedark.common.utility.damagesource.AOTDDamageType
import com.davidm1a2.afraidofthedark.common.utility.damagesource.IDoesAOTDDamageType
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class AmorphousMetalSwordItem : AOTDResearchRequiredSwordItem("amorphous_metal_sword", ModToolMaterials.AMORPHOUS_METAL, 3, -2.4f, ModResearches.CATALYSIS, Properties()), IDoesAOTDDamageType {
    override val type: AOTDDamageType = AOTDDamageType.SILVER

    override fun appendHoverText(itemStack: ItemStack, world: Level?, tooltip: MutableList<Component>, iTooltipFlag: TooltipFlag) {
        super.appendHoverText(itemStack, world, tooltip, iTooltipFlag)

        val player = Minecraft.getInstance().player

        if (player != null && player.getResearch().isResearched(requiredResearch)) {
            tooltip.add(TranslatableComponent("tooltip.afraidofthedark.amorphous_metal_tool.double_drops"))
            tooltip.add(TranslatableComponent("tooltip.afraidofthedark.amorphous_metal_tool.no_drops"))
        }
    }
}
