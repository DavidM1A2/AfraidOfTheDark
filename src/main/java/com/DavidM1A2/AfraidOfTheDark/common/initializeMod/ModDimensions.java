/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.initializeMod;

import com.DavidM1A2.AfraidOfTheDark.common.dimension.nightmare.NightmareWorldProvider;
import com.DavidM1A2.AfraidOfTheDark.common.dimension.voidChest.VoidChestWorldProvider;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDDimensions;

import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class ModDimensions
{
	public static void intialize()
	{
		DimensionType.register("Nightmare", "", AOTDDimensions.Nightmare.getWorldID(), NightmareWorldProvider.class, false);
		DimensionManager.registerDimension(AOTDDimensions.Nightmare.getWorldID(), DimensionType.getById(AOTDDimensions.Nightmare.getWorldID()));
		DimensionType.register("Void Chest", "", AOTDDimensions.VoidChest.getWorldID(), VoidChestWorldProvider.class, false);
		DimensionManager.registerDimension(AOTDDimensions.VoidChest.getWorldID(), DimensionType.getById(AOTDDimensions.VoidChest.getWorldID()));
	}
}
