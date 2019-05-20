package com.DavidM1A2.afraidofthedark.common.block;

import com.DavidM1A2.afraidofthedark.common.block.core.AOTDBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

import java.util.Random;

/**
 * Class representing the glow stalk mushroom stem
 */
public class BlockGlowStalk extends AOTDBlock
{
    /**
     * Constructor sets the item name and makes it glow
     */
    public BlockGlowStalk()
    {
        super("glow_stalk", Material.GROUND);
        this.setLightLevel(1.0f);
        this.setHardness(1.0f);
        this.setResistance(4.0f);
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     *
     * @return The same value as vanilla hugh mushroom blocks
     */
    @Override
    public int quantityDropped(Random random)
    {
        return Math.max(0, random.nextInt(10) - 7);
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *
     * @return The item dropped which will be either brown or red mushroom randomly picked
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return rand.nextBoolean() ? Item.getItemFromBlock(Blocks.BROWN_MUSHROOM) : Item.getItemFromBlock(Blocks.RED_MUSHROOM);
    }
}
