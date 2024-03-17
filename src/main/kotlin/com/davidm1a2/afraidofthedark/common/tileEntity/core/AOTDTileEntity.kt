package com.davidm1a2.afraidofthedark.common.tileEntity.core

import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraftforge.common.util.Constants

/**
 * Base class for all AOTD tile entities
 *
 * @constructor initializes the tile entity fields
 * @param tileEntityType The tile entity's type
 */
abstract class AOTDTileEntity(tileEntityType: TileEntityType<*>) : BlockEntity(tileEntityType) {
    protected fun setChangedAndTellClients() {
        setChanged()
        if (level?.isClientSide == false) {
            level?.sendBlockUpdated(blockPos, blockState, blockState, Constants.BlockFlags.BLOCK_UPDATE)
        }
    }
}