package com.davidm1a2.afraidofthedark.common.dimension

import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.world.dimension.DimensionType

fun ServerPlayerEntity.teleport(dimensionType: DimensionType) {
    this.teleport(
        this.server.getWorld(dimensionType),
        0.0,
        1000.0,
        0.0,
        0f,
        0f
    )
}