package com.DavidM1A2.afraidofthedark.common.event;

import com.DavidM1A2.afraidofthedark.common.constants.Constants;
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
    private static final String CATEGORY_DUNGEON_FREQUENCY = "dungeon_frequency";

    ///
    /// A list of configurable properties
    ///

    // Category types
    private static final String CATEGORY_WORLD_GENERATION = "world_generation";
    private static final String CATEGORY_DIMENSION = "dimension";

    // A reference to the configuration which is read from the file
    private Configuration configuration;

    // Multipliers for each dungeon type
    private float cryptMultiplier = 1.0f;
    private float darkForestMultiplier = 1.0f;
    private float gnomishCityFrequency = 1.0f;
    private float voidChestMultiplier = 1.0f;
    private float witchHutMultiplier = 1.0f;

    // The erie biome frequency
    private int erieBiomeFrequency = 10;

    // If debug messages should be sent
    private boolean debugMessages = false;

    // The number of blocks inbetween nightmare and void chest islands
    private int blocksBetweenIslands = 1000;

    // The priority to register the world generator at for AOTD
    private int worldGenPriority = 0;

    // The ID of the dimensions or -1 to mean "pick for me"
    private int nightmareDimensionId = -1;
    private int voidChestDimensionId = -1;

    /**
     * Initializes this configuration handler from a .cfg file
     *
     * @param configurationFile The .cfg file to initialize from
     */
    public ConfigurationHandler(File configurationFile)
    {
        // Create the configuration object from the given file
        this.configuration = new Configuration(configurationFile);
        // Refresh the configuration
        this.refreshConfiguration();
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
        this.configuration.addCustomCategoryComment(CATEGORY_DUNGEON_FREQUENCY, "Here you can set how frequently certain dungeons appear.");
        this.configuration.addCustomCategoryComment(CATEGORY_WORLD_GENERATION, "Here you can set world generation properties.");
        this.configuration.addCustomCategoryComment(CATEGORY_DIMENSION, "Here you can set dimension IDs");

        // Grab all the configuration elements from the file

        this.cryptMultiplier = this.configuration.getFloat("Crypt Multiplier", CATEGORY_DUNGEON_FREQUENCY, 1.0f, 0.0f, 100.0f, "Increases the number of Crypts in the world by the multiplier specified.");
        this.darkForestMultiplier = this.configuration.getFloat("Dark Forest Multiplier", CATEGORY_DUNGEON_FREQUENCY, 1.0f, 0.0f, 100.0f, "Increases the number of Dark Forests in the world by the multiplier specified.");
        this.gnomishCityFrequency = this.configuration.getFloat("Gnomish City Multiplier", CATEGORY_DUNGEON_FREQUENCY, 1.0f, 0.0f, 100.0f, "Increases the number of Gnomish Cities in the world by the multiplier specified.");
        this.voidChestMultiplier = this.configuration.getFloat("Void Chest Multiplier", CATEGORY_DUNGEON_FREQUENCY, 1.0f, 0.0f, 100.0f, "Increases the number of Void Chests in the world by the multiplier specified.");
        this.witchHutMultiplier = this.configuration.getFloat("Witch Hut Multiplier", CATEGORY_DUNGEON_FREQUENCY, 1.0f, 0.0f, 100.0f, "Increases the number of Witch Huts in the world by the multiplier specified.");

        this.erieBiomeFrequency = this.configuration.getInt("Erie Biome Frequency", Configuration.CATEGORY_GENERAL, 10, 0, 1000, "Increase this value to increase the number of Erie Biomes. 10 is the default MC forest value.");

        this.debugMessages = this.configuration.getBoolean("Debug Messages", Configuration.CATEGORY_GENERAL, false, "If you wish to receive all possible kinds of spammy debug messages in the console turn this on. Mostly used for developers only.");

        this.blocksBetweenIslands = this.configuration.getInt("Blocks Between Islands", CATEGORY_WORLD_GENERATION, 1000, 100, 100000, "Sets the number of blocks that are between nightmare and void chest islands. All players are technically in the same world, so this ensure they never see each other.");

        this.worldGenPriority = this.configuration.getInt("World Generation Priority", CATEGORY_WORLD_GENERATION, 0, -1000, 1000, "Sets the priority for afraid of the dark world generation. Higher numbers result in world generation running after other mods.");

        this.nightmareDimensionId = this.configuration.getInt("Nightmare Dimension ID", CATEGORY_DIMENSION, 0, 0, 256, "Sets the dimension ID of the nightmare realm. 0 indicates that the value should be dynamically chosen by forge.");
        this.voidChestDimensionId = this.configuration.getInt("Void Chest Dimension ID", CATEGORY_DIMENSION, 0, 0, 256, "Sets the dimension ID of the void chest realm. 0 indicates that the value should be dynamically chosen by forge.");

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
            this.addAll(new ConfigElement(ConfigurationHandler.this.configuration.getCategory(CATEGORY_DUNGEON_FREQUENCY)).getChildElements());
            this.addAll(new ConfigElement(ConfigurationHandler.this.configuration.getCategory(CATEGORY_DIMENSION)).getChildElements());
        }};
    }

    /**
     * @return The title of this configuration which will be in the form of .minecraft/......./afraidofthedark.cfg
     */
    public String getDisplayTitle()
    {
        return GuiConfig.getAbridgedConfigPath(this.configuration.toString());
    }

    ///
    /// Getters for each configuration property
    ///

    public float getCryptMultiplier()
    {
        return this.cryptMultiplier;
    }

    public float getDarkForestMultiplier()
    {
        return this.darkForestMultiplier;
    }

    public float getGnomishCityFrequency()
    {
        return this.gnomishCityFrequency;
    }

    public float getVoidChestMultiplier()
    {
        return this.voidChestMultiplier;
    }

    public float getWitchHutMultiplier()
    {
        return this.witchHutMultiplier;
    }

    public int getErieBiomeFrequency()
    {
        return this.erieBiomeFrequency;
    }

    public boolean showDebugMessages()
    {
        return this.debugMessages;
    }

    public int getBlocksBetweenIslands()
    {
        return blocksBetweenIslands;
    }

    public int getWorldGenPriority()
    {
        return this.worldGenPriority;
    }

    public int getNightmareDimensionId()
    {
        return this.nightmareDimensionId;
    }

    public int getVoidChestDimensionId()
    {
        return this.voidChestDimensionId;
    }
}
