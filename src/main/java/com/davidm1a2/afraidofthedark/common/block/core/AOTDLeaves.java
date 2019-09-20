package com.davidm1a2.afraidofthedark.common.block.core;

import com.davidm1a2.afraidofthedark.common.constants.Constants;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.apache.commons.lang3.BitField;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * A base class for any leaves added by the AOTD mod
 */
public abstract class AOTDLeaves extends BlockLeaves
{
    private BitField checkDecayField = new BitField(0x1);
    private BitField decayableField = new BitField(0x2);

    /**
     * Constructor just requires the name of the block to register as the registry name
     *
     * @param baseName The name to be used by the registry and unlocalized names
     */
    public AOTDLeaves(String baseName)
    {
        super();
        this.setUnlocalizedName(Constants.MOD_ID + ":" + baseName);
        this.setRegistryName(Constants.MOD_ID + ":" + baseName);
        // Don't forget the creative tab...
        this.setCreativeTab(Constants.AOTD_CREATIVE_TAB);
        // Set the default state
        this.setDefaultState(this.getDefaultState().withProperty(CHECK_DECAY, true).withProperty(DECAYABLE, true));
    }

    /**
     * Upon silk-touching a block, return this leaf block not nothing like normal
     *
     * @param state The block state that was mined
     * @return An itemstack of size 1 containing the leaf block
     */
    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state)
    {
        return new ItemStack(Item.getItemFromBlock(this), 1, 0);
    }

    /**
     * Gets the block state from a given metadata value
     *
     * @param meta The metadata value to start from
     * @return The block state to convert the metadata into
     */
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(DECAYABLE, decayableField.getValue(meta) == 1).withProperty(CHECK_DECAY, checkDecayField.getValue(meta) == 1);
    }

    /**
     * Opposite of getStateFromMeta, returns the block metadata value from the state
     *
     * @param state The block state in the world
     * @return The metadata value to conver the state into
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        int meta = 0;
        meta = decayableField.setBoolean(meta, state.getValue(DECAYABLE));
        meta = checkDecayField.setBoolean(meta, state.getValue(CHECK_DECAY));
        return meta;
    }

    /**
     * This is unused by any mod leaves
     *
     * @param meta ignored
     * @return null
     */
    @Override
    public BlockPlanks.EnumType getWoodType(int meta)
    {
        return null;
    }

    /**
     * Our leaves will have a decayable property and a check decay property
     *
     * @return A container with all properties of the leaf
     */
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, DECAYABLE, CHECK_DECAY);
    }

    /**
     * Upon shearing this block we return it like silk touch would
     *
     * @param item    The shear item used to mine the leaves
     * @param world   The block that is being mined
     * @param pos     The location of the block being mined
     * @param fortune If the shears have fortune
     * @return A list of itemstacks to drop after this leaf block is sheared
     */
    @Nonnull
    @Override
    public List<ItemStack> onSheared(@Nonnull ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
    {
        return NonNullList.withSize(1, new ItemStack(this, 1, 0));
    }
}
