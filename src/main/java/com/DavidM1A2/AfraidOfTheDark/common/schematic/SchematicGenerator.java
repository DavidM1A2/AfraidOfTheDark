/*
 * Credit to:
 * http://www.minecraftforge.net/forum/index.php/topic,21045.0.html
 */
package com.DavidM1A2.AfraidOfTheDark.common.schematic;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.DavidM1A2.AfraidOfTheDark.common.utility.Point3D;
import com.DavidM1A2.AfraidOfTheDark.common.utility.WorldGenerationUtility;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public final class SchematicGenerator
{
	private static final Set<Short> latePlacePriorityBlocks = new HashSet<Short>()
	{
		{
			add((short) Block.getIdFromBlock(Blocks.rail));
			add((short) Block.getIdFromBlock(Blocks.reeds));
			add((short) Block.getIdFromBlock(Blocks.powered_comparator));
			add((short) Block.getIdFromBlock(Blocks.powered_repeater));
			add((short) Block.getIdFromBlock(Blocks.sapling));
			add((short) Block.getIdFromBlock(Blocks.detector_rail));
			add((short) Block.getIdFromBlock(Blocks.redstone_torch));
			add((short) Block.getIdFromBlock(Blocks.vine));
			add((short) Block.getIdFromBlock(Blocks.standing_sign));
			add((short) Block.getIdFromBlock(Blocks.wall_sign));
			add((short) Block.getIdFromBlock(Blocks.redstone_wire));
			add((short) Block.getIdFromBlock(Blocks.torch));
			add((short) Block.getIdFromBlock(Blocks.wooden_button));
			add((short) Block.getIdFromBlock(Blocks.stone_button));
			add((short) Block.getIdFromBlock(Blocks.wooden_pressure_plate));
			add((short) Block.getIdFromBlock(Blocks.stone_pressure_plate));
			add((short) Block.getIdFromBlock(Blocks.light_weighted_pressure_plate));
			add((short) Block.getIdFromBlock(Blocks.heavy_weighted_pressure_plate));
			add((short) Block.getIdFromBlock(Blocks.deadbush));
			add((short) Block.getIdFromBlock(Blocks.bed));
			add((short) Block.getIdFromBlock(Blocks.trapdoor));
			add((short) Block.getIdFromBlock(Blocks.carpet));
			add((short) Block.getIdFromBlock(Blocks.iron_door));
			add((short) Block.getIdFromBlock(Blocks.ladder));
			add((short) Block.getIdFromBlock(Blocks.dark_oak_door));
			add((short) Block.getIdFromBlock(Blocks.birch_door));
			add((short) Block.getIdFromBlock(Blocks.oak_door));
			add((short) Block.getIdFromBlock(Blocks.acacia_door));
			add((short) Block.getIdFromBlock(Blocks.spruce_door));
			add((short) Block.getIdFromBlock(Blocks.double_plant));
		}
	};

	private static final int DIAMOND_BLOCK_ID = Block.getIdFromBlock(Blocks.diamond_block);
	private static final int AIR_BLOCK_ID = Block.getIdFromBlock(Blocks.air);

	public static void generateSchematic(Schematic schematic, World world, int xPosition, int yPosition, int zPosition)
	{
		if (schematic == null)
		{
			return;
		}
		else
		{
			SchematicGenerator.generateBlocks(schematic, world, xPosition, yPosition, zPosition);

			SchematicGenerator.loadTileEntities(schematic, world, xPosition, yPosition, zPosition);

			SchematicGenerator.loadEntities(schematic, world, xPosition, yPosition, zPosition);
		}
	}

	public static void generateSchematicWithLoot(Schematic schematic, World world, int xPosition, int yPosition, int zPosition, LootTable lootTable)
	{
		if (schematic == null)
		{
			return;
		}
		else
		{
			SchematicGenerator.generateBlocks(schematic, world, xPosition, yPosition, zPosition);

			SchematicGenerator.loadTileEntitiesWithLoot(schematic, world, xPosition, yPosition, zPosition, lootTable);

			SchematicGenerator.loadEntities(schematic, world, xPosition, yPosition, zPosition);
		}
	}

	private static void generateBlocks(Schematic schematic, World world, int xPosition, int yPosition, int zPosition)
	{
		int i = 0;

		List<Short> blocksToPlaceLater = new LinkedList<Short>();
		List<Byte> blocksToPlaceLaterMeta = new LinkedList<Byte>();
		List<Point3D> laterBlockPositions = new LinkedList<Point3D>();

		// A schematic is just a code representation of an MCEdit Schematic
		for (int y = 0; y < schematic.getHeight(); y++)
		{
			for (int z = 0; z < schematic.getLength(); z++)
			{
				for (int x = 0; x < schematic.getWidth(); x++)
				{
					// This is the next block in the schematic file we will place stored as a short

					//Block nextToPlace = Block.getBlockById(schematic.getBlocks()[i]);
					int nextToPlace = schematic.getBlocks()[i];

					if (nextToPlace != SchematicGenerator.AIR_BLOCK_ID)
					{
						// Diamond blocks represent air blocks in my schematic system. This allows for easy underground
						// structure generation.
						if (nextToPlace == SchematicGenerator.DIAMOND_BLOCK_ID)
						{
							WorldGenerationUtility.setBlockStateFast(world, new BlockPos(x + xPosition, y + yPosition, z + zPosition), Blocks.air.getDefaultState(), 3);
						}
						// latePlacePriorityBlocks is a hashset that contains a list of blocks that need another block
						// placed first in order to be placed (Like torches or laddedrs require a block to be hanging off, etc)
						else if (latePlacePriorityBlocks.contains(schematic.getBlocks()[i]))
						{
							// 3 Array lists that save each of these block's properties
							blocksToPlaceLater.add(schematic.getBlocks()[i]);
							blocksToPlaceLaterMeta.add(schematic.getData()[i]);
							laterBlockPositions.add(new Point3D(x + xPosition, y + yPosition, z + zPosition));
						}
						// If the block is air, we leave the block untouched, this makes underground world gen easy
						// Else, we set the block state
						else
						{
							WorldGenerationUtility.setBlockStateFast(world, new BlockPos(x + xPosition, y + yPosition, z + zPosition), Block.getBlockById(nextToPlace).getStateFromMeta(schematic.getData()[i]), 3);
						}
					}

					i = i + 1;
				}
			}
		}

		Iterator<Short> iteratorBlock = blocksToPlaceLater.iterator();
		Iterator<Byte> iteratorMeta = blocksToPlaceLaterMeta.iterator();
		Iterator<Point3D> iteratorLocation = laterBlockPositions.iterator();

		while (iteratorBlock.hasNext())
		{
			Block next = Block.getBlockById(iteratorBlock.next());
			IBlockState blockState = next.getStateFromMeta(iteratorMeta.next());
			BlockPos blockPos = (iteratorLocation.next()).toBlockPos();

			// Doors need to be specially placed because reasons, things, and stuff (top & bottom need to be placed at the same time)
			if (next instanceof BlockDoor)
			{
				if (blockState.getValue(BlockDoor.HALF).equals(BlockDoor.EnumDoorHalf.LOWER))
				{
					WorldGenerationUtility.setBlockStateFast(world, blockPos, blockState, 3);
					WorldGenerationUtility.setBlockStateFast(world, blockPos.offset(EnumFacing.UP), blockState.withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER), 3);
				}
			}
			else
			{
				WorldGenerationUtility.setBlockStateFast(world, blockPos, blockState, 3);
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
}
