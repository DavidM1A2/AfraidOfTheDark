/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.biomes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;

public class BiomeNightmare extends Biome
{
	private List<Biome.SpawnListEntry> spawnableCreatures = new ArrayList<>();

	public BiomeNightmare(int biomeID)
	{
		super(new Biome.BiomeProperties("Nightmare").setWaterColor(0xFF0000).setBaseHeight(0.125F).setHeightVariation(0.05F).setRainfall(1.0f));
		this.setRegistryName("nightmare");
		this.flowers.clear();
		this.spawnableCreatureList.clear();
		this.decorator.treesPerChunk = 0;
		this.topBlock = Blocks.DIRT.getDefaultState();
	}

	/**
	 * No longer allowing other mods to add stupid creatures to our realm
	 */
	@Override
	public List<SpawnListEntry> getSpawnableList(EnumCreatureType creatureType)
	{
		if (!spawnableCreatures.isEmpty())
			this.spawnableCreatures.clear();
		return spawnableCreatures;
	}

	/**
	 * returns the chance a creature has to spawn.
	 */
	@Override
	public float getSpawningChance()
	{
		return 0.0F;
	}
}
