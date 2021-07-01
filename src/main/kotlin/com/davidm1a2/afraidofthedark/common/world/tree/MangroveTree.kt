package com.davidm1a2.afraidofthedark.common.world.tree

import com.davidm1a2.afraidofthedark.common.world.tree.feature.MangroveTreeFeature
import net.minecraft.block.trees.Tree
import net.minecraft.world.gen.feature.AbstractTreeFeature
import net.minecraft.world.gen.feature.NoFeatureConfig
import java.util.*

/**
 * Mangrove tree generator
 */
class MangroveTree : Tree() {
    /**
     * Gets the tree feature
     *
     * @param random The RNG to use to pick a random tree feature
     */
    override fun getTreeFeature(random: Random): AbstractTreeFeature<NoFeatureConfig> {
        return MangroveTreeFeature(true)
    }
}