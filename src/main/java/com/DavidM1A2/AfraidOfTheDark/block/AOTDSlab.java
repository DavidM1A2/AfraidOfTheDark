/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.block;

import java.util.List;
import java.util.Random;

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

import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.refrence.AOTDTreeTypes;
import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

public abstract class AOTDSlab extends BlockSlab
{
	// Different variants of slabs
	public static final PropertyEnum VARIANT_PROP = PropertyEnum.create("variant", AOTDTreeTypes.class);

	public AOTDSlab(Material material)
	{
		super(material);
		this.setUnlocalizedName("NAME NOT SET");
		this.setCreativeTab(Refrence.AFRAID_OF_THE_DARK);
		this.setHardness(2.0F);
		this.setResistance(5.0F);
		this.setStepSound(soundTypeWood);

		IBlockState iblockstate = this.blockState.getBaseState();

		// Is this a double or single half slab?
		if (!this.isDouble())
		{
			iblockstate = iblockstate.withProperty(HALF_PROP, BlockSlab.EnumBlockHalf.BOTTOM);
		}
		this.useNeighborBrightness = !this.isDouble();

		this.setDefaultState(iblockstate.withProperty(VARIANT_PROP, AOTDTreeTypes.GRAVEWOOD));
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 * 
	 * @param fortune
	 *            the level of the Fortune enchantment on the player's tool
	 */
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(ModBlocks.gravewoodHalfSlab);
	}

	// What item does this block drop?
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World worldIn, BlockPos pos)
	{
		return Item.getItemFromBlock(ModBlocks.gravewoodHalfSlab);
	}

	/**
	 * Returns the slab block name with the type associated with it
	 */
	@Override
	public String getFullSlabName(int meta)
	{
		return this.getUnlocalizedName();
	}

	// What property is this slab?
	@Override
	public IProperty func_176551_l()
	{
		return VARIANT_PROP;
	}

	// Get type from item
	@Override
	public Object func_176553_a(ItemStack itemStack)
	{
		return AOTDTreeTypes.getTypeFromMeta(itemStack.getMetadata() & 7);
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		if (itemIn != Item.getItemFromBlock(ModBlocks.gravewoodDoubleSlab))
		{
			AOTDTreeTypes[] aenumtype = AOTDTreeTypes.values();
			int i = aenumtype.length;

			for (int j = 0; j < i; ++j)
			{
				AOTDTreeTypes enumtype = aenumtype[j];
				list.add(new ItemStack(itemIn, 1, enumtype.getMetadata()));
			}
		}
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState iblockstate = this.getDefaultState().withProperty(VARIANT_PROP, AOTDTreeTypes.GRAVEWOOD);

		if (!this.isDouble())
		{
			iblockstate = iblockstate.withProperty(HALF_PROP, (meta & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
		}

		return iblockstate;
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state)
	{
		byte b0 = 0;
		int i = b0 | ((AOTDTreeTypes) state.getValue(VARIANT_PROP)).getMetadata();

		if (!this.isDouble() && state.getValue(HALF_PROP) == BlockSlab.EnumBlockHalf.TOP)
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
		{ VARIANT_PROP }) : new BlockState(this, new IProperty[]
		{ HALF_PROP, VARIANT_PROP });
	}

	/**
	 * Get the damage value that this Block should drop
	 */
	@Override
	public int damageDropped(IBlockState state)
	{
		return ((AOTDTreeTypes) state.getValue(VARIANT_PROP)).getMetadata();
	}

	@Override
	public String getUnlocalizedName()
	{
		return String.format("tile.%s%s", Refrence.MOD_ID.toLowerCase() + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
		// Format for a block is: tile.modid:blockname.name
	}

	// Get the unlocalized name
	protected String getUnwrappedUnlocalizedName(String unlocalizedName)
	{
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
	}
}
