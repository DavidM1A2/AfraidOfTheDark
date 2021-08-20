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
            playersToTeleport.entries.removeIf { (player, world) ->
                val server = player.server
                // There's an edge case where the server doesn't exist after the player respawns.If the player
                // does not have a server associated with them yet wait a tick then try again
                if (server?.getLevel(world) != null) {
                    player.teleportTo(server.getLevel(world)!!, 0.0, 1000.0, 0.0, 0f, 0f)
                    true
                } else {
                    false
                }
            }
        }
    }
}