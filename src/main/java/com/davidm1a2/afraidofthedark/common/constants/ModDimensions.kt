package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.dimension.NoopTeleporter
import net.minecraft.world.DimensionType

/**
 * Mod dimensions class initializes dimension types used in AOTD
 */
object ModDimensions
{
    // Teleporter class used by all dimension teleportation, this does nothing
    val NOOP_TELEPORTER = NoopTeleporter()

    // These are initialized later when we know the right dimension ID
    lateinit var VOID_CHEST: DimensionType
    lateinit var NIGHTMARE: DimensionType
}