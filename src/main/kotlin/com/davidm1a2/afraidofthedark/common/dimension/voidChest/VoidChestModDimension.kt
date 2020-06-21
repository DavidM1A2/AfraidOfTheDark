package com.davidm1a2.afraidofthedark.common.dimension.voidChest

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.world.dimension.Dimension
import net.minecraft.world.dimension.DimensionType
import net.minecraftforge.common.ModDimension
import java.util.function.Function

/**
 * The void chest mod dimension object
 */
class VoidChestModDimension : ModDimension() {
    init {
        setRegistryName(Constants.MOD_ID, "void_chest")
    }

    /**
     * Getter for the factory to make a void chest dimension
     */
    override fun getFactory(): Function<DimensionType, out Dimension> {
        return Function { VoidChestDimension() }
    }
}