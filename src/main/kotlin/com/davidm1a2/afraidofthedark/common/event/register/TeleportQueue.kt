package com.davidm1a2.afraidofthedark.common.event.register

import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.world.dimension.DimensionType
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.LogicalSide

class TeleportQueue {
    private val playersToTeleport = mutableMapOf<ServerPlayerEntity, DimensionType>()

    fun teleport(player: ServerPlayerEntity, dimension: DimensionType) {
        playersToTeleport[player] = dimension
    }

    @SubscribeEvent
    fun onServerTickEvent(event: TickEvent.ServerTickEvent) {
        if (event.type == TickEvent.Type.SERVER && event.phase == TickEvent.Phase.END && event.side == LogicalSide.SERVER) {
            playersToTeleport.forEach { (player, dimension) ->
                player.teleport(
                    player.server.getWorld(dimension),
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