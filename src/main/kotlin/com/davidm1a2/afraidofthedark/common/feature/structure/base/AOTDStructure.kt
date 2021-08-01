package com.davidm1a2.afraidofthedark.common.feature.structure.base

import com.davidm1a2.afraidofthedark.common.capabilities.getStructureMapper
import com.mojang.datafixers.Dynamic
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.BiomeManager
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.GenerationStage
import net.minecraft.world.gen.Heightmap
import net.minecraft.world.gen.feature.IFeatureConfig
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraft.world.gen.placement.IPlacementConfig
import net.minecraft.world.gen.placement.Placement
import java.util.*
import kotlin.math.max

abstract class AOTDStructure<T : IFeatureConfig>(configFactory: (Dynamic<*>) -> T, private val checksCollision: Boolean = true) : Structure<T>(configFactory) {
    init {
        setRegistryName(structureName)
    }

    abstract fun getWidth(): Int

    abstract fun getLength(): Int

    override fun getSize(): Int {
        return (max(getWidth(), getLength()) + 15) / 16
    }

    abstract fun setupStructureIn(biome: Biome)

    abstract override fun getStructureName(): String

    /**
     * Returns true if the structure would logically fit at a given x, z position. This simply considers biomes and heightmap, and nothing else
     * like structure collisions.
     *
     * @param chunkGen The chunk generator used to access heightmap and world info
     * @param biomeManager The biome manager used to access biome info
     * @param random A pre-seeded RNG object using the chunk coordinates
     * @param xPos The x position of the center of the structure
     * @param zPos The z position of the center of the structure
     * @return true if the structure would fit at a given location, false otherwise
     */
    abstract fun canFitAt(chunkGen: ChunkGenerator<*>, biomeManager: BiomeManager, random: Random, xPos: Int, zPos: Int): Boolean

    /**
     * Returns true if a structure will be generated in a given chunk
     *
     * @param biomeManager The biome manager used to access biome info
     * @param chunkGenerator The chunk generator used to access heightmap and world info
     * @param random A pre-seeded RNG object using the chunk coordinates
     * @param centerChunkX The x position of the chunk that the structure could be in
     * @param centerChunkZ The z position of the chunk that the structure could be in
     * @param biome The biome in the center of the chunk
     */
    override fun canBeGenerated(
        biomeManager: BiomeManager,
        chunkGenerator: ChunkGenerator<*>,
        random: Random,
        centerChunkX: Int,
        centerChunkZ: Int,
        biome: Biome
    ): Boolean {
        val world = chunkGenerator.getWorld()
        val chunkPos = ChunkPos(centerChunkX, centerChunkZ)
        val structureMapper = world.getStructureMapper()
        synchronized(structureMapper) {
            val structureMap = structureMapper.getStructureMapFor(chunkPos)

            structureMap.planStructuresIn(chunkPos, biomeManager, chunkGenerator)
            return structureMap.getStructureCenterIn(chunkPos, this) != null
        }
    }

    protected fun addToBiome(biome: Biome, config: T) {
        biome.addStructure(this.withConfiguration(config))
        biome.addFeature(
            GenerationStage.Decoration.TOP_LAYER_MODIFICATION, // Top_Layer_Modification happens last so it has the highest priority
            this.withConfiguration(config).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG))
        )
    }

    internal fun getEdgeHeights(
        x: Int,
        z: Int,
        chunkGen: ChunkGenerator<*>,
        width: Int = getWidth(),
        length: Int = getLength()
    ): Sequence<Int> {
        return sequence {
            yield(chunkGen.func_222532_b(x - width / 2, z - length / 2, Heightmap.Type.WORLD_SURFACE_WG))
            yield(chunkGen.func_222532_b(x + width / 2, z - length / 2, Heightmap.Type.WORLD_SURFACE_WG))
            yield(chunkGen.func_222532_b(x - width / 2, z + length / 2, Heightmap.Type.WORLD_SURFACE_WG))
            yield(chunkGen.func_222532_b(x + width / 2, z + length / 2, Heightmap.Type.WORLD_SURFACE_WG))
            yield(chunkGen.func_222532_b(x, z - length / 2, Heightmap.Type.WORLD_SURFACE_WG))
            yield(chunkGen.func_222532_b(x, z + length / 2, Heightmap.Type.WORLD_SURFACE_WG))
            yield(chunkGen.func_222532_b(x - width / 2, z, Heightmap.Type.WORLD_SURFACE_WG))
            yield(chunkGen.func_222532_b(x + width / 2, z, Heightmap.Type.WORLD_SURFACE_WG))
            yield(chunkGen.func_222532_b(x, z, Heightmap.Type.WORLD_SURFACE_WG))
        }
    }

    protected fun getInteriorConfigs(
        x: Int,
        z: Int,
        chunkGen: ChunkGenerator<*>,
        biomeManager: BiomeManager,
        width: Int = getWidth(),
        length: Int = getLength(),
        stepNum: Int = 1
    ): Sequence<T> {
        return sequence {
            for (xPos in x until x + width step stepNum) {
                for (zPos in z until z + length step stepNum) {
                    yield(chunkGen.getStructureConfig(biomeManager.getBiome(BlockPos(xPos, 0, zPos)), this@AOTDStructure))
                }
            }
        }.filterNotNull()
    }

    protected fun getInteriorConfigEstimate(
        x: Int,
        z: Int,
        chunkGen: ChunkGenerator<*>,
        biomeManager: BiomeManager,
        width: Int = getWidth(),
        length: Int = getLength()
    ): Sequence<T> {
        return sequence {
            yield(chunkGen.getStructureConfig(biomeManager.getBiome(BlockPos(x - width / 2, 0, z - length / 2)), this@AOTDStructure))
            yield(chunkGen.getStructureConfig(biomeManager.getBiome(BlockPos(x + width / 2, 0, z - length / 2)), this@AOTDStructure))
            yield(chunkGen.getStructureConfig(biomeManager.getBiome(BlockPos(x - width / 2, 0, z + length / 2)), this@AOTDStructure))
            yield(chunkGen.getStructureConfig(biomeManager.getBiome(BlockPos(x + width / 2, 0, z + length / 2)), this@AOTDStructure))
            yield(chunkGen.getStructureConfig(biomeManager.getBiome(BlockPos(x, 0, z - length / 2)), this@AOTDStructure))
            yield(chunkGen.getStructureConfig(biomeManager.getBiome(BlockPos(x, 0, z + length / 2)), this@AOTDStructure))
            yield(chunkGen.getStructureConfig(biomeManager.getBiome(BlockPos(x - width / 2, 0, z)), this@AOTDStructure))
            yield(chunkGen.getStructureConfig(biomeManager.getBiome(BlockPos(x + width / 2, 0, z)), this@AOTDStructure))
            yield(chunkGen.getStructureConfig(biomeManager.getBiome(BlockPos(x, 0, z)), this@AOTDStructure))
        }.filterNotNull()
    }

    protected fun getOneInNValidChunks(n: Int): Double {
        return 1.0 / n
    }
}