package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.capabilities.getSpellLunarData
import com.davidm1a2.afraidofthedark.common.constants.ModSpellPowerSources
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.LogicalSide

class SpellLunarHandler {
    private val requiredResearch: Research? by lazy {
        ModSpellPowerSources.LUNAR.prerequisiteResearch
    }

    @SubscribeEvent
    fun onPlayerTick(event: TickEvent.PlayerTickEvent) {
        // Server side processing
        if (event.type == TickEvent.Type.PLAYER && event.phase == TickEvent.Phase.START && event.side == LogicalSide.SERVER) {
            val entityPlayer = event.player
            // Dead players don't have capabilities
            if (entityPlayer.tickCount % VITAE_TICK_INTERVAL == 0 && entityPlayer.isAlive) {
                if (requiredResearch == null || entityPlayer.getResearch().isResearched(requiredResearch!!)) {
                    tickLunarVitae(entityPlayer)
                }
            }
        }
    }

    private fun tickLunarVitae(entityPlayer: PlayerEntity) {
        val lunarData = entityPlayer.getSpellLunarData()

        val oldLunarVitae = lunarData.vitae
        val world = entityPlayer.level
        if (world.isDay) {
            // Decay vitae during the day
            lunarData.vitae = (lunarData.vitae - VITAE_DECAY_PER_INTERVAL).coerceAtLeast(0.0)
        } else {
            // Work towards our vitae cap at night
            val moonPhase = world.dimensionType().moonPhase(world.dayTime())
            val currentVitaeCap = VITAE_CAP_BY_MOON_PHASE[moonPhase] ?: 0.0
            if (lunarData.vitae > currentVitaeCap) {
                // Decay to the cap. This avoid players "cheesing" the power source by logging off after a full moon to preserve vitae
                lunarData.vitae = (lunarData.vitae - VITAE_DECAY_PER_INTERVAL).coerceAtLeast(currentVitaeCap)
            } else {
                // Grow to the cap
                lunarData.vitae = (lunarData.vitae + currentVitaeCap * VITAE_GAIN_PERCENT_PER_INTERVAL).coerceAtMost(currentVitaeCap)
            }
        }

        // Small optimization, only sync on an actual update
        if (oldLunarVitae != lunarData.vitae) {
            lunarData.sync(entityPlayer as ServerPlayerEntity)
        }
    }

    companion object {
        private const val VITAE_TICK_INTERVAL = 20

        // Nighttime is about 10,000 ticks. That means we will fill our cap 10000 / 20 [tick interval] * 0.01 = 5 times
        private const val VITAE_GAIN_PERCENT_PER_INTERVAL = 0.01

        // Decay 20 vitae per interval. After a full moon it will take a max of 2000 / 50 = 40 intervals = 40 seconds
        private const val VITAE_DECAY_PER_INTERVAL = 50.0

        // The maximum amount of vitae we can store in each moon phase
        private val VITAE_CAP_BY_MOON_PHASE = mapOf(
            0 to 2000.0, // Full (100%)
            1 to 300.0, // Waning Gibbous (75%)
            2 to 100.0, // Third Quarter (50%)
            3 to 30.0, // Waning Crescent (25%)
            4 to 10.0, // New Moon (0%)
            5 to 30.0, // Waxing Crescent (25%)
            6 to 100.0, // First Quarter (50%)
            7 to 300.0 // Waxing Gibbous (75%)
        )
    }
}