package com.davidm1a2.afraidofthedark.common.world.structure.gnomishcity

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.WorldHeightmap
import com.davidm1a2.afraidofthedark.common.world.structure.base.AOTDStructure
import net.minecraft.init.Biomes
import net.minecraft.util.SharedSeedRandom
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.biome.Biome
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.gen.IChunkGenerator
import net.minecraft.world.gen.feature.structure.StructureStart

class GnomishCityStructure : AOTDStructure<GnomishCityConfig>() {
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
            addToBiome(biome, GnomishCityConfig(0.0003 * ModCommonConfiguration.gnomishCityFrequency))
        }
    }

    override fun isEnabledIn(worldIn: IWorld): Boolean {
        return worldIn.dimension.type == DimensionType.OVERWORLD
    }

    override fun hasStartAt(worldIn: IWorld, chunkGen: IChunkGenerator<*>, rand: SharedSeedRandom, centerChunkX: Int, centerChunkZ: Int): Boolean {
        rand.setLargeFeatureSeed(chunkGen.seed, centerChunkX, centerChunkZ)

        val xPos = centerChunkX * 16
        val zPos = centerChunkZ * 16

        val frequency = getInteriorConfigs(xPos, zPos, chunkGen, 1, 1).map { it?.frequency ?: 0.0 }.min() ?: 0.0
        if (rand.nextDouble() >= frequency) {
            return false
        }

        var minHeight = Int.MAX_VALUE
        var maxHeight = Int.MIN_VALUE
        val heightGranularity = 16
        for (x in (-width / 2)..(width / 2) step heightGranularity) {
            for (z in (-length / 2)..(length / 2) step heightGranularity) {
                val height = WorldHeightmap.getHeight(x, z, worldIn, chunkGen)
                if (height < minHeight) {
                    minHeight = height
                }
                if (height > maxHeight) {
                    maxHeight = height
                }
            }
        }

        // If there's less than a 20 block difference, continue, otherwise exit out
        if (maxHeight - minHeight > 20) {
            return false
        }

        return doesNotCollide(worldIn, chunkGen, rand, centerChunkX, centerChunkZ)
    }

    override fun makeStart(worldIn: IWorld, generator: IChunkGenerator<*>, random: SharedSeedRandom, centerChunkX: Int, centerChunkZ: Int): StructureStart {
        random.setLargeFeatureSeed(generator.seed, centerChunkX, centerChunkZ)

        val xPos = centerChunkX * 16
        val zPos = centerChunkZ * 16
        val centerBiome = generator.biomeProvider.getBiome(BlockPos(xPos, 0, zPos), Biomes.PLAINS)!!

        return GnomishCityStructureStart(worldIn, centerChunkX, centerChunkZ, width, length, centerBiome, random, generator.seed, generator)
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