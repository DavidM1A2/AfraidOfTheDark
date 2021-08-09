package com.davidm1a2.afraidofthedark.common.event

import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.RegistryKey
import net.minecraft.world.World
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.LogicalSide

class TeleportQueue {
    private val playersToTeleport = mutableMapOf<ServerPlayerEntity, RegistryKey<World>>()

    fun teleport(player: ServerPlayerEntity, world: RegistryKey<World>) {
        playersToTeleport[player] = world
    }

    @SubscribeEvent
    fun onServerTickEvent(event: TickEvent.ServerTickEvent) {
        if (event.type == TickEvent.Type.SERVER && event.phase == TickEvent.Phase.END && event.side == LogicalSide.SERVER) {
            playersToTeleport.forEach { (player, world) ->
                player.teleportTo(
                    player.server.getLevel(world)!!,
                    0.0,
                    1000.0,
                    0.0,
                    0f,
                    0f
                )
            }
            playersToTeleport.clear()
        }
    }
}