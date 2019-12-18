package com.davidm1a2.afraidofthedark.common.block

import com.davidm1a2.afraidofthedark.common.block.core.AOTDBlockTileEntity
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.tileEntity.TileEntityVoidChest
import net.minecraft.block.BlockHorizontal
import net.minecraft.block.material.Material
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumBlockRenderType
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * Class representing a void chest that will be a tile entity as well
 *
 * @constructor initializes the block's properties
 */
class BlockVoidChest : AOTDBlockTileEntity("void_chest", Material.ROCK)
{
    init
    {
        // Default chests face north
        this.defaultState = getBlockState().baseState.withProperty(FACING_PROPERTY, EnumFacing.NORTH)
        // Set the block's hardness, blast resistance, and harvest level
        setHardness(4.0f)
        setResistance(50.0f)
        this.setHarvestLevel("pickaxe", 2)
    }

    /**
     * Called to test if this block is opaque or not
     *
     * @param state The block state
     * @return False since this is not opaque
     */
    override fun isOpaqueCube(state: IBlockState): Boolean
    {
        return false
    }

    /**
     * Called to test if this block is a full cube or not
     *
     * @param state The block state
     * @return False since it's not a full 1x1x1 block
     */
    override fun isFullCube(state: IBlockState): Boolean
    {
        return false
    }

    /**
     * Returns if this block has a custom breaking progress animation
     *
     * @param state The block state to test
     * @return True since it's not a normal block
     */
    @SideOnly(Side.CLIENT)
    override fun hasCustomBreakingProgress(state: IBlockState): Boolean
    {
        return true
    }

    /**
     * Tests the render type for this block, it's an entity block so return that
     *
     * @param state The block state to render
     * @return EntityBlock since it's an animated block
     */
    override fun getRenderType(state: IBlockState): EnumBlockRenderType
    {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED
    }

    /**
     * Gets the custom bounding box of this block which is the chest bounding box
     *
     * @param state  The state of this block
     * @param source The context which we can use to get environment details
     * @param pos    The position of the block
     * @return The void chest AABB
     */
    override fun getBoundingBox(state: IBlockState, source: IBlockAccess, pos: BlockPos): AxisAlignedBB
    {
        return VOID_CHEST_AABB
    }

    /**
     * Returns the state that this block will return after being placed with a set of parameters
     *
     * @param world  The world the block is being placed in
     * @param pos    The position the block is being placed it
     * @param facing The facing the block is being placed with
     * @param hitX   The X position that this block is being placed against
     * @param hitY   The Y position that this block is being placed against
     * @param hitZ   The Z position that this block is being placed against
     * @param meta   The metadata this block is placed with
     * @param placer The player or entity placing the block
     * @param hand   The hand that is holding the block
     * @return The block state after being placed
     */
    override fun getStateForPlacement(
        world: World,
        pos: BlockPos,
        facing: EnumFacing,
        hitX: Float,
        hitY: Float,
        hitZ: Float,
        meta: Int,
        placer: EntityLivingBase,
        hand: EnumHand
    ): IBlockState
    {
        // Face the block depending on the player's horizontal facing
        return this.defaultState.withProperty(FACING_PROPERTY, placer.horizontalFacing.opposite)
    }

    /**
     * Called after a block is placed
     *
     * @param worldIn The world the block is placed at
     * @param pos     The position the block is at
     * @param state   The state the block is in after placement
     * @param placer  The player/entity that placed the block
     * @param stack   The itemstack that this block came from
     */
    override fun onBlockPlacedBy(worldIn: World, pos: BlockPos, state: IBlockState, placer: EntityLivingBase, stack: ItemStack)
    {
        // Face the block depending on the player's horizontal facing and notify the player of the change
        worldIn.setBlockState(pos, state.withProperty(FACING_PROPERTY, placer.horizontalFacing.opposite), 2)
    }

    /**
     * Called when the block is right clicked
     *
     * @param worldIn  The world that the block is in
     * @param pos      The position that the block is at
     * @param state    The state the block is in
     * @param playerIn The player that right clicked
     * @param hand     The hand the player right clicked with
     * @param facing   The facing of the block
     * @param hitX     The X position that was right clicked
     * @param hitY     The Y position that was right clicked
     * @param hitZ     The Z position that was right clicked
     * @return True to allow the interaction, false otherwise
     */
    override fun onBlockActivated(
        worldIn: World,
        pos: BlockPos,
        state: IBlockState,
        playerIn: EntityPlayer,
        hand: EnumHand,
        facing: EnumFacing,
        hitX: Float,
        hitY: Float,
        hitZ: Float
    ): Boolean
    {
        // Test if the tile entity at this position is a void chest (it should be!)
        val tileEntity = worldIn.getTileEntity(pos)
        if (tileEntity is TileEntityVoidChest)
        {
            // Ensure the player can interact with the chest
            if (playerIn.getResearch().isResearched(ModResearches.VOID_CHEST))
            {
                // Let the player interact with the chest
                tileEntity.interact(playerIn)
            }
            else if (!worldIn.isRemote)
            {
                playerIn.sendMessage(TextComponentTranslation("aotd.void_chest.dont_understand"))
            }
        }
        return true
    }

    /**
     * Converts the block metadata value into block state
     *
     * @param meta The metadata value to convert
     * @return The block state that this metadata value represents
     */
    override fun getStateFromMeta(meta: Int): IBlockState
    {
        var enumfacing = EnumFacing.getFront(meta)
        if (enumfacing.axis == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.NORTH
        }
        return this.defaultState.withProperty(FACING_PROPERTY, enumfacing)
    }

    /**
     * Converts the block state to a metadata value
     *
     * @param state The state the convert
     * @return The metadata value that this state represents
     */
    override fun getMetaFromState(state: IBlockState): Int
    {
        return state.getValue(FACING_PROPERTY).index
    }

    /**
     * @return The block state properties that make this block work
     */
    override fun createBlockState(): BlockStateContainer
    {
        return BlockStateContainer(this, FACING_PROPERTY)
    }

    /**
     * Creates a tile entity for this block
     *
     * @param worldIn The world to create the tile entity in
     * @param meta    The metadata value of this block
     * @return The tile entity that this block represents
     */
    override fun createNewTileEntity(worldIn: World, meta: Int): TileEntity
    {
        return TileEntityVoidChest()
    }

    companion object
    {
        // The facing property of the void chest which tells it which way to open/close
        private val FACING_PROPERTY = BlockHorizontal.FACING
        // The hitbox of the chest is smaller than usual
        private val VOID_CHEST_AABB = AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 0.875, 0.9375)
    }
}