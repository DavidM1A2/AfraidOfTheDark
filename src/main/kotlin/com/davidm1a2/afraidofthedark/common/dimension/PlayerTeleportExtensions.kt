package com.davidm1a2.afraidofthedark.common.dimension

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.RegistryKey
import net.minecraft.world.World

fun ServerPlayerEntity.teleport(world: RegistryKey<World>) {
    AfraidOfTheDark.teleportQueue.teleport(this, world)
}