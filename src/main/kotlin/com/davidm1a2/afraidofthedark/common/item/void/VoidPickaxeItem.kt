package com.davidm1a2.afraidofthedark.common.item.void

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.item.core.AOTDResearchRequiredPickaxeItem
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class VoidPickaxeItem : AOTDResearchRequiredPickaxeItem(
    "void_pickaxe",
    ModToolMaterials.VOID,
    -24,
    -2.8f,
    ModResearches.AN_UNSETTLING_MATERIAL,
    Properties()
) {
    override fun inventoryTick(itemStack: ItemStack, world: Level, entity: Entity, itemSlot: Int, isSelected: Boolean) {
        VoidCommons.processItem(itemStack, world)
    }

    override fun appendHoverText(
        itemStack: ItemStack,
        world: Level?,
        tooltip: MutableList<Component>,
        iTooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(itemStack, world, tooltip, iTooltipFlag)

        val player = Minecraft.getInstance().player

        if (player != null && player.getResearch().isResearched(requiredResearch)) {
            tooltip.add(TranslatableComponent("tooltip.afraidofthedark.void_tool.autorepair"))
        }
    }
}
