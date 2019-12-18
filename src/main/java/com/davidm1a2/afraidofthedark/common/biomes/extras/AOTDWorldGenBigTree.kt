package com.davidm1a2.afraidofthedark.common.biomes.extras

import net.minecraft.block.BlockLeaves
import net.minecraft.block.BlockLog
import net.minecraft.block.BlockLog.EnumAxis
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.World
import net.minecraft.world.gen.feature.WorldGenAbstractTree
import net.minecraftforge.common.IPlantable
import java.util.*
import kotlin.math.*

/**
 * This is a custom implementation of net.minecraft.world.gen.feature.WorldGenBigTree that lets us customize certain features
 *
 * @constructor initializes the wood and leaf types as well as if this tree should notify clients that it was grown
 * @param notify If the tree should notify users that it has grown
 * @param wood   The wood type to use
 * @param leaves The leaf type to use
 * @property rand The random object
 * @property world The world object this tree is in
 * @property basePos The base position of the tree
 * @property heightLimit The maximum height the tree can have
 * @property height The actual height of the tree
 * @property foliageCoords A list of wood nodes to be surrounded with leaves
 * @property heightAttenuation How much room is reserved for leaves at the top
 * @property branchSlope The slope of the branches as they come off of the trunk
 * @property scaleWidth The width:height scale of the tree
 * @property leafDensity The leaf density to be used by the tree
 * @property trunkSize How thick the trunk should be
 * @property heightLimitMax The maximum height of the tree
 * @property leafDistanceLimit The maximum distance the leaves can be from the tree
 * @property leafIntegrity The percent leaf block integrity
 */
