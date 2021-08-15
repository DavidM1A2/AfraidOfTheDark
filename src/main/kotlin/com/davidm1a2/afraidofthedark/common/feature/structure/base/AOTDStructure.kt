package com.davidm1a2.afraidofthedark.common.feature.structure.base

import com.davidm1a2.afraidofthedark.common.capabilities.getStructureMapper
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.mojang.serialization.Codec
import net.minecraft.util.SharedSeedRandom
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.provider.BiomeProvider
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.GenerationStage
import net.minecraft.world.gen.Heightmap
import net.minecraft.world.gen.feature.IFeatureConfig
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraftforge.fml.server.ServerLifecycleHooks
import java.util.*
import kotlin.math.max

abstract class AOTDStructure<T : IFeatureConfig>(name: String, codec: Codec<T>) : Structure<T>(codec) {
    init {
        setRegistryName(Constants.MOD_ID, name)
    }

    abstract fun getWidth(): Int

    abstract fun getLength(): Int

    fun getSize(): Int {
        return (max(getWidth(), getLength()) + 15) / 16
    }

    override fun step(): GenerationStage.Decoration {
        // Use this as default because it has the higher priority
        return GenerationStage.Decoration.TOP_LAYER_MODIFICATION
    }

    /**
     * Returns true if the structure would logically fit at a given x, z position. This simply considers biomes and heightmap, and nothing else
     * like structure collisions.
     *
     * @param chunkGen The chunk generator used to access heightmap and world info
     * @param biomeProvider The biome provider used to access biome info
     * @param random A pre-seeded RNG object using the chunk coordinates
     * @param xPos The x position of the center of the structure
     * @param zPos The z position of the center of the structure
     * @return true if the structure would fit at a given location, false otherwise
     */
    abstract fun canFitAt(chunkGen: ChunkGenerator, biomeProvider: BiomeProvider, random: Random, xPos: Int, zPos: Int): Boolean

    override fun isFeatureChunk(
        chunkGenerator: ChunkGenerator,
        biomeProvider: BiomeProvider,
        seed: Long,
        random: SharedSeedRandom,
        centerChunkX: Int,
        centerChunkZ: Int,
        biome: Biome,
        featureChunkPos: ChunkPos,
        config: T
    ): Boolean {
        val world = ServerLifecycleHooks.getCurrentServer()?.allLevels?.find { it.chunkSource.generator === chunkGenerator }
            ?: throw IllegalStateException("Could not determine which world chunk generator $chunkGenerator belongs to")
        val chunkPos = ChunkPos(centerChunkX, centerChunkZ)
        val structureMapper = world.getStructureMapper()
        synchronized(structureMapper) {
            val structureMap = structureMapper.getStructureMapFor(chunkPos)

            structureMap.planStructuresIn(chunkPos, biomeProvider, chunkGenerator, seed)
            return structureMap.getStructureCenterIn(chunkPos, this) != null
        }
    }

    internal fun getEdgeHeights(
        x: Int,
        z: Int,
        chunkGen: ChunkGenerator,
        width: Int = getWidth(),
        length: Int = getLength()
    ): Sequence<Int> {
        return sequence {
            yield(chunkGen.getBaseHeight(x - width / 2, z - length / 2, Heightmap.Type.WORLD_SURFACE_WG))
            yield(chunkGen.getBaseHeight(x + width / 2, z - length / 2, Heightmap.Type.WORLD_SURFACE_WG))
            yield(chunkGen.getBaseHeight(x - width / 2, z + length / 2, Heightmap.Type.WORLD_SURFACE_WG))
            yield(chunkGen.getBaseHeight(x + width / 2, z + length / 2, Heightmap.Type.WORLD_SURFACE_WG))
            yield(chunkGen.getBaseHeight(x, z - length / 2, Heightmap.Type.WORLD_SURFACE_WG))
            yield(chunkGen.getBaseHeight(x, z + length / 2, Heightmap.Type.WORLD_SURFACE_WG))
            yield(chunkGen.getBaseHeight(x - width / 2, z, Heightmap.Type.WORLD_SURFACE_WG))
            yield(chunkGen.getBaseHeight(x + width / 2, z, Heightmap.Type.WORLD_SURFACE_WG))
            yield(chunkGen.getBaseHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG))
        }
    }

    protected fun getInteriorConfigs(
        x: Int,
        z: Int,
        biomeProvider: BiomeProvider,
        defaultIfAbsent: T?,
        width: Int = getWidth(),
        length: Int = getLength(),
        stepNum: Int = 1
    ): Sequence<T> {
        return sequence {
            for (xPos in x until x + width step stepNum) {
                for (zPos in z until z + length step stepNum) {
                    yield(getStructureConfig(biomeProvider.getNoiseBiome(xPos, 0, zPos)) ?: defaultIfAbsent)
                }
            }
        }.filterNotNull()
    }

    protected fun getInteriorConfigEstimate(
        x: Int,
        z: Int,
        biomeProvider: BiomeProvider,
        defaultIfAbsent: T?,
        width: Int = getWidth(),
        length: Int = getLength()
    ): Sequence<T> {
        return sequence {
            yield(getStructureConfig(biomeProvider.getNoiseBiome(x - width / 2, 0, z - length / 2)) ?: defaultIfAbsent)
            yield(getStructureConfig(biomeProvider.getNoiseBiome(x + width / 2, 0, z - length / 2)) ?: defaultIfAbsent)
            yield(getStructureConfig(biomeProvider.getNoiseBiome(x - width / 2, 0, z + length / 2)) ?: defaultIfAbsent)
            yield(getStructureConfig(biomeProvider.getNoiseBiome(x + width / 2, 0, z + length / 2)) ?: defaultIfAbsent)
            yield(getStructureConfig(biomeProvider.getNoiseBiome(x, 0, z - length / 2)) ?: defaultIfAbsent)
            yield(getStructureConfig(biomeProvider.getNoiseBiome(x, 0, z + length / 2)) ?: defaultIfAbsent)
            yield(getStructureConfig(biomeProvider.getNoiseBiome(x - width / 2, 0, z)) ?: defaultIfAbsent)
            yield(getStructureConfig(biomeProvider.getNoiseBiome(x + width / 2, 0, z)) ?: defaultIfAbsent)
            yield(getStructureConfig(biomeProvider.getNoiseBiome(x, 0, z)) ?: defaultIfAbsent)
        }.filterNotNull()
    }

    private fun getStructureConfig(biome: Biome): T? {
        val structure = biome.generationSettings.structures().map { it.get() }.find { it.feature == this }
        return structure?.config as? T
    }

    protected fun getOneInNValidChunks(n: Int): Double {
        return 1.0 / n
    }
}