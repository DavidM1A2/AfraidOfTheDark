/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.initializeMod;

import com.DavidM1A2.AfraidOfTheDark.common.biomes.BiomeErieForest;
import com.DavidM1A2.AfraidOfTheDark.common.biomes.BiomeNightmare;
import com.DavidM1A2.AfraidOfTheDark.common.biomes.BiomeVoidChest;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;

public class ModBiomes
{
	// Create a biome
	public static final BiomeGenBase erieForest = new BiomeErieForest(68);
	public static final BiomeGenBase nightmare = new BiomeNightmare(69);
	public static final BiomeGenBase voidChest = new BiomeVoidChest(70);

	public static void initialize()
	{
		// Register it and add it
		BiomeDictionary.registerBiomeType(ModBiomes.erieForest, Type.FOREST, Type.CONIFEROUS, Type.PLAINS);
		BiomeDictionary.registerBiomeType(ModBiomes.nightmare);
		BiomeDictionary.registerBiomeType(ModBiomes.voidChest);
		BiomeManager.addBiome(BiomeManager.BiomeType.COOL, new BiomeEntry(ModBiomes.erieForest, 10));
		BiomeManager.addBiome(BiomeManager.BiomeType.WARM, new BiomeEntry(ModBiomes.erieForest, 10));
		// This biome is only temporarily the spawn biome
		BiomeManager.addSpawnBiome(ModBiomes.erieForest);
	}
}
