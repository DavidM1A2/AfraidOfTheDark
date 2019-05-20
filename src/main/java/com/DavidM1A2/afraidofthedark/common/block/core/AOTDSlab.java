package com.DavidM1A2.afraidofthedark.common.block.core;

import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.apache.commons.lang3.BitField;

import java.util.Random;

/**
 * Base class for all AOTD slabs
 */
public abstract class AOTDSlab extends BlockSlab
{
    // Variant is required by slabs, so we are forced to create a default variant that does nothing
    private static final PropertyEnum<AOTDSlab.Variant> VARIANT = PropertyEnum.create("variant", AOTDSlab.Variant.class);
    // Field representing the top or bottom variant
    private BitField isTopField = new BitField(0x1);

    /**
     * Constructor sets the name of the slab and the material
     *
     * @param baseName The base name of the slab
     * @param material The material of slab
     */
    public AOTDSlab(String baseName, Material material)
    {
        super(material);
        // Set the name of the slab
        this.setUnlocalizedName(Constants.MOD_ID + ":" + baseName);
        this.setRegistryName(Constants.MOD_ID + ":" + baseName);
        // We must set the hardness to something otherwise it will be 0 by default
        this.setHardness(2.0F);
        // If the slab is not a double slab, add it to creative mode and make sure it's state is set to the bottom slab state
        if (!this.isDouble())
        {
            this.setCreativeTab(Constants.AOTD_CREATIVE_TAB);
            this.setDefaultState(this.blockState.getBaseState().withProperty(HALF, EnumBlockHalf.BOTTOM));
        }
    }

    /**
     * Gets the item dropped by this slab
     *
     * @param state   The block state of the broken block
     * @param rand    The random object which we don't need
     * @param fortune The fortune enchantment of the tool which we don't need
     * @return The half slab item of this slab
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        // If it's a half slab we just use the item, if it's a double slab we get the opposite which is the half slab
        if (!this.isDouble())
        {
            return Item.getItemFromBlock(this);
        }
        else
        {
            return Item.getItemFromBlock(this.getOpposite());
        }
    }

    /**
     * Gets the item dropped by this slab
     *
     * @param state  The block state of the broken block
     * @param target The ray trace result that returns what block we're hitting
     * @param world  The world the block is in
     * @param pos    The position of the block
     * @param player The player mining the block
     * @return The itemstack that is returned when the block is broken
     */
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        // If it's a half slab we just return the item stack, if it's a double slab we get the opposite which is the half slab
        if (!this.isDouble())
        {
            return new ItemStack(this, 1, 0);
        }
        else
        {
            return new ItemStack(this.getOpposite(), 1, 0);
        }
    }

    /**
     * Important custom function that will return the opposite slab type when called.
     *
     * @return If "this" is a half slab, getOpposite() should return a double slab. If "this" is a double slab, getOpposite() should return a half slab.
     */
    public abstract BlockSlab getOpposite();

    /**
     * Gets the block's state from the metadata field
     *
     * @param meta The metadata integer representing this block
     * @return The block state represented by this metadata integer
     */
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        // Start with the default state
        IBlockState iBlockState = this.getDefaultState().withProperty(VARIANT, AOTDSlab.Variant.DEFAULT);

        // if it's a half slab, store if the slab is on top or on bottom
        if (!this.isDouble())
        {
            iBlockState = iBlockState.withProperty(HALF, isTopField.getValue(meta) == 1 ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM);
        }

        return iBlockState;
    }

    /**
     * Gets the metadata field from the block's state
     *
     * @param state The block state to convert into a metadata field
     * @return The metadata value that this block state represents
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;

        // If the slab is a half slab, test if it's on top or not
        if (!this.isDouble())
        {
            i = isTopField.setBoolean(i, state.getValue(HALF) == EnumBlockHalf.TOP);
        }

        return i;
    }

    /**
     * Returns the block state container that this slab represents
     *
     * @return If the block is a half slab, return a container with 2 different properties, otherwise just return the slab with one property
     */
    @Override
    protected BlockStateContainer createBlockState()
    {
        return this.isDouble() ? new BlockStateContainer(this, VARIANT) : new BlockStateContainer(this, HALF, VARIANT);
    }

    /**
     * This method is required by the interface but the functionality is not required, so we just return the base method's unlocalized name
     *
     * @param meta The metadata of the slab
     * @return The unlocalized name of the slab
     */
    @Override
    public String getUnlocalizedName(int meta)
    {
        return super.getUnlocalizedName();
    }

    /**
     * Required variant property for this slab, since each slab is registered differently we only need one variant which is "default"
     *
     * @return The default varient that never changes
     */
    @Override
    public IProperty<?> getVariantProperty()
    {
        return VARIANT;
    }

    /**
     * Required variant property getter for this slab, since each slab is registered differently we only need one variant which is "default"
     *
     * @param stack Ignored, each slab has the same default variant
     * @return The default variant
     */
    @Override
    public Comparable<?> getTypeForItem(ItemStack stack)
    {
        return Variant.DEFAULT;
    }

    /**
     * Variant is required for slabs, so just make the variant have one option that is default
     */
    private enum Variant implements IStringSerializable
    {
        DEFAULT;

        public String getName()
        {
            return "default";
        }
    }
}
