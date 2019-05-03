package com.DavidM1A2.afraidofthedark.common.worldGeneration.schematic;

import com.DavidM1A2.afraidofthedark.common.worldGeneration.LootTable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

/**
 * Class used to generate schematics
 */
public class SchematicGenerator
{
	/**
	 * Generates a schematic fully in a world at a specific block position without loot
	 *
	 * @param schematic The schematic to generate
	 * @param world The world to generate the schematic in
	 * @param blockPos The position to generate the schematic at
	 */
	public static void generateSchematic(Schematic schematic, World world, BlockPos blockPos)
	{
		generateSchematic(schematic, world, blockPos, null, null);
	}

	/**
	 * Generates a specific chunk of a schematic in a world at a specific block position without loot
	 *
	 * @param schematic The schematic to generate
	 * @param world The world to generate the schematic in
	 * @param blockPos The position to generate the schematic at
	 * @param chunkPos Optional argument to specify which chunk specify to generate of this schematic
	 */
	public static void generateSchematic(Schematic schematic, World world, BlockPos blockPos, ChunkPos chunkPos)
	{
		generateSchematic(schematic, world, blockPos, chunkPos, null);
	}

	/**
	 * Generates a schematic fully in a world at a specific block position with a given loot table for chests
	 *
	 * @param schematic The schematic to generate
	 * @param world The world to generate the schematic in
	 * @param blockPos The position to generate the schematic at
	 * @param lootTable Optional argument to specify what loot to generate inside chests
	 */
	public static void generateSchematic(Schematic schematic, World world, BlockPos blockPos, LootTable lootTable)
	{
		generateSchematic(schematic, world, blockPos, null, lootTable);
	}

	/**
	 * Generates a specific chunk of a schematic in a world at a specific block position with a given loot table for chests
	 *
	 * @param schematic The schematic to generate
	 * @param world The world to generate the schematic in
	 * @param blockPos The position to generate the schematic at
	 * @param chunkPos Optional argument to specify which chunk specify to generate of this schematic
	 * @param lootTable Optional argument to specify what loot to generate inside chests
	 */
	public static void generateSchematic(Schematic schematic, World world, BlockPos blockPos, ChunkPos chunkPos, LootTable lootTable)
	{
		// Generate server side only
		if (!world.isRemote)
		{
			generateBlocks(schematic, world, blockPos, chunkPos);
			generateTileEntities(schematic, world, blockPos, chunkPos, lootTable);
			generateEntities(schematic, world, blockPos, chunkPos);
		}
	}

	/**
	 * Generates the blocks of a specific chunk of a schematic in a world at a specific block position
	 *
	 * @param schematic The schematic to generate
	 * @param world The world to generate the schematic in
	 * @param blockPos The position to generate the schematic at
	 * @param chunkPos Optional argument to specify which chunk specify to generate of this schematic
	 */
	private static void generateBlocks(Schematic schematic, World world, BlockPos blockPos, ChunkPos chunkPos)
	{
		// Store the schematic variables up here so we don't have lots of superfluous method calls. Method calls are slow
		// if they happen over and over again, instead cache our variable values and then do simply math which is much
		// faster!
		int posX = blockPos.getX();
		int posY = blockPos.getY();
		int posZ = blockPos.getZ();
		Block[] blocks = schematic.getBlocks();
		int[] data = schematic.getData();
		short width = schematic.getWidth();
		short height = schematic.getHeight();
		short length = schematic.getLength();

		// Compute the starting x, y, and z positions as well as the ending x, y, and z positions. We use this information
		// to only generate one chunk at a time if requested.
		int startY = posY;
		int endY = posY + height;
		int startZ = posZ;
		int endZ = posZ + length;
		int startX = posX;
		int endX = posX + width;

		// If we should only generate a single chunk update our start and end X/Z to respect that
		if (chunkPos != null)
		{
			startZ = MathHelper.clamp(startZ, chunkPos.getZStart(), chunkPos.getZEnd() + 1);
			endZ = MathHelper.clamp(endZ, chunkPos.getZStart(), chunkPos.getZEnd() + 1);

			startX = MathHelper.clamp(startX, chunkPos.getXStart(), chunkPos.getXEnd() + 1);
			endX = MathHelper.clamp(endX, chunkPos.getXStart(), chunkPos.getXEnd() + 1);
		}

		// Fixes cascading world gen with leaves/fences
		int setBlockFlags = 2 | 16;

		// Iterate over the Y axis first since that's the format that schematics use
		for (int y = startY; y < endY; y++)
		{
			// Get the y index which we can use to index into the blocks array
			int indexY = (y - posY) * length * width;
			// Iterate over the Z axis second
			for (int z = startZ; z < endZ; z++)
			{
				// Get the z index which we can use to index into the blocks array
				int indexZ = (z - posZ) * width;
				// Iterate over the X axis last
				for (int x = startX; x < endX; x++)
				{
					// Get the x index which we can use to index into the blocks array
					int indexX = x - posX;
					// Compute the index into our blocks array
					int index = indexY + indexZ + indexX;
					// Grab the reference to the next block to place
					Block nextToPlace = blocks[index];

					// If the block in the schematic is air then ignore it
					if (nextToPlace != Blocks.AIR)
					{
						// Diamond blocks represent air blocks in my schematic system. This allows for easy underground structure generation.
						if (nextToPlace == Blocks.DIAMOND_BLOCK)
						{
							// Set the block to air
							world.setBlockState(new BlockPos(x, y, z), Blocks.AIR.getDefaultState(), setBlockFlags);
						}
						else
						{
							// Otherwise set the block based on state from the data array
							world.setBlockState(new BlockPos(x, y, z), nextToPlace.getStateFromMeta(data[index]), setBlockFlags);
						}
					}
				}
			}
		}
	}

