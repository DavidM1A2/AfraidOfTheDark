package com.davidm1a2.afraidofthedark.common.block.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.world.level.block.BushBlock
import net.minecraft.world.level.block.SoundType

/**
 * Base class for any bush blocks used by Afraid of the dark
 *
 * @constructor requires a material parameter that defines some block properties
 * @param baseName The name of the block to be used by the game registry
 * @param properties The properties of this block
 */
abstract class AOTDBushBlock(baseName: String, properties: Properties) : BushBlock(properties.apply {
    instabreak()
    sound(SoundType.CROP)
}), IShowBlockCreative {
    init {
        this.setRegistryName(Constants.MOD_ID, baseName)
    }
}