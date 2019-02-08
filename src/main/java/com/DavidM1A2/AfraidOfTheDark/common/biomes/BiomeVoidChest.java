package com.DavidM1A2.afraidofthedark.common.biomes;

import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Void chest biome is used in the void chest dimension.
 */
public class BiomeVoidChest extends Biome
{
	// This list should always be empty
	private final List<Biome.SpawnListEntry> EMPTY_LIST = new ArrayList<>();

	/**
	 * Constructor sets up biome properties
	 */
	public BiomeVoidChest()
	{
		// Set this biome's properties. It takes height, variation, water color, and a name
		super(new Biome.BiomeProperties("Void Chest")
				.setWaterColor(0x537B09)
				.setBaseHeight(0.125f)
				.setHeightVariation(0.05f)
				.setRainDisabled());
		// Set the biome's registry name
		this.setRegistryName(new ResourceLocation(Constants.MOD_ID, "void_chest"));
		// The biome is blank, so nothing generates in it
		this.flowers.clear();
		this.topBlock = Blocks.AIR.getDefaultState();
		this.modSpawnableLists.clear();
		this.spawnableCaveCreatureList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.spawnableWaterCreatureList.clear();
	}

	/**
	 * No creatures can spawn here, ever
	 *
	 * @param creatureType ignored
	 * @return An empty list of spawnables
	 */
	@Override
	public List<SpawnListEntry> getSpawnableList(EnumCreatureType creatureType)
	{
		// Ensure the list stays empty
		EMPTY_LIST.clear();
		return EMPTY_LIST;
	}

	/**
	 * @return 0% chance
	 */
	@Override
	public float getSpawningChance()
	{
		return 0f;
	}

	/**
	 * @return Creates an empty biome decorator
	 */
	@Override
	public BiomeDecorator createBiomeDecorator()
	{
		// Return a biome decorator that does nothing
		return new BiomeDecorator()
		{
			/**
			 * Do nothing
			 *
			 * @param worldIn ignored
			 * @param random ignored
			 * @param biome ignored
			 * @param pos ignored
			 */
			@Override
			public void decorate(World worldIn, Random random, Biome biome, BlockPos pos)
			{
			}
		};
	}
}
