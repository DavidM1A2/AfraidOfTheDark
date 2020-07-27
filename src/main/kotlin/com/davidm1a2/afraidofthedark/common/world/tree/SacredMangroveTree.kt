package com.davidm1a2.afraidofthedark.common.world.tree

import com.davidm1a2.afraidofthedark.common.world.tree.feature.SacredMangroveTreeFeature
import net.minecraft.block.trees.AbstractTree
import net.minecraft.world.gen.feature.AbstractTreeFeature
import net.minecraft.world.gen.feature.NoFeatureConfig
import java.util.*

/**
 * Sacred mangrove tree generator
 */
class SacredMangroveTree : AbstractTree() {
    /**
     * Gets the tree feature
     *
     * @param random The RNG to use to pick a random tree feature
     */
    override fun getTreeFeature(random: Random): AbstractTreeFeature<NoFeatureConfig> {
        return SacredMangroveTreeFeature(true)
    }
}