package com.davidm1a2.afraidofthedark.common.item.core

import com.davidm1a2.afraidofthedark.common.event.custom.ManualResearchTriggerEvent
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraftforge.common.MinecraftForge

class ResearchUnlockingBlockItem(private val research: Research, block: Block, properties: Properties) : BlockItem(block, properties) {
    override fun inventoryTick(itemStack: ItemStack, world: Level, entity: Entity, slotIndex: Int, isSelected: Boolean) {
        if (!world.isClientSide) {
            if (entity.tickCount % CHECK_SPEED_TICKS == 0) {
                if (entity is Player) {
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