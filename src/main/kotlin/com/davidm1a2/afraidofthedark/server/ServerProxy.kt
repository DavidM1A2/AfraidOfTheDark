package com.davidm1a2.afraidofthedark.server

import com.davidm1a2.afraidofthedark.common.IProxy
import com.davidm1a2.afraidofthedark.common.event.ResearchOverlayHandler
import com.davidm1a2.afraidofthedark.server.event.ServerResearchOverlayHandler
import net.minecraft.entity.Entity
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvent
import net.minecraftforge.eventbus.api.IEventBus

class ServerProxy : IProxy {
    override val researchOverlayHandler: ResearchOverlayHandler = ServerResearchOverlayHandler()

    override fun registerSidedHandlers(forgeBus: IEventBus, modBus: IEventBus) {
    }

    override fun playSoundFixed(entity: Entity, event: SoundEvent, category: SoundCategory, volume: Float, pitch: Float) {
    }
}