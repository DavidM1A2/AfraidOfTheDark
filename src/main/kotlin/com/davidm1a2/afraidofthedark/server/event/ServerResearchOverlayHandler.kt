package com.davidm1a2.afraidofthedark.server.event

import com.davidm1a2.afraidofthedark.common.event.ResearchOverlayHandler
import com.davidm1a2.afraidofthedark.common.registry.research.Research
import org.apache.logging.log4j.LogManager

class ServerResearchOverlayHandler : ResearchOverlayHandler {
    override fun displayResearch(research: Research) {
        LOG.error("Attempted to display the research ${research.registryName} on the logical server. This should never happen")
    }

    companion object {
        private val LOG = LogManager.getLogger()
    }
}