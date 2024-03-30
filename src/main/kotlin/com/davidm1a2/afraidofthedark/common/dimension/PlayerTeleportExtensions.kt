package com.davidm1a2.afraidofthedark.common.dimension

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.Level

fun ServerPlayer.teleport(world: ResourceKey<Level>) {
    AfraidOfTheDark.teleportQueue.teleport(this, world)
}