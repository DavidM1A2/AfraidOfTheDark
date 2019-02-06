/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.afraidofthedark.proxy;

import com.DavidM1A2.afraidofthedark.common.handler.ResearchOverlayHandler;

/**
 * Proxy that is only instantiated on the SERVER
 */
public class ServerProxy extends CommonProxy
{
	/**
	 * Called to initialize leaf block renderers. This simply ensures the colors of the leaves are correctly applied. Server does not register any renderers
	 */
	@Override
	public void initializeLeafRenderers()
	{
		// Not used
	}

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
}
