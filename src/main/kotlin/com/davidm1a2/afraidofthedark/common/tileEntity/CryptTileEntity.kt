package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDZoneTileEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB

/**
 * Tile entity for the cryot block
 *
 * @constructor sets the block type of the tile entity
 */
class CryptTileEntity(blockPos: BlockPos, blockState: BlockState) : AOTDZoneTileEntity(ModTileEntities.CRYPT, blockPos, blockState) {
    override val zone: AABB by lazy {
        AABB(
            blockPos.x.toDouble(),
            blockPos.y.toDouble(),
            blockPos.z.toDouble(),
            (blockPos.x + 1).toDouble(),
            (blockPos.y + 1).toDouble(),
            (blockPos.z + 1).toDouble()
        ).inflate(13.0, 0.0, 13.0)
            .expandTowards(0.0, 5.0, 0.0)
    }

    companion object {
        fun serverTick(level: Level, blockPos: BlockPos, blockState: BlockState, blockEntity: CryptTileEntity) {
            AOTDZoneTileEntity.serverTick(level, blockPos, blockState, blockEntity)
        }
    }
}