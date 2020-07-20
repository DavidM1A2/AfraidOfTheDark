package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.world.structure.crypt.CryptStructure
import com.davidm1a2.afraidofthedark.common.world.structure.crypt.CryptStructureStart

/**
 * A list of structures to be registered
 */
object ModStructures {
    val CRYPT = CryptStructure()

    val STRUCTURES = listOf(
        CRYPT
    )

    val STRUCTURE_STARTS = listOf(
        CRYPT to CryptStructureStart::class.java
    )
}