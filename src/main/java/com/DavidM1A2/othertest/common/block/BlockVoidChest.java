package com.DavidM1A2.afraidofthedark.common.block;

import com.DavidM1A2.afraidofthedark.common.block.core.AOTDBlockTileEntity;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModResearches;
import com.DavidM1A2.afraidofthedark.common.tileEntity.TileEntityVoidChest;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * Class representing a void chest that will be a tile entity as well
 */
public class BlockVoidChest extends AOTDBlockTileEntity
{
    // The facing property of the void chest which tells it which way to open/close
    private static final PropertyDirection FACING_PROPERTY = BlockHorizontal.FACING;
    // The hitbox of the chest is smaller than usual
    private static final AxisAlignedBB VOID_CHEST_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);

    /**
     * Constructor initializes the block's properties
     */
    public BlockVoidChest()
    {
        super("void_chest", Material.ROCK);
        // Default chests face north
        this.setDefaultState(this.getBlockState().getBaseState().withProperty(FACING_PROPERTY, EnumFacing.NORTH));
        // Set the block's hardness, blast resistance, and harvest level
        this.setHardness(4.0F);
        this.setResistance(50.0F);
        this.setHarvestLevel("pickaxe", 2);
    }

    /**
     * Called to test if this block is opaque or not
     *
     * @param state The block state
     * @return False since this is not opaque
     */
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    /**
     * Called to test if this block is a full cube or not
     *
     * @param state The block state
     * @return False since it's not a full 1x1x1 block
     */
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    /**
     * Returns if this block has a custom breaking progress animation
     *
     * @param state The block state to test
     * @return True since it's not a normal block
     */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasCustomBreakingProgress(IBlockState state)
    {
        return true;
    }

    /**
     * Tests the render type for this block, it's an entity block so return that
     *
     * @param state The block state to render
     * @return EntityBlock since it's an animated block
     */
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    /**
     * Gets the custom bounding box of this block which is the chest bounding box
     *
     * @param state  The state of this block
     * @param source The context which we can use to get environment details
     * @param pos    The position of the block
     * @return The void chest AABB
     */
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return VOID_CHEST_AABB;
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
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        // Face the block depending on the player's horizontal facing
        return this.getDefaultState().withProperty(FACING_PROPERTY, placer.getHorizontalFacing().getOpposite());
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
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        // Face the block depending on the player's horizontal facing and notify the player of the change
        worldIn.setBlockState(pos, state.withProperty(FACING_PROPERTY, placer.getHorizontalFacing().getOpposite()), 2);
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
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        // Test if the tile entity at this position is a void chest (it should be!)
        if (worldIn.getTileEntity(pos) instanceof TileEntityVoidChest)
        {
            TileEntityVoidChest entityVoidChest = (TileEntityVoidChest) worldIn.getTileEntity(pos);
            // Ensure the player can interact with the chest
            if (playerIn.getCapability(ModCapabilities.PLAYER_RESEARCH, null).isResearched(ModResearches.VOID_CHEST))
            // Let the player interact with the chest
            {
                entityVoidChest.interact(playerIn);
            }
            // If the player doesn't have the research tell them that
            else if (!worldIn.isRemote)
            {
                playerIn.sendMessage(new TextComponentTranslation("aotd.void_chest.dont_understand"));
            }
        }
        return true;
    }

    /**
     * Converts the block metadata value into block state
     *
     * @param meta The metadata value to convert
     * @return The block state that this metadata value represents
     */
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getFront(meta);
        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(FACING_PROPERTY, enumfacing);
    }

    /**
     * Converts the block state to a metadata value
     *
     * @param state The state the convert
     * @return The metadata value that this state represents
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING_PROPERTY).getIndex();
    }

    /**
     * @return The block state properties that make this block work
     */
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING_PROPERTY);
    }

    /**
     * Creates a tile entity for this block
     *
     * @param worldIn The world to create the tile entity in
     * @param meta    The metadata value of this block
     * @return The tile entity that this block represents
     */
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityVoidChest();
    }
}
