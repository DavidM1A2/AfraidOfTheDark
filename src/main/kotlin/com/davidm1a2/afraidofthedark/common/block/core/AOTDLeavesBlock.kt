package com.davidm1a2.afraidofthedark.common.block.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.LeavesBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState

/**
 * A base class for any leaves added by the AOTD mod
 *
 * @constructor just requires the name of the block to register as the registry name
 * @param baseName The name to be used by the registry and unlocalized names
 * @param properties The properties of this block
 */
abstract class AOTDLeavesBlock(baseName: String, properties: Properties) : LeavesBlock(properties.apply {
    strength(0.2f)
    randomTicks()
    sound(SoundType.GRASS)
}), IShowBlockCreative {
    init {
        this.setRegistryName(Constants.MOD_ID, baseName)
    }

    override fun getFireSpreadSpeed(state: BlockState, world: BlockGetter, pos: BlockPos, face: Direction): Int {
        return 30
    }

    override fun getFlammability(state: BlockState, world: BlockGetter, pos: BlockPos, face: Direction): Int {
        return 60
    }
}