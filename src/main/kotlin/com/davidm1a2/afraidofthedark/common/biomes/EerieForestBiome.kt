package com.davidm1a2.afraidofthedark.common.biomes

import com.davidm1a2.afraidofthedark.common.biomes.base.AOTDBiome
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityClassification
import net.minecraft.util.math.BlockPos
import net.minecraft.world.biome.DefaultBiomeFeatures
import net.minecraft.world.gen.GenerationStage
import net.minecraft.world.gen.GenerationStage.Carving
import net.minecraft.world.gen.carver.WorldCarver
import net.minecraft.world.gen.feature.*
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig
import net.minecraft.world.gen.placement.FrequencyConfig
import net.minecraft.world.gen.placement.Placement
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig
import java.awt.Color

/**
 * Class representing the Eerie Forest biome
 *
 * @constructor initializes the biome's fields
 */
class EerieForestBiome : AOTDBiome(
    "eerie_forest",
    Builder()
        .surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilderConfig(Blocks.GRASS_BLOCK.defaultState, Blocks.DIRT.defaultState, Blocks.DIRT.defaultState))
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
        // Caves, but no canyons
        addCarver(Carving.AIR, createCarver(WorldCarver.CAVE, ProbabilityConfig(0.14285715f)))
        DefaultBiomeFeatures.addStructures(this)
        DefaultBiomeFeatures.addMonsterRooms(this)
        DefaultBiomeFeatures.addOres(this)
        addDefaultEntitySpawns()

        // Gravewood trees
        addFeature(
            GenerationStage.Decoration.VEGETAL_DECORATION,
            createDecoratedFeature(
                TreeFeature({ IFeatureConfig.NO_FEATURE_CONFIG }, true, 5, ModBlocks.GRAVEWOOD.defaultState, ModBlocks.GRAVEWOOD_LEAVES.defaultState, false),
                IFeatureConfig.NO_FEATURE_CONFIG,
                Placement.COUNT_EXTRA_HEIGHTMAP,
                AtSurfaceWithExtraConfig(10, 0.3f, 5)
            )
        )

        // Tall grass
        addFeature(
            GenerationStage.Decoration.VEGETAL_DECORATION,
            createDecoratedFeature(
                Feature.GRASS,
                GrassFeatureConfig(Blocks.GRASS.defaultState),
                Placement.COUNT_HEIGHTMAP_DOUBLE,
                FrequencyConfig(4)
            )
        )

        // Werewolves
        addSpawn(EntityClassification.MONSTER, SpawnListEntry(ModEntities.WEREWOLF, 25, 1, 4))
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