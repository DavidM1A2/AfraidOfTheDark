package com.davidm1a2.afraidofthedark.common.item.void

import net.minecraft.item.ItemStack
import net.minecraft.world.level.Level
import kotlin.math.max

object VoidCommons {
    // One HP per 5 seconds
    private const val REPAIR_FREQUENCY = 100

    fun processItem(itemStack: ItemStack, world: Level) {
        if (!world.isClientSide) {
            if (world.levelData.dayTime % REPAIR_FREQUENCY == 0L) {
                itemStack.damageValue = max(itemStack.damageValue - 1, 0)
            }
        }
    }
}