package com.davidm1a2.afraidofthedark.common.item.eldritchmetal

import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.item.core.AOTDResearchRequiredShovelItem
import com.davidm1a2.afraidofthedark.common.utility.damagesource.AOTDDamageType
import com.davidm1a2.afraidofthedark.common.utility.damagesource.IDoesAOTDDamageType
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class EldritchMetalShovelItem : AOTDResearchRequiredShovelItem("eldritch_metal_shovel", ModToolMaterials.ELDRITCH_METAL, 1.5f, -3f, ModResearches.AN_UNSETTLING_MATERIAL, Properties()), IEldritchItem,
    IDoesAOTDDamageType {
    override val type: AOTDDamageType = AOTDDamageType.SILVER

    override fun inventoryTick(itemStack: ItemStack, world: Level, entity: Entity, itemSlot: Int, isSelected: Boolean) {
        EldritchMetalCommons.processItem(itemStack, world, entity)
    }
}
