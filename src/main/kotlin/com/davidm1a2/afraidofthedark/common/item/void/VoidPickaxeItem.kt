package com.davidm1a2.afraidofthedark.common.item.void

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.item.core.AOTDResearchRequiredPickaxeItem
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World

class VoidPickaxeItem : AOTDResearchRequiredPickaxeItem(
    "void_pickaxe",
    ModToolMaterials.VOID,
    -24,
    -2.8f,
    ModResearches.AN_UNSETTLING_MATERIAL,
    Properties()
) {
    override fun inventoryTick(itemStack: ItemStack, world: World, entity: Entity, itemSlot: Int, isSelected: Boolean) {
        VoidCommons.processItem(itemStack, world)
    }

    override fun appendHoverText(
        itemStack: ItemStack,
        world: World?,
        tooltip: MutableList<ITextComponent>,
        iTooltipFlag: ITooltipFlag
    ) {
        super.appendHoverText(itemStack, world, tooltip, iTooltipFlag)

        val player = Minecraft.getInstance().player

        if (player != null && player.getResearch().isResearched(requiredResearch)) {
            tooltip.add(TranslationTextComponent("tooltip.afraidofthedark.void_tool.autorepair"))
        }
    }
}
