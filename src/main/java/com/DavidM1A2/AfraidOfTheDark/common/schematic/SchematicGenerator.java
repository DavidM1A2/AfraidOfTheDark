/*
 * Credit to:
 * http://www.minecraftforge.net/forum/index.php/topic,21045.0.html
 */
package com.DavidM1A2.AfraidOfTheDark.common.schematic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Point3D;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot.LootTable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public final class SchematicGenerator
{
	private static final Set<Short> latePlacePriorityBlocks = new HashSet<Short>(Arrays.asList(new Short[]
	{ (short) Block.getIdFromBlock(Blocks.rail), (short) Block.getIdFromBlock(Blocks.reeds), (short) Block.getIdFromBlock(Blocks.powered_comparator), (short) Block.getIdFromBlock(Blocks.powered_repeater), (short) Block.getIdFromBlock(Blocks.sapling), (short) Block.getIdFromBlock(
			Blocks.detector_rail), (short) Block.getIdFromBlock(Blocks.redstone_torch), (short) Block.getIdFromBlock(Blocks.vine), (short) Block.getIdFromBlock(Blocks.standing_sign), (short) Block.getIdFromBlock(Blocks.wall_sign), (short) Block.getIdFromBlock(Blocks.redstone_wire), (short) Block
					.getIdFromBlock(Blocks.torch), (short) Block.getIdFromBlock(Blocks.wooden_button), (short) Block.getIdFromBlock(Blocks.stone_button), (short) Block.getIdFromBlock(Blocks.wooden_pressure_plate), (short) Block.getIdFromBlock(Blocks.stone_pressure_plate), (short) Block
							.getIdFromBlock(Blocks.light_weighted_pressure_plate), (short) Block.getIdFromBlock(Blocks.heavy_weighted_pressure_plate), (short) Block.getIdFromBlock(Blocks.deadbush), (short) Block.getIdFromBlock(Blocks.bed), (short) Block.getIdFromBlock(Blocks.trapdoor), (short) Block
									.getIdFromBlock(Blocks.carpet), (short) Block.getIdFromBlock(Blocks.iron_door), (short) Block.getIdFromBlock(Blocks.ladder), (short) Block.getIdFromBlock(Blocks.dark_oak_door), (short) Block.getIdFromBlock(Blocks.birch_door), (short) Block.getIdFromBlock(
											Blocks.oak_door), (short) Block.getIdFromBlock(Blocks.acacia_door), (short) Block.getIdFromBlock(Blocks.spruce_door), (short) Block.getIdFromBlock(Blocks.double_plant) }));

	public static void generateSchematic(Schematic schematic, World world, int xPosition, int yPosition, int zPosition)
	{
		if (schematic == null)
		{
			return;
		}

		SchematicGenerator.generateBlocks(schematic, world, xPosition, yPosition, zPosition);

		SchematicGenerator.loadTileEntities(schematic, world, xPosition, yPosition, zPosition);

		SchematicGenerator.loadEntities(schematic, world, xPosition, yPosition, zPosition);
	}

	public static void generateSchematicWithLoot(Schematic schematic, World world, int xPosition, int yPosition, int zPosition, LootTable lootTable)
	{
		if (schematic == null)
		{
			return;
		}

		SchematicGenerator.generateBlocks(schematic, world, xPosition, yPosition, zPosition);

		SchematicGenerator.loadTileEntitiesWithLoot(schematic, world, xPosition, yPosition, zPosition, lootTable);

		SchematicGenerator.loadEntities(schematic, world, xPosition, yPosition, zPosition);
	}

	private static void generateBlocks(Schematic schematic, World world, int xPosition, int yPosition, int zPosition)
	{
		int i = 0;

		List<Short> blocksToPlaceLater = new LinkedList<Short>();
		List<Byte> blocksToPlaceLaterMeta = new LinkedList<Byte>();
		List<Point3D> laterBlockPositions = new LinkedList<Point3D>();

		SchematicGenerator.printIncorrectIds(schematic.getBlocks());

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
					else if (latePlacePriorityBlocks.contains(schematic.getBlocks()[i]))
					{
						blocksToPlaceLater.add(schematic.getBlocks()[i]);
						blocksToPlaceLaterMeta.add(schematic.getData()[i]);
						laterBlockPositions.add(new Point3D(currentLocation.getX(), currentLocation.getY(), currentLocation.getZ()));
					}
					else if (nextToPlace != Blocks.air)
					{
						world.setBlockState(currentLocation, nextToPlace.getStateFromMeta(schematic.getData()[i]));
					}

					i = i + 1;
				}
			}
		}

		Iterator iteratorBlock = blocksToPlaceLater.iterator();
		Iterator iteratorMeta = blocksToPlaceLaterMeta.iterator();
		Iterator iteratorLocation = laterBlockPositions.iterator();

		while (iteratorBlock.hasNext())
		{
			Block next = Block.getBlockById((Short) iteratorBlock.next());
			IBlockState blockState = next.getStateFromMeta((Byte) iteratorMeta.next());
			BlockPos blockPos = ((Point3D) iteratorLocation.next()).toBlockPos();

			// Doors need to be specially placed because reasons, things, and stuff (top & bottom need to be placed at the same time)
			if (next instanceof BlockDoor)
			{
				if (blockState.getValue(BlockDoor.HALF_PROP).equals(BlockDoor.EnumDoorHalf.LOWER))
				{
					world.setBlockState(blockPos, blockState);
					world.setBlockState(blockPos.offsetUp(), blockState.withProperty(BlockDoor.HALF_PROP, BlockDoor.EnumDoorHalf.UPPER));
				}
			}
			else
			{
				world.setBlockState(blockPos, blockState);
			}
		}
	}

	private static void loadTileEntities(Schematic schematic, World world, int xPosition, int yPosition, int zPosition)
	{
		if (schematic.getTileentities() != null)
		{
			for (int j = 0; j < schematic.getTileentities().tagCount(); j++)
			{
				NBTTagCompound tileEntityCompound = schematic.getTileentities().getCompoundTagAt(j);
				TileEntity tileEntity = TileEntity.createAndLoadEntity(tileEntityCompound);

				if (tileEntity != null)
				{
					BlockPos tileEntityLocation = new BlockPos(tileEntity.getPos().getX() + xPosition, tileEntity.getPos().getY() + yPosition, tileEntity.getPos().getZ() + zPosition);

					// Remove the default tile entity
					world.removeTileEntity(tileEntityLocation);

					world.setTileEntity(tileEntityLocation, tileEntity);
					tileEntity.setPos(tileEntityLocation);
				}
			}
		}
	}

	private static void loadTileEntitiesWithLoot(Schematic schematic, World world, int xPosition, int yPosition, int zPosition, LootTable lootTable)
	{
		if (schematic.getTileentities() != null)
		{
			for (int j = 0; j < schematic.getTileentities().tagCount(); j++)
			{
				NBTTagCompound tileEntityCompound = schematic.getTileentities().getCompoundTagAt(j);
				TileEntity tileEntity = TileEntity.createAndLoadEntity(tileEntityCompound);

				if (tileEntity != null)
				{
					BlockPos tileEntityLocation = new BlockPos(tileEntity.getPos().getX() + xPosition, tileEntity.getPos().getY() + yPosition, tileEntity.getPos().getZ() + zPosition);

					// Remove the default tile entity
					world.removeTileEntity(tileEntityLocation);

					world.setTileEntity(tileEntityLocation, tileEntity);
					tileEntity.setPos(tileEntityLocation);

					if (tileEntity instanceof TileEntityChest)
					{
						TileEntityChest tileEntityChest = (TileEntityChest) world.getTileEntity(tileEntityLocation);

						if (tileEntityChest != null)
						{
							lootTable.generate(tileEntityChest);
						}
					}
				}
			}
		}
	}

	private static void loadEntities(Schematic schematic, World world, int xPosition, int yPosition, int zPosition)
	{
		if (schematic.getEntities() != null)
		{
			for (int j = 0; j < schematic.getEntities().tagCount(); j++)
			{
				NBTTagCompound entityCompound = schematic.getEntities().getCompoundTagAt(j);
				Entity entity = EntityList.createEntityFromNBT(entityCompound, world);

				if (entity != null)
				{
					entity.setPosition(entity.posX + xPosition, entity.posY + yPosition, entity.posZ + zPosition);
					world.spawnEntityInWorld(entity);
				}
			}
		}
	}

	private static void printIncorrectIds(short[] blocks)
	{
		List<Short> incorrectIds = new ArrayList<Short>();
		int[] numberOfIncorrect = new int[20000];
		for (short b : blocks)
		{
			if (b < 0 && !incorrectIds.contains(b))
			{
				incorrectIds.add(b);
				numberOfIncorrect[Math.abs(b)] = 1;
			}
			else if (b < 0)
			{
				numberOfIncorrect[Math.abs(b)] = numberOfIncorrect[Math.abs(b)] + 1;
			}
		}
		for (short b : incorrectIds)
		{
			LogHelper.info(numberOfIncorrect[Math.abs(b)] + " incorrect ids of the id " + b + " found in the schematic. ");
		}
	}

}
