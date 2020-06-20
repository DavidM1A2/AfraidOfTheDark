package com.davidm1a2.afraidofthedark.common.world.gen.tree

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.trees.AbstractTree
import net.minecraft.world.gen.feature.AbstractTreeFeature
import net.minecraft.world.gen.feature.NoFeatureConfig
import net.minecraft.world.gen.feature.TreeFeature
import java.util.*

/**
 * Gravewood tree generator
 */
class GravewoodTree : AbstractTree() {
    /**
     * Gets the tree feature
     *
     * @param random The RNG to use to pick a random tree feature
     */
    override fun getTreeFeature(random: Random): AbstractTreeFeature<NoFeatureConfig> {
        return TreeFeature(true, 5, ModBlocks.GRAVEWOOD.defaultState, ModBlocks.GRAVEWOOD_LEAVES.defaultState, false)
    }
}