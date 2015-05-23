package com.DavidM1A2.AfraidOfTheDark.worldGeneration;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.block.AOTDBlock;
import com.DavidM1A2.AfraidOfTheDark.block.BlockMeteor;
import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModBlocks;

public class CreateMeteor
{
	private static final List<AOTDBlock> types = new ArrayList<AOTDBlock>()
	{
		{
			add(ModBlocks.meteoricSilver);
			add(ModBlocks.sunstone);
			add(ModBlocks.starMetal);
		}
	};

	public static void create(final World world, final BlockPos location, final int radius, final int height, final boolean hollow, final boolean isSphere)
	{
		int cx = location.getX();
		int cy = location.getY();
		int cz = location.getZ();
		for (int x = cx - radius; x <= cx + radius; x++)
		{
			for (int z = cz - radius; z <= cz + radius; z++)
			{
				for (int y = (isSphere ? cy - radius : cy); y < (isSphere ? cy + radius : cy + height); y++)
				{
					double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (isSphere ? (cy - y) * (cy - y) : 0);
					if (dist < radius * radius && !(hollow && dist < (radius - 1) * (radius - 1)))
					{
						Block current = world.getBlockState(new BlockPos(x, y, z)).getBlock();
						if (current instanceof BlockDirt || current instanceof BlockAir || current instanceof BlockLog || current instanceof BlockStone || current.getMaterial() == Material.water || current.getMaterial() == Material.lava)
						{
							world.setBlockState(new BlockPos(x, y, z), ModBlocks.meteor.getDefaultState());
						}
					}
				}
			}
		}

		CreateMeteor.createCore(world, location, MathHelper.ceiling_double_int(radius / 2.5), MathHelper.ceiling_double_int(height / 2.5), hollow, isSphere);
	}

	private static void createCore(World world, BlockPos location, int radius, int height, boolean hollow, boolean isSphere)
	{
		Block toPlace = types.get(world.rand.nextInt(types.size()));
		int cx = location.getX();
		int cy = location.getY();
		int cz = location.getZ();
		for (int x = cx - radius; x <= cx + radius; x++)
		{
			for (int z = cz - radius; z <= cz + radius; z++)
			{
				for (int y = (isSphere ? cy - radius : cy); y < (isSphere ? cy + radius : cy + height); y++)
				{
					double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (isSphere ? (cy - y) * (cy - y) : 0);
					if (dist < radius * radius && !(hollow && dist < (radius - 1) * (radius - 1)))
					{
						Block current = world.getBlockState(new BlockPos(x, y, z)).getBlock();
						if (current instanceof BlockMeteor || current instanceof BlockDirt || current instanceof BlockAir || current instanceof BlockLog || current instanceof BlockStone)
						{
							world.setBlockState(new BlockPos(x, y, z), toPlace.getDefaultState());
						}
					}
				}
			}
		}
	}
}
