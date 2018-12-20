/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.afraidofthedark.common.constants;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

/**
  * Refrences for static final variables
  */
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
	public static final String SERVER_PROXY_CLASS = "com.DavidM1A2.afraidofthedark.proxy.ServerProxy";
	public static final String CLIENT_PROXY_CLASS = "com.DavidM1A2.afraidofthedark.proxy.ClientProxy";
	public static final String GUI_FACTORY_CLASS = "com.DavidM1A2.afraidofthedark.client.gui.AOTDGuiFactory";

	// Creative Tab for the mod
	public static final CreativeTabs AOTD_CREATIVE_TAB = new CreativeTabs(Constants.MOD_ID)
	{
		/**
		 * Getter for the mod creative tab icon
		 *
		 * @return The icon for the creative tab as an item
		 */
		@Override
		public ItemStack getTabIconItem()
		{
			return new ItemStack(ModItems.JOURNAL);
		}
	};

	// Limit the user's commands and items when not in debug
	public static final boolean isDebug = true;

	// The base size of all GUIs
	public static final int GUI_WIDTH = 640;
	public static final int GUI_HEIGHT = 360;
}
