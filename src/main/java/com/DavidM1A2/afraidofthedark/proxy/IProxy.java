/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.afraidofthedark.proxy;

import com.DavidM1A2.afraidofthedark.common.event.ResearchOverlayHandler;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Interface containing methods for the proxy. This proxy will be instantiated differently on the server and client
 */
public interface IProxy
{
    /**
     * Called to initialize entity renderers
     */
    void initializeEntityRenderers();

    /**
     * Called to initialize tile entity renderers
     */
    void initializeTileEntityRenderers();

    /**
     * Called to initialize any mod blocks into the ore dictionary
     */
    void initializeOreDictionary();

    /**
     * Called to register any key bindings
     */
    void registerKeyBindings();

    /**
     * Called to register all packets used by AOTD
     */
    void registerPackets();

    /**
     * @return The research overlay handler client side or null server side
     */
    ResearchOverlayHandler getResearchOverlay();

    /**
     * Opens the "Insanity's Heights" book on the client side, does nothing server side
     *
     * @param entityPlayer The player that opened the book
     */
    void showInsanitysHeightsBook(EntityPlayer entityPlayer);
}
