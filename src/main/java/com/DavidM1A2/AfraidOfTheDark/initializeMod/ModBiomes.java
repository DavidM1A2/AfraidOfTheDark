package com.DavidM1A2.AfraidOfTheDark.initializeMod;

import com.DavidM1A2.AfraidOfTheDark.biomes.BiomeErieForest;
import com.mojang.realmsclient.dto.McoServer.WorldType;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;

public class ModBiomes 
{
	public static final BiomeGenBase erieForest = new BiomeErieForest(30);
	
	public static void initialize()
	{
		BiomeDictionary.registerBiomeType(erieForest, Type.FOREST, Type.CONIFEROUS, Type.PLAINS);
		BiomeManager.addBiome(BiomeManager.BiomeType.COOL, new BiomeEntry(erieForest, 10));
		BiomeManager.addBiome(BiomeManager.BiomeType.WARM, new BiomeEntry(erieForest, 10));
		BiomeManager.addSpawnBiome(erieForest);
	}
}
