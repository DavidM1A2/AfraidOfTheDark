/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.dimension.voidChest;

import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBiomes;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;

public class VoidChestChunkProvider implements IChunkProvider
{
	private final World worldObj;

	public VoidChestChunkProvider(World world)
	{
		worldObj = world;
	}

	/**
	 * Checks to see if a chunk exists at x, y
	 */
	@Override
	public boolean chunkExists(int x, int z)
	{
		return true;
	}

	@Override
	public Chunk provideChunk(int x, int z)
	{
		ChunkPrimer chunkprimer = new ChunkPrimer();
		IBlockState iblockstate = Blocks.air.getDefaultState();

		Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);

		byte[] biome = chunk.getBiomeArray();

		for (int i = 0; i < biome.length; i++)
		{
			biome[i] = (byte) ModBiomes.voidChest.biomeID;
		}

		chunk.generateSkylightMap();

		return chunk;
	}

	@Override
	public Chunk func_177459_a(BlockPos blockPos)
	{
		return this.provideChunk(blockPos.getX() >> 4, blockPos.getZ() >> 4);
	}

	@Override
	public void populate(IChunkProvider iChunkProvider, int x, int z)
	{
		// Every 62 chunks in the x direction (992 blocks)
		if (x * 16 % Constants.NightmareWorld.BLOCKS_BETWEEN_ISLANDS == 0 && z == 0)
		{
			x = x * 16;
			for (int i = 0; i < 48; i++)
			{
				for (int j = 0; j < 48; j++)
				{
					worldObj.setBlockState(new BlockPos(x + i, 100, z + j), Blocks.obsidian.getDefaultState());
					worldObj.setBlockState(new BlockPos(x + i, 100 + 48, z + j), Blocks.obsidian.getDefaultState());
					worldObj.setBlockState(new BlockPos(x + 0, 100 + i, z + j), Blocks.obsidian.getDefaultState());
					worldObj.setBlockState(new BlockPos(x + i, 100 + j, z + 0), Blocks.obsidian.getDefaultState());
					worldObj.setBlockState(new BlockPos(x + 48, 100 + i, z + j), Blocks.obsidian.getDefaultState());
					worldObj.setBlockState(new BlockPos(x + i, 100 + j, z + 48), Blocks.obsidian.getDefaultState());
				}
			}
		}
	}

	@Override
	public boolean func_177460_a(IChunkProvider p_177460_1_, Chunk p_177460_2_, int p_177460_3_, int p_177460_4_)
	{
		return false;
	}

	@Override
	public boolean saveChunks(boolean p_73151_1_, IProgressUpdate p_73151_2_)
	{
		return true;
	}

	@Override
	public boolean unloadQueuedChunks()
	{
		return false;
	}

	@Override
	public boolean canSave()
	{
		return true;
	}

	@Override
	public String makeString()
	{
		return "VoidChestWorld";
	}

	@Override
	public List func_177458_a(EnumCreatureType enumCreatureType, BlockPos blockPos)
	{
		return this.worldObj.getBiomeGenForCoords(blockPos).getSpawnableList(enumCreatureType);
	}

	@Override
	public BlockPos func_180513_a(World world, String structureType, BlockPos blockPos)
	{
		// Gen structures
		return null;
	}

	@Override
	public int getLoadedChunkCount()
	{
		return 0;
	}

	@Override
	public void func_180514_a(Chunk chunk, int x, int z)
	{
	}

	@Override
	public void saveExtraData()
	{
	}
}