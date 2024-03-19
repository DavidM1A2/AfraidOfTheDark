package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDZoneTileEntity
import net.minecraft.core.BlockPos
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.world.level.block.state.BlockState

/**
 * Tile entity for the cryot block
 *
 * @constructor sets the block type of the tile entity
 */
class CryptTileEntity(blockPos: BlockPos, blockState: BlockState) : AOTDZoneTileEntity(ModTileEntities.CRYPT) {
    override val zone: AxisAlignedBB by lazy {
        AxisAlignedBB(
            blockPos.x.toDouble(),
            blockPos.y.toDouble(),
            blockPos.z.toDouble(),
            (blockPos.x + 1).toDouble(),
            (blockPos.y + 1).toDouble(),
            (blockPos.z + 1).toDouble()
        ).inflate(13.0, 0.0, 13.0)
            .expandTowards(0.0, 5.0, 0.0)
    }
}