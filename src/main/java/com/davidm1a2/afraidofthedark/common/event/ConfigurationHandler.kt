package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraftforge.common.config.ConfigElement
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.client.config.GuiConfig
import net.minecraftforge.fml.client.config.IConfigElement
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.io.File

/**
 * Class used to maintain mod configurable options such as biome spawn rates, etc
 *
 * @constructor Initializes this configuration handler from a .cfg file
 * @param configurationFile The .cfg file to initialize from
 * @property configuration A reference to the configuration which is read from the file
 */
class ConfigurationHandler(configurationFile: File)
{
    private val configuration = Configuration(configurationFile)

    ///
    /// Properties below are dynamically loaded from the .cfg file
    ///

    // Multipliers for each dungeon type
    var cryptMultiplier = 1.0f
        private set
    var darkForestMultiplier = 1.0f
        private set
    var gnomishCityFrequency = 1.0f
        private set
    var voidChestMultiplier = 1.0f
        private set
    var witchHutMultiplier = 1.0f
        private set

    // The erie biome frequency
    var erieBiomeFrequency = 10
        private set

    // If debug messages should be sent
    var debugMessages = false
        private set

    // The number of blocks inbetween nightmare and void chest islands
    var blocksBetweenIslands = 1000
        private set

    // The priority to register the world generator at for AOTD
    var worldGenPriority = 0
        private set

    // True if structures should be cached in memory, false otherwise
    var cacheStructures = false
        private set

    // Timeout to clear structures in milliseconds if cache is set to false
    var cacheTimeout: Long = 0
        private set

    // The ID of the dimensions or 0 to mean "pick for me"
    var nightmareDimensionId = 0
        private set
    var voidChestDimensionId = 0
        private set

    // A of characters that the font supports
    var supportedCharacters = setOf<Char>()
        private set

    init
    {
        // Refresh the configuration
        refreshConfiguration()
    }

    /**
     * If our configuration changes we reload it from the file
     *
     * @param event The configuration file change event
     */
    @SubscribeEvent
    fun onConfigurationChangedEvent(event: OnConfigChangedEvent)
    {
        // Make sure the our configuration was the one updated
        if (event.modID == Constants.MOD_ID)
        {
            // Since we modified the configuration in game we write it to the file
            refreshConfiguration()
        }
    }

