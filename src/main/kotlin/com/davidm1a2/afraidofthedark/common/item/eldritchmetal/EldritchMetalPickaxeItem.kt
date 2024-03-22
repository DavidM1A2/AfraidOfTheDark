package com.davidm1a2.afraidofthedark.common.item.eldritchmetal

import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.item.core.AOTDResearchRequiredPickaxeItem
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class EldritchMetalPickaxeItem : AOTDResearchRequiredPickaxeItem("eldritch_metal_pickaxe", ModToolMaterials.ELDRITCH_METAL, 1, -2.8f, ModResearches.AN_UNSETTLING_MATERIAL, Properties()),
    IEldritchItem {
    override fun inventoryTick(itemStack: ItemStack, world: Level, entity: Entity, itemSlot: Int, isSelected: Boolean) {
        EldritchMetalCommons.processItem(itemStack, world, entity)
    }
}
