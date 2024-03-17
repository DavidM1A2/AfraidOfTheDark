package com.davidm1a2.afraidofthedark.server.event

import com.davidm1a2.afraidofthedark.common.event.custom.PlayerInBiomeEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.LogicalSide
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks

class PlayerBiomeEventHandler {
    private var ticksExisted = 0

    @SubscribeEvent
    fun onServerTickEvent(event: TickEvent.ServerTickEvent) {
        if (event.type == TickEvent.Type.SERVER && event.phase == TickEvent.Phase.END && event.side == LogicalSide.SERVER) {
            ticksExisted++
            if (ticksExisted % TICKS_BETWEEN_CHECKS == 0) {
                val server = ServerLifecycleHooks.getCurrentServer()
                server?.playerList?.players?.forEach {
                    // Some sanity checks to ensure the player is in a valid state
                    if (it.isAlive && !it.hasDisconnected()) {
                        val biome = it.level.getBiome(it.blockPosition())
                        MinecraftForge.EVENT_BUS.post(PlayerInBiomeEvent(it, biome))
                    }
                }
            }
        }
    }

    companion object {
        private const val TICKS_BETWEEN_CHECKS = 20 * 10 // 10 seconds
    }
}