package com.davidm1a2.afraidofthedark.common.block.mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDLeaves
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.state.IBlockState
import net.minecraft.item.Item
import java.util.*

/**
 * Mangrove leaves are grown by mangrove trees
 *
 * @constructor just sets the name of the block
 */
class BlockMangroveLeaves : AOTDLeaves("mangrove_leaves")
{
    /**
     * We override the item dropped to be a mangrove sapling
     *
     * @param state   The block state of the broken leaf block
     * @param rand    A random object which is ignored
     * @param fortune The fortune level that the leaf block was mined with
     * @return A mangrove sapling
     */
    override fun getItemDropped(state: IBlockState, rand: Random, fortune: Int): Item
    {
        return Item.getItemFromBlock(ModBlocks.MANGROVE_SAPLING)
    }
}