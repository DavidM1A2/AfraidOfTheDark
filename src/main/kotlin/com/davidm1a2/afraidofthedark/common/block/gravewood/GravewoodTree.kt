package com.davidm1a2.afraidofthedark.common.block.gravewood

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.trees.Tree
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig
import net.minecraft.world.gen.feature.ConfiguredFeature
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.FeatureSpread
import net.minecraft.world.gen.feature.TwoLayerFeature
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer
import java.util.*

/**
 * Gravewood tree generator
 */
class GravewoodTree : Tree() {
    override fun getConfiguredFeature(random: Random, withBeehives: Boolean): ConfiguredFeature<BaseTreeFeatureConfig, *> {
        return Feature.TREE.configured(
            BaseTreeFeatureConfig.Builder(
                SimpleBlockStateProvider(ModBlocks.GRAVEWOOD.defaultBlockState()),
                SimpleBlockStateProvider(ModBlocks.GRAVEWOOD_LEAVES.defaultBlockState()),
                BlobFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(0), 3),
                StraightTrunkPlacer(5, 3, 0),
                TwoLayerFeature(1, 0, 1)
            ).ignoreVines().build()
        )
    }
}