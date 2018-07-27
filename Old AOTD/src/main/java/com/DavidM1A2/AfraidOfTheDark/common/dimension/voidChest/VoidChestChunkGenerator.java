/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.dimension.voidChest;

import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBiomes;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDDimensions;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDSchematics;
import com.DavidM1A2.AfraidOfTheDark.common.schematic.SchematicGenerator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.Nullable;

public class VoidChestChunkGenerator implements IChunkGenerator
{
	private final World world;

	public VoidChestChunkGenerator(World world)
	{
		this.world = world;
	}

	@Override
	public boolean generateStructures(Chunk chunkIn, int x, int z)
	{
		return false;
	}

	@Override
	public Chunk generateChunk(int x, int z)
	{
		ChunkPrimer chunkprimer = new ChunkPrimer();
		IBlockState iblockstate = Blocks.AIR.getDefaultState();

		Chunk chunk = new Chunk(this.world, chunkprimer, x, z);

		byte[] biome = chunk.getBiomeArray();

		for (int i = 0; i < biome.length; i++)
		{
			biome[i] = (byte) Biome.getIdForBiome(ModBiomes.voidChest);
		}

		chunk.generateSkylightMap();

		return chunk;
	}

	@Override
	public void populate(int x, int z)
	{
		// Every 62 chunks in the x direction (992 blocks)
		if (x * 16 % AOTDDimensions.getBlocksBetweenIslands() == 0 && z == 0)
		{
			x = x * 16;
			for (int i = 0; i < 49; i++)
			{
				for (int j = 0; j < 49; j++)
				{
					world.setBlockState(new BlockPos(x + i, 100, z + j), Blocks.BARRIER.getDefaultState());
					world.setBlockState(new BlockPos(x + i, 100 + 48, z + j), Blocks.BARRIER.getDefaultState());
					world.setBlockState(new BlockPos(x + 0, 100 + i, z + j), Blocks.BARRIER.getDefaultState());
					world.setBlockState(new BlockPos(x + i, 100 + j, z + 0), Blocks.BARRIER.getDefaultState());
					world.setBlockState(new BlockPos(x + 48, 100 + i, z + j), Blocks.BARRIER.getDefaultState());
					world.setBlockState(new BlockPos(x + i, 100 + j, z + 48), Blocks.BARRIER.getDefaultState());
				}
			}

			SchematicGenerator.generateSchematic(AOTDSchematics.VoidChestPortal.getSchematic(), this.world, x + 20, 100, z - 2);
		}
	}

	@Override
	public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType enumCreatureType, BlockPos blockPos)
	{
		return this.world.getBiome(blockPos).getSpawnableList(enumCreatureType);
	}

	@Nullable
	@Override
	public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored)
	{
		return null;
	}

	@Override
	public void recreateStructures(Chunk chunkIn, int x, int z)
	{
	}

	@Override
	public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos)
	{
		return false;
	}
}