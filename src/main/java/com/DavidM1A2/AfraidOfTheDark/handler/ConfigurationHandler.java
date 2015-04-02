/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.handler;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

// Configuration handler
public class ConfigurationHandler
{
	public static Configuration configuration;
	public static boolean configValue = false;

	public static void initializataion(File configFile)
	{
		if (configuration == null)
		{
			// Create the configuration object from the given file
			configuration = new Configuration(configFile);
			loadConfiguration();
		}
	}

	@SubscribeEvent
	public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.modID.equalsIgnoreCase(Refrence.MOD_ID))
		{
			// Resync the configuration
			loadConfiguration();
		}
	}

	private static void loadConfiguration()
	{
		// Ex. Config value
		configValue = configuration.getBoolean("configValue", Configuration.CATEGORY_GENERAL, false, "This is an example of a configuration value of type boolean!");

		if (configuration.hasChanged())
		{
			configuration.save();
		}
	}
}
