package com.davidm1a2.afraidofthedark.common.block.sacred_mangrove

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModFeatures
import net.minecraft.block.trees.Tree
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig
import net.minecraft.world.gen.feature.ConfiguredFeature
import net.minecraft.world.gen.feature.FeatureSpread
import net.minecraft.world.gen.feature.TwoLayerFeature
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer
import java.util.*

/**
 * Sacred mangrove tree generator
 */
class SacredMangroveTree : Tree() {
    override fun getConfiguredFeature(random: Random, includesBeehives: Boolean): ConfiguredFeature<BaseTreeFeatureConfig, *> {
        return ModFeatures.SACRED_MANGROVE_TREE.configured(
            BaseTreeFeatureConfig.Builder(
                SimpleBlockStateProvider(ModBlocks.SACRED_MANGROVE.defaultBlockState()),
                SimpleBlockStateProvider(ModBlocks.SACRED_MANGROVE_LEAVES.defaultBlockState()),
                BlobFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(0), 3),
                StraightTrunkPlacer(5, 3, 0),
                TwoLayerFeature(1, 0, 1)
            ).build()
        )
    }
}