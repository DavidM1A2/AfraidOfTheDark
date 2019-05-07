package com.DavidM1A2.afraidofthedark.common.biomes.extras;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.IPlantable;

import java.util.List;
import java.util.Random;

/**
 * This is a custom implementation of net.minecraft.world.gen.feature.WorldGenBigTree that lets us customize certain features
 */
public class AOTDWorldGenBigTree extends WorldGenAbstractTree
{
    ///
    /// Variables computed from the options
    ///

    // The random object
    private Random rand;
    // The world object this tree is in
    private World world;
    // The base position of the tree
    private BlockPos basePos = BlockPos.ORIGIN;
    // The maximum height the tree can have
    private int heightLimit;
    // The actual height of the tree
    private int height;
    // A list of wood nodes to be surrounded with leaves
    private List<AOTDWorldGenBigTree.FoliageCoordinates> foliageCoords;

    ///
    /// Variables set from the user. Unfortunately setting these parameters too large causes the tree to be bigger than 16x16 which is not allowed through standard
    /// minecraft world generation due to run-away world generation
    ///

    // How much room is reserved for leaves at the top
    private double heightAttenuation = 0.618D;
    // The slope of the branches as they come off of the trunk
    private double branchSlope = 0.381D;
    // The width:height scale of the tree
    private double scaleWidth = 1.0D;
    // The leaf density to be used by the tree
    private double leafDensity = 1.0D;
    // How thick the trunk should be
    private int trunkSize = 1;
    // The maximum height of the tree
    private int heightLimitMax = 16;
    // The maximum distance the leaves can be from the tree
    private int leafDistanceLimit = 4;
    // The percent leaf block integrity
    private double leafIntegrity = 1.0;
    // The block to use as wood
    private IBlockState wood;
    // The block to use as leaves
    private IBlockState leaves;

    /**
     * Constructor initializes the wood and leaf types as well as if this tree should notify clients that it was grown
     *
     * @param notify If the tree should notify users that it has grown
     * @param wood   The wood type to use
     * @param leaves The leaf type to use
     */
    public AOTDWorldGenBigTree(boolean notify, IBlockState wood, IBlockState leaves)
    {
        super(notify);
        this.wood = wood;
        this.leaves = leaves;
    }

    /**
     * Generates the tree at a given location
     *
     * @param world    The world to build the tree in
     * @param rand     The random object to build the tree with
     * @param position The position of the tree
     * @return True if the tree was created, false otherwise
     */
    public boolean generate(World world, Random rand, BlockPos position)
    {
        this.world = world;
        // Fix chunk overflowing
        this.basePos = position;
        // Create a new random num generator for this tree
        this.rand = new Random(rand.nextLong());

        // If the height limit is 0 set it to a random value
        if (this.heightLimit == 0)
        {
            this.heightLimit = 5 + this.rand.nextInt(this.heightLimitMax);
        }

        // If the location is not valid, return false because the tree did not generate
        if (!this.validTreeLocation())
        {
            //Fix vanilla Mem leak, holds latest world
            this.world = null;
            return false;
        }
        // if the location is valid, generate the tree
        else
        {
            this.generateLeafNodeList();
            this.generateLeaves();
            this.generateTrunk();
            this.generateLeafNodeBases();
            //Fix vanilla Mem leak, holds latest world
            this.world = null;
            return true;
        }
    }

    /**
     * Setter for decoration defaults, in our case max leaf distance is set to 5
     */
    public void setDecorationDefaults()
    {
        this.leafDistanceLimit = 5;
    }

    ///
    /// Everything after here is straight from WorldGenBigTree
    ///

