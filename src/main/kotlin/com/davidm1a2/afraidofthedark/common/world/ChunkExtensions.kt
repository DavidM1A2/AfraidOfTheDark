package com.davidm1a2.afraidofthedark.common.world

import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.ChunkSection
import net.minecraft.world.gen.Heightmap

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
            @Suppress("DEPRECATION")
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