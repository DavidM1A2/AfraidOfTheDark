package com.davidm1a2.afraidofthedark.common.item.gnomishmetal

import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.event.custom.ManualResearchTriggerEvent
import com.davidm1a2.afraidofthedark.common.item.core.AOTDResearchRequiredPickaxeItem
import net.minecraft.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraftforge.common.MinecraftForge

class GnomishMetalPickaxeItem : AOTDResearchRequiredPickaxeItem("gnomish_metal_pickaxe", ModToolMaterials.GNOMISH_METAL, 0, -2.8f, ModResearches.EXCAVATION_ENGINEERING, Properties()) {
    override fun inventoryTick(stack: ItemStack, worldIn: World, entityIn: Entity, itemSlot: Int, isSelected: Boolean) {
        // Every 5 seconds check if this item is in a player's inventory, if so unlock the research
        if (!worldIn.isClientSide && entityIn.tickCount % 100 == 0) {
            if (entityIn is Player) {
                MinecraftForge.EVENT_BUS.post(ManualResearchTriggerEvent(entityIn, ModResearches.EXCAVATION_ENGINEERING))
            }
        }
    }
}
