package com.davidm1a2.afraidofthedark.common.block.sacred_mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlockLeaves
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.util.IItemProvider
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*

/**
 * Sacred mangrove leaves are grown by sacred mangrove trees
 *
 * @constructor just sets the name of the block
 */
class BlockSacredMangroveLeaves : AOTDBlockLeaves("sacred_mangrove_leaves", Properties.create(Material.LEAVES)) {
    override fun getItemDropped(state: IBlockState, world: World, blockPos: BlockPos, fortune: Int): IItemProvider {
        return ModBlocks.SACRED_MANGROVE_SAPLING
    }

    override fun getItemsToDropCount(state: IBlockState, fortune: Int, world: World, blockPos: BlockPos, random: Random): Int {
        return 0
    }
}