package com.davidm1a2.afraidofthedark.common.dimension

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.world.dimension.DimensionType

fun ServerPlayerEntity.teleport(dimensionType: DimensionType) {
    AfraidOfTheDark.teleportQueue.teleport(this, dimensionType)
}