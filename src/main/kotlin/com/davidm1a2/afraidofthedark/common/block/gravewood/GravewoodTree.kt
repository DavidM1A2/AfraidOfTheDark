package com.davidm1a2.afraidofthedark.common.block.gravewood

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.world.level.block.grower.AbstractTreeGrower
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration.TreeConfigurationBuilder
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer
import java.util.*

/**
 * Gravewood tree generator
 */
class GravewoodTree : AbstractTreeGrower() {
    override fun getConfiguredFeature(random: Random, withBeehives: Boolean): ConfiguredFeature<TreeConfiguration, *> {
        // TODO: Get this feature from the configured feature registry instead of hardcoding the values
        return Feature.TREE.configured(
            TreeConfigurationBuilder(
                SimpleStateProvider(ModBlocks.GRAVEWOOD.defaultBlockState()),
                StraightTrunkPlacer(5, 3, 0),
                SimpleStateProvider(ModBlocks.GRAVEWOOD_LEAVES.defaultBlockState()),
                SimpleStateProvider(ModBlocks.GRAVEWOOD_SAPLING.defaultBlockState()),
                BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                TwoLayersFeatureSize(1, 0, 1)
            ).ignoreVines().build()
        )
    }
}