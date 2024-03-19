package com.davidm1a2.afraidofthedark.common.world.structure.base

import com.davidm1a2.afraidofthedark.common.capabilities.getStructureMapper
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.mojang.serialization.Codec
import net.minecraft.core.MappedRegistry.RegistryEntry
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.LevelHeightAccessor
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.BiomeSource
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.WorldgenRandom
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature
import net.minecraft.world.level.levelgen.feature.StructureFeature
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks
import java.util.*
import kotlin.math.max

abstract class AOTDStructure<T : FeatureConfiguration>(name: String, codec: Codec<T>) : StructureFeature<T>(codec) {
    init {
        setRegistryName(Constants.MOD_ID, name)
    }

    abstract fun getWidth(): Int

    abstract fun getLength(): Int

    fun getSize(): Int {
        return (max(getWidth(), getLength()) + 15) / 16
    }

    override fun step(): GenerationStep.Decoration {
        // Use this as default because it has the higher priority
        return GenerationStep.Decoration.TOP_LAYER_MODIFICATION
    }

    /**
     * The structure configuration for a biome, or null if it does not exist in the biome
     *
     * @param biome The biome to configure the structure for
     * @param category The category the biome is in
     * @return The configured structure or null
     */
    abstract fun configured(biome: RegistryEntry<Biome>, category: Biome.BiomeCategory): ConfiguredStructureFeature<T, out StructureFeature<T>>?

    /**
     * The structure configuration when on a flat map
     *
     * @return The configured structure
     */
    abstract fun configuredFlat(): ConfiguredStructureFeature<T, out StructureFeature<T>>

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
    abstract fun canFitAt(chunkGen: ChunkGenerator, biomeProvider: BiomeSource, random: Random, xPos: Int, zPos: Int, levelHeightAccessor: LevelHeightAccessor): Boolean

    override fun isFeatureChunk(
        chunkGenerator: ChunkGenerator,
        biomeProvider: BiomeSource,
        seed: Long,
        random: WorldgenRandom,
        centerChunkPos: ChunkPos,
        biome: Biome,
        featureChunkPos: ChunkPos,
        config: T,
        levelHeightAccessor: LevelHeightAccessor
    ): Boolean {
        val world = ServerLifecycleHooks.getCurrentServer()?.allLevels?.find { it.chunkSource.generator === chunkGenerator }
            ?: throw IllegalStateException("Could not determine which world chunk generator $chunkGenerator belongs to")
        val structureMapper = world.getStructureMapper()
        synchronized(structureMapper) {
            val structureMap = structureMapper.getStructureMapFor(centerChunkPos)

            structureMap.planStructuresIn(centerChunkPos, biomeProvider, chunkGenerator, seed, levelHeightAccessor)
            return structureMap.getStructureCenterIn(centerChunkPos, this) != null
        }
    }

    internal fun getEdgeHeights(
        x: Int,
        z: Int,
        chunkGen: ChunkGenerator,
        width: Int = getWidth(),
        length: Int = getLength(),
        levelHeightAccessor: LevelHeightAccessor
    ): Sequence<Int> {
        return sequence {
            yield(chunkGen.getBaseHeight(x - width / 2, z - length / 2, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor))
            yield(chunkGen.getBaseHeight(x + width / 2, z - length / 2, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor))
            yield(chunkGen.getBaseHeight(x - width / 2, z + length / 2, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor))
            yield(chunkGen.getBaseHeight(x + width / 2, z + length / 2, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor))
            yield(chunkGen.getBaseHeight(x, z - length / 2, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor))
            yield(chunkGen.getBaseHeight(x, z + length / 2, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor))
            yield(chunkGen.getBaseHeight(x - width / 2, z, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor))
            yield(chunkGen.getBaseHeight(x + width / 2, z, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor))
            yield(chunkGen.getBaseHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, levelHeightAccessor))
        }
    }

    protected fun getInteriorConfigEstimate(
        x: Int,
        z: Int,
        biomeProvider: BiomeSource,
        defaultIfAbsent: T?,
        width: Int = getWidth(),
        length: Int = getLength()
    ): Sequence<T> {
        return getInteriorBiomeEstimate(x, z, biomeProvider, width, length)
            .map { getStructureConfig(it) ?: defaultIfAbsent }
            .filterNotNull()
    }

    protected fun getInteriorBiomeEstimate(
        x: Int,
        z: Int,
        biomeProvider: BiomeSource,
        width: Int = getWidth(),
        length: Int = getLength()
    ): Sequence<Biome> {
        return sequence {
            yield(biomeProvider.getNoiseBiomeAbsolute(x - width / 2, 0, z - length / 2))
            yield(biomeProvider.getNoiseBiomeAbsolute(x + width / 2, 0, z - length / 2))
            yield(biomeProvider.getNoiseBiomeAbsolute(x - width / 2, 0, z + length / 2))
            yield(biomeProvider.getNoiseBiomeAbsolute(x + width / 2, 0, z + length / 2))
            yield(biomeProvider.getNoiseBiomeAbsolute(x, 0, z - length / 2))
            yield(biomeProvider.getNoiseBiomeAbsolute(x, 0, z + length / 2))
            yield(biomeProvider.getNoiseBiomeAbsolute(x - width / 2, 0, z))
            yield(biomeProvider.getNoiseBiomeAbsolute(x + width / 2, 0, z))
            yield(biomeProvider.getNoiseBiomeAbsolute(x, 0, z))
        }
    }

    private fun BiomeSource.getNoiseBiomeAbsolute(x: Int, y: Int, z: Int): Biome {
        return getNoiseBiome(x shr 2, y shr 2, z shr 2)
    }

    private fun getStructureConfig(biome: Biome): T? {
        val structure = biome.generationSettings.structures().map { it.get() }.find { it.feature == this }
        return structure?.config as? T
    }

    protected fun getOneInNValidChunks(n: Int): Double {
        return 1.0 / n
    }
}