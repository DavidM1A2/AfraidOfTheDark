package com.DavidM1A2.afraidofthedark.common.block.core;

import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;

/**
 * Base class for AOTD stair blocks
 */
public abstract class AOTDStairs extends BlockStairs
{
    /**
     * Constructor takes in a name and model state that this block uses to get texture from
     *
     * @param baseName   The name of the block
     * @param modelState The state that this block copies its texture from
     */
    public AOTDStairs(String baseName, IBlockState modelState)
    {
        super(modelState);
        this.setUnlocalizedName(Constants.MOD_ID + ":" + baseName);
        this.setRegistryName(Constants.MOD_ID + ":" + baseName);
        this.setCreativeTab(Constants.AOTD_CREATIVE_TAB);
    }
}
