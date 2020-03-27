package com.davidm1a2.afraidofthedark.common.block.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.block.BlockStairs
import net.minecraft.block.state.IBlockState

/**
 * Base class for AOTD stair blocks
 *
 * @constructor takes in a name and model state that this block uses to get texture from
 * @param baseName   The name of the block
 * @param modelState The state that this block copies its texture from
 */
abstract class AOTDBlockStairs(baseName: String, modelState: IBlockState) : BlockStairs(modelState)
{
    init
    {
        unlocalizedName = "${Constants.MOD_ID}:$baseName"
        this.setRegistryName("${Constants.MOD_ID}:$baseName")
        setCreativeTab(Constants.AOTD_CREATIVE_TAB)
    }
}