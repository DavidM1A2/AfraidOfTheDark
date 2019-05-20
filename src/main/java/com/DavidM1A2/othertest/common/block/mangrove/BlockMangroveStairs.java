package com.DavidM1A2.afraidofthedark.common.block.mangrove;

import com.DavidM1A2.afraidofthedark.common.block.core.AOTDStairs;
import com.DavidM1A2.afraidofthedark.common.constants.ModBlocks;

/**
 * Mangrove stair block
 */
public class BlockMangroveStairs extends AOTDStairs
{
    /**
     * Constructor sets the name and texture to the mangrove plank texture
     */
    public BlockMangroveStairs()
    {
        super("mangrove_stairs", ModBlocks.GRAVEWOOD_PLANKS.getDefaultState());
    }
}
