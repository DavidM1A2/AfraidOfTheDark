package com.davidm1a2.afraidofthedark.common.block.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.block.BlockState
import net.minecraft.block.StairsBlock

/**
 * Base class for AOTD stair blocks
 *
 * @constructor takes in a name and model state that this block uses to get texture from
 * @param baseName   The name of the block
 * @param modelState The state that this block copies its texture from
 * @param properties The block properties
 */
abstract class AOTDStairsBlock(baseName: String, modelState: () -> BlockState, properties: Properties) : StairsBlock(modelState, properties),
    AOTDShowBlockCreative {
    init {
        this.setRegistryName(Constants.MOD_ID, baseName)
    }
}