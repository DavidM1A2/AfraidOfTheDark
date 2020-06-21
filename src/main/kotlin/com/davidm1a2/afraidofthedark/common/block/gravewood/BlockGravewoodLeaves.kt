package com.davidm1a2.afraidofthedark.common.block.gravewood

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlockLeaves
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.util.IItemProvider
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

/**
 * Gravewood leaves are grown by gravewood trees
 *
 * @constructor just sets the name of the block
 */
class BlockGravewoodLeaves : AOTDBlockLeaves("gravewood_leaves", Properties.create(Material.LEAVES)) {
    override fun getItemDropped(state: IBlockState, world: World, blockPos: BlockPos, fortune: Int): IItemProvider {
        return ModBlocks.GRAVEWOOD_SAPLING
    }
}