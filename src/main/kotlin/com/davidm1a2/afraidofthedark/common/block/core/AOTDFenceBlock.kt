package com.davidm1a2.afraidofthedark.common.block.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.block.FenceBlock
import net.minecraft.block.SoundType


/**
 * Base class for any fence blocks used by Afraid of the dark
 *
 * @constructor requires a material parameter that defines some block properties
 * @param baseName The name of the block to be used by the game registry
 * @param properties The properties of this block
 */
abstract class AOTDFenceBlock(baseName: String, properties: Properties) : FenceBlock(properties.apply {
    hardnessAndResistance(2.0f, 3.0f)
    sound(SoundType.WOOD)
}), AOTDShowBlockCreative {
    init {
        this.setRegistryName(Constants.MOD_ID, baseName)
    }
}