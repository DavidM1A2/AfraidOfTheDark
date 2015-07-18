/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration;

import java.util.ArrayList;
import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.MeteorTypes;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class CreateMeteor
{
	private static final List<Block> replaceableBlocks = new ArrayList<Block>()
	{
		{
			add(ModBlocks.astralSilverOre);
			add(ModBlocks.starMetalOre);
			add(ModBlocks.sunstoneOre);
			add(ModBlocks.meteor);
			add(Blocks.dirt);
			add(Blocks.grass);
			add(Blocks.leaves);
			add(Blocks.leaves2);
			add(Blocks.sand);
			add(Blocks.log);
			add(Blocks.log2);
			add(Blocks.vine);
			add(Blocks.deadbush);
			add(Blocks.double_plant);
			add(Blocks.ice);
			add(Blocks.air);
			add(Blocks.stone);
			add(Blocks.gravel);
			add(Blocks.sandstone);
			add(Blocks.snow);
			add(Blocks.snow_layer);
		}
	};

	public static void create(final World world, final BlockPos location, final int radius, final int height, final boolean hollow, final boolean isSphere, final MeteorTypes type)
	{
		int cx = location.getX();
		int cy = makeSureChunkIsGenerated(world, location);
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
						if (replaceableBlocks.contains(current) || current.getMaterial() == Material.water || current.getMaterial() == Material.lava)
						{
							world.setBlockState(new BlockPos(x, y, z), ModBlocks.meteor.getDefaultState());
						}
					}
				}
			}
		}

		CreateMeteor.createCore(world, new BlockPos(cx, cy, cz), MathHelper.ceiling_double_int(radius / 2.5), MathHelper.ceiling_double_int(height / 2.5), hollow, isSphere, type);
	}

	private static void createCore(World world, BlockPos location, int radius, int height, boolean hollow, boolean isSphere, MeteorTypes type)
	{
		Block toPlace = (type == MeteorTypes.silver) ? ModBlocks.astralSilverOre : (type == MeteorTypes.sunstone) ? ModBlocks.sunstoneOre : ModBlocks.starMetalOre;
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
						if (replaceableBlocks.contains(current) || current.getMaterial() == Material.water || current.getMaterial() == Material.lava)
						{
							world.setBlockState(new BlockPos(x, y, z), toPlace.getDefaultState());
						}
					}
				}
			}
		}
	}

	private static int makeSureChunkIsGenerated(World world, BlockPos location)
	{
		if (!world.getChunkProvider().chunkExists(location.getX(), location.getZ()))
		{
			world.getChunkProvider().provideChunk(location.getX(), location.getZ());
		}

		while (world.getBlockState(location).getBlock() instanceof BlockAir || world.getBlockState(location).getBlock().getMaterial() == Material.water)
		{
			location = new BlockPos(location.getX(), location.getY() - 1, location.getZ());
		}
		return location.getY();
	}
}
