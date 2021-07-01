package com.davidm1a2.afraidofthedark.common.world.tree.feature

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.LogBlock
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MutableBoundingBox
import net.minecraft.world.gen.IWorldGenerationReader
import net.minecraft.world.gen.feature.NoFeatureConfig
import net.minecraft.world.gen.feature.TreeFeature
import java.util.*
import kotlin.math.sqrt

/**
 * Mangrove tree feature
 *
 * @param notify True if placing blocks should notify, false otherwise
 */
class MangroveTreeFeature(notify: Boolean) : TreeFeature({ NoFeatureConfig.NO_FEATURE_CONFIG }, notify) {
    /**
     * Causes the tree to grow. Uses a custom tree generation algorithm
     *
     * @param changedBlocks A list of log blocks that were placed
     * @param world The world the tree is growing in
     * @param random The random object to grow the tree with
     * @param pos The position of the tree
     */
    override fun place(
        changedBlocks: MutableSet<BlockPos>,
        world: IWorldGenerationReader,
        random: Random,
        pos: BlockPos,
        boundingBox: MutableBoundingBox
    ): Boolean {
        // Generate the trunk and root blocks
        val topOfTrunk = generateBase(changedBlocks, world, pos, random, boundingBox)
        // Generate branches and leaves
        generateBranches(changedBlocks, world, topOfTrunk, random, boundingBox)
        // True since the tree grew
        return true
    }

    /**
     * Generates the mangrove tree base
     *
     * @param changedBlocks The set of log blocks
     * @param world The world to generate the tree in
     * @param pos The position to generate at
     * @param random The RNG object used to generate the tree
     * @param boundingBox The Bounding box to generate in
     * @return The position of the top of the trunk where leaves should gen at
     */
    private fun generateBase(
        changedBlocks: MutableSet<BlockPos>,
        world: IWorldGenerationReader,
        pos: BlockPos,
        random: Random,
        boundingBox: MutableBoundingBox
    ): BlockPos {
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
                .offset(xOffsetDirection, xDistanceFromTrunk)
                .offset(zOffsetDirection, zDistanceFromTrunk)

            // Iterate from the current height up until we reach trunk height
            for (j in 0 until heightBeforeTrunk) {
                // If our current distance to the center of the trunk is non-zero have a 50% chance to move towards the center
                if (xDistanceFromTrunk != 0 && random.nextBoolean()) {
                    currentPos = currentPos.offset(xOffsetDirection.opposite)
                    xDistanceFromTrunk--
                }
                if (zDistanceFromTrunk != 0 && random.nextBoolean()) {
                    currentPos = currentPos.offset(zOffsetDirection.opposite)
                    zDistanceFromTrunk--
                }

                // Add a log at the current position
                setLogState(changedBlocks, world, currentPos, MANGROVE_LOG_UP, boundingBox)

                // Have a 1/10 chance to generate an extra log one block up or down
                if (random.nextDouble() < 0.1) {
                    setLogState(
                        changedBlocks,
                        world,
                        if (random.nextBoolean()) currentPos.up() else currentPos.down(),
                        MANGROVE_LOG_UP,
                        boundingBox
                    )
                }

                // Always move up each iteration
                currentPos = currentPos.up()
            }

            // If we reached the top of the trunk before being all the way in generate horizontal logs all the way to the center
            while (xDistanceFromTrunk > 0 || zDistanceFromTrunk > 0) {
                // If we need to close the x distance do so
                if (xDistanceFromTrunk != 0) {
                    currentPos = currentPos.offset(xOffsetDirection.opposite)
                    xDistanceFromTrunk--
                }

                // If we need to close the z distance do so
                if (zDistanceFromTrunk != 0) {
                    currentPos = currentPos.offset(zOffsetDirection.opposite)
                    zDistanceFromTrunk--
                }

                // Set the block to log
                setLogState(changedBlocks, world, currentPos, MANGROVE_LOG_UP, boundingBox)
            }
        }

        // Now generate a somewhat straight trunk 6-9 blocks tall
        val trunkHeight = random.nextInt(4) + 6

        // Compute the current top of the trunk
        var currentPos = pos.up(heightBeforeTrunk)

