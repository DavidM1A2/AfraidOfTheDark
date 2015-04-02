/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.biomes;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenBigTree;

import com.DavidM1A2.AfraidOfTheDark.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.worldGeneration.Graveyard;

public class BiomeErieForest extends BiomeGenBase
{
	/*
	 * This will be the erie biome creation class
	 */
	public BiomeErieForest(int biomeID)
	{
		// This sets the features of the erie biome
		super(biomeID);
		this.biomeName = "Erie Forest";
		this.color = 0x000099;
		this.enableRain = true;
		this.fillerBlock = Blocks.dirt;
		this.flowers.clear();
		this.heightVariation = 0.1F;
		this.rootHeight = 0.0F;
		this.spawnableCreatureList.clear();
		this.topBlock = Blocks.mycelium;
		this.waterColorMultiplier = 0x000099;
		this.theBiomeDecorator.treesPerChunk = 15;
		this.worldGeneratorTrees = new WorldGenGravewoodTrees(false);
		this.worldGeneratorBigTree = new WorldGenBigTree(false);
	}

	@Override
	public void decorate(World world, Random random, int x, int z)
	{
		// This generates any extra structures in the erie biome
		if (Math.random() < 0.005)
		{
			int y = 255;
			for (int i = 255; i > 0; i--)
			{
				if (world.getBlock(x, y, z) == Blocks.mycelium)
				{
					y = y - 4;
					LogHelper.info("Chose: " + x + ", " + y + ", " + z);
					Graveyard.generate(world, random, x, y, z);
					break;
				}
				else
				{
					y = y - 1;
				}
			}
		}

		/*
		 * Custom Code ends here
		 */

		// This is the default MC function that does the tree and flower
		// creation in the biome
		int k;
		int l;
		int i1;
		int j1;
		int k1;

		for (k = 0; k < 4; ++k)
		{
			for (l = 0; l < 4; ++l)
			{
				i1 = x + k * 4 + 1 + 8 + random.nextInt(3);
				j1 = z + l * 4 + 1 + 8 + random.nextInt(3);
				k1 = world.getHeightValue(i1, j1);

				if (random.nextInt(20) == 0)
				{
					WorldGenBigMushroom worldgenbigmushroom = new WorldGenBigMushroom();
					worldgenbigmushroom.generate(world, random, i1, k1, j1);
				}
				else
				{
					WorldGenAbstractTree worldgenabstracttree = this.func_150567_a(random);
					worldgenabstracttree.setScale(1.0D, 1.0D, 1.0D);

					if (worldgenabstracttree.generate(world, random, i1, k1, j1))
					{
						worldgenabstracttree.func_150524_b(world, random, i1, k1, j1);
					}
				}
			}
		}

		k = random.nextInt(5) - 3;

		l = 0;

		while (l < k)
		{
			i1 = random.nextInt(3);

			if (i1 == 0)
			{
				genTallFlowers.func_150548_a(1);
			}
			else if (i1 == 1)
			{
				genTallFlowers.func_150548_a(4);
			}
			else if (i1 == 2)
			{
				genTallFlowers.func_150548_a(5);
			}

			j1 = 0;

			while (true)
			{
				if (j1 < 5)
				{
					k1 = x + random.nextInt(16) + 8;
					int i2 = z + random.nextInt(16) + 8;
					int l1 = random.nextInt(world.getHeightValue(k1, i2) + 32);

					if (!genTallFlowers.generate(world, random, k1, l1, i2))
					{
						++j1;
						continue;
					}
				}

				++l;
				break;
			}
		}

		super.decorate(world, random, x, z);
	}
}
