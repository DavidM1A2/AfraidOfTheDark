package com.davidm1a2.afraidofthedark.common.capabilities.world.structure

import com.davidm1a2.afraidofthedark.common.constants.ModFeatures
import com.davidm1a2.afraidofthedark.common.feature.structure.base.AOTDStructure
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.SharedSeedRandom
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.world.biome.BiomeManager
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraftforge.common.util.INBTSerializable
import kotlin.math.min
import kotlin.math.pow

class StructureMap : INBTSerializable<CompoundNBT> {
    private var root: StructureMapNode? = null

    /**
     * If the given chunkPos contains the passed in structure, then this will return the (x, z) position of the center of that structure. If it does not have
     * the structure, then it returns null.
     *
     * @param chunkPos The chunk to test
     * @param structure The structure to search for
     * @return The x, z position of the structure if it exists in this chunk, or null if it does not.
     */
    fun getStructureCenterIn(chunkPos: ChunkPos, structure: Structure<*>): BlockPos? {
        var currentGridSize: StructureGridSize? = StructureGridSize.LARGEST_GRID_SIZE
        var currentGridNode: StructureMapNode? = root

        do {
            if (currentGridNode!!.getStructure() == structure) {
                return currentGridNode.getStructurePos()
            }

            // Find the next quadrant
            currentGridSize = currentGridSize!!.nextSizeDown
            if (currentGridSize != null) {
                val relativeChildGridPos = currentGridSize.toRelativeGridPos(chunkPos)
                val childXQuadrant = Math.floorMod(relativeChildGridPos.x, 2)
                val childZQuadrant = Math.floorMod(relativeChildGridPos.z, 2)

                currentGridNode = when {
                    childXQuadrant == 0 && childZQuadrant == 0 -> currentGridNode.lowerLeftChild
                    childXQuadrant == 1 && childZQuadrant == 0 -> currentGridNode.lowerRightChild
                    childXQuadrant == 0 && childZQuadrant == 1 -> currentGridNode.upperLeftChild
                    childXQuadrant == 1 && childZQuadrant == 1 -> currentGridNode.upperRightChild
                    else -> throw IllegalStateException("Quadrant ($childXQuadrant, $childZQuadrant) is not valid.")
                }
            }
        } while (currentGridSize != null)

        return null
    }

    /**
     * Plans what structures will exist in this chunk.
     *
     * @param chunkPos The chunk to plan
     * @param biomeManager The biome manager used to test if a structure fits at this position
     * @param chunkGenerator The chunk generator used to test if a structure fits at this position
     */
    fun planStructuresIn(chunkPos: ChunkPos, biomeManager: BiomeManager, chunkGenerator: ChunkGenerator<*>) {
        planStructuresIn(chunkPos, biomeManager, chunkGenerator, null, StructureGridSize.LARGEST_GRID_SIZE)
    }

    private fun planStructuresIn(
        chunkPos: ChunkPos,
        biomeManager: BiomeManager,
        chunkGenerator: ChunkGenerator<*>,
        previousNode: StructureMapNode?,
        gridSize: StructureGridSize
    ) {
        val plannedNode = if (previousNode == null) {
            // Plan the root (this is a special case)
            if (root == null) {
                root = StructureMapNode()
                tryPlacingStructuresFor(root!!, chunkPos, biomeManager, chunkGenerator, gridSize)
            }
            root!!
        } else {
            // Plan the next child based on which quadrant it is in
            val relativeGridChunkPos = gridSize.toRelativeGridPos(chunkPos)
            val currentXQuadrant = Math.floorMod(relativeGridChunkPos.x, 2)
            val currentZQuadrant = Math.floorMod(relativeGridChunkPos.z, 2)

            when {
                currentXQuadrant == 0 && currentZQuadrant == 0 -> {
                    if (previousNode.lowerLeftChild == null) {
                        previousNode.lowerLeftChild = StructureMapNode()
                        tryPlacingStructuresFor(previousNode.lowerLeftChild!!, chunkPos, biomeManager, chunkGenerator, gridSize)
                    }
                    previousNode.lowerLeftChild!!
                }
                currentXQuadrant == 1 && currentZQuadrant == 0 -> {
                    if (previousNode.lowerRightChild == null) {
                        previousNode.lowerRightChild = StructureMapNode()
                        tryPlacingStructuresFor(previousNode.lowerRightChild!!, chunkPos, biomeManager, chunkGenerator, gridSize)
                    }
                    previousNode.lowerRightChild!!
                }
                currentXQuadrant == 0 && currentZQuadrant == 1 -> {
                    if (previousNode.upperLeftChild == null) {
                        previousNode.upperLeftChild = StructureMapNode()
                        tryPlacingStructuresFor(previousNode.upperLeftChild!!, chunkPos, biomeManager, chunkGenerator, gridSize)
                    }
                    previousNode.upperLeftChild!!
                }
                currentXQuadrant == 1 && currentZQuadrant == 1 -> {
                    if (previousNode.upperRightChild == null) {
                        previousNode.upperRightChild = StructureMapNode()
                        tryPlacingStructuresFor(previousNode.upperRightChild!!, chunkPos, biomeManager, chunkGenerator, gridSize)
                    }
                    previousNode.upperRightChild!!
                }
                else -> throw IllegalStateException("Quadrant ($currentXQuadrant, $currentZQuadrant) is not valid.")
            }
        }

        if (!plannedNode.hasStructure() && gridSize.nextSizeDown != null) {
            planStructuresIn(chunkPos, biomeManager, chunkGenerator, plannedNode, gridSize.nextSizeDown)
        }
    }

