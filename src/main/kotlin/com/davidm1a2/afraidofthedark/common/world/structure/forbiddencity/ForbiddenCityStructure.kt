package com.davidm1a2.afraidofthedark.common.world.structure.forbiddencity

import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.LevelHeightAccessor
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.BiomeSource
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature
import net.minecraft.world.level.levelgen.feature.StructureFeature
import net.minecraft.world.level.levelgen.feature.StructureFeature.StructureStartFactory
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration
import java.util.*

class ForbiddenCityStructure : AOTDStructure<NoneFeatureConfiguration>("forbidden_city", NoneFeatureConfiguration.CODEC) {
    private val width: Int
    private val length: Int

    init {
        val connectorWidth = ModSchematics.CONNECTOR.getWidth().toInt()

        // Room width is the same for all schematics
        val roomWidth = ModSchematics.ROOM_CAVE.getWidth().toInt()
        // 3 rooms + 2 tunnels - 4 connector blocks
        this.width = roomWidth * 3 + connectorWidth * 2 - 4

        // Room length is the same for all schematics
        val roomLength = ModSchematics.ROOM_CAVE.getLength().toInt()
        // 3 rooms + 2 tunnels - 4 connector blocks
        this.length = roomLength * 3 + connectorWidth * 2 - 4
    }

    override fun getWidth(): Int {
        return width
    }

    override fun getLength(): Int {
        return length
    }

    override fun getStartFactory(): StructureStartFactory<NoneFeatureConfiguration> {
        return StructureStartFactory { structure, chunkPos, reference, seed ->
            ForbiddenCityStructureStart(structure, chunkPos, reference, seed)
        }
    }

    override fun configured(biome: ResourceKey<Biome>, category: Biome.BiomeCategory): ConfiguredStructureFeature<NoneFeatureConfiguration, out StructureFeature<NoneFeatureConfiguration>>? {
        return if (category in VALID_BIOME_CATEGORIES) {
            configured(FeatureConfiguration.NONE)
        } else {
            null
        }
    }

    override fun configuredFlat(): ConfiguredStructureFeature<NoneFeatureConfiguration, out StructureFeature<NoneFeatureConfiguration>> {
        return configured(FeatureConfiguration.NONE)
    }

    override fun canFitAt(chunkGen: ChunkGenerator, biomeProvider: BiomeSource, random: Random, xPos: Int, zPos: Int, levelHeightAccessor: LevelHeightAccessor): Boolean {
        val chance = getOneInNValidChunks(3500) * ModCommonConfiguration.forbiddenCityFrequency
        if (random.nextDouble() >= chance) {
            return false
        }

        val surfaceStairIndex = ForbiddenCityStructureStart.getSurfaceStairIndex(xPos, zPos)
        val surfaceStairXIndex = surfaceStairIndex % 3
        val surfaceStairZIndex = surfaceStairIndex / 3

        val stairRoomPosX = xPos - width / 2 + surfaceStairXIndex * 50
        val stairRoomPosZ = zPos - length / 2 + surfaceStairZIndex * 50
        val stairCenterPosX = stairRoomPosX + ForbiddenCityStructureStart.STAIR_OFFSET + STAIRWELL_WIDTH / 2
        val stairCenterPosZ = stairRoomPosZ + ForbiddenCityStructureStart.STAIR_OFFSET + STAIRWELL_LENGTH / 2

        return getInteriorBiomeEstimate(stairCenterPosX, stairCenterPosZ, biomeProvider, STAIRWELL_WIDTH, STAIRWELL_LENGTH)
            .all { it.biomeCategory in VALID_STAIR_BIOME_CATEGORIES }
    }


    companion object {
        private val STAIRWELL_WIDTH = ModSchematics.STAIRWELL.getWidth().toInt()
        private val STAIRWELL_LENGTH = ModSchematics.STAIRWELL.getLength().toInt()

        private val VALID_BIOME_CATEGORIES = setOf(
            Biome.BiomeCategory.TAIGA,
            Biome.BiomeCategory.EXTREME_HILLS,
            Biome.BiomeCategory.JUNGLE,
            Biome.BiomeCategory.MESA,
            Biome.BiomeCategory.PLAINS,
            Biome.BiomeCategory.SAVANNA,
            Biome.BiomeCategory.ICY,
            Biome.BiomeCategory.BEACH,
            Biome.BiomeCategory.FOREST,
            Biome.BiomeCategory.OCEAN,
            Biome.BiomeCategory.DESERT,
            Biome.BiomeCategory.RIVER,
            Biome.BiomeCategory.SWAMP,
            Biome.BiomeCategory.MUSHROOM
        )
        private val VALID_STAIR_BIOME_CATEGORIES = setOf(
            Biome.BiomeCategory.TAIGA,
            Biome.BiomeCategory.EXTREME_HILLS,
            Biome.BiomeCategory.JUNGLE,
            Biome.BiomeCategory.MESA,
            Biome.BiomeCategory.PLAINS,
            Biome.BiomeCategory.SAVANNA,
            Biome.BiomeCategory.ICY,
            Biome.BiomeCategory.FOREST,
            Biome.BiomeCategory.DESERT,
            Biome.BiomeCategory.MUSHROOM
        )
    }
}