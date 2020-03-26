package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlock
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import net.minecraft.block.Block
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.BlockFaceShape
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.DamageSource
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.EnumPlantType
import net.minecraftforge.common.IPlantable
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.*

/**
 * Class representing the imbued cactus block
 *
 * @constructor sets the block's properties
 */
class BlockImbuedCactus : AOTDBlock("imbued_cactus", Material.CACTUS), IPlantable
{
    init
    {
        defaultState = this.blockState.baseState.withProperty(AGE, 0)
        tickRandomly = true
        setHardness(0.4f)
        soundType = SoundType.CLOTH
    }

    /**
     * Called every now and then to update the block's state
     *
     * @param world The world the block is in
     * @param pos The block's position
     * @param state The block's state
     * @param rand A random instance to use
     */
    override fun updateTick(world: World, pos: BlockPos, state: IBlockState, rand: Random)
    {
        if (!world.isAreaLoaded(pos, 1)) return  // Forge: prevent growing cactus from loading unloaded chunks with block update

        // Check if the air block above is air, if so grow
        if (world.isAirBlock(pos.up()))
        {
            var age = state.getValue<Int>(AGE)
            age++

            // If we're at max age grow and reset age to 0
            if (age == MAX_AGE)
            {
                age = 0

                // Compute the number of cactus blocks stacked
                var currentHeight = 0
                for (yOffset in 0 until MAX_HEIGHT)
                {
                    val blockBelow = world.getBlockState(pos.down(yOffset))
                    if (blockBelow.block == this)
                    {
                        currentHeight++
                    } else if (blockBelow.block != this)
                    {
                        break
                    }
                }

                // If we're at max height, grow a blossom, otherwise grow a cactus block
                if (currentHeight == MAX_HEIGHT)
                {
                    world.setBlockState(pos.up(), ModBlocks.IMBUED_CACTUS_BLOSSOM.defaultState)
                } else
                {
                    world.setBlockState(pos.up(), this.defaultState)
                }
            }

            // Update age
            world.setBlockState(pos, state.withProperty(AGE, age))
        }
    }

    /**
     * Gets the hitbox of this block
     *
     * @param blockState The state the block has
     * @param worldIn The world the block is in
     * @param pos The block's position
     * @return The cactus collision AABB
     */
    override fun getCollisionBoundingBox(blockState: IBlockState, worldIn: IBlockAccess, pos: BlockPos): AxisAlignedBB?
    {
        return CACTUS_COLLISION_AABB
    }

    /**
     * Return an AABB (in world coords!) that should be highlighted when the player is targeting this Block
     *
     * @param state The block's state
     * @param worldIn The block's world
     * @param pos The block's position
     * @return The cactus AABB
     */
    override fun getSelectedBoundingBox(state: IBlockState, worldIn: World, pos: BlockPos): AxisAlignedBB
    {
        return CACTUS_AABB.offset(pos)
    }

