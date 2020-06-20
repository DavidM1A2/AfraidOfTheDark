package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.util.IItemProvider
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*
import kotlin.math.max

/**
 * Class representing the glow stalk mushroom stem
 *
 * @constructor sets the item name and makes it glow
 */
class BlockGlowStalk : AOTDBlock(
    "glow_stalk",
    Properties.create(Material.GROUND)
        .lightValue(1)
        .hardnessAndResistance(1.0f, 4.0f)
) {
    override fun getItemDropped(state: IBlockState, world: World, blockPos: BlockPos, fortune: Int): IItemProvider {
        return if (kotlin.random.Random.nextBoolean()) Blocks.BROWN_MUSHROOM else Blocks.RED_MUSHROOM
    }

    override fun getItemsToDropCount(state: IBlockState, fortune: Int, world: World, blockPos: BlockPos, rand: Random): Int {
        return max(0, rand.nextInt(10) - 7)
    }
}