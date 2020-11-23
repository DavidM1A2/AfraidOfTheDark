package com.davidm1a2.afraidofthedark.common.tileEntity.core

import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.tileentity.TileEntityType

/**
 * Base class for all tile entities that require ticking every game tick
 *
 * @constructor initializes the tile entity fields
 * @param tileEntityType The tile entity's type
 */
abstract class AOTDTickingTileEntity(tileEntityType: TileEntityType<*>) : AOTDTileEntity(tileEntityType), ITickableTileEntity {
    // The number of ticks the tile entity has existed
    protected var ticksExisted: Long = 0
        private set

    /**
     * Called every tick to update the tile entity's state
     */
    override fun tick() {
        ticksExisted++
    }
}