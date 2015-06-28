/*
 * Credit to:
 * http://www.minecraftforge.net/forum/index.php/topic,21045.0.html
 */
package com.DavidM1A2.AfraidOfTheDark.common.schematic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import scala.actors.threadpool.Arrays;

import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.IChestGenerator;

public final class SchematicGenerator
{
	private static final List<Block> latePlacePriorityBlocks = Arrays.asList(new Block[]
	{ Blocks.rail, Blocks.reeds, Blocks.powered_comparator, Blocks.powered_repeater, Blocks.sapling, Blocks.detector_rail, Blocks.redstone_torch, Blocks.vine, Blocks.standing_sign, Blocks.wall_sign, Blocks.redstone_wire, Blocks.torch, Blocks.wooden_button, Blocks.stone_button,
			Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.deadbush, Blocks.bed, Blocks.trapdoor, Blocks.carpet, Blocks.iron_door, Blocks.ladder, Blocks.dark_oak_door, Blocks.birch_door, Blocks.oak_door,
			Blocks.acacia_door, Blocks.spruce_door });

	public static void generateSchematic(Schematic schematic, World world, int xPosition, int yPosition, int zPosition, IChestGenerator chestItemRandomizer, int lootAmount)
	{
		if (schematic == null)
		{
			return;
		}

		int i = 0;

		List<IBlockState> blocksToPlaceLater = new ArrayList<IBlockState>();
		List<BlockPos> laterBlockPositions = new ArrayList<BlockPos>();

		printIncorrectIds(schematic.getBlocks());

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
					else if (latePlacePriorityBlocks.contains(nextToPlace))
					{
						blocksToPlaceLater.add(nextToPlace.getStateFromMeta(schematic.getData()[i]));
						laterBlockPositions.add(currentLocation);
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
							WeightedRandomChestContent.generateChestContents(world.rand, chestItemRandomizer.getPossibleItems(world.rand), tileEntityChest, world.rand.nextInt(MathHelper.ceiling_double_int(lootAmount * 0.5)) + MathHelper.ceiling_double_int(lootAmount * 0.8));
						}
					}
				}
			}
		}

		if (schematic.getEntities() != null)
		{
			for (int j = 0; j < schematic.getEntities().tagCount(); j++)
			{
				NBTTagCompound entityCompound = schematic.getEntities().getCompoundTagAt(j);
				Entity entity = EntityList.createEntityFromNBT(entityCompound, world);

				if (entity != null)
				{
					entity.getPosition().add(xPosition, yPosition, zPosition);
					world.spawnEntityInWorld(entity);
				}
			}
		}
	}

	private static void printIncorrectIds(short[] blocks)
	{
		List<Short> incorrectIds = new ArrayList<Short>();
		int[] numberOfIncorrect = new int[10000];
		for (short b : blocks)
		{
			if (b < 0 && !incorrectIds.contains(b))
			{
				incorrectIds.add(b);
				numberOfIncorrect[Math.abs(b) - 2] = 1;
			}
			else if (b < 0)
			{
				numberOfIncorrect[Math.abs(b) - 2] = numberOfIncorrect[Math.abs(b) - 2] + 1;
			}
		}
		for (short b : incorrectIds)
		{
			LogHelper.info(numberOfIncorrect[Math.abs(b) - 2] + " incorrect ids of type " + b + " found in the schematic.");
		}
	}
}
