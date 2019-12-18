package com.davidm1a2.afraidofthedark.common.block.mangrove

import com.davidm1a2.afraidofthedark.common.block.core.AOTDSapling
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.BlockLeaves
import net.minecraft.block.BlockLog
import net.minecraft.block.BlockLog.EnumAxis
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*
import kotlin.math.sqrt

/**
 * Block representing a mangrove sapling
 *
 * @constructor initializes the sapling with a name
 */
class BlockMangroveSapling : AOTDSapling("mangrove_sapling")
{
    /**
     * Causes the tree to grow. Uses a custom tree generation algorithm
     *
     * @param world  The world the sapling is growing in
     * @param pos    The position of the sapling
     * @param state  The current state of the sapling
     * @param random The random object to grow the tree with
     */
    override fun causeTreeToGrow(world: World, pos: BlockPos, state: IBlockState, random: Random)
    {
        // Clear the sapling block
        world.setBlockState(pos, Blocks.AIR.defaultState)
        // Generate the trunk and root blocks
        val topOfTrunk = generateBase(world, pos, random)
        // Generate branches and leaves
        generateBranches(world, topOfTrunk, random)
    }

    /**
     * Generates the mangrove tree base
     *
     * @param world The world to generate the tree in
     * @param pos The position to generate at
     * @param random The RNG object used to generate the tree
     * @return The position of the top of the trunk where leaves should gen at
     */
    private fun generateBase(world: World, pos: BlockPos, random: Random): BlockPos
    {
        // The height to reach before the trunk starts is between 4 and 7 blocks
        val heightBeforeTrunk = random.nextInt(4) + 4

        // The number of roots coming off of the trunk base are 4 - 8
        val numRoots = random.nextInt(5) + 4

        // Iterate num roots times to generate that many roots
        for (i in 0 until numRoots)
        {
            // Pick a random direction to generate a root in
            val xOffsetDirection = if (random.nextBoolean()) EnumFacing.NORTH else EnumFacing.SOUTH
            val zOffsetDirection = if (random.nextBoolean()) EnumFacing.EAST else EnumFacing.WEST

            // Let the root start at the sapling height 3-5 blocks away
            var xDistanceFromTrunk = random.nextInt(3) + 3
            var zDistanceFromTrunk = random.nextInt(3) + 3

            // The current pos of the root block being placed
            var currentPos = pos
                .offset(xOffsetDirection, xDistanceFromTrunk)
                .offset(zOffsetDirection, zDistanceFromTrunk)

            // Iterate from the current height up until we reach trunk height
            for (j in 0 until heightBeforeTrunk)
            {
                // If our current distance to the center of the trunk is non-zero have a 50% chance to move towards the center
                if (xDistanceFromTrunk != 0 && random.nextBoolean())
                {
                    currentPos = currentPos.offset(xOffsetDirection.opposite)
                    xDistanceFromTrunk--
                }
                if (zDistanceFromTrunk != 0 && random.nextBoolean())
                {
                    currentPos = currentPos.offset(zOffsetDirection.opposite)
                    zDistanceFromTrunk--
                }

                // Add a log at the current position
                setBlockIfPossible(world, currentPos, MANGROVE_LOG_UP)

                // Have a 1/10 chance to generate an extra log one block up or down
                if (random.nextDouble() < 0.1)
                {
                    setBlockIfPossible(world, if (random.nextBoolean()) currentPos.up() else currentPos.down(), MANGROVE_LOG_UP)
                }

                // Always move up each iteration
                currentPos = currentPos.up()
            }

            // If we reached the top of the trunk before being all the way in generate horizontal logs all the way to the center
            while (xDistanceFromTrunk > 0 || zDistanceFromTrunk > 0)
            {
                // If we need to close the x distance do so
                if (xDistanceFromTrunk != 0)
                {
                    currentPos = currentPos.offset(xOffsetDirection.opposite)
                    xDistanceFromTrunk--
                }

                // If we need to close the z distance do so
                if (zDistanceFromTrunk != 0)
                {
                    currentPos = currentPos.offset(zOffsetDirection.opposite)
                    zDistanceFromTrunk--
                }

                // Set the block to log
                setBlockIfPossible(world, currentPos, MANGROVE_LOG_UP)
            }
        }

        // Now generate a somewhat straight trunk 6-9 blocks tall
        val trunkHeight = random.nextInt(4) + 6

        // Compute the current top of the trunk
        var currentPos = pos.up(heightBeforeTrunk)

        // Begin working upwards with a low chance to lean sideways
        for (i in 0 until trunkHeight)
        {
            // 10% chance to lean in a horizontal direction
            if (random.nextDouble() < 0.1)
            {
                currentPos = currentPos.offset(EnumFacing.HORIZONTALS[random.nextInt(EnumFacing.HORIZONTALS.size)])
            }

            // Set the block to log
            setBlockIfPossible(world, currentPos, MANGROVE_LOG_UP)

            // Advance up the trunk
            currentPos = currentPos.up()
        }
        return currentPos
    }