    /**
     * Returns a boolean indicating whether or not the current location for the tree, spanning basePos to to the height
     * limit, is valid.
     *
     * @return True if the location is valid, false otherwise
     */
    private boolean validTreeLocation()
    {
        // Grab the position of the block below the stump
        BlockPos posBelowTree = this.basePos.down();
        // Get the state of the block below the stump
        IBlockState blockBelowTreeState = this.world.getBlockState(posBelowTree);
        // Test if the block can sustain tree growth
        boolean isSoil = blockBelowTreeState.getBlock().canSustainPlant(blockBelowTreeState, this.world, posBelowTree, EnumFacing.UP, (IPlantable) Blocks.SAPLING);

        // If it cannot return false
        if (!isSoil)
        {
            return false;
        }
        // If it can, test how much room we have
        else
        {
            // Count the number of blocks above the base available for the trunk
            int blocksAvailableAbove = this.checkBlockLine(this.basePos, this.basePos.up(this.heightLimit - 1));

            // If it's -1 we've got enough room
            if (blocksAvailableAbove == -1)
            {
                return true;
            }
            // If it's less than 6 we don't have enough room
            else if (blocksAvailableAbove < 6)
            {
                return false;
            }
            // If it's not enough for the full tree but bigger than 6 then use that height as the max
            else
            {
                this.heightLimit = blocksAvailableAbove;
                return true;
            }
        }
    }

    /**
     * Generates a list of leaf nodes for the tree, to be populated by generate Leaves.
     */
    private void generateLeafNodeList()
    {
        // Compute the height of the tree based on the limit
        this.height = (int) (this.heightLimit * this.heightAttenuation);

        // If the height is greater than the limit we use the limit as the max
        if (this.height >= this.heightLimit)
        {
            this.height = this.heightLimit - 1;
        }

        int leafDensityConverted = (int) (1.382D + Math.pow(this.leafDensity * (double) this.heightLimit / 13.0D, 2.0D));

        if (leafDensityConverted < 1)
        {
            leafDensityConverted = 1;
        }

        int maxHeight = this.basePos.getY() + this.height;
        int k = this.heightLimit - this.leafDistanceLimit;
        this.foliageCoords = Lists.newArrayList();
        this.foliageCoords.add(new FoliageCoordinates(this.basePos.up(k), maxHeight));

        for (; k >= 0; k--)
        {
            float f = this.layerSize(k);

            if (f >= 0.0F)
            {
                for (int l = 0; l < leafDensityConverted; l++)
                {
                    double d0 = this.scaleWidth * (double) f * ((double) this.rand.nextFloat() + 0.328D);
                    double d1 = (double) (this.rand.nextFloat() * 2.0F) * Math.PI;
                    double d2 = d0 * Math.sin(d1) + 0.5D;
                    double d3 = d0 * Math.cos(d1) + 0.5D;
                    BlockPos blockpos = this.basePos.add(d2, (double) (k - 1), d3);
                    BlockPos blockpos1 = blockpos.up(this.leafDistanceLimit);

                    if (this.checkBlockLine(blockpos, blockpos1) == -1)
                    {
                        int i1 = this.basePos.getX() - blockpos.getX();
                        int j1 = this.basePos.getZ() - blockpos.getZ();
                        double d4 = (double) blockpos.getY() - Math.sqrt((double) (i1 * i1 + j1 * j1)) * this.branchSlope;
                        int k1 = d4 > (double) maxHeight ? maxHeight : (int) d4;
                        BlockPos blockpos2 = new BlockPos(this.basePos.getX(), k1, this.basePos.getZ());

                        if (this.checkBlockLine(blockpos2, blockpos) == -1)
                        {
                            this.foliageCoords.add(new FoliageCoordinates(blockpos, blockpos2.getY()));
                        }
                    }
                }
            }
        }
    }

    /**
     * Creates a cross of leaves at a given location
     *
     * @param pos       The position to create the cross section at
     * @param leafSize  The size the cross section should be
     * @param leafBlock The block to use as leaves
     */
    private void crossSection(BlockPos pos, float leafSize, IBlockState leafBlock)
    {
        int i = (int) (leafSize + 0.618D);

        for (int j = -i; j <= i; ++j)
        {
            for (int k = -i; k <= i; ++k)
            {
                if (Math.pow(Math.abs(j) + 0.5D, 2.0D) + Math.pow(Math.abs(k) + 0.5D, 2.0D) <= leafSize * leafSize)
                {
                    BlockPos blockpos = pos.add(j, 0, k);
                    IBlockState state = this.world.getBlockState(blockpos);

                    if ((state.getBlock().isAir(state, world, blockpos) || state.getBlock().isLeaves(state, world, blockpos)) && this.rand.nextDouble() < leafIntegrity)
                    {
                        this.setBlockAndNotifyAdequately(this.world, blockpos, leafBlock);
                    }
                }
            }
        }
    }

