package com.DavidM1A2.AfraidOfTheDark.worldGeneration;
import java.util.Random;
import com.DavidM1A2.AfraidOfTheDark.utility.LogHelper;
import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenerator;

public class VeronaTreeV40withleaves implements IWorldGenerator
{
	public VeronaTreeV40withleaves() 
	{

	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) 
	{

		switch (world.provider.dimensionId)
		{
		case 0:
		{
			if (Math.random() < 0.001)
			{
				int x = (chunkX * 16) + random.nextInt(17);
				int y = 255;
				int z = (chunkZ * 16) + random.nextInt(17);


				for (int i = 255; i > 0; i--)
				{
					if (Blocks.air != world.getBlock(x, y, z))
					{
						if (y > 11)
						{
							y = y - 10;
							LogHelper.info("Chose: " + x + ", " + y + ", " + z);		
							BigTreeModel.generate(world, random, x, y, z);
						}
						else
						{
							LogHelper.info("Chose: " + x + ", " + y + ", " + z);		
							BigTreeModel.generate(world, random, x, y, z);
						}
						break;
					}
					else
					{
						y = y - 1;
					}
				}
			}
		}
		}

	}	
}