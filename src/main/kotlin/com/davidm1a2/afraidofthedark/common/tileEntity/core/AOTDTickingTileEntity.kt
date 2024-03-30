package com.davidm1a2.afraidofthedark.common.tileEntity.core

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

/**
 * Base class for all tile entities that require ticking every game tick
 *
 * @constructor initializes the tile entity fields
 */
abstract class AOTDTickingTileEntity(type: BlockEntityType<*>, blockPos: BlockPos, blockState: BlockState) : AOTDTileEntity(type, blockPos, blockState) {
    // The number of ticks the tile entity has existed
    var ticksExisted: Long = 0
        private set

    override fun load(compound: CompoundTag) {
        super.load(compound)
        ticksExisted = compound.getLong(NBT_TICKS_EXISTED)
    }

    override fun save(compound: CompoundTag): CompoundTag {
        super.save(compound)
        compound.putLong(NBT_TICKS_EXISTED, ticksExisted)
        return compound
    }

    companion object {
        private const val NBT_TICKS_EXISTED = "ticks_existed"

        fun baseTick(level: Level, blockPos: BlockPos, blockState: BlockState, blockEntity: AOTDTickingTileEntity) {
            blockEntity.ticksExisted++
        }
    }
}