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

import com.DavidM1A2.AfraidOfTheDark.common.handler.ConfigurationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Point3D;
import com.DavidM1A2.AfraidOfTheDark.common.utility.WorldGenerationUtility;
import com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.LootTable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public final class SchematicGenerator
{
	private static final Set<Short> latePlacePriorityBlocks = new HashSet<Short>()
	{
		{
			add((short) Block.getIdFromBlock(Blocks.RAIL));
			add((short) Block.getIdFromBlock(Blocks.REEDS));
			add((short) Block.getIdFromBlock(Blocks.POWERED_COMPARATOR));
			add((short) Block.getIdFromBlock(Blocks.POWERED_REPEATER));
			add((short) Block.getIdFromBlock(Blocks.SAPLING));
			add((short) Block.getIdFromBlock(Blocks.DETECTOR_RAIL));
			add((short) Block.getIdFromBlock(Blocks.REDSTONE_TORCH));
			add((short) Block.getIdFromBlock(Blocks.VINE));
			add((short) Block.getIdFromBlock(Blocks.STANDING_SIGN));
			add((short) Block.getIdFromBlock(Blocks.WALL_SIGN));
			add((short) Block.getIdFromBlock(Blocks.REDSTONE_WIRE));
			add((short) Block.getIdFromBlock(Blocks.TORCH));
			add((short) Block.getIdFromBlock(Blocks.WOODEN_BUTTON));
			add((short) Block.getIdFromBlock(Blocks.STONE_BUTTON));
			add((short) Block.getIdFromBlock(Blocks.WOODEN_PRESSURE_PLATE));
			add((short) Block.getIdFromBlock(Blocks.STONE_PRESSURE_PLATE));
			add((short) Block.getIdFromBlock(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE));
			add((short) Block.getIdFromBlock(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE));
			add((short) Block.getIdFromBlock(Blocks.DEADBUSH));
			add((short) Block.getIdFromBlock(Blocks.BED));
			add((short) Block.getIdFromBlock(Blocks.TRAPDOOR));
			add((short) Block.getIdFromBlock(Blocks.CARPET));
			add((short) Block.getIdFromBlock(Blocks.IRON_DOOR));
			add((short) Block.getIdFromBlock(Blocks.LADDER));
			add((short) Block.getIdFromBlock(Blocks.DARK_OAK_DOOR));
			add((short) Block.getIdFromBlock(Blocks.BIRCH_DOOR));
			add((short) Block.getIdFromBlock(Blocks.OAK_DOOR));
			add((short) Block.getIdFromBlock(Blocks.ACACIA_DOOR));
			add((short) Block.getIdFromBlock(Blocks.SPRUCE_DOOR));
			add((short) Block.getIdFromBlock(Blocks.DOUBLE_PLANT));
		}
	};

	private static final Set<Short> lightUpdateBlocks = new HashSet<Short>()
	{
		{
			add((short) Block.getIdFromBlock(Blocks.GLOWSTONE));
			add((short) Block.getIdFromBlock(Blocks.TORCH));
			add((short) Block.getIdFromBlock(ModBlocks.glowStalk));
		}
	};

	private static final int DIAMOND_BLOCK_ID = Block.getIdFromBlock(Blocks.DIAMOND_BLOCK);
	private static final int AIR_BLOCK_ID = Block.getIdFromBlock(Blocks.AIR);

	public static void generateSchematic(Schematic schematic, World world, int xPosition, int yPosition, int zPosition)
	{
		if (schematic != null)
		{
			SchematicGenerator.generateBlocks(schematic, world, xPosition, yPosition, zPosition);

			SchematicGenerator.loadTileEntities(schematic, world, xPosition, yPosition, zPosition);

			SchematicGenerator.loadEntities(schematic, world, xPosition, yPosition, zPosition);
		}
	}

	public static void generateSchematicWithLoot(Schematic schematic, World world, int xPosition, int yPosition, int zPosition, LootTable lootTable)
	{
		if (schematic != null)
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
		List<Point3D> lightBlockPositions = new LinkedList<Point3D>();

		// A schematic is just a code representation of an MCEdit Schematic
		for (int y = 0; y < schematic.getHeight(); y++)
		{
			for (int z = 0; z < schematic.getLength(); z++)
			{
				for (int x = 0; x < schematic.getWidth(); x++)
				{
					// This is the next block in the schematic file we will place stored as a short

					int nextToPlace = schematic.getBlocks()[i];

					if (nextToPlace != SchematicGenerator.AIR_BLOCK_ID)
					{
						// Diamond blocks represent air blocks in my schematic system. This allows for easy underground
						// structure generation.
						if (nextToPlace == SchematicGenerator.DIAMOND_BLOCK_ID)
						{
							WorldGenerationUtility.setBlockStateFast(world, new BlockPos(x + xPosition, y + yPosition, z + zPosition), Blocks.AIR.getDefaultState(), 3);
						}
						// latePlacePriorityBlocks is a hashset that contains a list of blocks that need another block
						// placed first in order to be placed (Like torches or ladders require a block to be hanging off, etc)
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
							world.setBlockState(new BlockPos(x + xPosition, y + yPosition, z + zPosition), Block.getBlockById(nextToPlace).getStateFromMeta(schematic.getData()[i]), 3);
							//WorldGenerationUtility.setBlockStateFast(world, new BlockPos(x + xPosition, y + yPosition, z + zPosition), Block.getBlockById(nextToPlace).getStateFromMeta(schematic.getData()[i]), 3);
						}

						if (ConfigurationHandler.enableWorldGenLightUpdates && lightUpdateBlocks.contains(schematic.getBlocks()[i]))
						{
							lightBlockPositions.add(new Point3D(x + xPosition, y + yPosition, z + zPosition));
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

		Iterator<Point3D> blockLocationsToUpdate = lightBlockPositions.iterator();
		while (blockLocationsToUpdate.hasNext())
		{
			world.checkLight(blockLocationsToUpdate.next().toBlockPos());
		}
	}

	private static void loadTileEntities(Schematic schematic, World world, int xPosition, int yPosition, int zPosition)
	{
		if (schematic.getTileEntities() != null)
		{
			for (int j = 0; j < schematic.getTileEntities().tagCount(); j++)
			{
				NBTTagCompound tileEntityCompound = schematic.getTileEntities().getCompoundTagAt(j);
				TileEntity tileEntity = TileEntity.create(world, tileEntityCompound);

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
		if (schematic.getTileEntities() != null)
		{
			for (int j = 0; j < schematic.getTileEntities().tagCount(); j++)
			{
				NBTTagCompound tileEntityCompound = schematic.getTileEntities().getCompoundTagAt(j);
				TileEntity tileEntity = TileEntity.create(world, tileEntityCompound);

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
						lootTable.generate(tileEntityChest);
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
					world.spawnEntity(entity);
				}
			}
		}
	}
}