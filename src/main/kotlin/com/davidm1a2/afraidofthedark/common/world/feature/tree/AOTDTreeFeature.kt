package com.davidm1a2.afraidofthedark.common.world.feature.tree

import net.minecraft.block.BlockState
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.util.math.shapes.BitSetVoxelShapePart
import net.minecraft.world.ISeedReader
import net.minecraft.world.IWorld
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.IWorldGenerationReader
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.TreeFeature
import net.minecraft.world.gen.feature.template.Template
import java.util.Random

abstract class AOTDTreeFeature(
    private val log: BlockState,
    private val leaves: BlockState
) : Feature<BaseTreeFeatureConfig>(BaseTreeFeatureConfig.CODEC) {
    override fun place(
        world: ISeedReader,
        chunkGenerator: ChunkGenerator,
        random: Random,
        blockPos: BlockPos,
        ignored: BaseTreeFeatureConfig
    ): Boolean {
        val logPositions = mutableSetOf<BlockPos>()
        val leafPositions = mutableSetOf<BlockPos>()
        val boundingBox = MutableBoundingBox.getUnknownBox()

        place(world, chunkGenerator, random, blockPos, logPositions, leafPositions, boundingBox)

        updateLeaves(world, boundingBox, logPositions, leafPositions)
        return true
    }

    abstract fun place(
        world: ISeedReader,
        chunkGenerator: ChunkGenerator,
        random: Random,
        blockPos: BlockPos,
        logPositions: MutableSet<BlockPos>,
        leafPositions: MutableSet<BlockPos>,
        boundingBox: MutableBoundingBox
    )

    protected fun setLog(world: IWorldGenerationReader, blockPos: BlockPos, logPositions: MutableSet<BlockPos>, boundingBox: MutableBoundingBox) {
        setBlock(world, blockPos, log)
        logPositions.add(blockPos)
        boundingBox.expand(MutableBoundingBox(blockPos, blockPos))
    }

    protected fun setLeaf(world: IWorldGenerationReader, blockPos: BlockPos, leafPositions: MutableSet<BlockPos>, boundingBox: MutableBoundingBox) {
        setBlock(world, blockPos, leaves)
        leafPositions.add(blockPos)
        boundingBox.expand(MutableBoundingBox(blockPos, blockPos))
    }

    /**
     * Copy & Pasted from TreeFeature::updateLeaves. Tried my best to rename variables to make sense. It fixes the leaves' distance properties
     */
    private fun updateLeaves(world: IWorld, boundingBox: MutableBoundingBox, logPositions: Set<BlockPos>, leafPositions: Set<BlockPos>) {
        val leafCheckIterationSets = List<MutableSet<BlockPos>>(6) { mutableSetOf() }
        val voxelShape = BitSetVoxelShapePart(boundingBox.xSpan, boundingBox.ySpan, boundingBox.zSpan)

        for (leafPosition in leafPositions) {
            if (boundingBox.isInside(leafPosition)) {
                voxelShape.setFull(leafPosition.x - boundingBox.x0, leafPosition.y - boundingBox.y0, leafPosition.z - boundingBox.z0, true, true)
            }
        }

        val tempBlockPos = BlockPos.Mutable()
        for (logPosition in logPositions) {
            if (boundingBox.isInside(logPosition)) {
                voxelShape.setFull(logPosition.x - boundingBox.x0, logPosition.y - boundingBox.y0, logPosition.z - boundingBox.z0, true, true)
            }
            for (direction in Direction.values()) {
                tempBlockPos.setWithOffset(logPosition, direction)
                if (!logPositions.contains(tempBlockPos)) {
                    val blockNextToLog = world.getBlockState(tempBlockPos)
                    if (blockNextToLog.hasProperty(BlockStateProperties.DISTANCE)) {
                        leafCheckIterationSets[0].add(tempBlockPos.immutable())
                        TreeFeature.setBlockKnownShape(world, tempBlockPos, blockNextToLog.setValue(BlockStateProperties.DISTANCE, 1))
                        if (boundingBox.isInside(tempBlockPos)) {
                            voxelShape.setFull(
                                tempBlockPos.x - boundingBox.x0,
                                tempBlockPos.y - boundingBox.y0,
                                tempBlockPos.z - boundingBox.z0,
                                true,
                                true
                            )
                        }
                    }
                }
            }
        }

        for (index in 1..5) {
            val currentSet = leafCheckIterationSets[index - 1]
            val nextSet = leafCheckIterationSets[index]
            for (blockPos in currentSet) {
                if (boundingBox.isInside(blockPos)) {
                    voxelShape.setFull(blockPos.x - boundingBox.x0, blockPos.y - boundingBox.y0, blockPos.z - boundingBox.z0, true, true)
                }
                for (direction in Direction.values()) {
                    tempBlockPos.setWithOffset(blockPos, direction)
                    if (!currentSet.contains(tempBlockPos) && !nextSet.contains(tempBlockPos)) {
                        val leafState = world.getBlockState(tempBlockPos)
                        if (leafState.hasProperty(BlockStateProperties.DISTANCE)) {
                            val distance = leafState.getValue(BlockStateProperties.DISTANCE)
                            if (distance > index + 1) {
                                val newLeafState = leafState.setValue(BlockStateProperties.DISTANCE, index + 1)
                                TreeFeature.setBlockKnownShape(world, tempBlockPos, newLeafState)
                                if (boundingBox.isInside(tempBlockPos)) {
                                    voxelShape.setFull(
                                        tempBlockPos.x - boundingBox.x0,
                                        tempBlockPos.y - boundingBox.y0,
                                        tempBlockPos.z - boundingBox.z0,
                                        true,
                                        true
                                    )
                                }
                                nextSet.add(tempBlockPos.immutable())
                            }
                        }
                    }
                }
            }
        }

        Template.updateShapeAtEdge(world, 3, voxelShape, boundingBox.x0, boundingBox.y0, boundingBox.z0)
    }
}