        // Begin working upwards with a low chance to lean sideways
        for (i in 0 until trunkHeight) {
            // 10% chance to lean in a horizontal direction
            if (random.nextDouble() < 0.1) {
                currentPos = currentPos.offset(Direction.byHorizontalIndex(random.nextInt(4)))
            }

            // Set the block to log
            setLogState(changedBlocks, world, currentPos, MANGROVE_LOG_UP, boundingBox)

            // Advance up the trunk
            currentPos = currentPos.up()
        }
        return currentPos
    }

    /**
     * Generates branches and leaves off of the mangrove trunk
     *
     * @param changedBlocks The set of log blocks
     * @param world The world to generate branches in
     * @param topOfTrunk The top of the trunk
     * @param random The random object to generate with
     * @param boundingBox The bounding box of the tree
     */
    private fun generateBranches(
        changedBlocks: MutableSet<BlockPos>,
        world: IWorldGenerationReader,
        topOfTrunk: BlockPos,
        random: Random,
        boundingBox: MutableBoundingBox
    ) {
        // Create a leaf cluster at the top of the trunk
        generateLeafCluster(changedBlocks, world, topOfTrunk, random, boundingBox)
        // 3 to 5 branches
        val numBranches = random.nextInt(3) + 3
        for (i in 0 until numBranches) {
            // Compute a random branch direction (one in x and one in z)
            val branchDir1 = Direction.byHorizontalIndex(random.nextInt(4))
            val branchDir2 = if (random.nextBoolean()) branchDir1.rotateY() else branchDir1.rotateYCCW()
            // Compute a random branch length 3-7 blocks long
            val branchLength = random.nextInt(5) + 3
            // Grab the starting block pos to create the branch off of
            var currentBranchPos = topOfTrunk.offset(if (random.nextBoolean()) branchDir1 else branchDir2)
            // Iterate branch length number of times to create the logs required
            for (j in 0 until branchLength) {
                // Create a log block
                setLogState(changedBlocks, world, currentBranchPos, MANGROVE_LOG_UP, boundingBox)
                // 10% chance to create a leaf cluster along the way
                if (random.nextDouble() < 0.1) {
                    generateLeafCluster(changedBlocks, world, currentBranchPos, random, boundingBox)
                }
                // Move our position outwards by either moving in the dir1 or dir2 direction
                currentBranchPos = currentBranchPos.offset(if (random.nextBoolean()) branchDir1 else branchDir2)
                // 20% chance for our branch to move upwards
                if (random.nextDouble() < 0.2) {
                    currentBranchPos = currentBranchPos.up()
                }
            }
            // End the branch with a leaf cluster
            generateLeafCluster(changedBlocks, world, currentBranchPos, random, boundingBox)
        }
    }

    /**
     * Creates a leaf cluster at a given location in the world
     *
     * @param changedBlocks The set of log blocks
     * @param world The world to create the leaf cluster in
     * @param location The location to create the cluster at
     * @param random The random object to use
     * @param boundingBox The bounding box of the tree
     */
    private fun generateLeafCluster(
        changedBlocks: MutableSet<BlockPos>,
        world: IWorldGenerationReader,
        location: BlockPos,
        random: Random,
        boundingBox: MutableBoundingBox
    ) {
        // Set the center to a mangrove log
        setLogState(changedBlocks, world, location, MANGROVE_LOG_UP, boundingBox)
        // Leaf clusters will be 3 blocks tall and 5 wide
        for (x in -2..2) {
            for (y in -1..1) {
                for (z in -2..2) { // Grab the leaf block's position
                    val leafPos = location.add(x, y, z)
                    // Make the leaf clusters "circular" by checking distance to the center
                    if (sqrt(leafPos.distanceSq(location)) < (if (y == 0) 2.5 else 2.0)) {
                        // 10% chance to just ignore the leaf block
                        if (random.nextDouble() < 0.9) {
                            // 3% chance to create a 'vine' of hanging leaf blocks
                            if (random.nextDouble() < 0.03) {
                                // Create a log at the base of the leaf block
                                setLogState(changedBlocks, world, leafPos, MANGROVE_LOG_UP, boundingBox)
                                // Create a random length 'vine' 2-4 blocks long
                                val vineLength = random.nextInt(3) + 2
                                // Start at 1 so we don't try and replace our log block
                                for (i in 1..vineLength) {
                                    setLogState(changedBlocks, world, leafPos.down(i), MANGROVE_LEAVES, boundingBox)
                                }
                            } else {
                                setLogState(changedBlocks, world, leafPos, MANGROVE_LEAVES, boundingBox)
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        // Reference to the mangrove log pointing upwards
        private val MANGROVE_LOG_UP = ModBlocks.MANGROVE.defaultState.with(LogBlock.AXIS, Direction.Axis.Y)

        // Reference to the mangrove leaf block to place
        private val MANGROVE_LEAVES = ModBlocks.MANGROVE_LEAVES.defaultState
    }
}