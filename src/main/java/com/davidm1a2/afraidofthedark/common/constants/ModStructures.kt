package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.worldGeneration.structure.*

/**
 * A list of structures to be registered
 */
object ModStructures
{
    val CRYPT = StructureCrypt()
    val WITCH_HUT = StructureWitchHut()
    val VOID_CHEST = StructureVoidChest()
    val DARK_FOREST = StructureDarkForest()
    val NIGHTMARE_ISLAND = StructureNightmareIsland()
    val GNOMISH_CITY = StructureGnomishCity()

    var STRUCTURE_LIST = arrayOf(
            CRYPT,
            WITCH_HUT,
            VOID_CHEST,
            DARK_FOREST,
            NIGHTMARE_ISLAND,
            GNOMISH_CITY
    )
}