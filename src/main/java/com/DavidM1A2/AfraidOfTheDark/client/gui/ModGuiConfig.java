/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.handler.ConfigurationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.reference.Reference;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

/*
 * Setup the GUI configuration screen
 */
public class ModGuiConfig extends GuiConfig
{
	private static final List<IConfigElement> CONFIG_INGAME_OPTIONS = new ArrayList<IConfigElement>()
	{
		{
			addAll(new ConfigElement(ConfigurationHandler.configuration.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements());
			addAll(new ConfigElement(ConfigurationHandler.configuration.getCategory(ConfigurationHandler.CATEGORY_BIOME_IDS)).getChildElements());
			addAll(new ConfigElement(ConfigurationHandler.configuration.getCategory(ConfigurationHandler.CATEGORY_DUNGEON_FREQUENCY)).getChildElements());
		}
	};

	public ModGuiConfig(final GuiScreen guiScreen)
	{
		super(guiScreen, CONFIG_INGAME_OPTIONS, Reference.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(ConfigurationHandler.configuration.toString()));
	}
}
