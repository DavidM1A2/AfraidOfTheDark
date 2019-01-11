/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.afraidofthedark.proxy;

/**
 * Interface containing methods for the proxy. This proxy will be instantiated differently on the server and client
 */
public interface IProxy
{
	/**
	 * Called to initialize leaf block renderers. This simply ensures the colors of the leaves are correctly applied
	 */
	void initializeLeafRenderers();

	/**
	 * Called to initialize entity renderers
	 */
	void initializeEntityRenderers();

	/**
	 * Called to initialize any mod blocks into the ore dictionary
	 */
	void initializeOreDictionary();

	/**
	 * Called to initialize any mod smelting recipes
	 */
	void initializeSmeltingRecipes();

	/**
	 * Called to register all packets used by AOTD
	 */
	void registerPackets();
}
