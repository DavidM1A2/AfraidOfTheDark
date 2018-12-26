/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.core;

import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.reference.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class AOTDSlab extends BlockSlab
{
	public AOTDSlab(final Material material)
	{
		super(material);
		this.setUnlocalizedName("NAME NOT SET");
		if (this.displayInCreative() && !this.isDouble())
			this.setCreativeTab(Reference.AFRAID_OF_THE_DARK);
		this.setHardness(2.0F);
		this.setResistance(5.0F);
		this.setStepSound(Block.soundTypeWood);

		IBlockState iblockstate = this.blockState.getBaseState();

		// Is this a double or single half slab?
		if (!this.isDouble())
		{
			iblockstate = iblockstate.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
		}
		this.useNeighborBrightness = !this.isDouble();
	}

	protected boolean displayInCreative()
	{
		return true;
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 *
	 * @param fortune
	 *            the level of the Fortune enchantment on the player's tool
	 */
	@Override
	public abstract Item getItemDropped(final IBlockState state, final Random rand, final int fortune);

	// What item does this block drop?
	@Override
	@SideOnly(Side.CLIENT)
	public abstract Item getItem(final World worldIn, final BlockPos pos);

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(final int meta)
	{
		IBlockState iblockstate = this.getDefaultState();

		if (!this.isDouble())
		{
			iblockstate = iblockstate.withProperty(BlockSlab.HALF, (meta & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
		}

		return iblockstate;
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(final IBlockState state)
	{
		final byte b0 = 0;
		int i = b0;

		if (!this.isDouble() && (state.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP))
		{
			i |= 8;
		}

		return i;
	}

	// Create default block state
	@Override
	protected BlockState createBlockState()
	{
		return this.isDouble() ? new BlockState(this, new IProperty[]
		{}) : new BlockState(this, new IProperty[]
		{ BlockSlab.HALF });
	}

	/**
	 * Get the damage value that this Block should drop
	 */
	@Override
	public int damageDropped(final IBlockState state)
	{
		return 0;
	}

	@Override
	public IProperty<?> getVariantProperty()
	{
		return null;
	}

	@Override
	public Object getVariant(ItemStack stack)
	{
		return null;
	}

	/**
	 * Returns the slab block name with the type associated with it
	 */
	@Override
	public String getUnlocalizedName(final int meta)
	{
		return this.getUnlocalizedName();
	}

	@Override
	public String getUnlocalizedName()
	{
		return String.format("tile.%s%s", Reference.MOD_ID.toLowerCase() + ":", this.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
		// Format for a block is: tile.modid:blockname.name
	}

	// Get the unlocalized name
	protected String getUnwrappedUnlocalizedName(final String unlocalizedName)
	{
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
	}
}
