/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block;

import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDTreeTypes;
import com.DavidM1A2.AfraidOfTheDark.common.reference.Reference;
import com.google.common.base.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGravewood extends BlockLog
{
	// Different log variants
	public static final PropertyEnum VARIANT = PropertyEnum.create("variant", AOTDTreeTypes.class, new Predicate<AOTDTreeTypes>()
	{
		@Override
		public boolean apply(final AOTDTreeTypes type)
		{
			if (type == AOTDTreeTypes.GRAVEWOOD)
				return true;
			return false;
		}
	});

	/*
	 * Define a gravewood block
	 */
	public BlockGravewood()
	{
		super();
		this.setCreativeTab(Reference.AFRAID_OF_THE_DARK);
		this.setStepSound(Block.soundTypeWood);
		this.setUnlocalizedName("gravewood");
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final List list)
	{
		list.add(new ItemStack(itemIn, 1, AOTDTreeTypes.GRAVEWOOD.getMetadata()));
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(final int meta)
	{
		IBlockState iblockstate = this.getDefaultState();

		switch (meta & 12)
		{
			case 0:
				iblockstate = iblockstate.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y);
				break;
			case 4:
				iblockstate = iblockstate.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.X);
				break;
			case 8:
				iblockstate = iblockstate.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Z);
				break;
			default:
				iblockstate = iblockstate.withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.NONE);
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
		int i = b0 | state.<AOTDTreeTypes> getValue(BlockGravewood.VARIANT).getMetadata();

		switch (state.<BlockLog.EnumAxis> getValue(LOG_AXIS))
		{
			case X:
				i |= 4;
				break;
			case Z:
				i |= 8;
				break;
			case NONE:
				i |= 12;
				break;
			default:
				break;
		}

		return i;
	}

	// Default block states
	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[]
		{ BlockGravewood.VARIANT, BlockLog.LOG_AXIS });
	}

	// Can these woods stack?
	@Override
	protected ItemStack createStackedBlock(final IBlockState state)
	{
		return new ItemStack(Item.getItemFromBlock(this), 1, state.<AOTDTreeTypes> getValue(BlockGravewood.VARIANT).getMetadata());
	}

	/**
	 * Get the damage value that this Block should drop
	 */
	@Override
	public int damageDropped(final IBlockState state)
	{
		return state.<AOTDTreeTypes> getValue(BlockGravewood.VARIANT).getMetadata();
	}

	@Override
	public String getUnlocalizedName()
	{
		return String.format("tile.%s%s", Reference.MOD_ID.toLowerCase() + ":", this.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
		// tile.modid:blockname.name
	}

	protected String getUnwrappedUnlocalizedName(final String unlocalizedName)
	{
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
	}
}
