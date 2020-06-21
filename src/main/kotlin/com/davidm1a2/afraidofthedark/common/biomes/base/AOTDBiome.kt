package com.davidm1a2.afraidofthedark.common.biomes.base

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.entity.EntityType
import net.minecraft.entity.EnumCreatureType
import net.minecraft.init.Blocks
import net.minecraft.util.ResourceLocation
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.GenerationStage
import net.minecraft.world.gen.GenerationStage.Carving
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.IFeatureConfig
import net.minecraft.world.gen.feature.MinableConfig
import net.minecraft.world.gen.feature.ProbabilityConfig
import net.minecraft.world.gen.feature.structure.MineshaftConfig
import net.minecraft.world.gen.feature.structure.MineshaftStructure
import net.minecraft.world.gen.feature.structure.StrongholdConfig
import net.minecraft.world.gen.placement.CountRangeConfig
import net.minecraft.world.gen.placement.DepthAverageConfig
import net.minecraft.world.gen.placement.DungeonRoomConfig

/**
 * Base class for all AOTD biomes
 *
 * @param name The name of the biome
 * @param biomeBuilder The builder for the biome
 */
abstract class AOTDBiome(name: String, biomeBuilder: BiomeBuilder) : Biome(biomeBuilder) {
    init {
        registryName = ResourceLocation(Constants.MOD_ID, name)
    }

    /**
     * Adds caves to the biome
     */
    fun addCaves() {
        addCarver(
            Carving.AIR,
            createWorldCarverWrapper(CAVE_WORLD_CARVER, ProbabilityConfig(0.14285715f))
        )
    }

    /**
     * Adds ravines to the biome
     */
    fun addRavines() {
        addCarver(Carving.AIR, createWorldCarverWrapper(CANYON_WORLD_CARVER, ProbabilityConfig(0.02f)))
    }

    /**
     * Adds all the default structures to the biome
     */
    fun addDefaultStructures() {
        addStructure(Feature.MINESHAFT, MineshaftConfig(0.004, MineshaftStructure.Type.NORMAL))
        addStructure(Feature.STRONGHOLD, StrongholdConfig())
        addStructure(Feature.STRONGHOLD, StrongholdConfig())
    }

    /**
     * Adds all the default dungeons to the biome
     */
    fun addDefaultDungeons() {
        // Dungeons
        addFeature(
            GenerationStage.Decoration.UNDERGROUND_STRUCTURES,
            createCompositeFeature(
                Feature.DUNGEONS,
                IFeatureConfig.NO_FEATURE_CONFIG,
                DUNGEON_ROOM,
                DungeonRoomConfig(8)
            )
        )
    }

    /**
     * Adds all the default ores to the biome
     */
    fun addDefaultOreFeatures() {
        // Coal ore spawns
        addFeature(
            GenerationStage.Decoration.UNDERGROUND_ORES,
            createCompositeFeature(
                Feature.MINABLE,
                MinableConfig(MinableConfig.IS_ROCK, Blocks.COAL_ORE.defaultState, 17),
                COUNT_RANGE,
                CountRangeConfig(20, 0, 0, 128)
            )
        )
        // Iron ore spawns
        addFeature(
            GenerationStage.Decoration.UNDERGROUND_ORES,
            createCompositeFeature(
                Feature.MINABLE,
                MinableConfig(MinableConfig.IS_ROCK, Blocks.IRON_ORE.defaultState, 9),
                COUNT_RANGE,
                CountRangeConfig(20, 0, 0, 64)
            )
        )
        // Gold ore spawns
        addFeature(
            GenerationStage.Decoration.UNDERGROUND_ORES,
            createCompositeFeature(
                Feature.MINABLE,
                MinableConfig(MinableConfig.IS_ROCK, Blocks.GOLD_ORE.defaultState, 9),
                COUNT_RANGE,
                CountRangeConfig(2, 0, 0, 32)
            )
        )
        // Redstone ore spawns
        addFeature(
            GenerationStage.Decoration.UNDERGROUND_ORES,
            createCompositeFeature(
                Feature.MINABLE,
                MinableConfig(MinableConfig.IS_ROCK, Blocks.REDSTONE_ORE.defaultState, 8),
                COUNT_RANGE,
                CountRangeConfig(8, 0, 0, 16)
            )
        )
        // Diamond ore spawns
        addFeature(
            GenerationStage.Decoration.UNDERGROUND_ORES,
            createCompositeFeature(
                Feature.MINABLE,
                MinableConfig(MinableConfig.IS_ROCK, Blocks.DIAMOND_ORE.defaultState, 8),
                COUNT_RANGE,
                CountRangeConfig(1, 0, 0, 16)
            )
        )
        // Lapis spawns
        addFeature(
            GenerationStage.Decoration.UNDERGROUND_ORES,
            createCompositeFeature(
                Feature.MINABLE,
                MinableConfig(MinableConfig.IS_ROCK, Blocks.LAPIS_ORE.defaultState, 7),
                DEPTH_AVERAGE,
                DepthAverageConfig(1, 16, 16)
            )
        )
    }

    /**
     * Adds all the default entity spawns to the biome
     */
    fun addDefaultEntitySpawns() {
        addSpawn(EnumCreatureType.CREATURE, SpawnListEntry(EntityType.SHEEP, 12, 4, 4))
        addSpawn(EnumCreatureType.CREATURE, SpawnListEntry(EntityType.PIG, 10, 4, 4))
        addSpawn(EnumCreatureType.CREATURE, SpawnListEntry(EntityType.CHICKEN, 10, 4, 4))
        addSpawn(EnumCreatureType.CREATURE, SpawnListEntry(EntityType.COW, 8, 4, 4))
        addSpawn(EnumCreatureType.CREATURE, SpawnListEntry(EntityType.WOLF, 5, 4, 4))
        addSpawn(EnumCreatureType.AMBIENT, SpawnListEntry(EntityType.BAT, 10, 8, 8))
        addSpawn(EnumCreatureType.MONSTER, SpawnListEntry(EntityType.SPIDER, 100, 4, 4))
        addSpawn(EnumCreatureType.MONSTER, SpawnListEntry(EntityType.ZOMBIE, 95, 4, 4))
        addSpawn(EnumCreatureType.MONSTER, SpawnListEntry(EntityType.ZOMBIE_VILLAGER, 5, 1, 1))
        addSpawn(EnumCreatureType.MONSTER, SpawnListEntry(EntityType.SKELETON, 100, 4, 4))
        addSpawn(EnumCreatureType.MONSTER, SpawnListEntry(EntityType.CREEPER, 100, 4, 4))
        addSpawn(EnumCreatureType.MONSTER, SpawnListEntry(EntityType.SLIME, 100, 4, 4))
        addSpawn(EnumCreatureType.MONSTER, SpawnListEntry(EntityType.ENDERMAN, 10, 1, 4))
        addSpawn(EnumCreatureType.MONSTER, SpawnListEntry(EntityType.WITCH, 5, 1, 1))
    }
}