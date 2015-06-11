/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.AOTDTreeTypes;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Refrence;

public abstract class AOTDSlab extends BlockSlab
{
	// Different variants of slabs
	public static final PropertyEnum VARIANT_PROP = PropertyEnum.create("variant", AOTDTreeTypes.class);

	public AOTDSlab(final Material material)
	{
		super(material);
		this.setUnlocalizedName("NAME NOT SET");
		this.setCreativeTab(Constants.AFRAID_OF_THE_DARK);
		this.setHardness(2.0F);
		this.setResistance(5.0F);
		this.setStepSound(Block.soundTypeWood);

		IBlockState iblockstate = this.blockState.getBaseState();

		// Is this a double or single half slab?
		if (!this.isDouble())
		{
			iblockstate = iblockstate.withProperty(BlockSlab.HALF_PROP, BlockSlab.EnumBlockHalf.BOTTOM);
		}
		this.useNeighborBrightness = !this.isDouble();

		this.setDefaultState(iblockstate.withProperty(AOTDSlab.VARIANT_PROP, AOTDTreeTypes.GRAVEWOOD));
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 *
	 * @param fortune
	 *            the level of the Fortune enchantment on the player's tool
	 */
	@Override
	public Item getItemDropped(final IBlockState state, final Random rand, final int fortune)
	{
		return Item.getItemFromBlock(ModBlocks.gravewoodHalfSlab);
	}

	// What item does this block drop?
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(final World worldIn, final BlockPos pos)
	{
		return Item.getItemFromBlock(ModBlocks.gravewoodHalfSlab);
	}

	/**
	 * Returns the slab block name with the type associated with it
	 */
	@Override
	public String getFullSlabName(final int meta)
	{
		return this.getUnlocalizedName();
	}

	// What property is this slab?
	@Override
	public IProperty func_176551_l()
	{
		return AOTDSlab.VARIANT_PROP;
	}

	// Get type from item
	@Override
	public Object func_176553_a(final ItemStack itemStack)
	{
		return AOTDTreeTypes.getTypeFromMeta(itemStack.getMetadata() & 7);
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List list)
	{
		if (itemIn != Item.getItemFromBlock(ModBlocks.gravewoodDoubleSlab))
		{
			final AOTDTreeTypes[] aenumtype = AOTDTreeTypes.values();
			final int i = aenumtype.length;

			for (int j = 0; j < i; ++j)
			{
				final AOTDTreeTypes enumtype = aenumtype[j];
				list.add(new ItemStack(itemIn, 1, enumtype.getMetadata()));
			}
		}
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(final int meta)
	{
		IBlockState iblockstate = this.getDefaultState().withProperty(AOTDSlab.VARIANT_PROP, AOTDTreeTypes.GRAVEWOOD);

		if (!this.isDouble())
		{
			iblockstate = iblockstate.withProperty(BlockSlab.HALF_PROP, (meta & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
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
		int i = b0 | ((AOTDTreeTypes) state.getValue(AOTDSlab.VARIANT_PROP)).getMetadata();

		if (!this.isDouble() && (state.getValue(BlockSlab.HALF_PROP) == BlockSlab.EnumBlockHalf.TOP))
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
		{ AOTDSlab.VARIANT_PROP }) : new BlockState(this, new IProperty[]
		{ BlockSlab.HALF_PROP, AOTDSlab.VARIANT_PROP });
	}

	/**
	 * Get the damage value that this Block should drop
	 */
	@Override
	public int damageDropped(final IBlockState state)
	{
		return ((AOTDTreeTypes) state.getValue(AOTDSlab.VARIANT_PROP)).getMetadata();
	}

	@Override
	public String getUnlocalizedName()
	{
		return String.format("tile.%s%s", Refrence.MOD_ID.toLowerCase() + ":", this.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
		// Format for a block is: tile.modid:blockname.name
	}

	// Get the unlocalized name
	protected String getUnwrappedUnlocalizedName(final String unlocalizedName)
	{
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
	}
}
