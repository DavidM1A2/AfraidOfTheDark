package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.capabilities.getSpellInnateData
import com.davidm1a2.afraidofthedark.common.constants.ModSpellPowerSources
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.LogicalSide

class SpellInnateHandler {
    private val requiredResearch: Research? by lazy {
        ModSpellPowerSources.INNATE.prerequisiteResearch
    }

    @SubscribeEvent
    fun onPlayerTick(event: TickEvent.PlayerTickEvent) {
        // Server side processing
        if (event.type == TickEvent.Type.PLAYER && event.phase == TickEvent.Phase.START && event.side == LogicalSide.SERVER) {
            val entityPlayer = event.player
            if (entityPlayer.tickCount % INNATE_TICK_INTERVAL == 0 && entityPlayer.isAlive) {
                if (requiredResearch == null || entityPlayer.getResearch().isResearched(requiredResearch!!)) {
                    val innateData = entityPlayer.getSpellInnateData()
                    val oldVitae = innateData.vitae
                    val maxVitae = innateData.getMaxVitae(entityPlayer)
                    val newVitae = (oldVitae + VITAE_PER_INTERVAL).coerceAtMost(maxVitae)
                    if (oldVitae != newVitae) {
                        innateData.vitae = newVitae
                        innateData.sync(entityPlayer)
                    }
                }
            }
        }
    }

    companion object {
        private const val INNATE_TICK_INTERVAL = 20

        private const val VITAE_PER_INTERVAL = 1.0
    }
}