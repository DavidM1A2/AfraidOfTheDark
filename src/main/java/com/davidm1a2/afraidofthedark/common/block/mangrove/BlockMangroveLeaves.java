package com.davidm1a2.afraidofthedark.common.block.mangrove;

import com.davidm1a2.afraidofthedark.common.block.core.AOTDLeaves;
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

import java.util.Random;

/**
 * Mangrove leaves are grown by mangrove trees
 */
public class BlockMangroveLeaves extends AOTDLeaves
{
    /**
     * Constructor just sets the name of the block
     */
    public BlockMangroveLeaves()
    {
        super("mangrove_leaves");
    }

    /**
     * We override the item dropped to be a mangrove sapling
     *
     * @param state   The block state of the broken leaf block
     * @param rand    A random object which is ignored
     * @param fortune The fortune level that the leaf block was mined with
     * @return A mangrove sapling
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(ModBlocks.MANGROVE_SAPLING);
    }
}
