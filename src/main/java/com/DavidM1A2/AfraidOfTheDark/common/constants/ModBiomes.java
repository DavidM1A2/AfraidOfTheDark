package com.DavidM1A2.afraidofthedark.common.constants;

import com.DavidM1A2.afraidofthedark.common.biomes.BiomeErieForest;
import com.DavidM1A2.afraidofthedark.common.biomes.BiomeVoidChest;
import net.minecraft.world.biome.Biome;

/**
 * Storage for all mod biomes used in AOTD
 */
public class ModBiomes
{
	// The Erie Forest biome
	public static final Biome ERIE_FOREST = new BiomeErieForest();
	// The Void Chest biome
	public static final Biome VOID_CHEST = new BiomeVoidChest();

	// An array containing a list of biomes that AOTD adds
	public static final Biome[] BIOME_LIST = new Biome[]
	{
		ERIE_FOREST,
		VOID_CHEST
	};
}
