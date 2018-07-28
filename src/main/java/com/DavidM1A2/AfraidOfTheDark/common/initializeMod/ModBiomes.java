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

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBiomes
{
	// Create a biome
	public static final Biome erieForest = new BiomeErieForest(ConfigurationHandler.biomeErieID);
	public static final Biome nightmare = new BiomeNightmare(ConfigurationHandler.biomeNightmareID);
	public static final Biome voidChest = new BiomeVoidChest(ConfigurationHandler.biomeVoidChestID);

	public static void initialize()
	{
		// Register it and add it
		GameRegistry.register(erieForest);
		GameRegistry.register(nightmare);
		GameRegistry.register(voidChest);
		BiomeManager.addBiome(BiomeManager.BiomeType.COOL, new BiomeEntry(ModBiomes.erieForest, ConfigurationHandler.erieBiomeFrequency));
		BiomeManager.addBiome(BiomeManager.BiomeType.WARM, new BiomeEntry(ModBiomes.erieForest, ConfigurationHandler.erieBiomeFrequency));
		BiomeDictionary.addTypes(ModBiomes.erieForest, Type.FOREST, Type.CONIFEROUS, Type.PLAINS);
		BiomeDictionary.addTypes(ModBiomes.nightmare);
		BiomeDictionary.addTypes(ModBiomes.voidChest);
	}
}
