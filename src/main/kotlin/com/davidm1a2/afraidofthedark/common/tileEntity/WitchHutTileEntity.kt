package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDZoneTileEntity
import net.minecraft.core.BlockPos
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.world.level.block.state.BlockState

/**
 * Tile entity for the dark forest block that makes players drowsy
 *
 * @constructor sets the block type of the tile entity
 */
class WitchHutTileEntity(blockPos: BlockPos, blockState: BlockState) : AOTDZoneTileEntity(ModTileEntities.WITCH_HUT) {
    override val zone: AxisAlignedBB by lazy {
        AxisAlignedBB(
            blockPos.x.toDouble(),
            blockPos.y.toDouble(),
            blockPos.z.toDouble(),
            (blockPos.x + 1).toDouble(),
            (blockPos.y + 1).toDouble(),
            (blockPos.z + 1).toDouble()
        ).inflate(CHECK_RANGE, 0.0, CHECK_RANGE).expandTowards(0.0, CHECK_RANGE, 0.0)
    }

    companion object {
        private const val CHECK_RANGE = 5.0
    }
}