    /**
     * Re-read the configuration and update all fields internally. If any changed, write changes to the .cfg file
     */
    private fun refreshConfiguration()
    {
        // Add headers to our configuration
        configuration.addCustomCategoryComment(CATEGORY_DUNGEON_FREQUENCY, "Here you can set how frequently certain dungeons appear.")
        configuration.addCustomCategoryComment(CATEGORY_WORLD_GENERATION, "Here you can set world generation properties.")
        configuration.addCustomCategoryComment(CATEGORY_DIMENSION, "Here you can set dimension IDs")

        // Grab all the configuration elements from the file
        cryptMultiplier = configuration.getFloat(
                "Crypt Multiplier",
                CATEGORY_DUNGEON_FREQUENCY,
                1.0f,
                0.0f,
                100.0f,
                "Increases the number of Crypts in the world by the multiplier specified."
        )
        darkForestMultiplier = configuration.getFloat(
                "Dark Forest Multiplier",
                CATEGORY_DUNGEON_FREQUENCY,
                1.0f,
                0.0f,
                100.0f,
                "Increases the number of Dark Forests in the world by the multiplier specified."
        )
        gnomishCityFrequency = configuration.getFloat(
                "Gnomish City Multiplier",
                CATEGORY_DUNGEON_FREQUENCY,
                1.0f,
                0.0f,
                100.0f,
                "Increases the number of Gnomish Cities in the world by the multiplier specified."
        )
        voidChestMultiplier = configuration.getFloat(
                "Void Chest Multiplier",
                CATEGORY_DUNGEON_FREQUENCY,
                1.0f,
                0.0f,
                100.0f,
                "Increases the number of Void Chests in the world by the multiplier specified."
        )
        witchHutMultiplier = configuration.getFloat(
                "Witch Hut Multiplier",
                CATEGORY_DUNGEON_FREQUENCY,
                1.0f,
                0.0f,
                100.0f,
                "Increases the number of Witch Huts in the world by the multiplier specified."
        )

        erieBiomeFrequency = configuration.getInt(
                "Erie Biome Frequency",
                Configuration.CATEGORY_GENERAL,
                10,
                0,
                1000,
                "Increase this value to increase the number of Erie Biomes. 10 is the default MC forest value."
        )

        debugMessages = configuration.getBoolean(
                "Debug Messages",
                Configuration.CATEGORY_GENERAL,
                false,
                "If you wish to receive all possible kinds of spammy debug messages in the console turn this on. Mostly used for developers only."
        )
        val supportedCharactersRaw = configuration.getString(
                "Supported Characters",
                Configuration.CATEGORY_GENERAL,
                """0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIIKLMNOPQRSTUVWXYZ !"#$%&'()*+,-./:;<>=?@[\]^_`{|}~""",
                "A list of supported characters that will be rendered by the Afraid of the Dark font. Add characters from any language to be recognized by the mod. (Ex: цвèÓยั)"
        )
        supportedCharacters = supportedCharactersRaw.toCharArray().toSet()

        blocksBetweenIslands = configuration.getInt(
                "Blocks Between Islands",
                CATEGORY_WORLD_GENERATION,
                1000,
                100,
                100000,
                "Sets the number of blocks that are between nightmare and void chest islands. All players are technically in the same world, so this ensure they never see each other."
        )

        worldGenPriority = configuration.getInt(
                "World Generation Priority",
                CATEGORY_WORLD_GENERATION,
                0,
                -1000,
                1000,
                "Sets the priority for afraid of the dark world generation. Higher numbers result in world generation running after other mods."
        )

        cacheStructures = configuration.getBoolean(
                "Cache Structures",
                CATEGORY_WORLD_GENERATION,
                true,
                "True means structures will be loaded into computer memory when Minecraft is started up. This will accelerate world generation at the cost of RAM usage. False means structures will be loaded when needed, which will require less RAM but can incur lag spikes when finding structures."
        )

        cacheTimeout = configuration.getInt(
                "Cache Timeout",
                CATEGORY_WORLD_GENERATION,
                60000,
                10000,
                Int.MAX_VALUE,
                "Required if 'Cache Structures' is set to false, otherwise ignored. If a structure isn't needed for 'Cache Timeout' milliseconds it will be forgotten and cleared from RAM."
        ).toLong()

        nightmareDimensionId = configuration.getInt(
                "Nightmare Dimension ID",
                CATEGORY_DIMENSION,
                0,
                0,
                256,
                "Sets the dimension ID of the nightmare realm. 0 indicates that the value should be dynamically chosen by forge."
        )
        voidChestDimensionId = configuration.getInt(
                "Void Chest Dimension ID",
                CATEGORY_DIMENSION,
                0,
                0,
                256,
                "Sets the dimension ID of the void chest realm. 0 indicates that the value should be dynamically chosen by forge."
        )

        // If we changed the configuration at all, save it
        if (configuration.hasChanged())
        {
            configuration.save()
        }
    }

    /**
     * @return Returns a list of configurable elements
     */
    fun getInGameConfigurableOptions(): List<IConfigElement>
    {
        return ConfigElement(configuration.getCategory(Configuration.CATEGORY_GENERAL)).childElements +
               ConfigElement(configuration.getCategory(CATEGORY_DUNGEON_FREQUENCY)).childElements +
               ConfigElement(configuration.getCategory(CATEGORY_WORLD_GENERATION)).childElements +
               ConfigElement(configuration.getCategory(CATEGORY_DIMENSION)).childElements
    }

    /**
     * @return The title of this configuration which will be in the form of .minecraft/......./afraidofthedark.cfg
     */
    fun getDisplayTitle(): String
    {
        return GuiConfig.getAbridgedConfigPath(this.configuration.toString())
    }

    companion object
    {
        // Category types
        private const val CATEGORY_DUNGEON_FREQUENCY = "dungeon_frequency"
        private const val CATEGORY_WORLD_GENERATION = "world_generation"
        private const val CATEGORY_DIMENSION = "dimension"
    }
}