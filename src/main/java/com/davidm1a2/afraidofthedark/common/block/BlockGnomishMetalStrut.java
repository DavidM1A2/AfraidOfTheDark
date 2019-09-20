package com.davidm1a2.afraidofthedark.common.block;

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock;
import net.minecraft.block.material.Material;

/**
 * Class representing the gnomish metal strut
 */
public class BlockGnomishMetalStrut extends AOTDBlock
{
    /**
     * Constructor sets the name and block properties
     */
    public BlockGnomishMetalStrut()
    {
        super("gnomish_metal_strut", Material.ROCK);
        this.setHardness(2.0F);
        this.setResistance(10.0F);
        this.setHarvestLevel("pickaxe", 2);
    }
}