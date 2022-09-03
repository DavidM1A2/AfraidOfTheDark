package com.davidm1a2.afraidofthedark.common.item.eldritchmetal

import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.item.core.AOTDResearchRequiredAxeItem
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class EldritchMetalAxeItem : AOTDResearchRequiredAxeItem("eldritch_metal_axe", ModToolMaterials.ELDRITCH_METAL, 4.0f, -3f, ModResearches.AN_UNSETTLING_MATERIAL, Properties()), IEldritchItem {
    override fun inventoryTick(itemStack: ItemStack, world: World, entity: Entity, itemSlot: Int, isSelected: Boolean) {
        EldritchMetalCommons.processItem(itemStack, world, entity)
    }
}