package com.davidm1a2.afraidofthedark.common.world.feature.tree

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.LevelWriter
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration
import net.minecraft.world.level.levelgen.structure.BoundingBox
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape

abstract class AOTDTreeFeature(
    private val log: BlockState,
    private val leaves: BlockState
) : Feature<TreeConfiguration>(TreeConfiguration.CODEC) {
    override fun place(
        featurePlaceContext: FeaturePlaceContext<TreeConfiguration>
    ): Boolean {
        val logPositions = mutableSetOf<BlockPos>()
        val leafPositions = mutableSetOf<BlockPos>()
        val boundingBox = BoundingBox.encapsulatingPositions(listOf(featurePlaceContext.origin())).get()

        place(featurePlaceContext, logPositions, leafPositions, boundingBox)

        updateLeaves(featurePlaceContext, boundingBox, logPositions, leafPositions)
        return true
    }

    abstract fun place(
        featurePlaceContext: FeaturePlaceContext<TreeConfiguration>,
        logPositions: MutableSet<BlockPos>,
        leafPositions: MutableSet<BlockPos>,
        boundingBox: BoundingBox
    )

    protected fun setLog(world: WorldGenLevel, blockPos: BlockPos, logPositions: MutableSet<BlockPos>, boundingBox: BoundingBox) {
        setBlock(world, blockPos, log)
        logPositions.add(blockPos)
        boundingBox.encapsulate(blockPos)
    }

    protected fun setLeaf(world: WorldGenLevel, blockPos: BlockPos, leafPositions: MutableSet<BlockPos>, boundingBox: BoundingBox) {
        setBlock(world, blockPos, leaves)
        leafPositions.add(blockPos)
        boundingBox.encapsulate(BoundingBox(blockPos))
    }

    /**
     * Copy & Pasted from TreeFeature::updateLeaves. Tried my best to rename variables to make sense. It fixes the leaves' distance properties
     */
    private fun updateLeaves(featurePlaceContext: FeaturePlaceContext<TreeConfiguration>, boundingBox: BoundingBox, logPositions: Set<BlockPos>, leafPositions: Set<BlockPos>) {
        val leafCheckIterationSets = List<MutableSet<BlockPos>>(6) { mutableSetOf() }
        val voxelShape = BitSetDiscreteVoxelShape(boundingBox.xSpan, boundingBox.ySpan, boundingBox.zSpan)

        for (leafPosition in leafPositions) {
            if (boundingBox.isInside(leafPosition)) {
                voxelShape.fill(leafPosition.x - boundingBox.minX(), leafPosition.y - boundingBox.minY(), leafPosition.z - boundingBox.minZ())
            }
        }

        val tempBlockPos = BlockPos.MutableBlockPos()
        for (logPosition in logPositions) {
            if (boundingBox.isInside(logPosition)) {
                voxelShape.fill(logPosition.x - boundingBox.minX(), logPosition.y - boundingBox.minY(), logPosition.z - boundingBox.minZ())
            }
            for (direction in Direction.values()) {
                tempBlockPos.setWithOffset(logPosition, direction)
                if (!logPositions.contains(tempBlockPos)) {
                    val blockNextToLog = featurePlaceContext.level().getBlockState(tempBlockPos)
                    if (blockNextToLog.hasProperty(BlockStateProperties.DISTANCE)) {
                        leafCheckIterationSets[0].add(tempBlockPos.immutable())
                        setBlockKnownShape(featurePlaceContext.level(), tempBlockPos, blockNextToLog.setValue(BlockStateProperties.DISTANCE, 1))
                        if (boundingBox.isInside(tempBlockPos)) {
                            voxelShape.fill(
                                tempBlockPos.x - boundingBox.minX(),
                                tempBlockPos.y - boundingBox.minY(),
                                tempBlockPos.z - boundingBox.minZ()
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
                    voxelShape.fill(blockPos.x - boundingBox.minX(), blockPos.y - boundingBox.minX(), blockPos.z - boundingBox.minZ())
                }
                for (direction in Direction.values()) {
                    tempBlockPos.setWithOffset(blockPos, direction)
                    if (!currentSet.contains(tempBlockPos) && !nextSet.contains(tempBlockPos)) {
                        val leafState = featurePlaceContext.level().getBlockState(tempBlockPos)
                        if (leafState.hasProperty(BlockStateProperties.DISTANCE)) {
                            val distance = leafState.getValue(BlockStateProperties.DISTANCE)
                            if (distance > index + 1) {
                                val newLeafState = leafState.setValue(BlockStateProperties.DISTANCE, index + 1)
                                setBlockKnownShape(featurePlaceContext.level(), tempBlockPos, newLeafState)
                                if (boundingBox.isInside(tempBlockPos)) {
                                    voxelShape.fill(
                                        tempBlockPos.x - boundingBox.minX(),
                                        tempBlockPos.y - boundingBox.minY(),
                                        tempBlockPos.z - boundingBox.minZ()
                                    )
                                }
                                nextSet.add(tempBlockPos.immutable())
                            }
                        }
                    }
                }
            }
        }

        StructureTemplate.updateShapeAtEdge(featurePlaceContext.level(), 3, voxelShape, boundingBox.minX(), boundingBox.minY(), boundingBox.minZ())
    }

    // Copied from TreeFeature
    private fun setBlockKnownShape(levelWriter: LevelWriter, blockPos: BlockPos, blockState: BlockState) {
        levelWriter.setBlock(blockPos, blockState, 19)
    }
}