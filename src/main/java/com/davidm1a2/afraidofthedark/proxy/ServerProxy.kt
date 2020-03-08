package com.davidm1a2.afraidofthedark.proxy

import com.davidm1a2.afraidofthedark.common.event.ResearchOverlayHandler
import net.minecraft.entity.player.EntityPlayer

/**
 * Proxy that is only instantiated on the SERVER
 */
class ServerProxy : CommonProxy() {
    /**
     * Called to initialize entity renderers
     */
    override fun initializeEntityRenderers() {
        // Not used
    }

    /**
     * Called to initialize tile entity renderers
     */
    override fun initializeTileEntityRenderers() {
        // Not used
    }

    /**
     * Called to register any key bindings, there's none server side
     */
    override fun registerKeyBindings() {
        // Not used
    }

    /**
     * @return The research overlay does not exist server side
     */
    override val researchOverlay: ResearchOverlayHandler? = null

    /**
     * Opens the "Insanity's Heights" book on the client side, does nothing server side
     *
     * @param entityPlayer The player that opened the book
     */
    override fun showInsanitysHeightsBook(entityPlayer: EntityPlayer) {
        // Not used
    }
}