package com.davidm1a2.afraidofthedark.common.world.structure.base

import com.davidm1a2.afraidofthedark.common.capabilities.world.StructureCollisionMap
import com.davidm1a2.afraidofthedark.common.world.WorldHeightmap
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import net.minecraft.util.SharedSeedRandom
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.IWorld
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.GenerationStage
import net.minecraft.world.gen.IChunkGenSettings
import net.minecraft.world.gen.IChunkGenerator
import net.minecraft.world.gen.feature.IFeatureConfig
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraft.world.gen.feature.structure.StructureStart
import net.minecraft.world.gen.placement.IPlacementConfig
import java.util.*
import kotlin.math.max

abstract class AOTDStructure<T : IFeatureConfig> : Structure<T>() {
    abstract fun getWidth(): Int

    abstract fun getLength(): Int

    override fun getSize(): Int {
        return (max(getWidth(), getLength()) + 15) / 16
    }

    abstract fun setupStructureIn(biome: Biome)

    public abstract override fun getStructureName(): String

    fun addToBiome(biome: Biome, config: T) {
        biome.addStructure(this, config)
        biome.addFeature(
            GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.createCompositeFeature(
                this,
                config,
                Biome.PASSTHROUGH,
                IPlacementConfig.NO_PLACEMENT_CONFIG
            )
        )
    }

    protected fun doesNotCollide(worldIn: IWorld, chunkGen: IChunkGenerator<*>, rand: SharedSeedRandom, centerChunkX: Int, centerChunkZ: Int): Boolean {
        val expectedStart = makeStart(worldIn, chunkGen, rand, centerChunkX, centerChunkZ)
        val collisionMap = StructureCollisionMap.get(worldIn)
        synchronized(collisionMap) {
            return if (!collisionMap.isStructureBlocked(expectedStart)) {
                collisionMap.insertStructure(expectedStart)
                true
            } else {
                false
            }
        }
    }

    protected fun getEdgeHeights(
        x: Int,
        z: Int,
        worldIn: IWorld,
        chunkGen: IChunkGenerator<*>,
        width: Int = getWidth(),
        length: Int = getLength()
    ): Array<Int> {
        val corner1Height = WorldHeightmap.getHeight(x - width / 2, z - length / 2, worldIn, chunkGen)
        val corner2Height = WorldHeightmap.getHeight(x + width / 2, z - length / 2, worldIn, chunkGen)
        val corner3Height = WorldHeightmap.getHeight(x - width / 2, z + length / 2, worldIn, chunkGen)
        val corner4Height = WorldHeightmap.getHeight(x + width / 2, z + length / 2, worldIn, chunkGen)
        val edge1Height = WorldHeightmap.getHeight(x, z - length / 2, worldIn, chunkGen)
        val edge2Height = WorldHeightmap.getHeight(x, z + length / 2, worldIn, chunkGen)
        val edge3Height = WorldHeightmap.getHeight(x - width / 2, z, worldIn, chunkGen)
        val edge4Height = WorldHeightmap.getHeight(x + width / 2, z, worldIn, chunkGen)
        val centerHeight = WorldHeightmap.getHeight(x, z, worldIn, chunkGen)
        return arrayOf(corner1Height, corner2Height, corner3Height, corner4Height, edge1Height, edge2Height, edge3Height, edge4Height, centerHeight)
    }

    protected fun getInteriorConfigs(x: Int, z: Int, chunkGen: IChunkGenerator<*>, width: Int = getWidth(), length: Int = getLength()): Sequence<T?> {
        val biomes = chunkGen.biomeProvider.getBiomesInSquare(
            x,
            z,
            max(width, length)
        )

        return biomes.asSequence().map { chunkGen.getStructureConfig(it, this) as? T }
    }

    // Don't use this version, it doesnt accept a world argument
    override fun hasStartAt(chunkGen: IChunkGenerator<*>, rand: Random, chunkPosX: Int, chunkPosZ: Int): Boolean {
        throw UnsupportedOperationException("Use hasStartAt(World, IChunkGenerator, Random, Int, Int): Boolean instead!")
    }

    // Use this instead of the above version without world
    abstract fun hasStartAt(worldIn: IWorld, chunkGen: IChunkGenerator<*>, rand: SharedSeedRandom, centerChunkX: Int, centerChunkZ: Int): Boolean

    ///
    /// Code below is almost exactly the same as Structure.java, except one line change
    ///

