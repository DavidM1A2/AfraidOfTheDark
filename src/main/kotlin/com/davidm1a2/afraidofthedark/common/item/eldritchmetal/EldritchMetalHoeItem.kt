package com.davidm1a2.afraidofthedark.common.item.eldritchmetal

import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.item.core.AOTDResearchRequiredHoeItem
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class EldritchMetalHoeItem : AOTDResearchRequiredHoeItem("eldritch_metal_hoe", ModToolMaterials.ELDRITCH_METAL, -6, 0.0f, ModResearches.AN_UNSETTLING_MATERIAL, Properties()), IEldritchItem {
    override fun inventoryTick(itemStack: ItemStack, world: World, entity: Entity, itemSlot: Int, isSelected: Boolean) {
        EldritchMetalCommons.processItem(itemStack, world, entity)
    }
}
