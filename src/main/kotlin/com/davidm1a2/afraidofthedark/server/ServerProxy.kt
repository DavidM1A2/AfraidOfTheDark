package com.davidm1a2.afraidofthedark.server

import com.davidm1a2.afraidofthedark.common.IProxy
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity
import net.minecraftforge.eventbus.api.IEventBus
import org.apache.logging.log4j.LogManager

class ServerProxy : IProxy {
    override fun registerSidedHandlers(forgeBus: IEventBus, modBus: IEventBus) {
    }

    override fun playSoundFixed(entity: Entity, event: SoundEvent, category: SoundSource, volume: Float, pitch: Float) {
    }

    override fun displayResearch(research: Research) {
        LOG.error("Attempted to display the research ${research.registryName} on the logical server. This should never happen")
    }

    companion object {
        private val LOG = LogManager.getLogger()
    }
}