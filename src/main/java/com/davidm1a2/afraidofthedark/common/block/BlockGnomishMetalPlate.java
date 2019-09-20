package com.davidm1a2.afraidofthedark.common.block;

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock;
import net.minecraft.block.material.Material;

/**
 * Class representing the gnomish metal plate
 */
public class BlockGnomishMetalPlate extends AOTDBlock
{
    /**
     * Constructor sets the name and block properties
     */
    public BlockGnomishMetalPlate()
    {
        super("gnomish_metal_plate", Material.ROCK);
        this.setHardness(2.0F);
        this.setResistance(10.0F);
        this.setHarvestLevel("pickaxe", 2);
    }
}
