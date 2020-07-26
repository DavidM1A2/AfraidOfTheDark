package com.davidm1a2.afraidofthedark.common.biomes

import com.davidm1a2.afraidofthedark.common.biomes.base.AOTDBiome
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import net.minecraft.entity.EnumCreatureType
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.gen.GenerationStage
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.IFeatureConfig
import net.minecraft.world.gen.feature.TallGrassConfig
import net.minecraft.world.gen.feature.TreeFeature
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig
import net.minecraft.world.gen.placement.FrequencyConfig
import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig
import java.awt.Color

/**
 * Class representing the Eerie Forest biome
 *
 * @constructor initializes the biome's fields
 */
class BiomeEerieForest : AOTDBiome(
    "eerie_forest",
    BiomeBuilder()
        .surfaceBuilder(CompositeSurfaceBuilder(DEFAULT_SURFACE_BUILDER, SurfaceBuilderConfig(GRASS_BLOCK, DIRT, DIRT)))
        .waterColor(0x000099)
        .category(Category.FOREST)
        .depth(0.05f)
        .scale(0.125f)
        .precipitation(RainType.RAIN)
        .temperature(0.7f)
        .downfall(0.5f)
        .waterFogColor(0x000042)
) {
    init {
        addCaves()
        addDefaultStructures()
        addDefaultDungeons()
        addDefaultOreFeatures()
        addDefaultEntitySpawns()

        // Gravewood trees
        addFeature(
            GenerationStage.Decoration.VEGETAL_DECORATION,
            createCompositeFeature(
                TreeFeature(false, 4, ModBlocks.GRAVEWOOD.defaultState, ModBlocks.GRAVEWOOD_LEAVES.defaultState, false),
                IFeatureConfig.NO_FEATURE_CONFIG,
                AT_SURFACE_WITH_EXTRA,
                AtSurfaceWithExtraConfig(10, 0.3f, 5)
            )
        )

        // Tall grass
        addFeature(
            GenerationStage.Decoration.VEGETAL_DECORATION,
            createCompositeFeature(
                Feature.TALL_GRASS,
                TallGrassConfig(Blocks.GRASS.defaultState),
                TWICE_SURFACE,
                FrequencyConfig(4)
            )
        )

        // Werewolves
        addSpawn(EnumCreatureType.MONSTER, SpawnListEntry(ModEntities.WEREWOLF, 25, 1, 4))
    }

    /**
     * Use a brown grass color
     *
     * @param blockPos The position of the grass block
     * @return The new grass color
     */
    override fun getGrassColor(blockPos: BlockPos): Int {
        // hash code converts from color object to 32-bit integer, then get rid of the alpha parameter
        return Color(83, 56, 6).hashCode()
    }
}