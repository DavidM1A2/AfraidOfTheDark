package com.davidm1a2.afraidofthedark.common.block.core

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.block.BlockSlab
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.IStringSerializable
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World
import org.apache.commons.lang3.BitField
import java.util.*

/**
 * Base class for all AOTD slabs
 *
 * @constructor sets the name of the slab and the material
 * @param baseName The base name of the slab
 * @param material The material of slab
 */
abstract class AOTDSlab(baseName: String, material: Material) : BlockSlab(material)
{
    init
    {
        // Set the name of the slab
        unlocalizedName = Constants.MOD_ID + ":" + baseName
        this.setRegistryName(Constants.MOD_ID + ":" + baseName)
        // We must set the hardness to something otherwise it will be 0 by default
        setHardness(2.0f)
        // If the slab is not a double slab, add it to creative mode and make sure it's state is set to the bottom slab state
        if (!this.isDouble)
        {
            setCreativeTab(Constants.AOTD_CREATIVE_TAB)
            this.defaultState = blockState.baseState.withProperty(HALF, EnumBlockHalf.BOTTOM)
        }
    }

    /**
     * @return If "this" is a half slab, opposite should return a double slab. If "this" is a double slab, opposite should return a half slab.
     */
    abstract fun getOpposite(): BlockSlab

    /**
     * Gets the item dropped by this slab
     *
     * @param state   The block state of the broken block
     * @param rand    The random object which we don't need
     * @param fortune The fortune enchantment of the tool which we don't need
     * @return The half slab item of this slab
     */
    override fun getItemDropped(state: IBlockState, rand: Random, fortune: Int): Item
    {
        // If it's a half slab we just use the item, if it's a double slab we get the opposite which is the half slab
        return if (!this.isDouble)
        {
            Item.getItemFromBlock(this)
        }
        else
        {
            Item.getItemFromBlock(getOpposite())
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
    override fun getPickBlock(state: IBlockState, target: RayTraceResult, world: World, pos: BlockPos, player: EntityPlayer): ItemStack
    {
        // If it's a half slab we just return the item stack, if it's a double slab we get the opposite which is the half slab
        return if (!this.isDouble)
        {
            ItemStack(this, 1, 0)
        }
        else
        {
            ItemStack(getOpposite(), 1, 0)
        }
    }

    /**
     * Gets the block's state from the metadata field
     *
     * @param meta The metadata integer representing this block
     * @return The block state represented by this metadata integer
     */
    override fun getStateFromMeta(meta: Int): IBlockState
    {
        // Start with the default state
        var iBlockState = this.defaultState.withProperty(VARIANT, Variant.DEFAULT)
        // if it's a half slab, store if the slab is on top or on bottom
        if (!this.isDouble)
        {
            iBlockState = iBlockState.withProperty(
                HALF,
                if (IS_TOP_FIELD.getValue(meta) == 1) EnumBlockHalf.TOP else EnumBlockHalf.BOTTOM
            )
        }
        return iBlockState
    }

    /**
     * Gets the metadata field from the block's state
     *
     * @param state The block state to convert into a metadata field
     * @return The metadata value that this block state represents
     */
    override fun getMetaFromState(state: IBlockState): Int
    {
        var meta = 0
        // If the slab is a half slab, test if it's on top or not
        if (!this.isDouble)
        {
            meta = IS_TOP_FIELD.setBoolean(meta, state.getValue(HALF) == EnumBlockHalf.TOP)
        }
        return meta
    }

    /**
     * Returns the block state container that this slab represents
     *
     * @return If the block is a half slab, return a container with 2 different properties, otherwise just return the slab with one property
     */
    override fun createBlockState(): BlockStateContainer
    {
        return if (this.isDouble) BlockStateContainer(this, VARIANT)
        else BlockStateContainer(
            this,
            HALF,
            VARIANT
        )
    }

    /**
     * This method is required by the interface but the functionality is not required, so we just return the base method's unlocalized name
     *
     * @param meta The metadata of the slab
     * @return The unlocalized name of the slab
     */
    override fun getUnlocalizedName(meta: Int): String
    {
        return super.getUnlocalizedName()
    }

    /**
     * Required variant property for this slab, since each slab is registered differently we only need one variant which is "default"
     *
     * @return The default varient that never changes
     */
    override fun getVariantProperty(): IProperty<*>
    {
        return VARIANT
    }

    /**
     * Required variant property getter for this slab, since each slab is registered differently we only need one variant which is "default"
     *
     * @param stack Ignored, each slab has the same default variant
     * @return The default variant
     */
    override fun getTypeForItem(stack: ItemStack): Comparable<*>
    {
        return Variant.DEFAULT
    }

    /**
     * Variant is required for slabs, so just make the variant have one option that is default
     */
    private enum class Variant : IStringSerializable
    {
        DEFAULT;

        override fun getName(): String
        {
            return "default"
        }
    }

    companion object
    {
        // Variant is required by slabs, so we are forced to create a default variant that does nothing
        private val VARIANT = PropertyEnum.create(
            "variant",
            Variant::class.java
        )

        private val IS_TOP_FIELD = BitField(0x1)
    }
}