package com.davidm1a2.afraidofthedark.common.world

import com.davidm1a2.afraidofthedark.common.world.schematic.Schematic
import com.davidm1a2.afraidofthedark.common.world.structure.base.LootTable
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityChest
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.ChunkSection
import net.minecraft.world.chunk.IChunk
import net.minecraft.world.gen.Heightmap

/**
 * Generates a specific chunk of a schematic in a world at a specific block position without loot
 *
 * @param schematic The schematic to generate
 * @param blockPos  The position to generate the schematic at
 * @param chunkPos  Optional argument to specify which chunk specify to generate of this schematic
 * @param lootTable Optional argument to specify what loot to generate inside chests
 */
fun IChunk.generateSchematic(
    schematic: Schematic,
    blockPos: BlockPos,
    chunkPos: ChunkPos? = null,
    lootTable: LootTable? = null
) {
    // Generate server side only
    if (this.worldForge?.isRemote == false) {
        this.generateBlocks(schematic, blockPos, chunkPos)
        this.generateTileEntities(schematic, blockPos, chunkPos, lootTable)
    }
}

/**
 * Generates the blocks of a specific chunk of a schematic in a world at a specific block position
 *
 * @param schematic The schematic to generate
 * @param blockPos  The position to generate the schematic at
 * @param chunkPos  Optional argument to specify which chunk specify to generate of this schematic
 */
private fun IChunk.generateBlocks(schematic: Schematic, blockPos: BlockPos, chunkPos: ChunkPos?) {
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
                        this.setBlockState(
                            position,
                            Blocks.AIR.defaultState,
                            false
                        )
                    } else {
                        // Set the block state
                        this.setBlockState(
                            position,
                            nextToPlace,
                            false
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
private fun IChunk.generateTileEntities(
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
            // Get the X, Y, and Z coordinates of this entity if instantiated inside the world
            val tileEntityPosition = tileEntity.pos.add(blockPos)

            // If the chunk pos was not given or we are in the correct chunk spawn the entity in
            if (chunkPos == null || isInsideChunk(tileEntityPosition, chunkPos)) {
                // Remove the existing tile entity at the location
                this.removeTileEntity(tileEntityPosition)
                // Set the new position of our tile entity
                tileEntity.pos = tileEntityPosition
                // Add the tile entity to the world
                this.addTileEntity(tileEntityPosition, tileEntity)
                // If the tile entity is a chest and we have a loot table then generate the chest
                if (tileEntity is TileEntityChest && lootTable != null) {
                    lootTable.generate(tileEntity, tileEntityCompound, tileEntity.world!!.random)
                }
            }
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
fun Chunk.setBlockStateFast(pos: BlockPos, state: IBlockState, isMoving: Boolean): IBlockState? {
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