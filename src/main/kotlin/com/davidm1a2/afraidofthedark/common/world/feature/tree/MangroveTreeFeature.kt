package com.davidm1a2.afraidofthedark.common.world.feature.tree

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration
import net.minecraft.world.level.levelgen.structure.BoundingBox
import kotlin.math.sqrt

class MangroveTreeFeature : AOTDTreeFeature(
    ModBlocks.MANGROVE.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y),
    ModBlocks.MANGROVE_LEAVES.defaultBlockState()
) {
    init {
        setRegistryName(Constants.MOD_ID, "mangrove_tree")
    }

    override fun place(
        featurePlaceContext: FeaturePlaceContext<TreeConfiguration>,
        logPositions: MutableSet<BlockPos>,
        leafPositions: MutableSet<BlockPos>,
        boundingBox: BoundingBox
    ) {
        // Generate the trunk and root blocks
        val topOfTrunk = generateBase(featurePlaceContext, logPositions, boundingBox)
        // Generate branches and leaves
        generateBranches(featurePlaceContext, topOfTrunk, logPositions, leafPositions, boundingBox)
    }

    private fun generateBase(
        featurePlaceContext: FeaturePlaceContext<TreeConfiguration>,
        logPositions: MutableSet<BlockPos>,
        boundingBox: BoundingBox
    ): BlockPos {
        // Get fields from the context object
        val random = featurePlaceContext.random()
        val pos = featurePlaceContext.origin()
        val level = featurePlaceContext.level()

        // The height to reach before the trunk starts is between 4 and 7 blocks
        val heightBeforeTrunk = random.nextInt(4) + 4

        // The number of roots coming off of the trunk base are 4 - 8
        val numRoots = random.nextInt(5) + 4

        // Iterate num roots times to generate that many roots
        for (i in 0 until numRoots) {
            // Pick a random direction to generate a root in
            val xOffsetDirection = if (random.nextBoolean()) Direction.NORTH else Direction.SOUTH
            val zOffsetDirection = if (random.nextBoolean()) Direction.EAST else Direction.WEST

            // Let the root start at the sapling height 3-5 blocks away
            var xDistanceFromTrunk = random.nextInt(3) + 3
            var zDistanceFromTrunk = random.nextInt(3) + 3

            // The current pos of the root block being placed
            var currentPos = pos
                .relative(xOffsetDirection, xDistanceFromTrunk)
                .relative(zOffsetDirection, zDistanceFromTrunk)

            // Iterate from the current height up until we reach trunk height
            for (j in 0 until heightBeforeTrunk) {
                // If our current distance to the center of the trunk is non-zero have a 50% chance to move towards the center
                if (xDistanceFromTrunk != 0 && random.nextBoolean()) {
                    currentPos = currentPos.relative(xOffsetDirection.opposite)
                    xDistanceFromTrunk--
                }
                if (zDistanceFromTrunk != 0 && random.nextBoolean()) {
                    currentPos = currentPos.relative(zOffsetDirection.opposite)
                    zDistanceFromTrunk--
                }

                setLog(level, currentPos, logPositions, boundingBox)

                // Have a 1/10 chance to generate an extra log one block up or down
                if (random.nextDouble() < 0.1) {
                    setLog(level, if (random.nextBoolean()) currentPos.above() else currentPos.below(), logPositions, boundingBox)
                }

                // Always move up each iteration
                currentPos = currentPos.above()
            }

            // If we reached the top of the trunk before being all the way in generate horizontal logs all the way to the center
            while (xDistanceFromTrunk > 0 || zDistanceFromTrunk > 0) {
                // If we need to close the x distance do so
                if (xDistanceFromTrunk != 0) {
                    currentPos = currentPos.relative(xOffsetDirection.opposite)
                    xDistanceFromTrunk--
                }

                // If we need to close the z distance do so
                if (zDistanceFromTrunk != 0) {
                    currentPos = currentPos.relative(zOffsetDirection.opposite)
                    zDistanceFromTrunk--
                }

                // Set the block to log
                setLog(level, currentPos, logPositions, boundingBox)
            }
        }

        // Now generate a somewhat straight trunk 6-9 blocks tall
        val trunkHeight = random.nextInt(4) + 6

        // Compute the current top of the trunk
        var currentPos = pos.above(heightBeforeTrunk)

        // Begin working upwards with a low chance to lean sideways
        for (i in 0 until trunkHeight) {
            // 10% chance to lean in a horizontal direction
            if (random.nextDouble() < 0.1) {
                currentPos = currentPos.relative(Direction.from2DDataValue(random.nextInt(4)))
            }

            // Set the block to log
            setLog(level, currentPos, logPositions, boundingBox)

            // Advance up the trunk
            currentPos = currentPos.above()
        }
        return currentPos
    }

    private fun generateBranches(
        featurePlaceContext: FeaturePlaceContext<TreeConfiguration>,
        topOfTrunk: BlockPos,
        logPositions: MutableSet<BlockPos>,
        leafPositions: MutableSet<BlockPos>,
        boundingBox: BoundingBox
    ) {
        // Get fields from context
        val level = featurePlaceContext.level()
        val random = featurePlaceContext.random()

        // Create a leaf cluster at the top of the trunk
        generateLeafCluster(featurePlaceContext, topOfTrunk, logPositions, leafPositions, boundingBox)

        // 3 to 5 branches
        val numBranches = random.nextInt(3) + 3
        for (i in 0 until numBranches) {
            // Compute a random branch direction (one in x and one in z)
            val branchDir1 = Direction.from2DDataValue(random.nextInt(4))
            val branchDir2 = if (random.nextBoolean()) branchDir1.clockWise else branchDir1.counterClockWise
            // Compute a random branch length 3-7 blocks long
            val branchLength = random.nextInt(5) + 3
            // Grab the starting block pos to create the branch off of
            var currentBranchPos = topOfTrunk.relative(if (random.nextBoolean()) branchDir1 else branchDir2)
            // Iterate branch length number of times to create the logs required
            for (j in 0 until branchLength) {
                // Create a log block
                setLog(level, currentBranchPos, logPositions, boundingBox)
                // 10% chance to create a leaf cluster along the way
                if (random.nextDouble() < 0.1) {
                    generateLeafCluster(featurePlaceContext, currentBranchPos, logPositions, leafPositions, boundingBox)
                }
                // Move our position outwards by either moving in the dir1 or dir2 direction
                currentBranchPos = currentBranchPos.relative(if (random.nextBoolean()) branchDir1 else branchDir2)
                // 20% chance for our branch to move upwards
                if (random.nextDouble() < 0.2) {
                    currentBranchPos = currentBranchPos.above()
                }
            }
            // End the branch with a leaf cluster
            generateLeafCluster(featurePlaceContext, currentBranchPos, logPositions, leafPositions, boundingBox)
        }
    }

    private fun generateLeafCluster(
        featurePlaceContext: FeaturePlaceContext<TreeConfiguration>,
        location: BlockPos,
        logPositions: MutableSet<BlockPos>,
        leafPositions: MutableSet<BlockPos>,
        boundingBox: BoundingBox
    ) {
        // Grab fields from context
        val random = featurePlaceContext.random()
        val world = featurePlaceContext.level()

        // Set the center to a mangrove log
        setLog(featurePlaceContext.level(), location, logPositions, boundingBox)
        // Leaf clusters will be 3 blocks tall and 5 wide
        for (x in -2..2) {
            for (y in -1..1) {
                for (z in -2..2) { // Grab the leaf block's position
                    val leafPos = location.offset(x, y, z)
                    // Make the leaf clusters "circular" by checking distance to the center
                    if (sqrt(leafPos.distSqr(location)) < (if (y == 0) 2.5 else 2.0)) {
                        // 10% chance to just ignore the leaf block
                        if (random.nextDouble() < 0.9) {
                            // 3% chance to create a 'vine' of hanging leaf blocks
                            if (random.nextDouble() < 0.03) {
                                // Create a log at the base of the leaf block
                                setLog(world, leafPos, logPositions, boundingBox)
                                // Create a random length 'vine' 2-4 blocks long
                                val vineLength = random.nextInt(3) + 2
                                // Start at 1 so we don't try and replace our log block
                                for (i in 1..vineLength) {
                                    setLeaf(world, leafPos.below(i), leafPositions, boundingBox)
                                }
                            } else {
                                setLeaf(world, leafPos, leafPositions, boundingBox)
                            }
                        }
                    }
                }
            }
        }
    }
}