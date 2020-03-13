package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.biomes.BiomeEerieForest
import com.davidm1a2.afraidofthedark.common.biomes.BiomeNightmare
import com.davidm1a2.afraidofthedark.common.biomes.BiomeVoidChest

/**
 * Storage for all mod biomes used in AOTD
 */
object ModBiomes {
    val EERIE_FOREST = BiomeEerieForest()
    val VOID_CHEST = BiomeVoidChest()
    val NIGHTMARE = BiomeNightmare()

    // An array containing a list of biomes that AOTD adds
    val BIOME_LIST = arrayOf(
        EERIE_FOREST,
        VOID_CHEST,
        NIGHTMARE
    )
}