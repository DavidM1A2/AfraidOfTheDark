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
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public final class SchematicGenerator
{
	public static void generateSchematic(Schematic schematic, World world, int xPosition, int yPosition, int zPosition)
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
					if (nextToPlace == Blocks.diamond_block)
					{
						BlockPos currentLocation = new BlockPos(x + xPosition, y + yPosition, z + zPosition);
						world.setBlockToAir(new BlockPos(currentLocation));
					}
					else if (nextToPlace == Blocks.bed || nextToPlace == Blocks.torch)
					{
						blocksToPlaceLater.add(nextToPlace.getStateFromMeta(schematic.getData()[i]));
						laterBlockPositions.add(new BlockPos(x + xPosition, y + yPosition, z + zPosition));
					}
					else if (nextToPlace != Blocks.air)
					{
						BlockPos currentLocation = new BlockPos(x + xPosition, y + yPosition, z + zPosition);
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
					tileEntity.setPos(tileEntityLocation);
					world.setTileEntity(tileEntityLocation, tileEntity);
				}
			}
		}
	}
}
