package com.davidm1a2.afraidofthedark.common.world.structure.forbiddencity

import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import net.minecraft.util.RegistryKey
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.provider.BiomeProvider
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.IFeatureConfig
import net.minecraft.world.gen.feature.NoFeatureConfig
import net.minecraft.world.gen.feature.StructureFeature
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraft.world.gen.feature.structure.Structure.IStartFactory
import java.util.Random

class ForbiddenCityStructure : AOTDStructure<NoFeatureConfig>("forbidden_city", NoFeatureConfig.CODEC) {
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

    override fun getStartFactory(): IStartFactory<NoFeatureConfig> {
        return IStartFactory { structure, chunkX, chunkZ, mutableBoundingBox, reference, seed ->
            ForbiddenCityStructureStart(structure, chunkX, chunkZ, mutableBoundingBox, reference, seed)
        }
    }

    override fun configured(biome: RegistryKey<Biome>, category: Biome.Category): StructureFeature<NoFeatureConfig, out Structure<NoFeatureConfig>>? {
        return if (category in VALID_BIOME_CATEGORIES) {
            configured(IFeatureConfig.NONE)
        } else {
            null
        }
    }

    override fun configuredFlat(): StructureFeature<NoFeatureConfig, out Structure<NoFeatureConfig>> {
        return configured(IFeatureConfig.NONE)
    }

    override fun canFitAt(chunkGen: ChunkGenerator, biomeProvider: BiomeProvider, random: Random, xPos: Int, zPos: Int): Boolean {
        val chance = getOneInNValidChunks(3500) * ModCommonConfiguration.forbiddenCityFrequency
        if (random.nextDouble() >= chance) {
            return false
        }

        return true
    }


    companion object {
        private val VALID_BIOME_CATEGORIES = listOf(
            Biome.Category.TAIGA,
            Biome.Category.EXTREME_HILLS,
            Biome.Category.JUNGLE,
            Biome.Category.MESA,
            Biome.Category.PLAINS,
            Biome.Category.SAVANNA,
            Biome.Category.ICY,
            Biome.Category.BEACH,
            Biome.Category.FOREST,
            Biome.Category.OCEAN,
            Biome.Category.DESERT,
            Biome.Category.RIVER,
            Biome.Category.SWAMP,
            Biome.Category.MUSHROOM
        )
    }
}