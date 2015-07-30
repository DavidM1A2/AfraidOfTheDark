/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.handler;

import java.io.File;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.Refrence;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

// Configuration handler
public class ConfigurationHandler
{
	public static Configuration configuration;
	//public static boolean configValue = false;

	public static void initializataion(final File configFile)
	{
		if (ConfigurationHandler.configuration == null)
		{
			// Create the configuration object from the given file
			ConfigurationHandler.configuration = new Configuration(configFile);
			ConfigurationHandler.loadConfiguration();
		}
	}

	@SubscribeEvent
	public void onConfigurationChangedEvent(final ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if (event.modID.equalsIgnoreCase(Refrence.MOD_ID))
		{
			// Resync the configuration
			ConfigurationHandler.loadConfiguration();
		}
	}

	private static void loadConfiguration()
	{
		// Ex. Config value
		//ConfigurationHandler.configValue = ConfigurationHandler.configuration.getBoolean("configValue", Configuration.CATEGORY_GENERAL, false, "This is an example of a configuration value of type boolean!");

		if (ConfigurationHandler.configuration.hasChanged())
		{
			ConfigurationHandler.configuration.save();
		}
	}
}
