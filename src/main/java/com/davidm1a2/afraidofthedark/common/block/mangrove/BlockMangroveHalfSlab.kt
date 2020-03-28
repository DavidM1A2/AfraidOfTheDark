package com.davidm1a2.afraidofthedark.common.block.mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlockSlab
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.BlockSlab
import net.minecraft.block.material.Material

/**
 * Class representing a mangrove half slab
 *
 * @constructor sets the name and material
 */
class BlockMangroveHalfSlab : AOTDBlockSlab("mangrove_half_slab", Material.WOOD) {
    /**
     * @return The double slab since this is a half slab
     */
    override fun getOpposite(): BlockSlab {
        return ModBlocks.MANGROVE_DOUBLE_SLAB
    }

    /**
     * @return It's a half slab, so not double
     */
    override fun isDouble(): Boolean {
        return false
    }
}