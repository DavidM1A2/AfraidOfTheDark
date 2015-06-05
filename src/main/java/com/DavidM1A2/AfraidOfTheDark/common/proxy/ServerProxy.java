/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.proxy;

// Any server side-only things we want to do
public class ServerProxy extends CommonProxy
{
	@Override
	public void registerKeyBindings()
	{
		// NOOP
	}

	// Here we register packets and a channel
	@Override
	public void registerChannel()
	{
		super.registerChannel();
	}

	@Override
	public void registerRenderThings()
	{
		// NOOP
	}

	@Override
	public void registerMiscelaneous()
	{
		// Not used
	}
}
