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
	public static int biomeErieID = 68;
	public static int biomeVoidChestID = 69;
	public static int biomeNightmareID = 70;
	public static int erieBiomeFrequency = 10;
	public static float dungeonFrequencyMultiplier = 1.0f;

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
		ConfigurationHandler.biomeErieID = ConfigurationHandler.configuration.getInt("biomeErieID", Configuration.CATEGORY_GENERAL, 68, 5, 255, "The Biome ID for the erie forest. Use this to make this mod compatable with other biome mods.");
		ConfigurationHandler.biomeNightmareID = ConfigurationHandler.configuration.getInt("biomeNightmareID", Configuration.CATEGORY_GENERAL, 69, 5, 255, "The Biome ID for the nightmare. Use this to make this mod compatable with other biome mods.");
		ConfigurationHandler.biomeVoidChestID = ConfigurationHandler.configuration.getInt("biomeVoidChestID", Configuration.CATEGORY_GENERAL, 70, 5, 255, "The Biome ID for the Void Chest. Use this to make this mod compatable with other biome mods.");
		ConfigurationHandler.erieBiomeFrequency = ConfigurationHandler.configuration.getInt("erieBiomeFrequency", Configuration.CATEGORY_GENERAL, 10, 0, 1000, "Increase this value to increase the number of Erie Biomes. 10 is the default MC forest value.");
		ConfigurationHandler.dungeonFrequencyMultiplier = ConfigurationHandler.configuration.getFloat("dungeonFrequencyMultiplier", Configuration.CATEGORY_GENERAL, 1.0f, 0.0f, 1000.0f,
				"The dungeon frequency multiplier increases or decreases dungeon rarity. (ex. 2.0 would result in double the dungeons and 0.5 in half)");

		if (ConfigurationHandler.configuration.hasChanged())
		{
			ConfigurationHandler.configuration.save();
		}
	}
}
