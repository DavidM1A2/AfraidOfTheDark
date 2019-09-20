package com.davidm1a2.afraidofthedark.common.block;

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock;
import net.minecraft.block.material.Material;

/**
 * Class representing the igneous block created from 9 igneous gems
 */
public class BlockIgneous extends AOTDBlock
{
    /**
     * Constructor sets the block's properties
     */
    public BlockIgneous()
    {
        super("igneous_block", Material.ROCK);
        this.blockHardness = 4.0F;
        this.blockResistance = 1.0F;
        this.setLightLevel(1.0f);
        this.setHarvestLevel("pickaxe", 2);
    }
}
