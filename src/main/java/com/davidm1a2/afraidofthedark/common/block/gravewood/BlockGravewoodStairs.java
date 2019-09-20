package com.davidm1a2.afraidofthedark.common.block.gravewood;

import com.davidm1a2.afraidofthedark.common.block.core.AOTDStairs;
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks;

/**
 * Gravewood stair block
 */
public class BlockGravewoodStairs extends AOTDStairs
{
    /**
     * Constructor sets the name and texture to the gravewood plank texture
     */
    public BlockGravewoodStairs()
    {
        super("gravewood_stairs", ModBlocks.GRAVEWOOD_PLANKS.getDefaultState());
    }
}
