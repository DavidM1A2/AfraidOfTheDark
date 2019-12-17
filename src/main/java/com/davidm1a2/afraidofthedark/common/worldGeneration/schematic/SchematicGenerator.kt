package com.davidm1a2.afraidofthedark.common.worldGeneration.schematic

import com.davidm1a2.afraidofthedark.common.worldGeneration.LootTable
import com.davidm1a2.afraidofthedark.common.worldGeneration.WorldGenFast
import net.minecraft.entity.EntityList
import net.minecraft.init.Blocks
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityChest
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import java.util.*

/**
 * Class used to generate schematics
 */
object SchematicGenerator
{
    // A massive set of blocks that need to "be placed on" another block so we generate them after the solid blocks
    private val PHASE_2_BLOCKS = setOf(
        Blocks.SAPLING,
        Blocks.BED,
        Blocks.RAIL,
        Blocks.ACTIVATOR_RAIL,
        Blocks.DETECTOR_RAIL,
        Blocks.GOLDEN_RAIL,
        Blocks.DEADBUSH,
        Blocks.TALLGRASS,
        Blocks.YELLOW_FLOWER,
        Blocks.RED_FLOWER,
        Blocks.BROWN_MUSHROOM,
        Blocks.RED_MUSHROOM,
        Blocks.TORCH,
        Blocks.FIRE,
        Blocks.REDSTONE_WIRE,
        Blocks.WHEAT,
        Blocks.STANDING_SIGN,
        Blocks.WALL_SIGN,
        Blocks.LADDER,
        Blocks.LEVER,
        Blocks.STONE_PRESSURE_PLATE,
        Blocks.IRON_DOOR,
        Blocks.WOODEN_PRESSURE_PLATE,
        Blocks.REDSTONE_TORCH,
        Blocks.UNLIT_REDSTONE_TORCH,
        Blocks.STONE_BUTTON,
        Blocks.CACTUS,
        Blocks.REEDS,
        Blocks.POWERED_REPEATER,
        Blocks.UNPOWERED_REPEATER,
        Blocks.PUMPKIN_STEM,
        Blocks.MELON_STEM,
        Blocks.VINE,
        Blocks.WATERLILY,
        Blocks.NETHER_WART,
        Blocks.DRAGON_EGG,
        Blocks.COCOA,
        Blocks.TRIPWIRE_HOOK,
        Blocks.TRIPWIRE,
        Blocks.CARROTS,
        Blocks.POTATOES,
        Blocks.WOODEN_BUTTON,
        Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE,
        Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE,
        Blocks.CARPET,
        Blocks.DOUBLE_PLANT,
        Blocks.STANDING_BANNER,
        Blocks.WALL_BANNER,
        Blocks.SPRUCE_DOOR,
        Blocks.BIRCH_DOOR,
        Blocks.JUNGLE_DOOR,
        Blocks.ACACIA_DOOR,
        Blocks.DARK_OAK_DOOR,
        Blocks.BEETROOTS
    )

    /**
     * Generates a specific chunk of a schematic in a world at a specific block position without loot
     *
     * @param schematic The schematic to generate
     * @param world     The world to generate the schematic in
     * @param blockPos  The position to generate the schematic at
     * @param chunkPos  Optional argument to specify which chunk specify to generate of this schematic
     * @param lootTable Optional argument to specify what loot to generate inside chests
     */
    fun generateSchematic(
        schematic: Schematic,
        world: World,
        blockPos: BlockPos,
        chunkPos: ChunkPos? = null,
        lootTable: LootTable? = null
    )
    {
        // Generate server side only
        if (!world.isRemote)
        {
            generateBlocks(schematic, world, blockPos, chunkPos)
            generateTileEntities(schematic, world, blockPos, chunkPos, lootTable)
            generateEntities(schematic, world, blockPos, chunkPos)
            computeLight(schematic, world, blockPos, chunkPos)
        }
    }