	/**
	 * Generates the tile entities of a specific chunk of a schematic in a world at a specific block position with a given loot table for chests
	 *
	 * @param schematic The schematic to generate
	 * @param world The world to generate the schematic in
	 * @param blockPos The position to generate the schematic at
	 * @param chunkPos Optional argument to specify which chunk specify to generate of this schematic
	 * @param lootTable Optional argument to specify what loot to generate inside chests
	 */
	private static void generateTileEntities(Schematic schematic, World world, BlockPos blockPos, ChunkPos chunkPos, LootTable lootTable)
	{
		// Get the list of tile entities inside this schematic
		NBTTagList tileEntities = schematic.getTileEntities();
		// Iterate over each tile entity
		for (int i = 0; i < tileEntities.tagCount(); i++)
		{
			// Grab the compound that represents this tile entity
			NBTTagCompound tileEntityCompound = tileEntities.getCompoundTagAt(i);
			// Instantiate the tile entity object from the compound
			TileEntity tileEntity = TileEntity.create(world, tileEntityCompound);

			// If the entity is valid, continue...
			if (tileEntity != null)
			{
				// Get the X, Y, and Z coordinates of this entity if instantiated inside the world
				BlockPos tileEntityPosition = tileEntity.getPos().add(blockPos);

				// If the chunk pos was not given or we are in the correct chunk spawn the entity in
				if (chunkPos == null || isInsideChunk(tileEntityPosition, chunkPos))
				{
					// Remove the existing tile entity at the location
					world.removeTileEntity(tileEntityPosition);
					// Set the new position of our tile entity
					tileEntity.setPos(tileEntityPosition);
					// Add the tile entity to the world
					world.setTileEntity(tileEntityPosition, tileEntity);
					// If the tile entity is a chest and we have a loot table then generate the chest
					if (tileEntity instanceof TileEntityChest && lootTable != null)
						lootTable.generate((TileEntityChest) tileEntity);
				}
			}
		}
	}

	/**
	 * Generates the entities of a specific chunk of a schematic in a world at a specific block position
	 *
	 * @param schematic The schematic to generate entities for
	 * @param world The world to generate the schematic entities in
	 * @param blockPos The position to generate the schematic entities at
	 * @param chunkPos Optional argument to specify which chunk specify to generate of this schematic
	 */
	private static void generateEntities(Schematic schematic, World world, BlockPos blockPos, ChunkPos chunkPos)
	{
		// Get the list of entities inside this schematic
		NBTTagList entities = schematic.getEntities();
		// Iterate over each entity
		for (int i = 0; i < entities.tagCount(); i++)
		{
			// Grab the compound that represents this entity
			NBTTagCompound entityCompound = entities.getCompoundTagAt(i);
			// Instantiate the entity object from the compound
			Entity entity = EntityList.createEntityFromNBT(entityCompound, world);
			// If the entity is valid, continue...
			if (entity != null)
			{
				// Get the X, Y, and Z coordinates of this entity if instantiated inside the world
				double newX = entity.posX + blockPos.getX();
				double newY = entity.posY + blockPos.getY();
				double newZ = entity.posZ + blockPos.getZ();

				// If the chunk pos was not given or we are in the correct chunk spawn the entity in
				if (chunkPos == null || isInsideChunk(newX, newZ, chunkPos))
				{
					entity.setPosition(newX, newY, newZ);
					world.spawnEntity(entity);
				}
			}
		}
	}

	/**
	 * Returns true if the x and z position are inside the given chunk, or false otherwise
	 *
	 * @param x The X position of the point to test
	 * @param z The Z position of the point to test
	 * @param chunkPos The chunk to test
	 * @return True if the (x, z) coordinate is inside the chunk or false otherwise
	 */
	private static boolean isInsideChunk(double x, double z, ChunkPos chunkPos)
	{
		return x >= chunkPos.getXStart() && x <= chunkPos.getXEnd() && z >= chunkPos.getZStart() && z <= chunkPos.getZEnd();
	}

	/**
	 * Returns true if the x and z position are inside the given chunk, or false otherwise
	 *
	 * @param blockPos The position of the point to test
	 * @param chunkPos The chunk to test
	 * @return True if the (x, z) coordinate is inside the chunk or false otherwise
	 */
	private static boolean isInsideChunk(BlockPos blockPos, ChunkPos chunkPos)
	{
		return blockPos.getX() >= chunkPos.getXStart() && blockPos.getX() <= chunkPos.getXEnd() && blockPos.getZ() >= chunkPos.getZStart() && blockPos.getZ() <= chunkPos.getZEnd();
	}
}
