package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import java.util.*
import kotlin.math.max

/**
 * Class representing the glow stalk mushroom stem
 *
 * @constructor sets the item name and makes it glow
 */
class BlockGlowStalk : AOTDBlock("glow_stalk", Material.GROUND)
{
    init
    {
        setLightLevel(1.0f)
        setHardness(1.0f)
        setResistance(4.0f)
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     *
     * @return The same value as vanilla hugh mushroom blocks
     */
    override fun quantityDropped(random: Random): Int
    {
        return max(0, random.nextInt(10) - 7)
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *
     * @return The item dropped which will be either brown or red mushroom randomly picked
     */
    override fun getItemDropped(state: IBlockState, rand: Random, fortune: Int): Item
    {
        return Item.getItemFromBlock(if (rand.nextBoolean()) Blocks.BROWN_MUSHROOM else Blocks.RED_MUSHROOM)
    }
}