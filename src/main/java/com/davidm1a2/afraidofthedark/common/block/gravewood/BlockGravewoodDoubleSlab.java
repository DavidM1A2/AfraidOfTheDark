package com.davidm1a2.afraidofthedark.common.block.gravewood;

import com.davidm1a2.afraidofthedark.common.block.core.AOTDSlab;
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;

/**
 * Class representing a gravewood double slab
 */
public class BlockGravewoodDoubleSlab extends AOTDSlab
{
    /**
     * Constructor sets the name and material
     */
    public BlockGravewoodDoubleSlab()
    {
        super("gravewood_double_slab", Material.WOOD);
    }

    /**
     * @return It's a double slab
     */
    @Override
    public boolean isDouble()
    {
        return true;
    }

    /**
     * @return The opposite is a half slab since this one is a double slab
     */
    @Override
    public BlockSlab getOpposite()
    {
        return (BlockSlab) ModBlocks.GRAVEWOOD_HALF_SLAB;
    }
}
