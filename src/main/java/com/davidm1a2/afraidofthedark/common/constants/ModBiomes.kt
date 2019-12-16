package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.biomes.BiomeErieForest
import com.davidm1a2.afraidofthedark.common.biomes.BiomeNightmare
import com.davidm1a2.afraidofthedark.common.biomes.BiomeVoidChest

/**
 * Storage for all mod biomes used in AOTD
 */
object ModBiomes
{
    // The Erie Forest biome
    val ERIE_FOREST = BiomeErieForest()
    // The Void Chest biome
    val VOID_CHEST = BiomeVoidChest()
    // The Nightmare biome
    val NIGHTMARE = BiomeNightmare()

    // An array containing a list of biomes that AOTD adds
    val BIOME_LIST = arrayOf(
        ERIE_FOREST,
        VOID_CHEST,
        NIGHTMARE
    )
}