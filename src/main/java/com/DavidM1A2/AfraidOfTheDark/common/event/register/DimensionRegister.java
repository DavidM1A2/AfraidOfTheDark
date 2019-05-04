package com.DavidM1A2.afraidofthedark.common.event.register;

import com.DavidM1A2.afraidofthedark.common.constants.ModDimensions;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

/**
 * Class that registers all AOTD dimensions into the game
 */
public class DimensionRegister
{
	/**
	 * Registers all AOTD dimensions
	 */
	public static void initialize()
	{
		for (DimensionType dimensionType : ModDimensions.DIMENSION_LIST)
			DimensionManager.registerDimension(dimensionType.getId(), dimensionType);
	}
}