    /**
     * True if this is a full cube, false otherwise
     *
     * @param state The block's state
     * @return false, since this is not a full cube
     */
    override fun isFullCube(state: IBlockState): Boolean
    {
        return false
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     *
     * @param state The block's state
     * @return false, the block doesn't fully hide the blocks behind it
     */
    override fun isOpaqueCube(state: IBlockState): Boolean
    {
        return false
    }

    /**
     * Checks if this block can be placed exactly at the given position.
     *
     * @param worldIn The world the block is being placed
     * @param pos The position the block is being placed
     * @return true if the block would fit there, false otherwise
     */
    override fun canPlaceBlockAt(worldIn: World, pos: BlockPos): Boolean
    {
        return if (super.canPlaceBlockAt(worldIn, pos)) canBlockStay(worldIn, pos) else false
    }

    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     *
     * @param state The block's current state
     * @param worldIn The world the block is in
     * @param blockIn The current block
     * @param fromPos The position that updated
     */
    override fun neighborChanged(state: IBlockState, worldIn: World, pos: BlockPos, blockIn: Block, fromPos: BlockPos)
    {
        // If the block can't survive here anymore break it
        if (!canBlockStay(worldIn, pos))
        {
            worldIn.destroyBlock(pos, true)
        }
    }

    /**
     * Called When an Entity Collided with the Block, imbued cactus does 2 damage
     *
     * @param worldIn The world the block is in
     * @param pos The position the block is at
     * @param state The block's current state
     * @param entityIn The entity that hit the block
     */
    override fun onEntityCollidedWithBlock(worldIn: World, pos: BlockPos, state: IBlockState, entityIn: Entity)
    {
        entityIn.attackEntityFrom(DamageSource.CACTUS, 2.0f)
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     *
     * @param meta The metadata value
     * @return The block state from meta
     */
    override fun getStateFromMeta(meta: Int): IBlockState
    {
        return this.defaultState.withProperty(AGE, Integer.valueOf(meta))
    }

    /**
     * Convert the BlockState into the correct metadata value
     *
     * @param state The block state to convert
     * @return The metadata value for the state
     */
    override fun getMetaFromState(state: IBlockState): Int
    {
        return state.getValue<Int>(AGE)
    }

    /**
     * Returns cutout since the block layer is transparent
     */
    @SideOnly(Side.CLIENT)
    override fun getBlockLayer(): BlockRenderLayer
    {
        return BlockRenderLayer.CUTOUT
    }

    /**
     * Gets the type of plant this is. This is used to determine what blocks this can be placed on
     *
     * @param world The world the block is in
     * @param pos The position this block is in
     * @return Desert, since this is cactus
     */
    override fun getPlantType(world: IBlockAccess, pos: BlockPos): EnumPlantType
    {
        return EnumPlantType.Desert
    }

    /**
     * Gets the default state of the plant used to determine if this block can sustain
     *
     * @param world The world the block is being placed in
     * @param pos The position the block is being placed at
     * @return The default state of this cactus (it doesn't start off differently)
     */
    override fun getPlant(world: IBlockAccess, pos: BlockPos): IBlockState
    {
        return defaultState
    }

    /**
     * Creates the state data that this block will store
     *
     * @return A default block state with the age property
     */
    override fun createBlockState(): BlockStateContainer
    {
        return BlockStateContainer(this, AGE)
    }

    /**
     * Get the geometry of the queried face at the given position and state. This is used to decide whether things like
     * buttons are allowed to be placed on the face, or how glass panes connect to the face, among other things.
     *
     * @param worldIn The world the block is in
     * @param state The state of the block
     * @param pos The position the block is in
     * @param face The side of the block
     * @return UNDEFINED since this is not a traditional solid block
     */
    override fun getBlockFaceShape(worldIn: IBlockAccess, state: IBlockState, pos: BlockPos, face: EnumFacing): BlockFaceShape
    {
        return BlockFaceShape.UNDEFINED
    }

    /**
     * Returns true if this block can sustain an imbued cactus block above it
     *
     * @param state The state of the block
     * @param world The world the block is in
     * @param pos The position of the block
     * @param direction The side of the block
     * @param plantable The block being tested
     */
    override fun canSustainPlant(state: IBlockState, world: IBlockAccess, pos: BlockPos, direction: EnumFacing, plantable: IPlantable): Boolean
    {
        val plantStateToPlace = plantable.getPlant(world, pos.offset(direction))
        return plantStateToPlace.block == this || plantStateToPlace.block == ModBlocks.IMBUED_CACTUS_BLOSSOM
    }

    /**
     * Tests if this block can stay at this position
     *
     * @param world The world the block is in
     * @param pos The position the block is at
     * @return True if the block fits here, false otherwise
     */
    private fun canBlockStay(world: World, pos: BlockPos): Boolean
    {
        val blockStateBelow = world.getBlockState(pos.down())
        return blockStateBelow.block.canSustainPlant(blockStateBelow, world, pos.down(), EnumFacing.UP, this) && !world.getBlockState(pos.up()).material.isLiquid
    }

    companion object
    {
        private const val MAX_HEIGHT = 3
        private const val MAX_AGE = 5
        private val AGE = PropertyInteger.create("age", 0, MAX_AGE)
        private val CACTUS_COLLISION_AABB = AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 0.9375, 0.9375)
        private val CACTUS_AABB = AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 1.0, 0.9375)
    }
}