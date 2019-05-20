package com.DavidM1A2.afraidofthedark.common.block.mangrove;

import com.DavidM1A2.afraidofthedark.common.block.core.AOTDSapling;
import com.DavidM1A2.afraidofthedark.common.constants.ModBlocks;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;
import java.util.Set;

/**
 * Block representing a mangrove sapling
 */
public class BlockMangroveSapling extends AOTDSapling
{
    // Reference to the mangrove log pointing upwards
    private static final IBlockState MANGROVE_LOG_UP = ModBlocks.MANGROVE.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y);
    // Reference to the mangrove leaf block to place
    private static final IBlockState MANGROVE_LEAVES = ModBlocks.MANGROVE_LEAVES.getDefaultState()
            .withProperty(BlockLeaves.DECAYABLE, true)
            .withProperty(BlockLeaves.CHECK_DECAY, false);

    // A set of blocks that can be replaced by wood upon growing
    private static final Set<Block> REPLACEABLE_BLOCKS = ImmutableSet.<Block>builder()
            .add(Blocks.TALLGRASS)
            .add(Blocks.AIR)
            .add(Blocks.LEAVES)
            .add(Blocks.LEAVES2)
            .add(Blocks.WATER)
            .add(Blocks.FLOWING_WATER)
            .add(Blocks.GRASS)
            .add(ModBlocks.MANGROVE_LEAVES)
            .build();

    /**
     * Constructor initializes the sapling with a name
     */
    public BlockMangroveSapling()
    {
        super("mangrove_sapling");
    }

    /**
     * Causes the tree to grow. Uses a custom tree generation algorithm
     *
     * @param world  The world the sapling is growing in
     * @param pos    The position of the sapling
     * @param state  The current state of the sapling
     * @param random The random object to grow the tree with
     */
    @Override
    public void causeTreeToGrow(World world, BlockPos pos, IBlockState state, Random random)
    {
        // Clear the sapling block
        world.setBlockState(pos, Blocks.AIR.getDefaultState());

        // Generate the trunk and root blocks
        BlockPos topOfTrunk = generateBase(world, pos, random);

        // Generate branches and leaves
        generateBranches(world, topOfTrunk, random);
    }

    /**
     * Generates the mangrove tree base
     *
     * @param world The world to generate the tree in
     * @param pos The position to generate at
     * @param random The RNG object used to generate the tree
     * @return The position of the top of the trunk where leaves should gen at
     */
    private BlockPos generateBase(World world, BlockPos pos, Random random)
    {
        // The height to reach before the trunk starts is between 4 and 7 blocks
        int heightBeforeTrunk = random.nextInt(4) + 4;
        // The number of roots coming off of the trunk base are 4 - 8
        int numRoots = random.nextInt(5) + 4;

        // Iterate num roots times to generate that many roots
        for (int i = 0; i < numRoots; i++)
        {
            // Pick a random direction to generate a root in
            EnumFacing xOffsetDirection = random.nextBoolean() ? EnumFacing.NORTH : EnumFacing.SOUTH;
            EnumFacing zOffsetDirection = random.nextBoolean() ? EnumFacing.EAST : EnumFacing.WEST;
            // Let the root start at the sapling height 3-5 blocks away
            int xDistanceFromTrunk = random.nextInt(3) + 3;
            int zDistanceFromTrunk = random.nextInt(3) + 3;
            // THe current pos of the root block being placed
            BlockPos currentPos = pos
                    .offset(xOffsetDirection, xDistanceFromTrunk)
                    .offset(zOffsetDirection, zDistanceFromTrunk);
            // Iterate from the current height up until we reach trunk height
            for (int j = 0; j < heightBeforeTrunk; j++)
            {
                // If our current distance to the center of the trunk is non-zero have a 50% chance to move towards the center
                if (xDistanceFromTrunk != 0 && random.nextBoolean())
                {
                    currentPos = currentPos.offset(xOffsetDirection.getOpposite());
                    xDistanceFromTrunk--;
                }
                if (zDistanceFromTrunk != 0 && random.nextBoolean())
                {
                    currentPos = currentPos.offset(zOffsetDirection.getOpposite());
                    zDistanceFromTrunk--;
                }

                // Add a log at the current position
                this.setBlockIfPossible(world, currentPos, MANGROVE_LOG_UP);

                // Have a 1/10 chance to generate an extra log one block up or down
                if (random.nextDouble() < 0.1)
                {
                    this.setBlockIfPossible(world, random.nextBoolean() ? currentPos.up() : currentPos.down(), MANGROVE_LOG_UP);
                }

                // Always move up each iteration
                currentPos = currentPos.up();
            }
            // If we reached the top of the trunk before being all the way in generate horizontal logs all the way to the center
            while (xDistanceFromTrunk > 0 || zDistanceFromTrunk > 0)
            {
                // If we need to close the x distance do so
                if (xDistanceFromTrunk != 0)
                {
                    currentPos = currentPos.offset(xOffsetDirection.getOpposite());
                    xDistanceFromTrunk--;
                }
                // If we need to close the z distance do so
                if (zDistanceFromTrunk != 0)
                {
                    currentPos = currentPos.offset(zOffsetDirection.getOpposite());
                    zDistanceFromTrunk--;
                }
                // Set the block to log
                this.setBlockIfPossible(world, currentPos, MANGROVE_LOG_UP);
            }
        }

        // Now generate a somewhat straight trunk 4-6 blocks tall
        int trunkHeight = random.nextInt(3) + 4;
        // Compute the current top of the trunk
        BlockPos currentPos = pos.up(heightBeforeTrunk);
        // Begin working upwards with a low chance to lean sideways
        for (int i = 0; i < trunkHeight; i++)
        {
            // 10% chance to lean in a horizontal direction
            if (random.nextDouble() < 0.1)
            {
                currentPos = currentPos.offset(EnumFacing.HORIZONTALS[random.nextInt(EnumFacing.HORIZONTALS.length)]);
            }
            // Set the block to log
            this.setBlockIfPossible(world, currentPos, MANGROVE_LOG_UP);
            // Advance up the trunk
            currentPos = currentPos.up();
        }

        return currentPos;
    }

    /**
     * Generates branches and leaves off of the mangrove trunk
     *
     * @param world The world to generate branches in
     * @param topOfTrunk The top of the trunk
     * @param random The random object to generate with
     */
    private void generateBranches(World world, BlockPos topOfTrunk, Random random)
    {
        // Create a leaf cluster at the top of the trunk
        this.generateLeafCluster(world, topOfTrunk, random);

        // 3 to 5 branches
        int numBranches = random.nextInt(3) + 3;
        for (int i = 0; i < numBranches; i++)
        {
            // Compute a random branch direction (one in x and one in z)
            EnumFacing branchDir1 = EnumFacing.HORIZONTALS[random.nextInt(EnumFacing.HORIZONTALS.length)];
            EnumFacing branchDir2 = random.nextBoolean() ? branchDir1.rotateY() : branchDir1.rotateYCCW();

            // Compute a random branch length 3-7 blocks long
            int branchLength = random.nextInt(5) + 3;
            // Grab the starting block pos to create the branch off of
            BlockPos currentBranchPos = topOfTrunk.offset(random.nextBoolean() ? branchDir1 : branchDir2);
            // Iterate branch length number of times to create the logs required
            for (int j = 0; j < branchLength; j++)
            {
                // Create a log block
                this.setBlockIfPossible(world, currentBranchPos, MANGROVE_LOG_UP);

                // 10% chance to create a leaf cluster along the way
                if (random.nextDouble() < 0.1)
                {
                    this.generateLeafCluster(world, currentBranchPos, random);
                }

                // Move our position outwards by either moving in the dir1 or dir2 direction
                currentBranchPos = currentBranchPos.offset(random.nextBoolean() ? branchDir1 : branchDir2);

                // 20% chance for our branch to move upwards
                if (random.nextDouble() < 0.2)
                {
                    currentBranchPos = currentBranchPos.up();
                }
            }

            // End the branch with a leaf cluster
            this.generateLeafCluster(world, currentBranchPos, random);
        }
    }

    /**
     * Creates a leaf cluster at a given location in the world
     *
     * @param world The world to create the leaf cluster in
     * @param location The location to create the cluster at
     * @param random The random object to use
     */
    private void generateLeafCluster(World world, BlockPos location, Random random)
    {
        // Set the center to a mangrove log
        this.setBlockIfPossible(world, location, MANGROVE_LOG_UP);

        // Leaf clusters will be 3 blocks tall and 5 wide
        for (int x = -2; x <= 2; x++)
        {
            for (int y = -1; y <= 1; y++)
            {
                for (int z = -2; z <= 2; z++)
                {
                    // Grab the leaf block's position
                    BlockPos leafPos = location.add(x, y, z);
                    // Make the leaf clusters "circular" by checking distance to the center
                    if (Math.sqrt(leafPos.distanceSq(location)) < (y == 0 ? 2.5 : 2))
                    {
                        // 10% chance to just ignore the leaf block
                        if (random.nextDouble() < 0.9)
                        {
                            // 3% chance to create a 'vine' of hanging leaf blocks
                            if (random.nextDouble() < 0.03)
                            {
                                // Create a log at the base of the leaf block
                                this.setBlockIfPossible(world, leafPos, MANGROVE_LOG_UP);
                                // Create a random length 'vine' 2-4 blocks long
                                int vineLength = random.nextInt(3) + 2;
                                // Start at 1 so we don't try and replace our log block
                                for (int i = 1; i <= vineLength; i++)
                                {
                                    this.setBlockIfPossible(world, leafPos.down(i), MANGROVE_LEAVES);
                                }
                            }
                            // Just set the leaf block
                            else
                            {
                                this.setBlockIfPossible(world, leafPos, MANGROVE_LEAVES);
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
    private void setBlockIfPossible(World world, BlockPos location, IBlockState blockState)
    {
        // Grab the current block at the position
        IBlockState current = world.getBlockState(location);
        // Test if we can overwrite the block
        if (REPLACEABLE_BLOCKS.contains(current.getBlock()))
        {
            world.setBlockState(location, blockState);
        }
    }
}
