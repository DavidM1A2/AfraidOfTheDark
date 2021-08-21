package com.davidm1a2.afraidofthedark.common

import com.davidm1a2.afraidofthedark.common.event.ResearchOverlayHandler
import net.minecraftforge.eventbus.api.IEventBus

interface IProxy {
    val researchOverlayHandler: ResearchOverlayHandler

    fun registerSidedHandlers(forgeBus: IEventBus, modBus: IEventBus)
}