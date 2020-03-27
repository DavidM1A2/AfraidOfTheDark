package com.davidm1a2.afraidofthedark.common.block.sacred_mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlockSlab
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.BlockSlab
import net.minecraft.block.material.Material

/**
 * Class representing a sacred mangrove double slab
 *
 * @constructor sets the name and material
 */
class BlockSacredMangroveDoubleSlab : AOTDBlockSlab("sacred_mangrove_double_slab", Material.WOOD)
{
    /**
     * @return The half slab since this is a double slab
     */
    override fun getOpposite(): BlockSlab
    {
        return ModBlocks.SACRED_MANGROVE_HALF_SLAB
    }

    /**
     * @return It's a double slab
     */
    override fun isDouble(): Boolean {
        return true
    }
}