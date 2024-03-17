package com.davidm1a2.afraidofthedark.common.block.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState

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
}), IShowBlockCreative {
    init {
        this.setRegistryName(Constants.MOD_ID, baseName)
    }

    override fun getFireSpreadSpeed(state: BlockState, world: BlockGetter, pos: BlockPos, face: Direction): Int {
        return 5
    }

    override fun getFlammability(state: BlockState, world: BlockGetter, pos: BlockPos, face: Direction): Int {
        return 5
    }
}