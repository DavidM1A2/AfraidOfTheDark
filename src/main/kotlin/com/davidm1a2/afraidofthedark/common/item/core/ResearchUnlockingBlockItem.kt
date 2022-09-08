package com.davidm1a2.afraidofthedark.common.item.core

import com.davidm1a2.afraidofthedark.common.event.custom.ManualResearchTriggerEvent
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.block.Block
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge

class ResearchUnlockingBlockItem(private val research: Research, block: Block, properties: Properties) : BlockItem(block, properties) {
    override fun inventoryTick(itemStack: ItemStack, world: World, entity: Entity, slotIndex: Int, isSelected: Boolean) {
        if (!world.isClientSide) {
            if (entity.tickCount % CHECK_SPEED_TICKS == 0) {
                if (entity is PlayerEntity) {
                    MinecraftForge.EVENT_BUS.post(ManualResearchTriggerEvent(entity, research))
                }
            }
        }
        super.inventoryTick(itemStack, world, entity, slotIndex, isSelected)
    }

    companion object {
        private const val CHECK_SPEED_TICKS = 20
    }
}