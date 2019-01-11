/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.afraidofthedark.proxy;

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
}
