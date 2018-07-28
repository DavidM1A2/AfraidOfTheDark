package com.DavidM1A2.AfraidOfTheDark.common.constants;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to maintain mod configurable options such as biome spawn rates, etc
 */
public class ConfigurationHandler
{
	// Singleton design pattern for this class since we only have 1 configuration
	private static final ConfigurationHandler INSTANCE = new ConfigurationHandler();

	// A reference to the configuration which is read from the file
	private Configuration configuration;

	///
	/// A list of configurable properties
	///

	private int biomeErieID = 68;
	private int biomeVoidChestID = 69;
	private int biomeNightmareID = 70;
	private int erieBiomeFrequency = 10;
	private float dungeonFrequencyMultiplier = 1.0f;
	private float cryptFrequency = 0.5f;
	private float darkForestFrequency = 0.4f;
	private float gnomishCityFrequency = 0.4f;
	private float springFrequency = 0.32f;
	private float voidChestFrequency = 0.24f;
	private float witchHutFrequency = 0.8f;
	private boolean debugMessages = false;
	private boolean enableAOTDAnimations = true;
	private boolean enableWorldGenLightUpdates = false;

	///
	/// Two categories of properties
	///

	private static final String CATEGORY_BIOME_IDS = "biome_ids";
	private static final String CATEGORY_DUNGEON_FREQUENCY = "dungeon_frequency";

	/**
	 * Initializes this configuration handler from a .cfg file
	 *
	 * @param configurationFile The .cfg file to initialize from
	 */
	public void initFromConfigurationFile(File configurationFile)
	{
		// Make sure the configuration is null, then initialize it
		if (this.configuration == null)
		{
			// Create the configuration object from the given file
			this.configuration = new Configuration(configurationFile);
			// Refresh the configuration
			this.refreshConfiguration();
		}
	}

	/**
	 * If our configuration changes we reload it from the file
	 *
	 * @param event The configuration file change event
	 */
	@SubscribeEvent
	public void onConfigurationChangedEvent(final ConfigChangedEvent.OnConfigChangedEvent event)
	{
		// Make sure the our configuration was the one updated
		if (event.getModID().equals(Constants.MOD_ID))
		{
			// Since we modified the configuration in game we write it to the file
			this.refreshConfiguration();
		}
	}

	/**
	 * Re-read the configuration and update all fields internally. If any changed, write changes to the .cfg file
	 */
	private void refreshConfiguration()
	{
		// Add headers to our configuration
		this.configuration.addCustomCategoryComment(CATEGORY_BIOME_IDS, "Here you can configure Afraid of the Dark biome IDs");
		this.configuration.addCustomCategoryComment(CATEGORY_DUNGEON_FREQUENCY, "Here you can set how frequently certain dungeons appear.");

		// Pull all configuration elements from the file
		this.biomeErieID = this.configuration.getInt("Erie Biome ID", CATEGORY_BIOME_IDS, 68, 5, 255, "The Biome ID for the erie forest. Use this to make this mod compatable with other biome mods.");
		this.biomeNightmareID = this.configuration.getInt("Nightmare Biome ID", CATEGORY_BIOME_IDS, 69, 5, 255, "The Biome ID for the nightmare. Use this to make this mod compatable with other biome mods.");
		this.biomeVoidChestID = this.configuration.getInt("Void Chest Biome ID", CATEGORY_BIOME_IDS, 70, 5, 255, "The Biome ID for the Void Chest. Use this to make this mod compatable with other biome mods.");
		this.erieBiomeFrequency = this.configuration.getInt("Erie Biome Frequency", Configuration.CATEGORY_GENERAL, 10, 0, 1000, "Increase this value to increase the number of Erie Biomes. 10 is the default MC forest value.");
		this.dungeonFrequencyMultiplier = this.configuration.getFloat("Dungeon Frequency Multiplier", CATEGORY_DUNGEON_FREQUENCY, 1.0f, 0.0f, 1000.0f, "The dungeon frequency multiplier increases or decreases dungeon rarity across ALL Afraid of the Dark dungeons. (ex. 2.0 would result in double the dungeons and 0.5 in half)");

		this.cryptFrequency = this.configuration.getFloat("Crypt Frequency", CATEGORY_DUNGEON_FREQUENCY, 0.5f, 0.0f, 100.0f, "This property is the percentage chance per chunk that a crypt will spawn.");
		this.darkForestFrequency = this.configuration.getFloat("Dark Forest Frequency", CATEGORY_DUNGEON_FREQUENCY, 0.4f, 0.0f, 100.0f, "This property is the percentage chance per chunk that a dark forest will spawn.");
		this.gnomishCityFrequency = this.configuration.getFloat("Gnomish City Frequency", CATEGORY_DUNGEON_FREQUENCY, 0.4f, 0.0f, 100.0f, "This property is the percentage chance per chunk that a gnomish city will spawn.");
		this.springFrequency = this.configuration.getFloat("Spring Frequency", CATEGORY_DUNGEON_FREQUENCY, 0.32f, 0.0f, 100.0f, "This property is the percentage chance per chunk that a spring will spawn.");
		this.voidChestFrequency = this.configuration.getFloat("Void Chest Frequency", CATEGORY_DUNGEON_FREQUENCY, 0.24f, 0.0f, 100.0f, "This property is the percentage chance per chunk that a void chest will spawn.");
		this.witchHutFrequency = this.configuration.getFloat("Witch Hut Frequency", CATEGORY_DUNGEON_FREQUENCY, 0.8f, 0.0f, 100.0f, "This property is the percentage chance per chunk that a witch hut will spawn.");

		this.debugMessages = this.configuration.getBoolean("Debug Messages", Configuration.CATEGORY_GENERAL, false, "If you wish to receive all possible kinds of spammy debug messages in the console turn this on. Mostly used for developers only.");

		this.enableAOTDAnimations = this.configuration.getBoolean("Entity Animations Enabled", Configuration.CATEGORY_GENERAL, true, "Disable this to remove all animations from entities in the Afraid of the Dark mod. This may improve performance but mod entities will no longer have animations.");

		this.enableWorldGenLightUpdates = this.configuration.getBoolean("World Generation Lighting Updates", Configuration.CATEGORY_GENERAL, false, "Enabling this will decrease world generation performance but decrease the chance of lighting glitches in AOTD dungeons.");

		// If we changed the configuration at all, save it
		if (this.configuration.hasChanged())
		{
			this.configuration.save();
		}
	}

