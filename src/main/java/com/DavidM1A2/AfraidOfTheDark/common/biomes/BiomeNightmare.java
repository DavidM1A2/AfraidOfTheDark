package com.DavidM1A2.AfraidOfTheDark.common.biomes;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeNightmare extends BiomeGenBase
{
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
	}
}
