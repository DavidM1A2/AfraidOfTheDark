package com.davidm1a2.afraidofthedark.common.block.mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDSlab
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.BlockSlab
import net.minecraft.block.material.Material

/**
 * Class representing a mangrove double slab
 *
 * @constructor sets the name and material
 */
class BlockMangroveDoubleSlab : AOTDSlab("mangrove_double_slab", Material.WOOD)
{
    /**
     * @return The half slab since this is a double slab
     */
    override fun getOpposite(): BlockSlab
    {
        return ModBlocks.MANGROVE_HALF_SLAB as BlockSlab
    }

    /**
     * @return It's a double slab
     */
    override fun isDouble(): Boolean
    {
        return true
    }
}