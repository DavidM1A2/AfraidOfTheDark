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

    // These are initialized later. It's unreliable to use these client side, because the client never initializes these
    // fields when connecting to a dedicated server. Why? Because RegisterDimensionsEvent never fires client side. Why
    // store these at all then? It turns out teleporting a player requires use of this "DimensionType" object. Since we
    // only teleport players from server side, this is fine.
    lateinit var VOID_CHEST_TYPE: DimensionType
    lateinit var NIGHTMARE_TYPE: DimensionType

    val DIMENSION_LIST = arrayOf(
        NIGHTMARE,
        VOID_CHEST
    )
}