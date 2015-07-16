/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.initializeMod;

import com.DavidM1A2.AfraidOfTheDark.common.dimension.nightmare.NightmareWorldProvider;
import com.DavidM1A2.AfraidOfTheDark.common.dimension.voidChest.VoidChestWorldProvider;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;

import net.minecraftforge.common.DimensionManager;

public class ModDimensions
{
	public static void intialize()
	{
		DimensionManager.registerProviderType(Constants.NightmareWorld.NIGHTMARE_WORLD_ID, NightmareWorldProvider.class, true);
		DimensionManager.registerDimension(Constants.NightmareWorld.NIGHTMARE_WORLD_ID, Constants.NightmareWorld.NIGHTMARE_WORLD_ID);
		DimensionManager.registerProviderType(Constants.VoidChestWorld.VOID_CHEST_WORLD_ID, VoidChestWorldProvider.class, false);
		DimensionManager.registerDimension(Constants.VoidChestWorld.VOID_CHEST_WORLD_ID, Constants.VoidChestWorld.VOID_CHEST_WORLD_ID);
	}
}
