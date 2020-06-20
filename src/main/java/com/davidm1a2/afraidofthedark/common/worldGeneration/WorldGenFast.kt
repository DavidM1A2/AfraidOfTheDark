package com.davidm1a2.afraidofthedark.common.worldGeneration

import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldType
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.ChunkSection
import net.minecraft.world.gen.Heightmap
import net.minecraftforge.common.util.BlockSnapshot

/**
 * Utility class to perform special world operations more quickly than default MC like setblock
 */
object WorldGenFast {
    /**
     * Faster version of world.setBlockState() that does not perform any lighting computation or updates
     *
     * @param world    The world to set blocks in
     * @param pos      The position to set the block in
     * @param newState The new state of the block
     * @param flags    Any additional flags to pass down, see original setBlockState() for flag documentation
     * @return True if the block was set, false otherwise
     */
    fun setBlockStateFast(world: World, pos: BlockPos, newState: IBlockState, flags: Int): Boolean {
        /*
        Code copied and modified:
         */

        @Suppress("NAME_SHADOWING")
        var pos = pos
        return if (World.isOutsideBuildHeight(pos)) {
            false
        } else if (!world.isRemote && world.worldInfo.terrainType == WorldType.DEBUG_ALL_BLOCK_STATES) {
            false
        } else {
            val chunk = world.getChunk(pos)
            pos = pos.toImmutable() // Forge - prevent mutable BlockPos leaks
            var blockSnapshot: BlockSnapshot? = null
            if (world.captureBlockSnapshots && !world.isRemote) {
                blockSnapshot = BlockSnapshot.getBlockSnapshot(world, pos, flags)
                world.capturedBlockSnapshots.add(blockSnapshot)
            }

            /*
            // Removed since we aren't updating light
            IBlockState oldState = world.getBlockState(pos);
            int oldLight = oldState.getLightValue(world, pos);
            int oldOpacity = oldState.getLightOpacity(world, pos);
             */

            // We need to modify how the chunk sets block state too, call our internal chunk set block state
            val iblockstate = setChunkBlockStateFast(chunk, pos, newState, (flags and 64) != 0)
            if (iblockstate == null) {
                if (blockSnapshot != null) {
                    world.capturedBlockSnapshots.remove(blockSnapshot)
                }
                false
            } else {
                /*
                // Removed since we aren't updating light
                if (newState.getLightOpacity(world, pos) != oldOpacity || newState.getLightValue(world, pos) != oldLight)
                {
                    world.profiler.startSection("checkLight");
                    world.checkLight(pos);
                    world.profiler.endSection();
                }
                 */

                if (blockSnapshot == null) // Don't notify clients or update physics while capturing blockstates
                {
                    world.markAndNotifyBlock(pos, chunk, iblockstate, newState, flags)
                }
                true
            }
        }
    }

    /**
     * Faster version of chunk.setBlockState() that does not perform any lighting computation or updates. It's copied
     * exactly with some code commented out
     *
     * @param chunk The chunk to perform updates on
     * @param pos   The blockpos to update
     * @param state The state to set
     * @return The set block state or null if the state wasn't set
     */
    private fun setChunkBlockStateFast(chunk: Chunk, pos: BlockPos, state: IBlockState, isMoving: Boolean): IBlockState? {
        val i = pos.x and 15
        val j = pos.y
        val k = pos.z and 15

        /*
        No need for this check, it's not lighting related but requires more effort and requires reflection so ignore it
        int l = k << 4 | i;

        if (j >= chunk.precipitationHeightMap[l] - 1)
        {
            chunk.precipitationHeightMap[l] = -999;
        }
         */

        /*
        // No need for any height information since we're not updating lighting
        int i1 = this.heightMap[l];
         */

        val iblockstate = chunk.getBlockState(pos)
        return if (iblockstate == state) {
            null
        } else {
            val block = state.block
            val block1 = iblockstate.block

            /*
            Computes the new light value, we don't need this
            int k1 = iblockstate.getLightOpacity(chunk.getWorld(), pos); // Relocate old light value lookup here, so that it is called before TE is removed.
             */

            var chunksection = chunk.sections[j shr 4]

            /*
            This flag is used for height tests, ignore it too
            boolean flag = false;
             */
            if (chunksection == Chunk.EMPTY_SECTION) {
                if (state.isAir) {
                    return null
                }
                chunksection = ChunkSection(j shr 4 shl 4, chunk.world.dimension.hasSkyLight())
                chunk.sections[j shr 4] = chunksection

                /*
                    This flag is used for height tests as mentioned above
                    flag = j >= i1;
                 */
            }

            chunksection[i, j and 15, k] = state

            chunksection.set(i, j and 15, k, state)
            chunk.getHeightmap(Heightmap.Type.MOTION_BLOCKING).update(i, j, k, state)
            chunk.getHeightmap(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES).update(i, j, k, state)
            chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR).update(i, j, k, state)
            chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE).update(i, j, k, state)

            //if (block1 != block)
            run {
                if (!chunk.world.isRemote) {
                    iblockstate.onReplaced(chunk.world, pos, state, isMoving)
                } else if (block1 !== block && iblockstate.hasTileEntity()) {
                    chunk.world.removeTileEntity(pos)
                }
            }

            if (chunksection[i, j and 15, k].block !== block) {
                null
            } else {
                /*
                This does the actual light update, kill it with fire
                if (flag)
                {
                    this.generateSkylightMap();
                }
                else
                {
                    int j1 = state.getLightOpacity(chunk.getWorld(), pos);

                    if (j1 > 0)
                    {
                        if (j >= i1)
                        {
                            chunk.relightBlock(i, j + 1, k);
                        }
                    }
                    else if (j == i1 - 1)
                    {
                        chunk.relightBlock(i, j, k);
                    }

                    if (j1 != k1 && (j1 < k1 || chunk.getLightFor(EnumSkyBlock.SKY, pos) > 0 || chunk.getLightFor(EnumSkyBlock.BLOCK, pos) > 0))
                    {
                        chunk.propagateSkylightOcclusion(i, k);
                    }
                }
                 */

                if (iblockstate.hasTileEntity()) {
                    chunk.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK)?.updateContainingBlockInfo()
                }

                if (!chunk.world.isRemote) {
                    state.onBlockAdded(chunk.world, pos, iblockstate)
                }

                if (state.hasTileEntity()) {
                    var tileentity1: TileEntity? = chunk.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK)
                    if (tileentity1 == null) {
                        tileentity1 = state.createTileEntity(chunk.world)
                        chunk.world.setTileEntity(pos, tileentity1)
                    } else {
                        tileentity1.updateContainingBlockInfo()
                    }
                }

                chunk.markDirty()
                return iblockstate
            }
        }
    }
}