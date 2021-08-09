package com.davidm1a2.afraidofthedark.common.feature.structure.gnomishcity

import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.feature.structure.base.AOTDStructure
import net.minecraft.world.biome.provider.BiomeProvider
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.NoFeatureConfig
import net.minecraft.world.gen.feature.structure.Structure.IStartFactory
import java.util.*

class GnomishCityStructure : AOTDStructure<NoFeatureConfig>("gnomish_city", NoFeatureConfig.CODEC) {
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
            GnomishCityStructureStart(structure, chunkX, chunkZ, mutableBoundingBox, reference, seed)
        }
    }

    override fun canFitAt(chunkGen: ChunkGenerator, biomeProvider: BiomeProvider, random: Random, xPos: Int, zPos: Int): Boolean {
        val chance = getOneInNValidChunks(3500) * ModCommonConfiguration.gnomishCityFrequency
        if (random.nextDouble() >= chance) {
            return false
        }

        return true
    }
}