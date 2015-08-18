/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.initializeMod;

import com.DavidM1A2.AfraidOfTheDark.common.biomes.BiomeErieForest;
import com.DavidM1A2.AfraidOfTheDark.common.biomes.BiomeNightmare;
import com.DavidM1A2.AfraidOfTheDark.common.biomes.BiomeVoidChest;
import com.DavidM1A2.AfraidOfTheDark.common.handler.ConfigurationHandler;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;

public class ModBiomes
{
	// Create a biome
	public static final BiomeGenBase erieForest = new BiomeErieForest(ConfigurationHandler.biomeErieID);
	public static final BiomeGenBase nightmare = new BiomeNightmare(ConfigurationHandler.biomeNightmareID);
	public static final BiomeGenBase voidChest = new BiomeVoidChest(ConfigurationHandler.biomeVoidChestID);

	public static void initialize()
	{
		// Register it and add it
		BiomeDictionary.registerBiomeType(ModBiomes.erieForest, Type.FOREST, Type.CONIFEROUS, Type.PLAINS);
		BiomeDictionary.registerBiomeType(ModBiomes.nightmare);
		BiomeDictionary.registerBiomeType(ModBiomes.voidChest);

		BiomeManager.addBiome(BiomeManager.BiomeType.COOL, new BiomeEntry(ModBiomes.erieForest, ConfigurationHandler.erieBiomeFrequency));
		BiomeManager.addBiome(BiomeManager.BiomeType.WARM, new BiomeEntry(ModBiomes.erieForest, ConfigurationHandler.erieBiomeFrequency));
	}
}
