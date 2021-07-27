package com.davidm1a2.afraidofthedark.common.feature.structure.gnomishcity

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.feature.structure.base.AOTDStructure
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.BiomeManager
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.IFeatureConfig
import net.minecraft.world.gen.feature.NoFeatureConfig
import net.minecraft.world.gen.feature.structure.Structure.IStartFactory
import java.util.*

class GnomishCityStructure : AOTDStructure<NoFeatureConfig>({ IFeatureConfig.NO_FEATURE_CONFIG }) {
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

    override fun getStructureName(): String {
        return "${Constants.MOD_ID}:gnomish_city"
    }

    override fun getWidth(): Int {
        return width
    }

    override fun getLength(): Int {
        return length
    }

    override fun setupStructureIn(biome: Biome) {
        if (biome.category in VALID_BIOME_CATEGORIES) {
            addToBiome(biome, IFeatureConfig.NO_FEATURE_CONFIG)
        }
    }

    override fun getStartFactory(): IStartFactory {
        return IStartFactory { structure, chunkX, chunkZ, mutableBoundingBox, reference, seed ->
            GnomishCityStructureStart(structure, chunkX, chunkZ, mutableBoundingBox, reference, seed)
        }
    }

    override fun canFitAt(chunkGen: ChunkGenerator<*>, biomeManager: BiomeManager, random: Random, xPos: Int, zPos: Int): Boolean {
        val chance = getOneInNChunksChance(1000) * ModCommonConfiguration.gnomishCityFrequency
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