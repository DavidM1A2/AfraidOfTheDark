package com.davidm1a2.afraidofthedark.common.config

import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import net.minecraftforge.common.ForgeConfigSpec

/**
 * AfraidOfTheDark common config
 *
 * @param builder The builder to append each config option into
 */
class CommonConfig(builder: ForgeConfigSpec.Builder) {
    // Multipliers for each dungeon type
    private val cryptMultiplier: ForgeConfigSpec.DoubleValue
    private val darkForestMultiplier: ForgeConfigSpec.DoubleValue
    private val gnomishCityFrequency: ForgeConfigSpec.DoubleValue
    private val voidChestMultiplier: ForgeConfigSpec.DoubleValue
    private val witchHutMultiplier: ForgeConfigSpec.DoubleValue
    private val desertOasisMultiplier: ForgeConfigSpec.DoubleValue
    private val observatoryMultiplier: ForgeConfigSpec.DoubleValue

    // The eerie biome frequency
    private val eerieBiomeFrequency: ForgeConfigSpec.IntValue

    // True if structures should be cached in memory, false otherwise
    private val cacheStructures: ForgeConfigSpec.BooleanValue

    // Timeout to clear structures in milliseconds if cache is set to false
    private val cacheTimeout: ForgeConfigSpec.LongValue

    init {
        builder.push("dungeon_frequency")

        cryptMultiplier = builder
            .comment("Increases the number of Crypts in the world by the multiplier specified.")
            .translation("config.afraidofthedark:crypt_multiplier")
            .defineInRange("crypt_multiplier", 1.0, 0.0, 100.0)
        darkForestMultiplier = builder
            .comment("Increases the number of Dark Forests in the world by the multiplier specified.")
            .translation("config.afraidofthedark:dark_forest_multiplier")
            .defineInRange("dark_forest_multiplier", 1.0, 0.0, 100.0)
        gnomishCityFrequency = builder
            .comment("Increases the number of Gnomish Cities in the world by the multiplier specified.")
            .translation("config.afraidofthedark:gnomish_city_multiplier")
            .defineInRange("gnomish_city_multiplier", 1.0, 0.0, 100.0)
        voidChestMultiplier = builder
            .comment("Increases the number of Void Chests in the world by the multiplier specified.")
            .translation("config.afraidofthedark:void_chest_multiplier")
            .defineInRange("void_chest_multiplier", 1.0, 0.0, 100.0)
        witchHutMultiplier = builder
            .comment("Increases the number of Witch Huts in the world by the multiplier specified.")
            .translation("config.afraidofthedark:witch_hut_multiplier")
            .defineInRange("witch_hut_multiplier", 1.0, 0.0, 100.0)
        desertOasisMultiplier = builder
            .comment("Increases the number of Desert Oasis in the world by the multiplier specified.")
            .translation("config.afraidofthedark:desert_oasis_multiplier")
            .defineInRange("desert_oasis_multiplier", 1.0, 0.0, 100.0)
        observatoryMultiplier = builder
            .comment("Increases the number of Observatories in the world by the multiplier specified.")
            .translation("config.afraidofthedark:observatory_multiplier")
            .defineInRange("observatory_multiplier", 1.0, 0.0, 100.0)

        builder.pop()

        builder.push("world_generation")

        eerieBiomeFrequency = builder
            .comment("Increase this value to increase the number of Eerie Biomes. 10 is the default MC forest value.")
            .translation("config.afraidofthedark:eerie_biome_frequency")
            .defineInRange("eerie_biome_frequency", 10, 0, 1000)

        cacheStructures = builder
            .comment("True means structures will be loaded into computer memory when Minecraft is started up. This will accelerate world generation at the cost of RAM usage. False means structures will be loaded when needed, which will require less RAM but can incur lag spikes when finding structures.")
            .translation("config.afraidofthedark:cache_structures")
            .define("cache_structures", true)
        cacheTimeout = builder
            .comment("Required if 'Cache Structures' is set to false, otherwise ignored. If a structure isn't needed for 'Cache Timeout' milliseconds it will be forgotten and cleared from RAM.")
            .translation("config.afraidofthedark:cache_timeout")
            .defineInRange("cache_timeout", 60000L, 10000L, Long.MAX_VALUE)

        builder.pop()
    }

    fun reload() {
        ModCommonConfiguration.cryptMultiplier = cryptMultiplier.get()
        ModCommonConfiguration.darkForestMultiplier = darkForestMultiplier.get()
        ModCommonConfiguration.gnomishCityFrequency = gnomishCityFrequency.get()
        ModCommonConfiguration.voidChestMultiplier = voidChestMultiplier.get()
        ModCommonConfiguration.witchHutMultiplier = witchHutMultiplier.get()
        ModCommonConfiguration.desertOasisMultiplier = desertOasisMultiplier.get()
        ModCommonConfiguration.observatoryMultiplier = observatoryMultiplier.get()
        ModCommonConfiguration.eerieBiomeFrequency = eerieBiomeFrequency.get()
        ModCommonConfiguration.cacheStructures = cacheStructures.get()
        ModCommonConfiguration.cacheTimeout = cacheTimeout.get()
    }
}