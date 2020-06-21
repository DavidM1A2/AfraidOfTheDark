package com.davidm1a2.afraidofthedark.common.dimension.nightmare

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.world.dimension.Dimension
import net.minecraft.world.dimension.DimensionType
import net.minecraftforge.common.ModDimension
import java.util.function.Function

/**
 * The nightmare mod dimension object
 */
class NightmareModDimension : ModDimension() {
    init {
        setRegistryName(Constants.MOD_ID, "nightmare")
    }

    /**
     * Getter for the factory to make a nightmare dimension
     */
    override fun getFactory(): Function<DimensionType, out Dimension> {
        return Function { NightmareDimension() }
    }
}