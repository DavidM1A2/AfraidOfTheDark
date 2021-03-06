package com.davidm1a2.afraidofthedark.common.world.tree

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModFeatures
import net.minecraft.block.LogBlock
import net.minecraft.block.trees.Tree
import net.minecraft.util.Direction
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider
import net.minecraft.world.gen.feature.ConfiguredFeature
import net.minecraft.world.gen.feature.TreeFeatureConfig
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer
import java.util.*

/**
 * Mangrove tree generator
 */
class MangroveTree : Tree() {
    override fun getTreeFeature(random: Random, includesBeehives: Boolean): ConfiguredFeature<TreeFeatureConfig, *> {
        return ModFeatures.MANGROVE_TREE.withConfiguration(
            TreeFeatureConfig.Builder(
                SimpleBlockStateProvider(ModBlocks.MANGROVE.defaultState.with(LogBlock.AXIS, Direction.Axis.Y)),
                SimpleBlockStateProvider(ModBlocks.MANGROVE_LEAVES.defaultState),
                BlobFoliagePlacer(2, 0)
            ).build()
        )
    }
}