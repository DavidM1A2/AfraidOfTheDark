package com.davidm1a2.afraidofthedark.common.block.sacred_mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDLeaves
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.state.IBlockState
import net.minecraft.item.Item
import java.util.*

/**
 * Sacred mangrove leaves are grown by sacred mangrove trees
 *
 * @constructor just sets the name of the block
 */
class BlockSacredMangroveLeaves : AOTDLeaves("sacred_mangrove_leaves") {
    /**
     * We override the item dropped to be a sacred mangrove sapling
     *
     * @param state   The block state of the broken leaf block
     * @param rand    A random object which is ignored
     * @param fortune The fortune level that the leaf block was mined with
     * @return A sacred mangrove sapling
     */
    override fun getItemDropped(state: IBlockState, rand: Random, fortune: Int): Item {
        return Item.getItemFromBlock(ModBlocks.SACRED_MANGROVE_SAPLING)
    }

    /**
     * Returns the quantity of items to drop on block destruction
     *
     * @param random The random object to use to pick a quantity
     * @return 0 since sacred mangrove leaves don't drop saplings.
     */
    override fun quantityDropped(random: Random): Int {
        return 0
    }
}