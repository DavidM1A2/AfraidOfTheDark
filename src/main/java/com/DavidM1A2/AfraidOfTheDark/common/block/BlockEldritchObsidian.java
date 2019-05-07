package com.DavidM1A2.afraidofthedark.common.block;

import com.DavidM1A2.afraidofthedark.common.block.core.AOTDBlock;
import net.minecraft.block.material.Material;

/**
 * Class that represents an eldritch obsidian block
 */
public class BlockEldritchObsidian extends AOTDBlock
{
    /**
     * Constructor initializes the block
     */
    public BlockEldritchObsidian()
    {
        super("eldritch_obsidian", Material.ROCK);
        this.setHardness(10.0F);
        this.setResistance(50.0F);
        this.setHarvestLevel("pickaxe", 3);
    }
}
