/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.refrence;

// Refrences for static final variables
public class Refrence
{
	// The ID of the mod
	public static final String MOD_ID = "AfraidOfTheDark";
	// The mod name
	public static final String MOD_NAME = "Afraid of the Dark";
	// The minecraft version number and version
	public static final String VERSION = "1.8-Alpha 0.1";
	// Refrences to the proxies
	public static final String SERVER_PROXY_CLASS = "com.DavidM1A2.AfraidOfTheDark.proxy.ServerProxy";
	public static final String CLIENT_PROXY_CLASS = "com.DavidM1A2.AfraidOfTheDark.proxy.ClientProxy";
	public static final String GUI_FACTORY_CLASS = "com.DavidM1A2.AfraidOfTheDark.client.gui.GuiFactory";
	// Network channel name is the same as the ID
	public static final String NETWORK_CHANNEL_NAME = Refrence.MOD_ID;
}
