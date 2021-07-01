package com.davidm1a2.afraidofthedark.common.dimension.voidChest

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.world.World
import net.minecraft.world.dimension.Dimension
import net.minecraft.world.dimension.DimensionType
import net.minecraftforge.common.ModDimension
import java.util.function.BiFunction

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
    override fun getFactory(): BiFunction<World, DimensionType, out Dimension> {
        return BiFunction { world, dimensionType -> VoidChestDimension(world, dimensionType) }
    }
}