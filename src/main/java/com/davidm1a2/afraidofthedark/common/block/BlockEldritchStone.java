package com.davidm1a2.afraidofthedark.common.block;

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock;
import net.minecraft.block.material.Material;

/**
 * Class that represents an eldritch stone block
 */
public class BlockEldritchStone extends AOTDBlock
{
    /**
     * Constructor initializes block properties
     */
    public BlockEldritchStone()
    {
        super("eldritch_stone", Material.ROCK);
        this.setHardness(5.0F);
        this.setHarvestLevel("pickaxe", 1);
    }
}
