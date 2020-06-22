/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.davidm1a2.afraidofthedark.proxy

import com.davidm1a2.afraidofthedark.common.event.ResearchOverlayHandler
import net.minecraft.entity.player.EntityPlayer

/**
 * Interface containing methods for the proxy. This proxy will be instantiated differently on the server and client
 */
interface IProxy {
    /**
     * @return The research overlay handler client side or null server side
     */
    val researchOverlay: ResearchOverlayHandler?

    /**
     * Initializes the research overlay handler
     */
    fun initializeResearchOverlayHandler()

    /**
     * Called to initialize entity renderers
     */
    fun initializeEntityRenderers()

    /**
     * Called to initialize tile entity renderers
     */
    fun initializeTileEntityRenderers()

    /**
     * Called to register any key bindings
     */
    fun registerKeyBindings()

    /**
     * Opens the "Insanity's Heights" book on the client side, does nothing server side
     *
     * @param entityPlayer The player that opened the book
     */
    fun showInsanitysHeightsBook(entityPlayer: EntityPlayer)
}