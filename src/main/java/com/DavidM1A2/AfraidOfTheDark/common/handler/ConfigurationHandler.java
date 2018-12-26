/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.handler;

import java.io.File;

import com.DavidM1A2.AfraidOfTheDark.common.reference.Reference;

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
	public static float cryptFrequency = 0.5f;
	public static float darkForestFrequency = 0.4f;
	public static float gnomishCityFrequency = 0.4f;
	public static float springFrequency = 0.32f;
	public static float voidChestFrequency = 0.24f;
	public static float witchHutFrequency = 0.8f;
	public static boolean debugMessages = false;
	public static boolean enableAOTDAnimations = true;
	public static boolean enableWorldGenLightUpdates = false;

	public static final String CATEGORY_BIOME_IDS = "biome ids";
	public static final String CATEGORY_DUNGEON_FREQUENCY = "dungeon frequency";

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
		if (event.modID.equalsIgnoreCase(Reference.MOD_ID))
		{
			// Resync the configuration
			ConfigurationHandler.loadConfiguration();
		}
	}

	private static void loadConfiguration()
	{
		ConfigurationHandler.configuration.addCustomCategoryComment(CATEGORY_BIOME_IDS, "Here you can configure Afraid of the Dark biome IDs");
		ConfigurationHandler.configuration.addCustomCategoryComment(CATEGORY_DUNGEON_FREQUENCY, "Here you can set how frequently certain dungeons appear.");
		ConfigurationHandler.biomeErieID = ConfigurationHandler.configuration.getInt("biomeErieID", CATEGORY_BIOME_IDS, 68, 5, 255, "The Biome ID for the erie forest. Use this to make this mod compatable with other biome mods.");
		ConfigurationHandler.biomeNightmareID = ConfigurationHandler.configuration.getInt("biomeNightmareID", CATEGORY_BIOME_IDS, 69, 5, 255, "The Biome ID for the nightmare. Use this to make this mod compatable with other biome mods.");
		ConfigurationHandler.biomeVoidChestID = ConfigurationHandler.configuration.getInt("biomeVoidChestID", CATEGORY_BIOME_IDS, 70, 5, 255, "The Biome ID for the Void Chest. Use this to make this mod compatable with other biome mods.");
		ConfigurationHandler.erieBiomeFrequency = ConfigurationHandler.configuration.getInt("erieBiomeFrequency", Configuration.CATEGORY_GENERAL, 10, 0, 1000, "Increase this value to increase the number of Erie Biomes. 10 is the default MC forest value.");
		ConfigurationHandler.dungeonFrequencyMultiplier = ConfigurationHandler.configuration.getFloat("dungeonFrequencyMultiplier", CATEGORY_DUNGEON_FREQUENCY, 1.0f, 0.0f, 1000.0f,
				"The dungeon frequency multiplier increases or decreases dungeon rarity across ALL Afraid of the Dark dungeons. (ex. 2.0 would result in double the dungeons and 0.5 in half)");

		ConfigurationHandler.cryptFrequency = ConfigurationHandler.configuration.getFloat("cryptFrequency", CATEGORY_DUNGEON_FREQUENCY, 0.5f, 0.0f, 100.0f, "This property is the percentage chance per chunk that a crypt will spawn.");
		ConfigurationHandler.darkForestFrequency = ConfigurationHandler.configuration.getFloat("darkForestFrequency", CATEGORY_DUNGEON_FREQUENCY, 0.4f, 0.0f, 100.0f, "This property is the percentage chance per chunk that a dark forest will spawn.");
		ConfigurationHandler.gnomishCityFrequency = ConfigurationHandler.configuration.getFloat("gnomishCityFrequency", CATEGORY_DUNGEON_FREQUENCY, 0.4f, 0.0f, 100.0f, "This property is the percentage chance per chunk that a gnomish city will spawn.");
		ConfigurationHandler.springFrequency = ConfigurationHandler.configuration.getFloat("springFrequency", CATEGORY_DUNGEON_FREQUENCY, 0.32f, 0.0f, 100.0f, "This property is the percentage chance per chunk that a spring will spawn.");
		ConfigurationHandler.voidChestFrequency = ConfigurationHandler.configuration.getFloat("voidChestFrequency", CATEGORY_DUNGEON_FREQUENCY, 0.24f, 0.0f, 100.0f, "This property is the percentage chance per chunk that a void chest will spawn.");
		ConfigurationHandler.witchHutFrequency = ConfigurationHandler.configuration.getFloat("witchHutFrequency", CATEGORY_DUNGEON_FREQUENCY, 0.8f, 0.0f, 100.0f, "This property is the percentage chance per chunk that a witch hut will spawn.");

		ConfigurationHandler.debugMessages = ConfigurationHandler.configuration.getBoolean("debugMessages", Configuration.CATEGORY_GENERAL, false, "If you wish to receive all possible kinds of spammy debug messages in the console turn this on. Mostly used for developers only.");

		ConfigurationHandler.enableAOTDAnimations = ConfigurationHandler.configuration.getBoolean("enableAOTDEntityAnimations", Configuration.CATEGORY_GENERAL, true,
				"Disable this to remove all animations from entities in the Afraid of the Dark mod. This may improve performance but mod entities will no longer have animations.");

		ConfigurationHandler.enableWorldGenLightUpdates = ConfigurationHandler.configuration.getBoolean("enableWorldGenLightUpdates", Configuration.CATEGORY_GENERAL, false, "Enabling this will decrease world generation performance but decrease the chance of lighting glitches in AOTD dungeons.");

		if (ConfigurationHandler.configuration.hasChanged())
		{
			ConfigurationHandler.configuration.save();
		}
	}
}
