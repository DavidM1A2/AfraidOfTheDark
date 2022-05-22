package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.capabilities.getSpellSolarData
import com.davidm1a2.afraidofthedark.common.constants.ModSpellPowerSources
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.LogicalSide

class SpellSolarHandler {
    private val requiredResearch: Research? by lazy {
        ModSpellPowerSources.SOLAR.prerequisiteResearch
    }

    @SubscribeEvent
    fun onPlayerTick(event: TickEvent.PlayerTickEvent) {
        // Server side processing
        if (event.type == TickEvent.Type.PLAYER && event.phase == TickEvent.Phase.START && event.side == LogicalSide.SERVER) {
            val entityPlayer = event.player
            // Dead players don't have capabilities
            if (entityPlayer.tickCount % VITAE_TICK_INTERVAL == 0 && entityPlayer.isAlive) {
                if (requiredResearch == null || entityPlayer.getResearch().isResearched(requiredResearch!!)) {
                    tickSolarVitae(entityPlayer)
                }
            }
        }
    }

    private fun tickSolarVitae(entityPlayer: PlayerEntity) {
        val solarData = entityPlayer.getSpellSolarData()

        val oldSolarVitae = solarData.vitae
        if (entityPlayer.level.isDay) {
            val vitaeCap = solarData.getMaxVitae(entityPlayer.level)
            if (solarData.vitae < vitaeCap) {
                solarData.vitae = (solarData.vitae + vitaeCap * VITAE_GAIN_PERCENT_PER_INTERVAL).coerceAtMost(vitaeCap)
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
    }
}