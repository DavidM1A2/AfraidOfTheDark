/*
 * Credit to:
 * http://www.minecraftforge.net/forum/index.php/topic,21045.0.html
 */
package com.DavidM1A2.AfraidOfTheDark.common.schematic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.IChestGenerator;

public final class SchematicGenerator
{
	public static void generateSchematic(Schematic schematic, World world, int xPosition, int yPosition, int zPosition, IChestGenerator chestItemRandomizer)
	{
		if (schematic == null)
		{
			return;
		}

		int i = 0;

		List<IBlockState> blocksToPlaceLater = new ArrayList<IBlockState>();
		List<BlockPos> laterBlockPositions = new ArrayList<BlockPos>();

		for (int y = 0; y < schematic.getHeight(); y++)
		{
			for (int z = 0; z < schematic.getLength(); z++)
			{
				for (int x = 0; x < schematic.getWidth(); x++)
				{
					Block nextToPlace = Block.getBlockById(schematic.getBlocks()[i]);
					BlockPos currentLocation = new BlockPos(x + xPosition, y + yPosition, z + zPosition);
					if (nextToPlace == Blocks.diamond_block)
					{
						world.setBlockToAir(new BlockPos(currentLocation));
					}
					else if (nextToPlace == Blocks.bed || nextToPlace == Blocks.torch || nextToPlace == Blocks.standing_sign || nextToPlace == Blocks.wall_sign || nextToPlace == Blocks.trapdoor)
					{
						blocksToPlaceLater.add(nextToPlace.getStateFromMeta(schematic.getData()[i]));
						laterBlockPositions.add(new BlockPos(x + xPosition, y + yPosition, z + zPosition));
					}
					else if (nextToPlace != Blocks.air)
					{
						world.setBlockToAir(new BlockPos(currentLocation));
						world.setBlockState(currentLocation, nextToPlace.getStateFromMeta(schematic.getData()[i]));
					}

					i = i + 1;
				}
			}
		}

		for (int j = 0; j < blocksToPlaceLater.size(); j++)
		{
			world.setBlockToAir(laterBlockPositions.get(j));
			world.setBlockState(laterBlockPositions.get(j), blocksToPlaceLater.get(j));
		}

		if (schematic.getTileentities() != null)
		{
			for (int j = 0; j < schematic.getTileentities().tagCount(); j++)
			{
				NBTTagCompound tileEntityCompound = schematic.getTileentities().getCompoundTagAt(j);
				TileEntity tileEntity = TileEntity.createAndLoadEntity(tileEntityCompound);

				if (tileEntity != null)
				{
					BlockPos tileEntityLocation = new BlockPos(tileEntity.getPos().add(xPosition, yPosition, zPosition));

					// Remove the default tile entity
					world.removeTileEntity(tileEntityLocation);

					world.setTileEntity(tileEntityLocation, tileEntity);
					tileEntity.setPos(tileEntityLocation);

					if (tileEntity instanceof TileEntityChest && chestItemRandomizer != null)
					{
						TileEntityChest tileEntityChest = (TileEntityChest) world.getTileEntity(tileEntityLocation);
						if (tileEntityChest != null)
						{
							WeightedRandomChestContent.generateChestContents(world.rand, chestItemRandomizer.getPossibleItems(world.rand), tileEntityChest, 1);
						}
					}
				}
			}
		}
	}
}
