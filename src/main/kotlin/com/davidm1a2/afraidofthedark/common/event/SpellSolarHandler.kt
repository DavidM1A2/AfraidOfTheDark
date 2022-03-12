package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.capabilities.getSpellSolarData
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.LogicalSide

class SpellSolarHandler {
    @SubscribeEvent
    fun onPlayerTick(event: TickEvent.PlayerTickEvent) {
        // Server side processing
        if (event.type == TickEvent.Type.PLAYER && event.phase == TickEvent.Phase.START && event.side == LogicalSide.SERVER) {
            val entityPlayer = event.player
            // Dead players don't have capabilities
            if (entityPlayer.tickCount % VITAE_TICK_INTERVAL == 0 && entityPlayer.isAlive) {
                tickSolarVitae(entityPlayer)
            }
        }
    }

    private fun tickSolarVitae(entityPlayer: PlayerEntity) {
        val solarData = entityPlayer.getSpellSolarData()

        val oldSolarVitae = solarData.vitae
        if (entityPlayer.level.isDay) {
            if (solarData.vitae < VITAE_CAP) {
                solarData.vitae = (solarData.vitae + VITAE_CAP * VITAE_GAIN_PERCENT_PER_INTERVAL).coerceAtMost(VITAE_CAP)
            }
        } else {
            // Decay vitae during the day
            solarData.vitae = (solarData.vitae - VITAE_DECAY_PER_INTERVAL).coerceAtLeast(0.0)
        }

        // Small optimization, only sync on an actual update
        if (oldSolarVitae != solarData.vitae) {
            solarData.sync(entityPlayer as ServerPlayerEntity)
        }
    }

    companion object {
        private const val VITAE_TICK_INTERVAL = 20

        // Nighttime is about 14,000 ticks. That means we will fill our cap 14000 / 20 [tick interval] * 0.02 = 14 times
        private const val VITAE_GAIN_PERCENT_PER_INTERVAL = 0.02

        // Decay 25 vitae per interval = 200/25 = 8 seconds to decay all vitae
        private const val VITAE_DECAY_PER_INTERVAL = 25.0

        // The maximum amount of vitae we can store
        private const val VITAE_CAP = 200.0
    }
}