    private fun getStructureStart(
        worldIn: IWorld,
        generator: IChunkGenerator<out IChunkGenSettings?>,
        rand: SharedSeedRandom,
        packedChunkPos: Long
    ): StructureStart {
        return if (!generator.biomeProvider.hasStructure(this)) {
            NO_STRUCTURE
        } else {
            val long2objectmap = generator.getStructureReferenceToStartMap(this)
            var structurestart = long2objectmap[packedChunkPos]
            if (structurestart != null) {
                structurestart
            } else {
                val chunkpos = ChunkPos(packedChunkPos)
                val ichunk = worldIn.chunkProvider.provideChunkOrPrimer(chunkpos.x, chunkpos.z, false)
                if (ichunk != null) {
                    structurestart = ichunk.getStructureStart(this.structureName)
                    if (structurestart != null) {
                        long2objectmap[packedChunkPos] = structurestart
                        return structurestart
                    }
                }
                structurestart = if (hasStartAt(worldIn, generator, rand, chunkpos.x, chunkpos.z)) { // Modified this one line, added worldIn parameter!
                    val structurestart1 = makeStart(worldIn, generator, rand, chunkpos.x, chunkpos.z)
                    if (structurestart1.isSizeableStructure) structurestart1 else NO_STRUCTURE
                } else {
                    NO_STRUCTURE
                }
                if (structurestart!!.isSizeableStructure) {
                    worldIn.chunkProvider.provideChunkOrPrimer(chunkpos.x, chunkpos.z, true)!!.putStructureStart(this.structureName, structurestart)
                }
                long2objectmap[packedChunkPos] = structurestart
                structurestart
            }
        }
    }

    ///
    /// Code below copied from Structure.java
    ///

    override fun func_212245_a(
        p_212245_1_: IWorld,
        p_212245_2_: IChunkGenerator<out IChunkGenSettings?>,
        p_212245_3_: Random?,
        p_212245_4_: BlockPos,
        p_212245_5_: T?
    ): Boolean {
        return if (!isEnabledIn(p_212245_1_)) {
            false
        } else {
            val i = this.size
            val j = p_212245_4_.x shr 4
            val k = p_212245_4_.z shr 4
            val l = j shl 4
            val i1 = k shl 4
            val j1 = ChunkPos.asLong(j, k)
            var flag = false
            for (k1 in j - i..j + i) {
                for (l1 in k - i..k + i) {
                    val i2 = ChunkPos.asLong(k1, l1)
                    val structurestart = getStructureStart(p_212245_1_, p_212245_2_, p_212245_3_ as SharedSeedRandom, i2)
                    if (structurestart !== NO_STRUCTURE && structurestart.boundingBox
                            .intersectsWith(l, i1, l + 15, i1 + 15)
                    ) {
                        p_212245_2_.getStructurePositionToReferenceMap(this)
                            .computeIfAbsent(j1) { p_208203_0_: Long -> LongOpenHashSet() }.add(i2)
                        p_212245_1_.chunkProvider.provideChunkOrPrimer(j, k, true)!!.addStructureReference(this.structureName, i2)
                        structurestart.generateStructure(p_212245_1_, p_212245_3_, MutableBoundingBox(l, i1, l + 15, i1 + 15), ChunkPos(j, k))
                        structurestart.notifyPostProcessAt(ChunkPos(j, k))
                        flag = true
                    }
                }
            }
            flag
        }
    }

    override fun findNearest(
        worldIn: World,
        chunkGenerator: IChunkGenerator<out IChunkGenSettings>,
        pos: BlockPos,
        radius: Int,
        p_211405_5_: Boolean
    ): BlockPos? {
        return if (!chunkGenerator.biomeProvider.hasStructure(this)) {
            null
        } else {
            val i = pos.x shr 4
            val j = pos.z shr 4
            var k = 0
            val sharedseedrandom = SharedSeedRandom()
            while (k <= radius) {
                for (l in -k..k) {
                    val flag = l == -k || l == k
                    for (i1 in -k..k) {
                        val flag1 = i1 == -k || i1 == k
                        if (flag || flag1) {
                            val chunkpos = getStartPositionForPosition(chunkGenerator, sharedseedrandom, i, j, l, i1)
                            val structurestart = getStructureStart(worldIn, chunkGenerator, sharedseedrandom, chunkpos.asLong())
                            if (structurestart !== NO_STRUCTURE) {
                                if (p_211405_5_ && structurestart.func_212687_g()) {
                                    structurestart.func_212685_h()
                                    return structurestart.pos
                                }
                                if (!p_211405_5_) {
                                    return structurestart.pos
                                }
                            }
                            if (k == 0) {
                                break
                            }
                        }
                    }
                    if (k == 0) {
                        break
                    }
                }
                ++k
            }
            null
        }
    }
}