    /**
     * Generates the blocks of a specific chunk of a schematic in a world at a specific block position
     *
     * @param schematic The schematic to generate
     * @param world     The world to generate the schematic in
     * @param blockPos  The position to generate the schematic at
     * @param chunkPos  Optional argument to specify which chunk specify to generate of this schematic
     */
    private fun generateBlocks(schematic: Schematic, world: World, blockPos: BlockPos, chunkPos: ChunkPos?)
    {
        // Store the schematic variables up here so we don't have lots of superfluous method calls. Method calls are slow
        // if they happen over and over again, instead cache our variable values and then do simply math which is much
        // faster!
        val posX = blockPos.x
        val posY = blockPos.y
        val posZ = blockPos.z
        val blocks = schematic.getBlocks()
        val data = schematic.getData()
        val width = schematic.getWidth()
        val height = schematic.getHeight()
        val length = schematic.getLength()

        // Compute the starting x, y, and z positions as well as the ending x, y, and z positions. We use this information
        // to only generate one chunk at a time if requested.
        val endY = posY + height
        var startZ = posZ
        var endZ = posZ + length
        var startX = posX
        var endX = posX + width

        // If we should only generate a single chunk update our start and end X/Z to respect that
        if (chunkPos != null)
        {
            startZ = MathHelper.clamp(startZ, chunkPos.zStart, chunkPos.zEnd + 1)
            endZ = MathHelper.clamp(endZ, chunkPos.zStart, chunkPos.zEnd + 1)
            startX = MathHelper.clamp(startX, chunkPos.xStart, chunkPos.xEnd + 1)
            endX = MathHelper.clamp(endX, chunkPos.xStart, chunkPos.xEnd + 1)
        }

        // Fixes cascading world gen with leaves/fences
        val setBlockFlags = 2 or 16

        // Keep a list of indices we need to generate in phase 2 of the generation because they rely on other blocks
        val phase2Blocks: MutableList<Int> = mutableListOf()
        val phase2Positions: MutableList<BlockPos> = mutableListOf()

        ///
        /// Phase 1 is to generate all solid blocks, this should take most of the time
        ///

        // Iterate over the Y axis first since that's the format that schematics use
        for (y in posY until endY)
        {
            // Get the y index which we can use to index into the blocks array
            val indexY = (y - posY) * length * width
            // Iterate over the Z axis second
            for (z in startZ until endZ)
            {
                // Get the z index which we can use to index into the blocks array
                val indexZ = (z - posZ) * width
                // Iterate over the X axis last
                for (x in startX until endX)
                {
                    // Get the x index which we can use to index into the blocks array
                    val indexX = x - posX
                    // Compute the index into our blocks array
                    val index = indexY + indexZ + indexX

                    // Grab the reference to the next block to place
                    val nextToPlace = blocks[index]
                    // If the block in the schematic is air then ignore it
                    if (nextToPlace !== Blocks.AIR)
                    {
                        val position = BlockPos(x, y, z)
                        // Structure void blocks represent air blocks in my schematic system. This allows for easy underground structure generation.
                        if (nextToPlace === Blocks.STRUCTURE_VOID)
                        {
                            // Set the block to air
                            WorldGenFast.setBlockStateFast(world, position, Blocks.AIR.defaultState, setBlockFlags)
                        }
                        else
                        {
                            // If we can generate this block now do so
                            if (!PHASE_2_BLOCKS.contains(nextToPlace))
                            {
                                // Grab the blockstate to place
                                @Suppress("DEPRECATION")
                                val blockState = blocks[index].getStateFromMeta(data[index])
                                // Otherwise set the block based on state from the data array
                                WorldGenFast.setBlockStateFast(world, position, blockState, setBlockFlags)
                            }
                            else
                            {
                                phase2Blocks.add(index)
                                phase2Positions.add(position)
                            }
                        }
                    }
                }
            }
        }

        ///
        /// Phase 2 is to generate all non-solid blocks that "hang off" or are "placed on" other blocks
        ///

        for (i in phase2Blocks.indices)
        {
            // Grab our stored off block index and blockpos
            val index = phase2Blocks[i]
            val position = phase2Positions[i]
            // Grab the block state
            @Suppress("DEPRECATION")
            val blockState = blocks[index].getStateFromMeta(data[index])
            // Set the block
            WorldGenFast.setBlockStateFast(world, position, blockState, setBlockFlags)
        }
    }

    /**
     * Generates the tile entities of a specific chunk of a schematic in a world at a specific block position with a given loot table for chests
     *
     * @param schematic The schematic to generate
     * @param world     The world to generate the schematic in
     * @param blockPos  The position to generate the schematic at
     * @param chunkPos  Optional argument to specify which chunk specify to generate of this schematic
     * @param lootTable Optional argument to specify what loot to generate inside chests
     */
    private fun generateTileEntities(
        schematic: Schematic,
        world: World,
        blockPos: BlockPos,
        chunkPos: ChunkPos?,
        lootTable: LootTable?
    )
    {
        // Get the list of tile entities inside this schematic
        val tileEntities = schematic.getTileEntities()
        // Iterate over each tile entity
        for (i in 0 until tileEntities.tagCount())
        {
            // Grab the compound that represents this tile entity
            val tileEntityCompound = tileEntities.getCompoundTagAt(i)
            // Instantiate the tile entity object from the compound
            val tileEntity = TileEntity.create(world, tileEntityCompound)
            // If the entity is valid, continue...
            if (tileEntity != null)
            {
                // Get the X, Y, and Z coordinates of this entity if instantiated inside the world
                val tileEntityPosition = tileEntity.pos.add(blockPos)
                // If the chunk pos was not given or we are in the correct chunk spawn the entity in
                if (chunkPos == null || isInsideChunk(tileEntityPosition, chunkPos))
                {
                    // Remove the existing tile entity at the location
                    world.removeTileEntity(tileEntityPosition)
                    // Set the new position of our tile entity
                    tileEntity.pos = tileEntityPosition
                    // Add the tile entity to the world
                    world.setTileEntity(tileEntityPosition, tileEntity)
                    // If the tile entity is a chest and we have a loot table then generate the chest
                    if (tileEntity is TileEntityChest && lootTable != null)
                    {
                        lootTable.generate(tileEntity)
                    }
                }
            }
        }
    }

