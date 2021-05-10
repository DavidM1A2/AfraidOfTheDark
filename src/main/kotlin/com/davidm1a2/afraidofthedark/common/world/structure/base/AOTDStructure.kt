package com.davidm1a2.afraidofthedark.common.world.structure.base

import com.davidm1a2.afraidofthedark.common.capabilities.getStructureCollisionMap
import com.davidm1a2.afraidofthedark.common.world.WorldHeightmap
import com.mojang.datafixers.Dynamic
import net.minecraft.util.SharedSeedRandom
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.IWorld
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
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

    override fun hasStartAt(chunkGenerator: ChunkGenerator<*>, random: Random, centerChunkX: Int, centerChunkZ: Int): Boolean {
        (random as SharedSeedRandom).setLargeFeatureSeed(chunkGenerator.seed, centerChunkX, centerChunkZ)

        val xPos = centerChunkX * 16
        val zPos = centerChunkZ * 16
        val world = chunkGenerator.getWorld()

        return if (hasStartAt(world, chunkGenerator, random, xPos, zPos)) {
            val centerBiome = chunkGenerator.biomeProvider.getBiome(BlockPos(xPos + 9, 0, zPos + 9))
            val structureStart =
                startFactory.create(this, centerChunkX, centerChunkZ, centerBiome, MutableBoundingBox.getNewBoundingBox(), 0, chunkGenerator.seed)
            structureStart.init(chunkGenerator, (world as ServerWorld).saveHandler.structureTemplateManager, centerChunkX, centerChunkZ, centerBiome)
            return if (checksCollision) {
                doesNotCollide(world as World, structureStart)
            } else {
                true
            }
        } else {
            false
        }
    }

    abstract fun hasStartAt(worldIn: IWorld, chunkGen: ChunkGenerator<*>, random: Random, xPos: Int, zPos: Int): Boolean

    fun addToBiome(biome: Biome, config: T) {
        biome.addStructure(this, config)
        biome.addFeature(
            GenerationStage.Decoration.TOP_LAYER_MODIFICATION, // Top_Layer_Modification happens last so it has the highest priority
            Biome.createDecoratedFeature(
                this,
                config,
                Placement.NOPE,
                IPlacementConfig.NO_PLACEMENT_CONFIG
            )
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
    ): Array<Int> {
        val corner1Height = WorldHeightmap.getHeight(x - width / 2, z - length / 2, world, chunkGen)
        val corner2Height = WorldHeightmap.getHeight(x + width / 2, z - length / 2, world, chunkGen)
        val corner3Height = WorldHeightmap.getHeight(x - width / 2, z + length / 2, world, chunkGen)
        val corner4Height = WorldHeightmap.getHeight(x + width / 2, z + length / 2, world, chunkGen)
        val edge1Height = WorldHeightmap.getHeight(x, z - length / 2, world, chunkGen)
        val edge2Height = WorldHeightmap.getHeight(x, z + length / 2, world, chunkGen)
        val edge3Height = WorldHeightmap.getHeight(x - width / 2, z, world, chunkGen)
        val edge4Height = WorldHeightmap.getHeight(x + width / 2, z, world, chunkGen)
        val centerHeight = WorldHeightmap.getHeight(x, z, world, chunkGen)
        return arrayOf(corner1Height, corner2Height, corner3Height, corner4Height, edge1Height, edge2Height, edge3Height, edge4Height, centerHeight)
    }

    protected fun getInteriorConfigs(
        x: Int,
        z: Int,
        chunkGen: ChunkGenerator<*>,
        width: Int = getWidth(),
        length: Int = getLength(),
        stepNum: Int = 1
    ): Sequence<T?> {
        val biomeProvider = chunkGen.biomeProvider
        return sequence {
            for (xPos in x until x + width step stepNum) {
                for (zPos in z until z + length step stepNum) {
                    val biome = biomeProvider.getBiome(xPos, zPos)
                    yield(chunkGen.getStructureConfig(biome, this@AOTDStructure))
                }
            }
        }
    }
}