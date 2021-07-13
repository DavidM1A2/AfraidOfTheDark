package com.davidm1a2.afraidofthedark.common.world.tree

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.trees.Tree
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider
import net.minecraft.world.gen.feature.ConfiguredFeature
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.TreeFeatureConfig
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer
import java.util.*

/**
 * Gravewood tree generator
 */
class GravewoodTree : Tree() {
    override fun getTreeFeature(random: Random, withBeehives: Boolean): ConfiguredFeature<TreeFeatureConfig, *> {
        return Feature.NORMAL_TREE.withConfiguration(
            TreeFeatureConfig.Builder(
                SimpleBlockStateProvider(ModBlocks.GRAVEWOOD.defaultState),
                SimpleBlockStateProvider(ModBlocks.GRAVEWOOD_LEAVES.defaultState),
                BlobFoliagePlacer(2, 0)
            )
                .baseHeight(5)
                .heightRandA(2)
                .foliageHeight(3)
                .ignoreVines()
                .setSapling(ModBlocks.GRAVEWOOD_SAPLING)
                .build()
        )
    }
}