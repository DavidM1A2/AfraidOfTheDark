package com.davidm1a2.afraidofthedark.common.worldGeneration

import com.davidm1a2.afraidofthedark.common.worldGeneration.schematic.Schematic
import net.minecraft.entity.EntityType
import net.minecraft.init.Blocks
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityChest
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World
import java.util.*

/**
 * Relights a chunk in a world, usually takes 10-400ms to execute
 *
 * @param chunkPos The chunk to relight
 */
fun World.relightChunk(chunkPos: ChunkPos) {
    val chunk = this.getChunk(chunkPos.x, chunkPos.z)
    chunk.generateSkylightMap()
    chunk.markDirty()
}

/**
 * Generates a specific chunk of a schematic in a world at a specific block position without loot
 *
 * @param schematic The schematic to generate
 * @param blockPos  The position to generate the schematic at
 * @param chunkPos  Optional argument to specify which chunk specify to generate of this schematic
 * @param lootTable Optional argument to specify what loot to generate inside chests
 */
fun World.generateSchematic(
    schematic: Schematic,
    blockPos: BlockPos,
    chunkPos: ChunkPos? = null,
    lootTable: LootTable? = null
) {
    // Generate server side only
    if (!this.isRemote) {
        this.generateBlocks(schematic, blockPos, chunkPos)
        this.generateTileEntities(schematic, blockPos, chunkPos, lootTable)
        this.generateEntities(schematic, blockPos, chunkPos)
    }
}

/**
 * Generates the blocks of a specific chunk of a schematic in a world at a specific block position
 *
 * @param schematic The schematic to generate
 * @param blockPos  The position to generate the schematic at
 * @param chunkPos  Optional argument to specify which chunk specify to generate of this schematic
 */
private fun World.generateBlocks(schematic: Schematic, blockPos: BlockPos, chunkPos: ChunkPos?) {
    // Store the schematic variables up here so we don't have lots of superfluous method calls. Method calls are slow
    // if they happen over and over again, instead cache our variable values and then do simply math which is much
    // faster!
    val posX = blockPos.x
    val posY = blockPos.y
    val posZ = blockPos.z
    val blocks = schematic.getBlocks()
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
    if (chunkPos != null) {
        startZ = startZ.coerceIn(chunkPos.zStart, chunkPos.zEnd + 1)
        endZ = endZ.coerceIn(chunkPos.zStart, chunkPos.zEnd + 1)
        startX = startX.coerceIn(chunkPos.xStart, chunkPos.xEnd + 1)
        endX = endX.coerceIn(chunkPos.xStart, chunkPos.xEnd + 1)
    }

    val setBlockFlags = 2

    ///
    /// Phase 1 is to generate all solid blocks, this should take most of the time
    ///

    // Iterate over the Y axis first since that's the format that schematics use
    for (y in posY until endY) {
        // Get the y index which we can use to index into the blocks array
        val indexY = (y - posY) * length * width

        // Iterate over the Z axis second
        for (z in startZ until endZ) {
            // Get the z index which we can use to index into the blocks array
            val indexZ = (z - posZ) * width

            // Iterate over the X axis last
            for (x in startX until endX) {
                // Get the x index which we can use to index into the blocks array
                val indexX = x - posX

                // Compute the index into our blocks array
                val index = indexY + indexZ + indexX

                // Grab the reference to the next block to place
                val nextToPlace = blocks[index]

                // If the block in the schematic is air then ignore it
                if (!nextToPlace.isAir) {
                    val position = BlockPos(x, y, z)
                    // Structure void blocks represent air blocks in my schematic system. This allows for easy underground structure generation.
                    if (nextToPlace.block == Blocks.STRUCTURE_VOID) {
                        // Set the block to air
                        WorldGenFast.setBlockStateFast(this, position, Blocks.AIR.defaultState, setBlockFlags)
                    } else {
                        // Set the block state
                        WorldGenFast.setBlockStateFast(this, position, nextToPlace, setBlockFlags)
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
 * @param blockPos  The position to generate the schematic at
 * @param chunkPos  Optional argument to specify which chunk specify to generate of this schematic
 * @param lootTable Optional argument to specify what loot to generate inside chests
 */
private fun World.generateTileEntities(
    schematic: Schematic,
    blockPos: BlockPos,
    chunkPos: ChunkPos?,
    lootTable: LootTable?
) {
    // Get the list of tile entities inside this schematic
    val tileEntities = schematic.getTileEntities()
    // Iterate over each tile entity
    for (i in 0 until tileEntities.size) {
        // Grab the compound that represents this tile entity
        val tileEntityCompound = tileEntities.getCompound(i)
        // Instantiate the tile entity object from the compound
        val tileEntity = TileEntity.create(tileEntityCompound)
        // If the entity is valid, continue...
        if (tileEntity != null) {
            // Set the world
            tileEntity.world = this
            // Get the X, Y, and Z coordinates of this entity if instantiated inside the world
            val tileEntityPosition = tileEntity.pos.add(blockPos)

            // If the chunk pos was not given or we are in the correct chunk spawn the entity in
            if (chunkPos == null || isInsideChunk(tileEntityPosition, chunkPos)) {
                // Remove the existing tile entity at the location
                this.removeTileEntity(tileEntityPosition)
                // Set the new position of our tile entity
                tileEntity.pos = tileEntityPosition
                // Add the tile entity to the world
                this.setTileEntity(tileEntityPosition, tileEntity)
                // If the tile entity is a chest and we have a loot table then generate the chest
                if (tileEntity is TileEntityChest && lootTable != null) {
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
 * @param blockPos  The position to generate the schematic entities at
 * @param chunkPos  Optional argument to specify which chunk specify to generate of this schematic
 */
private fun World.generateEntities(schematic: Schematic, blockPos: BlockPos, chunkPos: ChunkPos?) {
    // Get the list of entities inside this schematic
    val entities = schematic.getEntities()

    // Iterate over each entity
    for (i in 0 until entities.size) {
        // Grab the compound that represents this entity
        val entityCompound = entities.getCompound(i)

        // Instantiate the entity object from the compound
        val entity = EntityType.create(entityCompound, this)

        // If the entity is valid, continue...
        if (entity != null) {
            // Update the UUID to be random so that it does not conflict with other entities from the same schematic
            entity.setUniqueId(UUID.randomUUID())

            // Get the X, Y, and Z coordinates of this entity if instantiated inside the world
            val newX = entity.posX + blockPos.x
            val newY = entity.posY + blockPos.y
            val newZ = entity.posZ + blockPos.z

            // If the chunk pos was not given or we are in the correct chunk spawn the entity in
            if (chunkPos == null || isInsideChunk(newX, newZ, chunkPos)) {
                entity.setPosition(newX, newY, newZ)
                this.spawnEntity(entity)
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
private fun isInsideChunk(x: Double, z: Double, chunkPos: ChunkPos): Boolean {
    return x >= chunkPos.xStart && x <= chunkPos.xEnd && z >= chunkPos.zStart && z <= chunkPos.zEnd
}

/**
 * Returns true if the x and z position are inside the given chunk, or false otherwise
 *
 * @param blockPos The position of the point to test
 * @param chunkPos The chunk to test
 * @return True if the (x, z) coordinate is inside the chunk or false otherwise
 */
private fun isInsideChunk(blockPos: BlockPos, chunkPos: ChunkPos): Boolean {
    return blockPos.x >= chunkPos.xStart && blockPos.x <= chunkPos.xEnd && blockPos.z >= chunkPos.zStart && blockPos.z <= chunkPos.zEnd
}