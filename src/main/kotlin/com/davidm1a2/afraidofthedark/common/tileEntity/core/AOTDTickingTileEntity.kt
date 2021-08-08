package com.davidm1a2.afraidofthedark.common.tileEntity.core

import net.minecraft.block.BlockState
import net.minecraft.nbt.CompoundNBT
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

    override fun load(blockState: BlockState, compound: CompoundNBT) {
        super.load(blockState, compound)
        ticksExisted = compound.getLong(NBT_TICKS_EXISTED)
    }

    override fun save(compound: CompoundNBT): CompoundNBT {
        super.save(compound)
        compound.putLong(NBT_TICKS_EXISTED, ticksExisted)
        return compound
    }

    companion object {
        private const val NBT_TICKS_EXISTED = "ticks_existed"
    }
}