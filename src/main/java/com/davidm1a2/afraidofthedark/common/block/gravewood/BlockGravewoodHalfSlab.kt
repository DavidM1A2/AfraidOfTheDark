package com.davidm1a2.afraidofthedark.common.block.gravewood

import com.davidm1a2.afraidofthedark.common.block.core.AOTDSlab
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.BlockSlab
import net.minecraft.block.material.Material

/**
 * Class representing a gravewood half slab
 *
 * @constructor sets the name and material
 */
class BlockGravewoodHalfSlab : AOTDSlab("gravewood_half_slab", Material.WOOD)
{
    /**
     * @return The double slab since this is a half slab
     */
    override fun getOpposite(): BlockSlab
    {
        return ModBlocks.GRAVEWOOD_DOUBLE_SLAB as BlockSlab
    }

    /**
     * @return It's a half slab, so not double
     */
    override fun isDouble(): Boolean
    {
        return false
    }
}