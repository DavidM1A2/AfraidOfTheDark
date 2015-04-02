package com.DavidM1A2.AfraidOfTheDark.initializeMod;

import com.DavidM1A2.AfraidOfTheDark.worldGeneration.GenerateSilverOre;
import com.DavidM1A2.AfraidOfTheDark.worldGeneration.VeronaTreeV40withleaves;

import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModGeneration 
{
	public static final GenerateSilverOre generateSilverOre = new GenerateSilverOre();
	public static final VeronaTreeV40withleaves generateTree = new VeronaTreeV40withleaves();
	
	public static void initialize()
	{
		GameRegistry.registerWorldGenerator(generateSilverOre, 1);
		//GameRegistry.registerWorldGenerator(generateTree, 1);
	}
}
