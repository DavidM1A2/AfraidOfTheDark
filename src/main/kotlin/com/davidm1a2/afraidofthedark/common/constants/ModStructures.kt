package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.world.structure.crypt.CryptStructure
import com.davidm1a2.afraidofthedark.common.world.structure.crypt.CryptStructureStart
import com.davidm1a2.afraidofthedark.common.world.structure.darkforest.DarkForestStructure
import com.davidm1a2.afraidofthedark.common.world.structure.darkforest.DarkForestStructureStart
import com.davidm1a2.afraidofthedark.common.world.structure.observatory.ObservatoryStructure
import com.davidm1a2.afraidofthedark.common.world.structure.observatory.ObservatoryStructureStart
import com.davidm1a2.afraidofthedark.common.world.structure.voidchest.VoidChestStructure
import com.davidm1a2.afraidofthedark.common.world.structure.voidchest.VoidChestStructureStart
import com.davidm1a2.afraidofthedark.common.world.structure.witchhut.WitchHutStructure
import com.davidm1a2.afraidofthedark.common.world.structure.witchhut.WitchHutStructureStart

/**
 * A list of structures to be registered
 */
object ModStructures {
    val CRYPT = CryptStructure()
    val WITCH_HUT = WitchHutStructure()
    val VOID_CHEST = VoidChestStructure()
    val OBSERVATORY = ObservatoryStructure()
    val DARK_FOREST = DarkForestStructure()

    val STRUCTURES = listOf(
        CRYPT,
        WITCH_HUT,
        VOID_CHEST,
        OBSERVATORY,
        DARK_FOREST
    )

    val STRUCTURE_STARTS = listOf(
        CRYPT to CryptStructureStart::class.java,
        WITCH_HUT to WitchHutStructureStart::class.java,
        VOID_CHEST to VoidChestStructureStart::class.java,
        OBSERVATORY to ObservatoryStructureStart::class.java,
        DARK_FOREST to DarkForestStructureStart::class.java
    )
}