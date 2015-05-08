/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.block;

import java.util.List;

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

import com.DavidM1A2.AfraidOfTheDark.refrence.AOTDTreeTypes;
import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;
import com.google.common.base.Predicate;

public class BlockGravewood extends BlockLog
{
	public static final PropertyEnum VARIANT = PropertyEnum.create("variant", AOTDTreeTypes.class, new Predicate()
	{
		@Override
		public boolean apply(Object input)
		{
			AOTDTreeTypes type = (AOTDTreeTypes) input;
			if (type == AOTDTreeTypes.GRAVEWOOD)
			{
				return true;
			}
			return false;
		}
	});

	/*
	 * Define a gravewood block
	 */
	public BlockGravewood()
	{
		super();
		this.setCreativeTab(Refrence.AFRAID_OF_THE_DARK);
		this.setUnlocalizedName("gravewood");
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		list.add(new ItemStack(itemIn, 1, AOTDTreeTypes.GRAVEWOOD.getMetadata()));
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState iblockstate = this.getDefaultState();

		switch (meta & 12)
		{
			case 0:
				iblockstate = iblockstate.withProperty(AXIS_PROP, BlockLog.EnumAxis.Y);
				break;
			case 4:
				iblockstate = iblockstate.withProperty(AXIS_PROP, BlockLog.EnumAxis.X);
				break;
			case 8:
				iblockstate = iblockstate.withProperty(AXIS_PROP, BlockLog.EnumAxis.Z);
				break;
			default:
				iblockstate = iblockstate.withProperty(AXIS_PROP, BlockLog.EnumAxis.NONE);
		}

		return iblockstate;
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState state)
	{
		byte b0 = 0;
		int i = b0 | ((AOTDTreeTypes) state.getValue(VARIANT)).getMetadata();

		switch (BlockGravewood.SwitchEnumAxis.switchAxis[((BlockLog.EnumAxis) state.getValue(AXIS_PROP)).ordinal()])
		{
			case 1:
				i |= 4;
				break;
			case 2:
				i |= 8;
				break;
			case 3:
				i |= 12;
		}

		return i;
	}

	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[]
		{ VARIANT, AXIS_PROP });
	}

	protected ItemStack createStackedBlock(IBlockState state)
	{
		return new ItemStack(Item.getItemFromBlock(this), 1, ((AOTDTreeTypes) state.getValue(VARIANT)).getMetadata());
	}

	/**
	 * Get the damage value that this Block should drop
	 */
	public int damageDropped(IBlockState state)
	{
		return ((AOTDTreeTypes) state.getValue(VARIANT)).getMetadata();
	}

	@Override
	public String getUnlocalizedName()
	{
		return String.format("tile.%s%s", Refrence.MOD_ID.toLowerCase() + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
		// tile.modid:blockname.name
	}

	protected String getUnwrappedUnlocalizedName(String unlocalizedName)
	{
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
	}

	private static final class SwitchEnumAxis
	{
		static final int[] switchAxis = new int[BlockLog.EnumAxis.values().length];

		static
		{
			try
			{
				switchAxis[BlockLog.EnumAxis.X.ordinal()] = 1;
			}
			catch (NoSuchFieldError var3)
			{
			}

			try
			{
				switchAxis[BlockLog.EnumAxis.Z.ordinal()] = 2;
			}
			catch (NoSuchFieldError var2)
			{
			}

			try
			{
				switchAxis[BlockLog.EnumAxis.NONE.ordinal()] = 3;
			}
			catch (NoSuchFieldError var1)
			{
			}
		}
	}
}