	/**
	 * @return Returns a list of configurable elements
	 */
	public List<IConfigElement> getInGameConfigurableOptions()
	{
		return new ArrayList<IConfigElement>()
		{{
			this.addAll(new ConfigElement(ConfigurationHandler.this.configuration.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements());
			this.addAll(new ConfigElement(ConfigurationHandler.this.configuration.getCategory(CATEGORY_BIOME_IDS)).getChildElements());
			this.addAll(new ConfigElement(ConfigurationHandler.this.configuration.getCategory(CATEGORY_DUNGEON_FREQUENCY)).getChildElements());
		}};
	}

	/**
	 * @return The title of this configuration which will be in the form of .minecraft/......./afraidofthedark.cfg
	 */
	public String getDisplayTitle()
	{
		return GuiConfig.getAbridgedConfigPath(this.configuration.toString());
	}

	/**
	 * @return The one instance of the configuration handler
	 */
	public static ConfigurationHandler getInstance()
	{
		return INSTANCE;
	}

	///
	/// Getters for each configuration property
	///

	public int getBiomeErieID()
	{
		return this.biomeErieID;
	}

	public int getBiomeVoidChestID()
	{
		return this.biomeVoidChestID;
	}

	public int getBiomeNightmareID()
	{
		return this.biomeNightmareID;
	}

	public int getErieBiomeFrequency()
	{
		return this.erieBiomeFrequency;
	}

	public float getDungeonFrequencyMultiplier()
	{
		return this.dungeonFrequencyMultiplier;
	}

	public float getCryptFrequency()
	{
		return this.cryptFrequency;
	}

	public float getDarkForestFrequency()
	{
		return this.darkForestFrequency;
	}

	public float getGnomishCityFrequency()
	{
		return this.gnomishCityFrequency;
	}

	public float getSpringFrequency()
	{
		return this.springFrequency;
	}

	public float getVoidChestFrequency()
	{
		return this.voidChestFrequency;
	}

	public float getWitchHutFrequency()
	{
		return this.witchHutFrequency;
	}

	public boolean showDebugMessages()
	{
		return this.debugMessages;
	}

	public boolean enableAOTDAnimations()
	{
		return this.enableAOTDAnimations;
	}

	public boolean enableWorldGenLightUpdates()
	{
		return this.enableWorldGenLightUpdates;
	}
}
