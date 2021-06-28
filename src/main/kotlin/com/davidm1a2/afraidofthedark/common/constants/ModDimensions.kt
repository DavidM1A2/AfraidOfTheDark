package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.dimension.nightmare.NightmareModDimension
import com.davidm1a2.afraidofthedark.common.dimension.voidChest.VoidChestModDimension
import net.minecraft.world.dimension.DimensionType
import net.minecraftforge.common.DimensionManager

/**
 * Mod dimensions class initializes dimension types used in AOTD
 */
object ModDimensions {
    val NIGHTMARE = NightmareModDimension()
    val VOID_CHEST = VoidChestModDimension()

    // These are initialized later
    val VOID_CHEST_TYPE: DimensionType by lazy {
        DimensionManager.registerOrGetDimension(VOID_CHEST.registryName, VOID_CHEST, null, false)
    }
    val NIGHTMARE_TYPE: DimensionType by lazy {
        DimensionManager.registerOrGetDimension(NIGHTMARE.registryName, NIGHTMARE, null, false)
    }

    val DIMENSION_LIST = arrayOf(
        NIGHTMARE,
        VOID_CHEST
    )
}