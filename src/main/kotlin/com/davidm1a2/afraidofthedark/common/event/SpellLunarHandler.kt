package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.capabilities.getSpellLunarData
import com.davidm1a2.afraidofthedark.common.constants.ModSpellPowerSources
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.world.entity.player.Player
import net.minecraft.entity.player.ServerPlayer
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

    private fun tickLunarVitae(entityPlayer: Player) {
        val lunarData = entityPlayer.getSpellLunarData()

        val oldLunarVitae = lunarData.vitae
        val world = entityPlayer.level
        if (world.isDay) {
            // Decay vitae during the day
            lunarData.vitae = (lunarData.vitae - VITAE_DECAY_PER_INTERVAL).coerceAtLeast(0.0)
        } else {
            val currentVitaeCap = lunarData.getMaxVitae(world)
            // Work towards our vitae cap at night
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
            lunarData.sync(entityPlayer as ServerPlayer)
        }
    }

    companion object {
        private const val VITAE_TICK_INTERVAL = 20

        // Nighttime is about 10,000 ticks. That means we will fill our cap 10000 / 20 [tick interval] * 0.01 = 5 times
        private const val VITAE_GAIN_PERCENT_PER_INTERVAL = 0.01

        // Decay 20 vitae per interval. After a full moon it will take a max of 2000 / 50 = 40 intervals = 40 seconds
        private const val VITAE_DECAY_PER_INTERVAL = 50.0
    }
}