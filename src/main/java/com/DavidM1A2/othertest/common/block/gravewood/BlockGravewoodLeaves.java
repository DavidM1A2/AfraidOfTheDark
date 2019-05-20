package com.DavidM1A2.afraidofthedark.common.block.gravewood;

import com.DavidM1A2.afraidofthedark.common.block.core.AOTDLeaves;
import com.DavidM1A2.afraidofthedark.common.constants.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

import java.util.Random;

/**
 * Gravewood leaves are grown by gravewood trees
 */
public class BlockGravewoodLeaves extends AOTDLeaves
{
    /**
     * Constructor just sets the name of the block
     */
    public BlockGravewoodLeaves()
    {
        super("gravewood_leaves");
    }

    /**
     * We override the item dropped to be a gravewood sapling
     *
     * @param state   The block state of the broken leaf block
     * @param rand    A random object which is ignored
     * @param fortune The fortune level that the leaf block was mined with
     * @return A gravewood sapling
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(ModBlocks.GRAVEWOOD_SAPLING);
    }
}
