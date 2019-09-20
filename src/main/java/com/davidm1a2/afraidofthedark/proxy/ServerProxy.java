/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.davidm1a2.afraidofthedark.proxy;

import com.davidm1a2.afraidofthedark.common.event.ResearchOverlayHandler;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Proxy that is only instantiated on the SERVER
 */
public class ServerProxy extends CommonProxy
{
    /**
     * Called to initialize entity renderers
     */
    @Override
    public void initializeEntityRenderers()
    {
        // Not used
    }

    /**
     * Called to initialize tile entity renderers
     */
    @Override
    public void initializeTileEntityRenderers()
    {
        // Not used
    }

    /**
     * Called to register any key bindings, there's none server side
     */
    @Override
    public void registerKeyBindings()
    {
        // Not used
    }

    /**
     * @return The research overlay does not exist server side
     */
    @Override
    public ResearchOverlayHandler getResearchOverlay()
    {
        return null;
    }

    /**
     * Opens the "Insanity's Heights" book on the client side, does nothing server side
     *
     * @param entityPlayer The player that opened the book
     */
    @Override
    public void showInsanitysHeightsBook(EntityPlayer entityPlayer)
    {
        // Not used
    }
}