class AOTDWorldGenBigTree(
    notify: Boolean,
    private val wood: IBlockState,
    private val leaves: IBlockState
) : WorldGenAbstractTree(notify)
{
    ///
    /// Variables computed from the options
    ///

    private lateinit var rand: Random
    private var world: World? = null
    private var basePos = BlockPos.ORIGIN
    private var heightLimit = 0
    private var height = 0
    private var foliageCoords: MutableList<FoliageCoordinates>? = null

    ///
    /// Variables set from the user. Unfortunately setting these parameters too large causes the tree to be bigger than 16x16 which is not allowed through standard
    /// minecraft world generation due to run-away world generation
    ///

    var heightAttenuation = 0.618
    var branchSlope = 0.381
    var scaleWidth = 1.0
    var leafDensity = 1.0
    var trunkSize = 1
    var heightLimitMax = 16
    var leafDistanceLimit = 4
    var leafIntegrity = 1.0

    /**
     * Generates the tree at a given location
     *
     * @param world    The world to build the tree in
     * @param rand     The random object to build the tree with
     * @param position The position of the tree
     * @return True if the tree was created, false otherwise
     */
    override fun generate(world: World, rand: Random, position: BlockPos): Boolean
    {
        this.world = world
        // Fix chunk overflowing
        basePos = position
        // Create a new random num generator for this tree
        this.rand = Random(rand.nextLong())

        // If the height limit is 0 set it to a random value
        if (heightLimit == 0)
        {
            heightLimit = 5 + this.rand.nextInt(heightLimitMax)
        }

        // If the location is not valid, return false because the tree did not generate
        return if (!validTreeLocation())
        {
            //Fix vanilla Mem leak, holds latest world
            this.world = null
            false
        }
        else
        {
            generateLeafNodeList()
            generateLeaves()
            generateTrunk()
            generateLeafNodeBases()
            // Fix vanilla Mem leak, holds latest world
            this.world = null
            true
        }
    }

    /**
     * Setter for decoration defaults, in our case max leaf distance is set to 5
     */
    override fun setDecorationDefaults()
    {
        leafDistanceLimit = 5
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
    private fun validTreeLocation(): Boolean
    {
        // Grab the position of the block below the stump
        val posBelowTree = basePos.down()
        // Get the state of the block below the stump
        val blockBelowTreeState = world!!.getBlockState(posBelowTree)
        // Test if the block can sustain tree growth
        val isSoil = blockBelowTreeState.block.canSustainPlant(blockBelowTreeState, world!!, posBelowTree, EnumFacing.UP, Blocks.SAPLING as IPlantable)
        // If it cannot return false
        return if (!isSoil)
        {
            false
        }
        else
        {
            // Count the number of blocks above the base available for the trunk
            val blocksAvailableAbove = checkBlockLine(basePos, basePos.up(heightLimit - 1))
            // If it's -1 we've got enough room
            when
            {
                blocksAvailableAbove == -1 ->
                {
                    true
                }
                blocksAvailableAbove < 6 ->
                {
                    false
                }
                else ->
                {
                    heightLimit = blocksAvailableAbove
                    true
                }
            }
        }
    }

    /**
     * Generates a list of leaf nodes for the tree, to be populated by generate Leaves.
     */
    private fun generateLeafNodeList()
    {
        // Compute the height of the tree based on the limit
        height = (heightLimit * heightAttenuation).toInt()
        // If the height is greater than the limit we use the limit as the max
        if (height >= heightLimit)
        {
            height = heightLimit - 1
        }
        var leafDensityConverted = (1.382 + (leafDensity * heightLimit.toDouble() / 13.0).pow(2.0)).toInt()
        if (leafDensityConverted < 1)
        {
            leafDensityConverted = 1
        }
        val maxHeight = basePos.y + height
        var k = heightLimit - leafDistanceLimit
        foliageCoords = mutableListOf()
        foliageCoords!!.add(FoliageCoordinates(basePos.up(k), maxHeight))
        while (k >= 0)
        {
            val f = layerSize(k)
            if (f >= 0.0f)
            {
                for (l in 0 until leafDensityConverted)
                {
                    val d0 = scaleWidth * f.toDouble() * (rand.nextFloat().toDouble() + 0.328)
                    val d1 = (rand.nextFloat() * 2.0f).toDouble() * Math.PI
                    val d2 = d0 * sin(d1) + 0.5
                    val d3 = d0 * cos(d1) + 0.5
                    val blockpos = basePos.add(d2, (k - 1).toDouble(), d3)
                    val blockpos1 = blockpos.up(leafDistanceLimit)
                    if (checkBlockLine(blockpos, blockpos1) == -1)
                    {
                        val i1 = basePos.x - blockpos.x
                        val j1 = basePos.z - blockpos.z
                        val d4 = blockpos.y.toDouble() - sqrt((i1 * i1 + j1 * j1).toDouble()) * branchSlope
                        val k1 = if (d4 > maxHeight.toDouble()) maxHeight else d4.toInt()
                        val blockpos2 = BlockPos(basePos.x, k1, basePos.z)
                        if (checkBlockLine(blockpos2, blockpos) == -1)
                        {
                            foliageCoords!!.add(FoliageCoordinates(blockpos, blockpos2.y))
                        }
                    }
                }
            }
            k--
        }
    }

    /**
     * Creates a cross of leaves at a given location
     *
     * @param pos       The position to create the cross section at
     * @param leafSize  The size the cross section should be
     * @param leafBlock The block to use as leaves
     */
    private fun crossSection(pos: BlockPos, leafSize: Float, leafBlock: IBlockState)
    {
        val i = (leafSize + 0.618).toInt()
        for (j in -i..i)
        {
            for (k in -i..i)
            {
                if ((abs(j) + 0.5).pow(2.0) + (abs(k) + 0.5).pow(2.0) <= leafSize * leafSize)
                {
                    val blockpos = pos.add(j, 0, k)
                    val state = world!!.getBlockState(blockpos)
                    if ((state.block.isAir(state, world!!, blockpos) || state.block.isLeaves(state, world!!, blockpos)) && rand.nextDouble() < leafIntegrity)
                    {
                        setBlockAndNotifyAdequately(world!!, blockpos, leafBlock)
                    }
                }
            }
        }
    }

    /**
     * Gets the rough size of a layer of the tree.
     */
    private fun layerSize(y: Int): Float
    {
        return if (y < heightLimit * 0.3f)
        {
            -1.0f
        }
        else
        {
            val f = heightLimit / 2.0f
            val f1 = f - y
            var f2 = MathHelper.sqrt(f * f - f1 * f1)
            if (f1 == 0.0f)
            {
                f2 = f
            }
            else if (abs(f1) >= f)
            {
                return 0.0f
            }
            f2 * 0.5f
        }
    }

    /**
     * Get the size of a leaf cluster
     *
     * @param leafDistance The leaf distance to grow
     * @return The distance the leaves should go outwards
     */
    private fun leafSize(leafDistance: Int): Float
    {
        return if (leafDistance in 0 until leafDistanceLimit)
        {
            if (leafDistance != 0 && leafDistance != leafDistanceLimit - 1) 3.0f else 2.0f
        }
        else
        {
            -1.0f
        }
    }

    /**
     * Creates a tree limb given a start and end position
     *
     * @param startBlockPos The tree log start position
     * @param endBlockPos   The tree log end position
     */
    private fun limb(startBlockPos: BlockPos, endBlockPos: BlockPos)
    {
        val blockpos = endBlockPos.add(-startBlockPos.x, -startBlockPos.y, -startBlockPos.z)
        val i = getGreatestDistance(blockpos)
        val f = blockpos.x.toFloat() / i.toFloat()
        val f1 = blockpos.y.toFloat() / i.toFloat()
        val f2 = blockpos.z.toFloat() / i.toFloat()
        for (j in 0..i)
        {
            val blockpos1 = startBlockPos.add(
                (0.5f + j.toFloat() * f).toDouble(),
                (0.5f + j.toFloat() * f1).toDouble(),
                (0.5f + j.toFloat() * f2).toDouble()
            )
            val logAxis = getLogAxis(startBlockPos, blockpos1)
            setBlockAndNotifyAdequately(world!!, blockpos1, wood.withProperty(BlockLog.LOG_AXIS, logAxis))
        }
    }

    /**
     * Returns the absolute greatest distance in the BlockPos object.
     */
    private fun getGreatestDistance(posIn: BlockPos): Int
    {
        val i = MathHelper.abs(posIn.x)
        val j = MathHelper.abs(posIn.y)
        val k = MathHelper.abs(posIn.z)
        return if (k > i && k > j)
        {
            k
        }
        else
        {
            if (j > i) j else i
        }
    }

    /**
     * Computes the log axis that should be used for the placed log
     *
     * @param startBlockPos The start position of the branch
     * @param endBlockPos   The end position of the branch
     * @return The axis the log should face
     */
    private fun getLogAxis(startBlockPos: BlockPos, endBlockPos: BlockPos): EnumAxis
    {
        var logAxis = EnumAxis.Y
        val xDistance = abs(endBlockPos.x - startBlockPos.x)
        val zDistance = abs(endBlockPos.z - startBlockPos.z)
        val k = max(xDistance, zDistance)
        if (k > 0)
        {
            logAxis = if (xDistance == k)
            {
                EnumAxis.X
            }
            else
            {
                EnumAxis.Z
            }
        }
        return logAxis
    }

    /**
     * Generates the leaf portion of the tree as specified by the leafNodes list.
     */
    private fun generateLeaves()
    {
        for (foliageCoordinates in foliageCoords!!) for (i in 0 until leafDistanceLimit) crossSection(
            foliageCoordinates.up(i),
            leafSize(i),
            leaves.withProperty(BlockLeaves.CHECK_DECAY, java.lang.Boolean.FALSE)
        )
    }

    /**
     * Places the trunk for the big tree that is being generated. Able to generate double-sized trunks by changing a
     * field that is always 1 to 2.
     */
    private fun generateTrunk()
    {
        val blockpos = basePos
        val blockpos1 = basePos.up(height)
        limb(blockpos, blockpos1)
        if (trunkSize == 2)
        {
            limb(blockpos.east(), blockpos1.east())
            limb(blockpos.east().south(), blockpos1.east().south())
            limb(blockpos.south(), blockpos1.south())
        }
    }

    /**
     * Generates additional wood blocks to fill out the bases of different leaf nodes that would otherwise degrade.
     */
    private fun generateLeafNodeBases()
    {
        for (foliageCoordinates in foliageCoords!!)
        {
            val i = foliageCoordinates.branchBase
            val blockpos = BlockPos(basePos.x, i, basePos.z)
            if (blockpos != foliageCoordinates && i - basePos.y >= heightLimit * 0.2)
            {
                limb(blockpos, foliageCoordinates)
                setBlockAndNotifyAdequately(
                    world!!,
                    foliageCoordinates.up(leafDistanceLimit / 2),
                    wood.withProperty(BlockLog.LOG_AXIS, EnumAxis.Y)
                )
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
    private fun checkBlockLine(posOne: BlockPos, posTwo: BlockPos): Int
    {
        val blockpos = posTwo.add(-posOne.x, -posOne.y, -posOne.z)
        val i = getGreatestDistance(blockpos)
        val f = blockpos.x.toFloat() / i.toFloat()
        val f1 = blockpos.y.toFloat() / i.toFloat()
        val f2 = blockpos.z.toFloat() / i.toFloat()
        return if (i == 0)
        {
            -1
        }
        else
        {
            for (j in 0..i)
            {
                val blockpos1 = posOne.add(
                    (0.5f + j.toFloat() * f).toDouble(),
                    (0.5f + j.toFloat() * f1).toDouble(),
                    (0.5f + j.toFloat() * f2).toDouble()
                )
                if (!isReplaceable(world!!, blockpos1))
                {
                    return j
                }
            }
            -1
        }
    }

    private inner class FoliageCoordinates internal constructor(pos: BlockPos, val branchBase: Int) : BlockPos(pos.x, pos.y, pos.z)
}