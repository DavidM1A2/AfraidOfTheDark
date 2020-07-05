package com.davidm1a2.afraidofthedark.common.world

import com.davidm1a2.afraidofthedark.common.world.schematic.Schematic
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityType
import net.minecraft.init.Blocks
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityChest
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.World
import net.minecraft.world.WorldType
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.ChunkSection
import net.minecraft.world.gen.Heightmap
import net.minecraftforge.common.util.BlockSnapshot
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
                        this.setBlockStateFast(
                            position,
                            Blocks.AIR.defaultState,
                            setBlockFlags
                        )
                    } else {
                        // Set the block state
                        this.setBlockStateFast(
                            position,
                            nextToPlace,
                            setBlockFlags
                        )
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

/**
 * Faster version of world.setBlockState() that does not perform any lighting computation
 *
 * @param pos      The position to set the block in
 * @param newState The new state of the block
 * @param flags    Any additional flags to pass down, see original setBlockState() for flag documentation
 * @return True if the block was set, false otherwise
 */
fun World.setBlockStateFast(pos: BlockPos, newState: IBlockState, flags: Int): Boolean {
    return if (World.isOutsideBuildHeight(pos)) {
        false
    } else if (!isRemote && worldInfo.terrainType === WorldType.DEBUG_ALL_BLOCK_STATES) {
        false
    } else {
        val chunk = this.getChunk(pos)
        // val block = newState.block
        val immutablePos = pos.toImmutable() // Forge - prevent mutable BlockPos leaks
        var blockSnapshot: BlockSnapshot? = null
        if (captureBlockSnapshots && !isRemote) {
            blockSnapshot = BlockSnapshot.getBlockSnapshot(this, immutablePos, flags)
            capturedBlockSnapshots.add(blockSnapshot)
        }

        // val old = getBlockState(immutablePos)
        // val oldLight = old.getLightValue(this, immutablePos)
        // val oldOpacity = old.getOpacity(this, immutablePos)

        val iblockstate = chunk.setBlockStateFast(immutablePos, newState, flags and 64 != 0)
        if (iblockstate == null) {
            if (blockSnapshot != null) capturedBlockSnapshots.remove(blockSnapshot)
            false
        } else {
            // val iblockstate1 = getBlockState(immutablePos)
            // if (iblockstate1.getOpacity(this, immutablePos) != oldOpacity || iblockstate1.getLightValue(this, immutablePos) != oldLight) {
            //     profiler.startSection("checkLight")
            //     checkLight(immutablePos)
            //     profiler.endSection()
            // }

            if (blockSnapshot == null) { // Don't notify clients or update physics while capturing blockstates
                markAndNotifyBlock(immutablePos, chunk, iblockstate, newState, flags)
            }
            true
        }
    }
}

/**
 * Faster version of chunk.setBlockState() that does not perform any lighting computation or updates. It's copied
 * exactly with some code commented out
 *
 * @param pos The blockpos to update
 * @param state The state to set
 * @return The set block state or null if the state wasn't set
 */
private fun Chunk.setBlockStateFast(pos: BlockPos, state: IBlockState, isMoving: Boolean): IBlockState? {
    val i = pos.x and 15
    val j = pos.y
    val k = pos.z and 15
    // val l = getHeightmap(Heightmap.Type.LIGHT_BLOCKING).getHeight(i, k)
    val iblockstate = this.getBlockState(pos)
    return if (iblockstate === state) {
        null
    } else {
        val block = state.block
        val block1 = iblockstate.block
        var chunksection = sections[j shr 4]
        // val j1 = iblockstate.getOpacity(world, pos) // Relocate old light value lookup here, so that it is called before TE is removed.
        // var flag = false
        if (chunksection === Chunk.EMPTY_SECTION) {
            if (state.isAir) {
                return null
            }
            chunksection = ChunkSection(j shr 4 shl 4, world.dimension.hasSkyLight())
            sections[j shr 4] = chunksection
            // flag = j >= l
        }
        chunksection[i, j and 15, k] = state
        getHeightmap(Heightmap.Type.MOTION_BLOCKING).update(i, j, k, state)
        getHeightmap(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES).update(i, j, k, state)
        getHeightmap(Heightmap.Type.OCEAN_FLOOR).update(i, j, k, state)
        getHeightmap(Heightmap.Type.WORLD_SURFACE).update(i, j, k, state)
        if (!world.isRemote) {
            iblockstate.onReplaced(world, pos, state, isMoving)
        } else if (block1 !== block && iblockstate.hasTileEntity()) {
            world.removeTileEntity(pos)
        }
        if (chunksection[i, j and 15, k].block !== block) {
            null
        } else {
            // if (flag) {
            //     generateSkylightMap()
            // } else {
            //     val i1 = state.getOpacity(world, pos)
            //     relightBlock(i, j, k, state)
            //     if (i1 != j1 && (i1 < j1 || getLightFor(EnumLightType.SKY, pos) > 0 || getLightFor(EnumLightType.BLOCK, pos) > 0)) {
            //         propagateSkylightOcclusion(i, k)
            //     }
            // }
            if (iblockstate.hasTileEntity()) {
                val tileentity = this.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK)
                tileentity?.updateContainingBlockInfo()
            }
            if (!world.isRemote) {
                state.onBlockAdded(world, pos, iblockstate)
            }
            if (state.hasTileEntity()) {
                var tileentity1 = this.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK)
                if (tileentity1 == null) {
                    tileentity1 = state.createTileEntity(world)
                    world.setTileEntity(pos, tileentity1)
                } else {
                    tileentity1.updateContainingBlockInfo()
                }
            }
            markDirty()
            iblockstate
        }
    }
}