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
	 * Called to register any item renderers, ignored here
	 */
	@Override
	public void registerItemRenders()
	{
		// Not used
	}

	/**
	 * Called to register any block renderers, ignored here
	 */
	@Override
	public void registerBlockRenders()
	{
		// Not used		
	}
}
