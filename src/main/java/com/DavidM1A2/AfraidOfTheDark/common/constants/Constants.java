/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.constants;

// Refrences for static final variables
public class Constants
{
	// The ID of the mod
	public static final String MOD_ID = "afraidofthedark";
	// The mod name
	public static final String MOD_NAME = "Afraid of the Dark";
	// The minecraft version number and version
	public static final String MOD_VERSION = "1.2";
	// The minecraft version number
	public static final String MC_VERSION = "1.12.2";
	// Refrences to the proxies
	public static final String SERVER_PROXY_CLASS = "com.DavidM1A2.AfraidOfTheDark.proxy.ServerProxy";
	public static final String CLIENT_PROXY_CLASS = "com.DavidM1A2.AfraidOfTheDark.proxy.ClientProxy";
	public static final String GUI_FACTORY_CLASS = "com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDGuiFactory";

	// Limit the user's commands and items when not in debug
	public static final boolean isDebug = true;
}
