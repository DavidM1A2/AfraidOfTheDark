/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.proxy;

// Interface containing methods for the proxy
public interface IProxy {
	public abstract void registerKeyBindings();

	public abstract void registerEntityRenders();

	public abstract void registerMiscRenders();

	public abstract void registerChannel();

	public abstract void registerMiscelaneous();
}
