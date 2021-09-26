package com.davidm1a2.afraidofthedark.common.block.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.block.BlockState
import net.minecraft.block.RotatedPillarBlock
import net.minecraft.block.SoundType
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockReader

/**
 * Base class for all AOTD log blocks
 *
 * @constructor just sets default state and initializes sounds
 * @param baseName The name of the block to register
 * @param properties The properties of this block
 */
abstract class AOTDLogBlock(baseName: String, properties: Properties) : RotatedPillarBlock(properties.apply {
    strength(2.0f)
    sound(SoundType.WOOD)
}), AOTDShowBlockCreative {
    init {
        this.setRegistryName(Constants.MOD_ID, baseName)
    }

    override fun getFireSpreadSpeed(state: BlockState, world: IBlockReader, pos: BlockPos, face: Direction): Int {
        return 5
    }

    override fun getFlammability(state: BlockState, world: IBlockReader, pos: BlockPos, face: Direction): Int {
        return 5
    }
}