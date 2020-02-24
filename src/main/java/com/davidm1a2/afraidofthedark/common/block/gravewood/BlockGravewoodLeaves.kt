package com.davidm1a2.afraidofthedark.common.block.gravewood

import com.davidm1a2.afraidofthedark.common.block.core.AOTDLeaves
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.state.IBlockState
import net.minecraft.item.Item
import java.util.*

/**
 * Gravewood leaves are grown by gravewood trees
 *
 * @constructor just sets the name of the block
 */
class BlockGravewoodLeaves : AOTDLeaves("gravewood_leaves") {
    /**
     * We override the item dropped to be a gravewood sapling
     *
     * @param state   The block state of the broken leaf block
     * @param rand    A random object which is ignored
     * @param fortune The fortune level that the leaf block was mined with
     * @return A gravewood sapling
     */
    override fun getItemDropped(state: IBlockState, rand: Random, fortune: Int): Item {
        return Item.getItemFromBlock(ModBlocks.GRAVEWOOD_SAPLING)
    }
}