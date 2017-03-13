/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.utility;

import com.DavidM1A2.AfraidOfTheDark.common.block.BlockVoidChestPortal;
import com.DavidM1A2.AfraidOfTheDark.common.block.gravewood.BlockGravewood;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockDirt.DirtType;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.fluids.BlockFluidBase;

public class WorldGenerationUtility
{
	public static int getFirstNonAirBlock(World world, int x, int z) throws UnsupportedLocationException
	{
		int y = 255;
		while (y > 0)
		{
			Block current = world.getBlockState(new BlockPos(x, y, z)).getBlock();
			if (!(current instanceof BlockAir))
			{
				return y;
			}
			else if (current instanceof BlockVoidChestPortal)
			{
				world.setBlockState(new BlockPos(x, y, z), Blocks.STONE.getDefaultState());
				return y;
			}
			y = y - 1;
		}
		return 0;
	}

	public static int getPlaceToSpawnAverage(World world, int x, int z, int height, int width) throws UnsupportedLocationException
	{
		int y1 = 0;
		int y2 = 0;
		int y3 = 0;
		int y4 = 0;

		y1 = getTheYValueAtCoords(world, x, z);
		y2 = getTheYValueAtCoords(world, x + width, z);
		y3 = getTheYValueAtCoords(world, x, z + height);
		y4 = getTheYValueAtCoords(world, x + width, z + height);

		if (y1 == 0 || y2 == 0 || y3 == 0 || y4 == 0)
		{
			throw new UnsupportedLocationException(y1, y2, y3, y4);
		}
		else
		{
			return (y1 + y2 + y3 + y4) / 4;
		}
	}

