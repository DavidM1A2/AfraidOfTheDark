package com.davidm1a2.afraidofthedark.common.tileEntity.core

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

/**
 * Base class for all AOTD tile entities
 *
 * @constructor initializes the tile entity fields
 */
abstract class AOTDTileEntity(type: BlockEntityType<*>, blockPos: BlockPos, blockState: BlockState) : BlockEntity(type, blockPos, blockState) {
    protected fun setChangedAndTellClients() {
        setChanged()
        if (level?.isClientSide == false) {
            level?.sendBlockUpdated(blockPos, blockState, blockState, Block.UPDATE_CLIENTS)
        }
    }
}