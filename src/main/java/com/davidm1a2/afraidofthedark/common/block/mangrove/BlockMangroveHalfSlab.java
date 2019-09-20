package com.davidm1a2.afraidofthedark.common.block.mangrove;

import com.davidm1a2.afraidofthedark.common.block.core.AOTDSlab;
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;

/**
 * Class representing a mangrove half slab
 */
public class BlockMangroveHalfSlab extends AOTDSlab
{
    /**
     * Constructor sets the name and material
     */
    public BlockMangroveHalfSlab()
    {
        super("mangrove_half_slab", Material.WOOD);
    }

    /**
     * @return It's a half slab, so not double
     */
    @Override
    public boolean isDouble()
    {
        return false;
    }

    /**
     * @return The opposite is a double slab since this one is a half slab
     */
    @Override
    public BlockSlab getOpposite()
    {
        return (BlockSlab) ModBlocks.MANGROVE_DOUBLE_SLAB;
    }
}
