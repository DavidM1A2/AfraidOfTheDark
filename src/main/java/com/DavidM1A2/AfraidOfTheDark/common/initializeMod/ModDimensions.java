/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.initializeMod;

import net.minecraftforge.common.DimensionManager;

import com.DavidM1A2.AfraidOfTheDark.common.dimension.nightmare.NightmareWorldProvider;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;

public class ModDimensions
{
	public static void intialize()
	{
		DimensionManager.registerProviderType(Constants.NightmareWorld.NIGHTMARE_WORLD_ID, NightmareWorldProvider.class, true);
		DimensionManager.registerDimension(Constants.NightmareWorld.NIGHTMARE_WORLD_ID, Constants.NightmareWorld.NIGHTMARE_WORLD_ID);
	}
}
