package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.dimension.nightmare.NightmareModDimension
import com.davidm1a2.afraidofthedark.common.dimension.voidChest.VoidChestModDimension
import net.minecraft.world.dimension.DimensionType

/**
 * Mod dimensions class initializes dimension types used in AOTD
 */
object ModDimensions {
    val NIGHTMARE = NightmareModDimension()
    val VOID_CHEST = VoidChestModDimension()

    // These are initialized later
    lateinit var VOID_CHEST_TYPE: DimensionType
    lateinit var NIGHTMARE_TYPE: DimensionType

    val DIMENSION_LIST = arrayOf(
        NIGHTMARE,
        VOID_CHEST
    )
}