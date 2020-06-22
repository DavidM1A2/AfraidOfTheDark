package com.davidm1a2.afraidofthedark.proxy

import com.davidm1a2.afraidofthedark.common.event.ResearchOverlayHandler
import net.minecraft.entity.player.EntityPlayer

/**
 * Proxy that is only instantiated on the SERVER
 */
class ServerProxy : IProxy {
    override val researchOverlay: ResearchOverlayHandler? = null

    override fun initializeResearchOverlayHandler() {
        // Not used
    }

    override fun initializeEntityRenderers() {
        // Not used
    }

    override fun initializeTileEntityRenderers() {
        // Not used
    }

    override fun registerKeyBindings() {
        // Not used
    }

    override fun showInsanitysHeightsBook(entityPlayer: EntityPlayer) {
        // Not used
    }
}