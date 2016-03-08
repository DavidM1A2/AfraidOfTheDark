/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.initializeMod;

import com.DavidM1A2.AfraidOfTheDark.common.dimension.nightmare.NightmareWorldProvider;
import com.DavidM1A2.AfraidOfTheDark.common.dimension.voidChest.VoidChestWorldProvider;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.AOTDDimensions;

import net.minecraftforge.common.DimensionManager;

public class ModDimensions
{
	public static void intialize()
	{
		DimensionManager.registerProviderType(AOTDDimensions.Nightmare.getWorldID(), NightmareWorldProvider.class, false);
		DimensionManager.registerDimension(AOTDDimensions.Nightmare.getWorldID(), AOTDDimensions.Nightmare.getWorldID());
		DimensionManager.registerProviderType(AOTDDimensions.VoidChest.getWorldID(), VoidChestWorldProvider.class, false);
		DimensionManager.registerDimension(AOTDDimensions.VoidChest.getWorldID(), AOTDDimensions.VoidChest.getWorldID());
	}
}