    /**
     * Generates the entities of a specific chunk of a schematic in a world at a specific block position
     *
     * @param schematic The schematic to generate entities for
     * @param world     The world to generate the schematic entities in
     * @param blockPos  The position to generate the schematic entities at
     * @param chunkPos  Optional argument to specify which chunk specify to generate of this schematic
     */
    private fun generateEntities(schematic: Schematic, world: World, blockPos: BlockPos, chunkPos: ChunkPos?)
    {
        // Get the list of entities inside this schematic
        val entities = schematic.getEntities()
        // Iterate over each entity
        for (i in 0 until entities.tagCount())
        {
            // Grab the compound that represents this entity
            val entityCompound = entities.getCompoundTagAt(i)
            // Instantiate the entity object from the compound
            val entity = EntityList.createEntityFromNBT(entityCompound, world)
            // If the entity is valid, continue...
            if (entity != null)
            {
                // Update the UUID to be random so that it does not conflict with other entities from the same schematic
                entity.setUniqueId(UUID.randomUUID())
                // Get the X, Y, and Z coordinates of this entity if instantiated inside the world
                val newX = entity.posX + blockPos.x
                val newY = entity.posY + blockPos.y
                val newZ = entity.posZ + blockPos.z
                // If the chunk pos was not given or we are in the correct chunk spawn the entity in
                if (chunkPos == null || isInsideChunk(newX, newZ, chunkPos))
                {
                    entity.setPosition(newX, newY, newZ)
                    world.spawnEntity(entity)
                }
            }
        }
    }

    /**
     * Returns true if the x and z position are inside the given chunk, or false otherwise
     *
     * @param x        The X position of the point to test
     * @param z        The Z position of the point to test
     * @param chunkPos The chunk to test
     * @return True if the (x, z) coordinate is inside the chunk or false otherwise
     */
    private fun isInsideChunk(x: Double, z: Double, chunkPos: ChunkPos): Boolean
    {
        return x >= chunkPos.xStart && x <= chunkPos.xEnd && z >= chunkPos.zStart && z <= chunkPos.zEnd
    }

    /**
     * Returns true if the x and z position are inside the given chunk, or false otherwise
     *
     * @param blockPos The position of the point to test
     * @param chunkPos The chunk to test
     * @return True if the (x, z) coordinate is inside the chunk or false otherwise
     */
    private fun isInsideChunk(blockPos: BlockPos, chunkPos: ChunkPos): Boolean
    {
        return blockPos.x >= chunkPos.xStart && blockPos.x <= chunkPos.xEnd && blockPos.z >= chunkPos.zStart && blockPos.z <= chunkPos.zEnd
    }

    /**
     * Since we are using a version of set block that ignores light we need to do a final pass over the structure to
     * re-light it.
     *
     * @param schematic The schematic that was generated
     * @param world     The world that the schematic was generated in
     * @param blockPos  The position the schematic was generated at
     * @param chunkPos  The chunk position the schematic was generated at
     */
    @Suppress("UNUSED_PARAMETER")
    private fun computeLight(schematic: Schematic, world: World, blockPos: BlockPos, chunkPos: ChunkPos?)
    {
        /*
        This code doesn't seem correct, not sure how to force a re-light, it might not be possible
        // If we generated a single chunk light it
        if (chunkPos != null)
        {
            world.getChunkFromChunkCoords(chunkPos.x, chunkPos.z).generateSkylightMap();
        }
        // If we generated a bunch of chunks light them all
        else
        {
            ChunkPos corner1 = new ChunkPos(blockPos);
            ChunkPos corner2 = new ChunkPos(blockPos.add(schematic.getWidth(), 0, schematic.getLength()));
            for (int chunkX = corner1.x; chunkX <= corner1.x; chunkX++)
            {
                for (int chunkZ = corner2.z; chunkZ <= corner2.z; chunkZ++)
                {
                    world.getChunkFromChunkCoords(chunkX, chunkZ).generateSkylightMap();
                }
            }
        }
         */
    }
}