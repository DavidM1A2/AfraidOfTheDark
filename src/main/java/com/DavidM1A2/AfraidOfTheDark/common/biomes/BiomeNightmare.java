/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.biomes;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeNightmare extends BiomeGenBase
{
	private List<BiomeGenBase.SpawnListEntry> spawnableCreatures = Lists.<BiomeGenBase.SpawnListEntry> newArrayList();

	public BiomeNightmare(int biomeID)
	{
		super(biomeID);

		this.biomeName = "Nightmare";
		this.color = 0xFF0000;
		this.enableRain = true;
		this.setFillerBlockMetadata(5159473);
		this.flowers.clear();
		this.setHeight(new BiomeGenBase.Height(0.125F, 0.05F));
		this.spawnableCreatureList.clear();
		this.waterColorMultiplier = 0xFF0000;
		this.theBiomeDecorator.treesPerChunk = 0;
		this.topBlock = Blocks.dirt.getDefaultState();
		this.rainfall = 1.0F;
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
