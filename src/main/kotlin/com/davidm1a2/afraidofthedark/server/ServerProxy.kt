package com.davidm1a2.afraidofthedark.server

import com.davidm1a2.afraidofthedark.common.IProxy
import com.davidm1a2.afraidofthedark.common.event.ResearchOverlayHandler
import com.davidm1a2.afraidofthedark.server.event.ServerResearchOverlayHandler
import net.minecraftforge.eventbus.api.IEventBus

class ServerProxy : IProxy {
    override val researchOverlayHandler: ResearchOverlayHandler = ServerResearchOverlayHandler()

    override fun registerSidedHandlers(forgeBus: IEventBus, modBus: IEventBus) {
    }
}