    /**
     * Gets the rough size of a layer of the tree.
     */
    private float layerSize(int y)
    {
        if (y < this.heightLimit * 0.3F)
        {
            return -1.0F;
        }
        else
        {
            float f = this.heightLimit / 2.0F;
            float f1 = f - y;
            float f2 = MathHelper.sqrt(f * f - f1 * f1);

            if (f1 == 0.0F)
            {
                f2 = f;
            }
            else if (Math.abs(f1) >= f)
            {
                return 0.0F;
            }

            return f2 * 0.5F;
        }
    }

    /**
     * Get the size of a leaf cluster
     *
     * @param leafDistance The leaf distance to grow
     * @return The distance the leaves should go outwards
     */
    private float leafSize(int leafDistance)
    {
        if (leafDistance >= 0 && leafDistance < this.leafDistanceLimit)
        {
            return leafDistance != 0 && leafDistance != this.leafDistanceLimit - 1 ? 3.0F : 2.0F;
        }
        else
        {
            return -1.0F;
        }
    }

    /**
     * Creates a tree limb given a start and end position
     *
     * @param startBlockPos The tree log start position
     * @param endBlockPos   The tree log end position
     */
    private void limb(BlockPos startBlockPos, BlockPos endBlockPos)
    {
        BlockPos blockpos = endBlockPos.add(-startBlockPos.getX(), -startBlockPos.getY(), -startBlockPos.getZ());
        int i = this.getGreatestDistance(blockpos);
        float f = (float) blockpos.getX() / (float) i;
        float f1 = (float) blockpos.getY() / (float) i;
        float f2 = (float) blockpos.getZ() / (float) i;

        for (int j = 0; j <= i; ++j)
        {
            BlockPos blockpos1 = startBlockPos.add((double) (0.5F + (float) j * f), (double) (0.5F + (float) j * f1), (double) (0.5F + (float) j * f2));
            BlockLog.EnumAxis logAxis = this.getLogAxis(startBlockPos, blockpos1);
            this.setBlockAndNotifyAdequately(this.world, blockpos1, this.wood.withProperty(BlockLog.LOG_AXIS, logAxis));
        }
    }

    /**
     * Returns the absolute greatest distance in the BlockPos object.
     */
    private int getGreatestDistance(BlockPos posIn)
    {
        int i = MathHelper.abs(posIn.getX());
        int j = MathHelper.abs(posIn.getY());
        int k = MathHelper.abs(posIn.getZ());

        if (k > i && k > j)
        {
            return k;
        }
        else
        {
            return j > i ? j : i;
        }
    }

    /**
     * Computes the log axis that should be used for the placed log
     *
     * @param startBlockPos The start position of the branch
     * @param endBlockPos   The end position of the branch
     * @return The axis the log should face
     */
    private BlockLog.EnumAxis getLogAxis(BlockPos startBlockPos, BlockPos endBlockPos)
    {
        BlockLog.EnumAxis logAxis = BlockLog.EnumAxis.Y;
        int xDistance = Math.abs(endBlockPos.getX() - startBlockPos.getX());
        int zDistance = Math.abs(endBlockPos.getZ() - startBlockPos.getZ());
        int k = Math.max(xDistance, zDistance);

        if (k > 0)
        {
            if (xDistance == k)
            {
                logAxis = BlockLog.EnumAxis.X;
            }
            else
            {
                logAxis = BlockLog.EnumAxis.Z;
            }
        }

        return logAxis;
    }

    /**
     * Generates the leaf portion of the tree as specified by the leafNodes list.
     */
    private void generateLeaves()
    {
        for (FoliageCoordinates foliageCoordinates : this.foliageCoords)
            for (int i = 0; i < this.leafDistanceLimit; ++i)
                this.crossSection(foliageCoordinates.up(i), this.leafSize(i), this.leaves.withProperty(BlockLeaves.CHECK_DECAY, Boolean.FALSE));
    }

