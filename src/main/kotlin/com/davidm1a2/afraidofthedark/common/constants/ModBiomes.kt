package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.biomes.EerieForestBiome
import com.davidm1a2.afraidofthedark.common.biomes.NightmareBiome
import com.davidm1a2.afraidofthedark.common.biomes.VoidChestBiome

/**
 * Storage for all mod biomes used in AOTD
 */
object ModBiomes {
    val EERIE_FOREST = EerieForestBiome()
    val VOID_CHEST = VoidChestBiome()
    val NIGHTMARE = NightmareBiome()

    // An array containing a list of biomes that AOTD adds
    val BIOME_LIST = arrayOf(
        EERIE_FOREST,
        VOID_CHEST,
        NIGHTMARE
    )
}