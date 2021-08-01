package com.davidm1a2.afraidofthedark.common.biomes

import com.davidm1a2.afraidofthedark.common.biomes.base.AOTDBiome
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityClassification
import net.minecraft.world.biome.DefaultBiomeFeatures
import net.minecraft.world.gen.GenerationStage
import net.minecraft.world.gen.GenerationStage.Carving
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider
import net.minecraft.world.gen.carver.WorldCarver
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.ProbabilityConfig
import net.minecraft.world.gen.feature.TreeFeatureConfig
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig
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
        .depth(0.125f)
        .scale(0.03f)
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
            Feature.NORMAL_TREE
                .withConfiguration(
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
                .withPlacement(Placement.COUNT_EXTRA_HEIGHTMAP.configure(AtSurfaceWithExtraConfig(10, 0.3f, 5)))
        )

        // Tall grass
        DefaultBiomeFeatures.addTallGrass(this)

        // Werewolves (roughly the same chance as other monsters)
        addSpawn(EntityClassification.MONSTER, SpawnListEntry(ModEntities.WEREWOLF, 100, 1, 4))
    }

    override fun getGrassColor(xPos: Double, zPos: Double): Int {
        // hash code converts from color object to 32-bit integer, then get rid of the alpha parameter
        return Color(83, 56, 6).hashCode()
    }
}