    /**
     * Places the trunk for the big tree that is being generated. Able to generate double-sized trunks by changing a
     * field that is always 1 to 2.
     */
    private void generateTrunk()
    {
        BlockPos blockpos = this.basePos;
        BlockPos blockpos1 = this.basePos.up(this.height);
        this.limb(blockpos, blockpos1);

        if (this.trunkSize == 2)
        {
            this.limb(blockpos.east(), blockpos1.east());
            this.limb(blockpos.east().south(), blockpos1.east().south());
            this.limb(blockpos.south(), blockpos1.south());
        }
    }

    /**
     * Generates additional wood blocks to fill out the bases of different leaf nodes that would otherwise degrade.
     */
    private void generateLeafNodeBases()
    {
        for (FoliageCoordinates foliageCoordinates : this.foliageCoords)
        {
            int i = foliageCoordinates.getBranchBase();
            BlockPos blockpos = new BlockPos(this.basePos.getX(), i, this.basePos.getZ());

            if (!blockpos.equals(foliageCoordinates) && (i - this.basePos.getY() >= this.heightLimit * 0.2D))
            {
                this.limb(blockpos, foliageCoordinates);
                this.setBlockAndNotifyAdequately(this.world, foliageCoordinates.up(leafDistanceLimit / 2), this.wood.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y));
            }
        }
    }

    /**
     * Checks a line of blocks in the world from the first coordinate to triplet to the second, returning the distance
     * (in blocks) before a non-air, non-leaf block is encountered and/or the end is encountered.
     *
     * @param posOne First location to check
     * @param posTwo The end or second position to check
     * @return The number of blocks that are non-air from first -> second pos
     */
    private int checkBlockLine(BlockPos posOne, BlockPos posTwo)
    {
        BlockPos blockpos = posTwo.add(-posOne.getX(), -posOne.getY(), -posOne.getZ());
        int i = this.getGreatestDistance(blockpos);
        float f = (float) blockpos.getX() / (float) i;
        float f1 = (float) blockpos.getY() / (float) i;
        float f2 = (float) blockpos.getZ() / (float) i;

        if (i == 0)
        {
            return -1;
        }
        else
        {
            for (int j = 0; j <= i; ++j)
            {
                BlockPos blockpos1 = posOne.add((double) (0.5F + (float) j * f), (double) (0.5F + (float) j * f1), (double) (0.5F + (float) j * f2));

                if (!this.isReplaceable(world, blockpos1))
                {
                    return j;
                }
            }

            return -1;
        }
    }

    public AOTDWorldGenBigTree setHeightAttenuation(double heightAttenuation)
    {
        this.heightAttenuation = heightAttenuation;
        return this;
    }

    ///
    /// Setters/Getters
    ///

    public AOTDWorldGenBigTree setBranchSlope(double branchSlope)
    {
        this.branchSlope = branchSlope;
        return this;
    }

    public AOTDWorldGenBigTree setScaleWidth(double scaleWidth)
    {
        this.scaleWidth = scaleWidth;
        return this;
    }

    public AOTDWorldGenBigTree setLeafDensity(double leafDensity)
    {
        this.leafDensity = leafDensity;
        return this;
    }

    public AOTDWorldGenBigTree setTrunkSize(int trunkSize)
    {
        this.trunkSize = trunkSize;
        return this;
    }

    public AOTDWorldGenBigTree setHeightLimitMax(int heightLimitMax)
    {
        this.heightLimitMax = heightLimitMax;
        return this;
    }

    public AOTDWorldGenBigTree setLeafIntegrity(double leafIntegrity)
    {
        this.leafIntegrity = leafIntegrity;
        return this;
    }

    private class FoliageCoordinates extends BlockPos
    {
        private final int branchBase;

        FoliageCoordinates(BlockPos pos, int branchBase)
        {
            super(pos.getX(), pos.getY(), pos.getZ());
            this.branchBase = branchBase;
        }

        int getBranchBase()
        {
            return this.branchBase;
        }
    }
}
