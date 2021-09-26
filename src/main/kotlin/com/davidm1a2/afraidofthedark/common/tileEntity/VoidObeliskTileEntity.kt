package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDZoneTileEntity
import net.minecraft.util.math.AxisAlignedBB

/**
 * Tile entity for the dark forest block that makes players drowsy
 *
 * @constructor sets the block type of the tile entity
 */
class VoidObeliskTileEntity : AOTDZoneTileEntity(ModTileEntities.VOID_OBELISK) {
    override val zone: AxisAlignedBB by lazy {
        AxisAlignedBB(
            blockPos.x.toDouble(),
            blockPos.y.toDouble(),
            blockPos.z.toDouble(),
            (blockPos.x + 1).toDouble(),
            (blockPos.y + 1).toDouble(),
            (blockPos.z + 1).toDouble()
        ).inflate(CHECK_RANGE.toDouble())
    }

    companion object {
        private const val CHECK_RANGE = 15
    }
}