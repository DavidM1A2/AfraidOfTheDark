/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.refrence;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

// Refrences for static final variables
public class Refrence
{
	// The ID of the mod
	public static final String MOD_ID = "AfraidOfTheDark";
	// The mod name
	public static final String MOD_NAME = "Afraid of the Dark";
	// The minecraft version number and version
	public static final String VERSION = "1.8.9-1.1.3";
	// Refrences to the proxies
	public static final String SERVER_PROXY_CLASS = "com.DavidM1A2.AfraidOfTheDark.proxy.ServerProxy";
	public static final String CLIENT_PROXY_CLASS = "com.DavidM1A2.AfraidOfTheDark.proxy.ClientProxy";
	public static final String GUI_FACTORY_CLASS = "com.DavidM1A2.AfraidOfTheDark.client.gui.GuiFactory";
	// Network channel name is the same as the ID
	public static final String NETWORK_CHANNEL_NAME = Refrence.MOD_ID;

	// The creative tab
	public static final CreativeTabs AFRAID_OF_THE_DARK = new CreativeTabs(Refrence.MOD_ID.toLowerCase())
	{
		// Icon of the tab is the journal
		@Override
		public Item getTabIconItem()
		{
			return ModItems.journal;
		}
	};

	// Limit the user's commands and items when not in debug
	public static final boolean isDebug = true;
}
