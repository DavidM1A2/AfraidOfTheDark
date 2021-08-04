package com.davidm1a2.afraidofthedark.common.block.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.block.RotatedPillarBlock
import net.minecraft.block.SoundType

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
}