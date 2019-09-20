package com.davidm1a2.afraidofthedark.common.block.mangrove;

import com.davidm1a2.afraidofthedark.common.block.core.AOTDSlab;
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;

/**
 * Class representing a mangrove double slab
 */
public class BlockMangroveDoubleSlab extends AOTDSlab
{
    /**
     * Constructor sets the name and material
     */
    public BlockMangroveDoubleSlab()
    {
        super("mangrove_double_slab", Material.WOOD);
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
        return (BlockSlab) ModBlocks.MANGROVE_HALF_SLAB;
    }
}