    /**
     * Generates branches and leaves off of the mangrove trunk
     *
     * @param world The world to generate branches in
     * @param topOfTrunk The top of the trunk
     * @param random The random object to generate with
     */
    private fun generateBranches(world: World, topOfTrunk: BlockPos, random: Random)
    {
        // Create a leaf cluster at the top of the trunk
        generateLeafCluster(world, topOfTrunk, random)
        // 3 to 5 branches
        val numBranches = random.nextInt(3) + 3
        for (i in 0 until numBranches)
        {
            // Compute a random branch direction (one in x and one in z)
            val branchDir1 = EnumFacing.HORIZONTALS[random.nextInt(EnumFacing.HORIZONTALS.size)]
            val branchDir2 = if (random.nextBoolean()) branchDir1.rotateY() else branchDir1.rotateYCCW()
            // Compute a random branch length 3-7 blocks long
            val branchLength = random.nextInt(5) + 3
            // Grab the starting block pos to create the branch off of
            var currentBranchPos = topOfTrunk.offset(if (random.nextBoolean()) branchDir1 else branchDir2)
            // Iterate branch length number of times to create the logs required
            for (j in 0 until branchLength)
            {
                // Create a log block
                setBlockIfPossible(world, currentBranchPos, MANGROVE_LOG_UP)
                // 10% chance to create a leaf cluster along the way
                if (random.nextDouble() < 0.1)
                {
                    generateLeafCluster(world, currentBranchPos, random)
                }
                // Move our position outwards by either moving in the dir1 or dir2 direction
                currentBranchPos = currentBranchPos.offset(if (random.nextBoolean()) branchDir1 else branchDir2)
                // 20% chance for our branch to move upwards
                if (random.nextDouble() < 0.2)
                {
                    currentBranchPos = currentBranchPos.up()
                }
            }
            // End the branch with a leaf cluster
            generateLeafCluster(world, currentBranchPos, random)
        }
    }

    /**
     * Creates a leaf cluster at a given location in the world
     *
     * @param world The world to create the leaf cluster in
     * @param location The location to create the cluster at
     * @param random The random object to use
     */
    private fun generateLeafCluster(world: World, location: BlockPos, random: Random)
    {
        // Set the center to a mangrove log
        setBlockIfPossible(world, location, MANGROVE_LOG_UP)
        // Leaf clusters will be 3 blocks tall and 5 wide
        for (x in -2..2)
        {
            for (y in -1..1)
            {
                for (z in -2..2)
                { // Grab the leaf block's position
                    val leafPos = location.add(x, y, z)
                    // Make the leaf clusters "circular" by checking distance to the center
                    if (sqrt(leafPos.distanceSq(location)) < (if (y == 0) 2.5 else 2.0))
                    {
                        // 10% chance to just ignore the leaf block
                        if (random.nextDouble() < 0.9)
                        {
                            // 3% chance to create a 'vine' of hanging leaf blocks
                            if (random.nextDouble() < 0.03)
                            {
                                // Create a log at the base of the leaf block
                                setBlockIfPossible(world, leafPos, MANGROVE_LOG_UP)
                                // Create a random length 'vine' 2-4 blocks long
                                val vineLength = random.nextInt(3) + 2
                                // Start at 1 so we don't try and replace our log block
                                for (i in 1..vineLength)
                                {
                                    setBlockIfPossible(world, leafPos.down(i), MANGROVE_LEAVES)
                                }
                            }
                            else
                            {
                                setBlockIfPossible(world, leafPos, MANGROVE_LEAVES)
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Sets a block to a given block state if nothing is in the way
     *
     * @param world      The world to set the block in
     * @param location   The location to place the block
     * @param blockState THe state to place at the position
     */
    private fun setBlockIfPossible(world: World, location: BlockPos, blockState: IBlockState)
    {
        // Grab the current block at the position
        val current = world.getBlockState(location)
        // Test if we can overwrite the block
        if (REPLACEABLE_BLOCKS.contains(current.block))
        {
            world.setBlockState(location, blockState)
        }
    }

    companion object
    {
        // Reference to the mangrove log pointing upwards
        private val MANGROVE_LOG_UP = ModBlocks.MANGROVE.defaultState.withProperty(BlockLog.LOG_AXIS, EnumAxis.Y)
        // Reference to the mangrove leaf block to place
        private val MANGROVE_LEAVES = ModBlocks.MANGROVE_LEAVES.defaultState
            .withProperty(BlockLeaves.DECAYABLE, true)
            .withProperty(BlockLeaves.CHECK_DECAY, false)

        // A set of blocks that can be replaced by wood upon growing
        private val REPLACEABLE_BLOCKS = setOf(
            Blocks.TALLGRASS,
            Blocks.AIR,
            Blocks.LEAVES,
            Blocks.LEAVES2,
            Blocks.WATER,
            Blocks.FLOWING_WATER,
            Blocks.GRASS,
            ModBlocks.MANGROVE_LEAVES
        )
    }
}