package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.world.structure.crypt.CryptStructure
import com.davidm1a2.afraidofthedark.common.world.structure.crypt.CryptStructureStart
import com.davidm1a2.afraidofthedark.common.world.structure.witchhut.WitchHutStructure
import com.davidm1a2.afraidofthedark.common.world.structure.witchhut.WitchHutStructureStart

/**
 * A list of structures to be registered
 */
object ModStructures {
    val CRYPT = CryptStructure()
    val WITCH_HUT = WitchHutStructure()

    val STRUCTURES = listOf(
        CRYPT,
        WITCH_HUT
    )

    val STRUCTURE_STARTS = listOf(
        CRYPT to CryptStructureStart::class.java,
        WITCH_HUT to WitchHutStructureStart::class.java
    )
}