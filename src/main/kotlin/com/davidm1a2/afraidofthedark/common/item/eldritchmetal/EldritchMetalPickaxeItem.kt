package com.davidm1a2.afraidofthedark.common.item.eldritchmetal

import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.item.core.AOTDResearchRequiredPickaxeItem
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class EldritchMetalPickaxeItem : AOTDResearchRequiredPickaxeItem("eldritch_metal_pickaxe", ModToolMaterials.ELDRITCH_METAL, 1, -2.8f, ModResearches.AN_UNSETTLING_MATERIAL, Properties()),
    IEldritchItem {
    override fun inventoryTick(itemStack: ItemStack, world: World, entity: Entity, itemSlot: Int, isSelected: Boolean) {
        EldritchMetalCommons.processItem(itemStack, world, entity)
    }
}
