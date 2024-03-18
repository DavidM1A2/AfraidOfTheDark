package com.davidm1a2.afraidofthedark.common.block.sacred_mangrove

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModFeatures
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.world.level.block.grower.AbstractTreeGrower
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer
import java.util.*

/**
 * Sacred mangrove tree generator
 */
class SacredMangroveTree : AbstractTreeGrower() {
    override fun getConfiguredFeature(random: Random, includesBeehives: Boolean): ConfiguredFeature<TreeConfiguration, *> {
        return ModFeatures.SACRED_MANGROVE_TREE.configured(
            TreeConfiguration.TreeConfigurationBuilder(
                SimpleStateProvider(ModBlocks.SACRED_MANGROVE.defaultBlockState()),
                StraightTrunkPlacer(5, 3, 0),
                SimpleStateProvider(ModBlocks.SACRED_MANGROVE_LEAVES.defaultBlockState()),
                SimpleStateProvider(ModBlocks.SACRED_MANGROVE_SAPLING.defaultBlockState()),
                BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                TwoLayersFeatureSize(1, 0, 1)
            ).build()
        )
    }
}