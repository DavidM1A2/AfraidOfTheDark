package com.davidm1a2.afraidofthedark.common.world.tree

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.trees.Tree
import net.minecraft.world.gen.feature.AbstractTreeFeature
import net.minecraft.world.gen.feature.IFeatureConfig
import net.minecraft.world.gen.feature.NoFeatureConfig
import net.minecraft.world.gen.feature.TreeFeature
import java.util.*

/**
 * Gravewood tree generator
 */
class GravewoodTree : Tree() {
    /**
     * Gets the tree feature
     *
     * @param random The RNG to use to pick a random tree feature
     */
    override fun getTreeFeature(random: Random): AbstractTreeFeature<NoFeatureConfig> {
        return TreeFeature({ IFeatureConfig.NO_FEATURE_CONFIG }, true, 5, ModBlocks.GRAVEWOOD.defaultState, ModBlocks.GRAVEWOOD_LEAVES.defaultState, false)
    }
}