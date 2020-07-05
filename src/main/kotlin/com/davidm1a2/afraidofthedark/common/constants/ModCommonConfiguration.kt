package com.davidm1a2.afraidofthedark.common.constants

/**
 * All the common configuration that AOTD gets from "afraidofthedark.cfg"
 */
object ModCommonConfiguration {
    // Multipliers for each dungeon type
    var cryptMultiplier = 1.0
    var darkForestMultiplier = 1.0
    var gnomishCityFrequency = 1.0
    var voidChestMultiplier = 1.0
    var witchHutMultiplier = 1.0
    var desertOasisMultiplier = 1.0
    var observatoryMultiplier = 1.0

    // The eerie biome frequency
    var eerieBiomeFrequency = 10

    // The number of blocks inbetween nightmare and void chest islands
    var blocksBetweenIslands = 1000

    // The priority to register the world generator at for AOTD
    var worldGenPriority = 0

    // True if structures should be cached in memory, false otherwise
    var cacheStructures = false

    // Timeout to clear structures in milliseconds if cache is set to false
    var cacheTimeout = 0L
}