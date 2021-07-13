package com.davidm1a2.afraidofthedark.common.world.structure.base

import com.davidm1a2.afraidofthedark.common.capabilities.getStructureCollisionMap
import com.davidm1a2.afraidofthedark.common.capabilities.getStructureMissCounter
import com.davidm1a2.afraidofthedark.common.world.WorldHeightmap
import com.mojang.datafixers.Dynamic
import net.minecraft.util.SharedSeedRandom
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.IWorld
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.BiomeManager
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.GenerationStage
import net.minecraft.world.gen.feature.IFeatureConfig
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraft.world.gen.feature.structure.StructureStart
import net.minecraft.world.gen.placement.IPlacementConfig
import net.minecraft.world.gen.placement.Placement
import net.minecraft.world.server.ServerWorld
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

    override fun getStartPositionForPosition(
        p_211744_1_: ChunkGenerator<*>,
        p_211744_2_: Random,
        p_211744_3_: Int,
        p_211744_4_: Int,
        p_211744_5_: Int,
        p_211744_6_: Int
    ): ChunkPos {
        return super.getStartPositionForPosition(p_211744_1_, p_211744_2_, p_211744_3_, p_211744_4_, p_211744_5_, p_211744_6_)
    }

    override fun canBeGenerated(
        biomeManager: BiomeManager,
        chunkGenerator: ChunkGenerator<*>,
        random: Random,
        centerChunkX: Int,
        centerChunkZ: Int,
        biome: Biome
    ): Boolean {
        (random as SharedSeedRandom).setLargeFeatureSeed(chunkGenerator.seed, centerChunkX, centerChunkZ)

        val xPos = centerChunkX * 16
        val zPos = centerChunkZ * 16
        val world = chunkGenerator.getWorld()

        val structureMissCounter = world.getStructureMissCounter()

        structureMissCounter.increment(this)
        val currentCount = structureMissCounter.get(this)
        return if (hasStartAt(world, chunkGenerator, random, currentCount, xPos, zPos)) {
            val structureStart = startFactory.create(this, centerChunkX, centerChunkZ, MutableBoundingBox.getNewBoundingBox(), 0, chunkGenerator.seed)
            structureStart.init(chunkGenerator, (world as ServerWorld).saveHandler.structureTemplateManager, centerChunkX, centerChunkZ, biome)
            return if (checksCollision) {
                doesNotCollide(world, structureStart).also {
                    if (it) {
                        structureMissCounter.reset(this)
                    }
                }
            } else {
                true
            }
        } else {
            false
        }
    }

    abstract fun hasStartAt(worldIn: World, chunkGen: ChunkGenerator<*>, random: Random, missCount: Int, xPos: Int, zPos: Int): Boolean

    fun addToBiome(biome: Biome, config: T) {
        biome.addStructure(this.withConfiguration(config))
        biome.addFeature(
            GenerationStage.Decoration.TOP_LAYER_MODIFICATION, // Top_Layer_Modification happens last so it has the highest priority
            this.withConfiguration(config).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG))
        )
    }

    private fun doesNotCollide(worldIn: World, expectedStart: StructureStart): Boolean {
        val collisionMap = worldIn.getStructureCollisionMap()
        synchronized(collisionMap) {
            return if (!collisionMap.isStructureBlocked(expectedStart)) {
                collisionMap.insertStructure(expectedStart)
                true
            } else {
                false
            }
        }
    }

    internal fun getEdgeHeights(
        x: Int,
        z: Int,
        chunkGen: ChunkGenerator<*>,
        world: IWorld = chunkGen.getWorld(),
        width: Int = getWidth(),
        length: Int = getLength()
    ): Sequence<Int> {
        return sequence {
            yield(WorldHeightmap.getHeight(x - width / 2, z - length / 2, world, chunkGen))
            yield(WorldHeightmap.getHeight(x + width / 2, z - length / 2, world, chunkGen))
            yield(WorldHeightmap.getHeight(x - width / 2, z + length / 2, world, chunkGen))
            yield(WorldHeightmap.getHeight(x + width / 2, z + length / 2, world, chunkGen))
            yield(WorldHeightmap.getHeight(x, z - length / 2, world, chunkGen))
            yield(WorldHeightmap.getHeight(x, z + length / 2, world, chunkGen))
            yield(WorldHeightmap.getHeight(x - width / 2, z, world, chunkGen))
            yield(WorldHeightmap.getHeight(x + width / 2, z, world, chunkGen))
            yield(WorldHeightmap.getHeight(x, z, world, chunkGen))
        }
    }

    protected fun getInteriorConfigs(
        x: Int,
        z: Int,
        chunkGen: ChunkGenerator<*>,
        width: Int = getWidth(),
        length: Int = getLength(),
        stepNum: Int = 1
    ): Sequence<T> {
        val biomeProvider = chunkGen.biomeProvider
        return sequence {
            for (xPos in x until x + width step stepNum) {
                for (zPos in z until z + length step stepNum) {
                    val biome = biomeProvider.getNoiseBiome(xPos, 0, zPos)
                    yield(chunkGen.getStructureConfig(biome, this@AOTDStructure))
                }
            }
        }.filterNotNull()
    }

    protected fun getInteriorConfigEstimate(
        x: Int,
        z: Int,
        chunkGen: ChunkGenerator<*>,
        width: Int = getWidth(),
        length: Int = getLength()
    ): Sequence<T> {
        val biomeProvider = chunkGen.biomeProvider
        return sequence {
            yield(chunkGen.getStructureConfig(biomeProvider.getNoiseBiome(x - width / 2, 0, z - length / 2), this@AOTDStructure))
            yield(chunkGen.getStructureConfig(biomeProvider.getNoiseBiome(x + width / 2, 0, z - length / 2), this@AOTDStructure))
            yield(chunkGen.getStructureConfig(biomeProvider.getNoiseBiome(x - width / 2, 0, z + length / 2), this@AOTDStructure))
            yield(chunkGen.getStructureConfig(biomeProvider.getNoiseBiome(x + width / 2, 0, z + length / 2), this@AOTDStructure))
            yield(chunkGen.getStructureConfig(biomeProvider.getNoiseBiome(x, 0, z - length / 2), this@AOTDStructure))
            yield(chunkGen.getStructureConfig(biomeProvider.getNoiseBiome(x, 0, z + length / 2), this@AOTDStructure))
            yield(chunkGen.getStructureConfig(biomeProvider.getNoiseBiome(x - width / 2, 0, z), this@AOTDStructure))
            yield(chunkGen.getStructureConfig(biomeProvider.getNoiseBiome(x + width / 2, 0, z), this@AOTDStructure))
            yield(chunkGen.getStructureConfig(biomeProvider.getNoiseBiome(x, 0, z), this@AOTDStructure))
        }.filterNotNull()
    }

    protected fun Double.powOptimized(n: Int): Double {
        var result = this
        for (ignored in 0 until n - 1) {
            result = result * this
        }
        return result
    }
}