	private static int getTheYValueAtCoords(World world, int x, int z)
	{
		int temp = 255;
		while (temp > 0)
		{
			Block current = world.getBlockState(new BlockPos(x, temp, z)).getBlock();
			if (current instanceof BlockFluidBase)
			{
				return 0;
			}
			if (current instanceof BlockGrass)
			{
				return temp;
			}
			if (current instanceof BlockDirt)
			{
				if (world.canSeeSky(new BlockPos(x, temp, z)))
				{
					return temp;
				}
				else if (world.getBlockState(new BlockPos(x, temp, z)) == Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, DirtType.PODZOL))
				{
					return temp;
				}
				else if (world.getBlockState(new BlockPos(x, temp + 1, z)).getBlock() instanceof BlockLog || world.getBlockState(new BlockPos(x, temp + 1, z)).getBlock() instanceof BlockGravewood)
				{
					return temp;
				}
			}
			if (world.getBlockState(new BlockPos(x, temp + 1, z)).getBlock() instanceof BlockSnow)
			{
				return temp;
			}
			temp = temp - 1;
		}
		return 0;
	}

	public static int getPlaceToSpawnLowest(World world, int x, int z, int height, int width) throws UnsupportedLocationException
	{
		int y1 = 0;
		int y2 = 0;
		int y3 = 0;
		int y4 = 0;

		y1 = getTheYValueAtCoords(world, x, z);
		y2 = getTheYValueAtCoords(world, x + width, z);
		y3 = getTheYValueAtCoords(world, x, z + height);
		y4 = getTheYValueAtCoords(world, x + width, z + height);

		if (y1 == 0 || y2 == 0 || y3 == 0 || y4 == 0)
		{
			throw new UnsupportedLocationException(y1, y2, y3, y4);
		}
		else
		{
			return Math.min(y1, Math.min(y2, Math.min(y3, y4)));
		}
	}

	/*
	 * Got this function from here: https://github.com/jabelar/MagicBeans-1.8fixed/blob/master/src/main/java/com/blogspot/jabelarminecraft/magicbeans/utilities/Utilities.java
	 */
	public static IBlockState setBlockStateInChunkFast(Chunk parChunk, BlockPos parBlockPos, IBlockState parIBlockState)
	{
		int chunkX = parBlockPos.getX() & 15;
		int chunkY = parBlockPos.getY();
		int chunkZ = parBlockPos.getZ() & 15;
		//        int mapKey = chunkZ << 4 | chunkX;

		//		if (chunkY >= parChunk.getPrecipitationHeight(parBlockPos).getY())
		//		{
		//			//            // There is no setter for precipitationHeightMap so will need to use reflection 
		//			//            parChunk.precipitationHeightMap[mapKey] = -999;
		//		}

		//        int currentHeight = parChunk.getHeightMap()[mapKey];
		IBlockState existingIBlockState = parChunk.getBlockState(parBlockPos);

		if (existingIBlockState == parIBlockState)
		{
			return null;
		}
		else
		{
			Block newBlock = parIBlockState.getBlock();
			Block existingBlock = existingIBlockState.getBlock();
			ExtendedBlockStorage extendedblockstorage = parChunk.getBlockStorageArray()[chunkY >> 4];
			//            boolean isHigherThanCurrentHeight = false;

			if (extendedblockstorage == null)
			{
				if (newBlock == Blocks.AIR)
				{
					return null;
				}

				extendedblockstorage = parChunk.getBlockStorageArray()[chunkY >> 4] = new ExtendedBlockStorage(chunkY >> 4 << 4, !parChunk.getWorld().provider.getHasNoSky());
				//                isHigherThanCurrentHeight = chunkY >= currentHeight;
			}

			//            int newLightOpacity = newBlock.getLightOpacity(parChunk.getWorld(), parBlockPos);

			extendedblockstorage.set(chunkX, chunkY & 15, chunkZ, parIBlockState);

			if (!parChunk.getWorld().isRemote)
			{
				if (existingIBlockState.getBlock() != parIBlockState.getBlock()) //Only fire block breaks when the block changes.
					existingBlock.breakBlock(parChunk.getWorld(), parBlockPos, existingIBlockState);
				TileEntity te = parChunk.getTileEntity(parBlockPos, Chunk.EnumCreateEntityType.CHECK);
				if (te != null && te.shouldRefresh(parChunk.getWorld(), parBlockPos, existingIBlockState, parIBlockState))
					parChunk.getWorld().removeTileEntity(parBlockPos);
			}
			else if (existingBlock.hasTileEntity(existingIBlockState))
			{
				TileEntity te = parChunk.getTileEntity(parBlockPos, Chunk.EnumCreateEntityType.CHECK);
				if (te != null && te.shouldRefresh(parChunk.getWorld(), parBlockPos, existingIBlockState, parIBlockState))
					parChunk.getWorld().removeTileEntity(parBlockPos);
			}

			if (extendedblockstorage.get(chunkX, chunkY & 15, chunkZ) != newBlock)
			{
				return null;
			}
			else
			{
				//                if (isHigherThanCurrentHeight)
				//                {
				//                    parChunk.generateSkylightMap();
				//                }
				//                else
				//                {
				//                    int existingLightOpacity = newBlock.getLightOpacity(parChunk.getWorld(), parBlockPos);
				//
				//                    if (newLightOpacity > 0)
				//                    {
				//                        if (chunkY >= currentHeight)
				//                        {
				//                            parChunk.relightBlock(chunkX, chunkY + 1, chunkZ);
				//                        }
				//                    }
				//                    else if (chunkY == currentHeight - 1)
				//                    {
				//                        parChunk.relightBlock(chunkX, chunkY, chunkZ);
				//                    }
				//
				//                    if (newLightOpacity != existingLightOpacity && (newLightOpacity < existingLightOpacity || parChunk.getLightFor(EnumSkyBlock.SKY, parBlockPos) > 0 || parChunk.getLightFor(EnumSkyBlock.BLOCK, parBlockPos) > 0))
				//                    {
				//                        parChunk.propagateSkylightOcclusion(chunkX, chunkZ);
				//                    }
				//                }

				TileEntity tileentity;

				if (!parChunk.getWorld().isRemote && existingBlock != newBlock)
				{
					newBlock.onBlockAdded(parChunk.getWorld(), parBlockPos, parIBlockState);
				}

				if (newBlock.hasTileEntity(parIBlockState))
				{
					tileentity = parChunk.getTileEntity(parBlockPos, Chunk.EnumCreateEntityType.CHECK);

					if (tileentity == null)
					{
						tileentity = newBlock.createTileEntity(parChunk.getWorld(), parIBlockState);
						parChunk.getWorld().setTileEntity(parBlockPos, tileentity);
					}

					if (tileentity != null)
					{
						tileentity.updateContainingBlockInfo();
					}
				}

				parChunk.setModified(true);
				return existingIBlockState;
			}
		}
	}

	/**
	 * Got this function from here:
	 * https://github.com/jabelar/MagicBeans-1.8fixed/blob/master/src/main/java/com/blogspot/jabelarminecraft/magicbeans/utilities/Utilities.java
	 * 
	 * Sets the block state at a given location. Flag 1 will cause a block update. Flag 2 will send the change to clients (you almost always want
	 * parWorld). Flag 4 prevents the block from being re-rendered, if parWorld is a client world. Flags can be added together.
	 * 
	 * @param parFlags
	 *            Flag 1 will cause a block update. Flag 2 will send the change to clients (you almost always want parWorld). Flag 4 prevents the
	 *            block from being re-rendered, if parWorld is a client world. Flags can be added together.
	 */
	public static boolean setBlockStateFast(World parWorld, BlockPos parBlockPos, IBlockState parIBlockState, int parFlags)
	{
		if (!(parBlockPos.getX() >= -30000000 && parBlockPos.getZ() >= -30000000 && parBlockPos.getX() < 30000000 && parBlockPos.getZ() < 30000000 && parBlockPos.getY() >= 0 && parBlockPos.getY() < 256))
		{
			return false;
		}
		else if (!parWorld.isRemote && parWorld.getWorldInfo().getTerrainType() == WorldType.DEBUG_WORLD)
		{
			return false;
		}
		else
		{
			Chunk theChunk = parWorld.getChunkFromBlockCoords(parBlockPos);
			//            Block newBlock = parIBlockState.getBlock();

			BlockSnapshot blockSnapshot = null;
			if (parWorld.captureBlockSnapshots && !parWorld.isRemote)
			{
				blockSnapshot = BlockSnapshot.getBlockSnapshot(parWorld, parBlockPos, parFlags);
				parWorld.capturedBlockSnapshots.add(blockSnapshot);
			}

			IBlockState theIBlockState = setBlockStateInChunkFast(theChunk, parBlockPos, parIBlockState);

			if (theIBlockState == null)
			{
				if (blockSnapshot != null) parWorld.capturedBlockSnapshots.remove(blockSnapshot);
				return false;
			}
			else
			{
				//                Block block1 = theIBlockState.getBlock();
				//
				//                if (newBlock.getLightOpacity() != block1.getLightOpacity() || newBlock.getLightValue() != block1.getLightValue())
				//                {
				//                    parWorld.theProfiler.startSection("checkLight");
				// parWorld.checkLight(parBlockPos);
				//                    parWorld.theProfiler.endSection();
				//                }

				if (blockSnapshot == null) // Don't notify clients or update physics while capturing blockstates
				{
					parWorld.markAndNotifyBlock(parBlockPos, theChunk, theIBlockState, parIBlockState, parFlags); // Modularize client and physic updates
				}

				return true;
			}
		}
	}
}