    private fun tryPlacingStructuresFor(
        node: StructureMapNode,
        chunkPos: ChunkPos,
        biomeManager: BiomeManager,
        chunkGenerator: ChunkGenerator<*>,
        gridSize: StructureGridSize
    ) {
        val absoluteGridChunkPos = gridSize.toAbsoluteGridPos(chunkPos)
        val random = SharedSeedRandom()
        random.setLargeFeatureSeed(chunkGenerator.seed, absoluteGridChunkPos.x, absoluteGridChunkPos.z)

        val centerXPos = absoluteGridChunkPos.xStart + gridSize.blockSize / 2
        val centerZPos = absoluteGridChunkPos.zStart + gridSize.blockSize / 2

        val possibleStructures = GRID_SIZE_TO_STRUCTURES[gridSize]!!.shuffled(random)
        for (structure in possibleStructures) {
            // We don't know the structure's rotation at this point, so we can't use independent x & z axis wiggle rooms
            val structureSize = min(structure.getLength(), structure.getWidth())
            val gridSizeBlocks = gridSize.blockSize
            val wiggleRoom = gridSizeBlocks - structureSize

            val numPlacementAttempts = 4.0.pow(gridSize.ordinal).toInt()
            for (i in 0 until numPlacementAttempts) {
                val xPosOffset = random.nextInt(wiggleRoom) - wiggleRoom / 2
                val zPosOffset = random.nextInt(wiggleRoom) - wiggleRoom / 2
                val xPos = centerXPos + xPosOffset
                val zPos = centerZPos + zPosOffset

                if (structure.canFitAt(chunkGenerator, biomeManager, random, xPos, zPos)) {
                    node.insertStructure(structure, BlockPos(xPos, 0, zPos))
                    return
                }
            }
        }
    }

    override fun serializeNBT(): CompoundNBT {
        val nbt = CompoundNBT()

        root?.let { nbt.put(NBT_ROOT, it.serializeNBT()) }

        return nbt
    }

    override fun deserializeNBT(nbt: CompoundNBT) {
        root = if (nbt.contains(NBT_ROOT)) {
            StructureMapNode().apply {
                deserializeNBT(nbt.getCompound(NBT_ROOT))
            }
        } else {
            null
        }
    }

    companion object {
        private const val NBT_ROOT = "root"

        private val GRID_SIZE_TO_STRUCTURES: Map<StructureGridSize, List<AOTDStructure<*>>> by lazy {
            val toReturn = mutableMapOf<StructureGridSize, List<AOTDStructure<*>>>()

            var previousGridSize: StructureGridSize? = null
            var currentGridSize: StructureGridSize? = StructureGridSize.LARGEST_GRID_SIZE
            val structures = ModFeatures.FEATURES.filterIsInstance<AOTDStructure<*>>()

            do {
                toReturn[currentGridSize!!] = structures.filter {
                    when {
                        previousGridSize == null -> it.size > currentGridSize!!.nextSizeDown!!.chunkSize
                        currentGridSize!!.nextSizeDown == null -> it.size <= currentGridSize!!.chunkSize
                        else -> it.size > currentGridSize!!.nextSizeDown!!.chunkSize && it.size <= currentGridSize!!.chunkSize
                    }
                }

                previousGridSize = currentGridSize
                currentGridSize = currentGridSize.nextSizeDown
            } while (currentGridSize != null)

            toReturn
        }
    }
}