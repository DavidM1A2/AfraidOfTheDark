package com.davidm1a2.afraidofthedark.common.block.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.block.FenceGateBlock
import net.minecraft.block.SoundType

/**
 * Base class for any fence gate blocks used by Afraid of the dark. Unfortunately the minecraft base class only allows
 * specifying wood type fence gates...
 *
 * @constructor requires a material parameter that defines some block properties
 * @param baseName The name of the block to be used by the game registry
 * @param properties The properties of this block
 */
abstract class AOTDFenceGateBlock(baseName: String, properties: Properties) : FenceGateBlock(properties.apply {
    strength(2.0f, 5.0f)
    sound(SoundType.WOOD)
}), IShowBlockCreative {
    init {
        this.setRegistryName(Constants.MOD_ID, baseName)
    }
}