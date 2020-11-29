package com.davidm1a2.afraidofthedark.common.world.structure.gnomishcity

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.WorldHeightmap
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import net.minecraft.world.IWorld
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure.IStartFactory
import java.util.*

class GnomishCityStructure : AOTDStructure<GnomishCityConfig>({ GnomishCityConfig.deserialize() }) {
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
            addToBiome(biome, GnomishCityConfig())
        }
    }

    override fun getStartFactory(): IStartFactory {
        return IStartFactory { structure, chunkX, chunkZ, biome, mutableBoundingBox, reference, seed ->
            GnomishCityStructureStart(structure, chunkX, chunkZ, biome, mutableBoundingBox, reference, seed)
        }
    }

    override fun hasStartAt(worldIn: IWorld, chunkGen: ChunkGenerator<*>, random: Random, xPos: Int, zPos: Int): Boolean {
        val frequency = 0.0012 * ModCommonConfiguration.gnomishCityFrequency
        if (random.nextDouble() >= frequency) {
            return false
        }

        var minHeight = Int.MAX_VALUE
        var maxHeight = Int.MIN_VALUE
        val heightGranularity = 16
        for (xOffset in (-width / 2)..(width / 2) step heightGranularity) {
            for (zOffset in (-length / 2)..(length / 2) step heightGranularity) {
                val height = WorldHeightmap.getHeight(xPos + xOffset, zPos + zOffset, worldIn, chunkGen)
                if (height < minHeight) {
                    minHeight = height
                }
                if (height > maxHeight) {
                    maxHeight = height
                }
            }
        }

        // If there's less than a 25 block difference, continue, otherwise exit out
        if (maxHeight - minHeight > 25) {
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