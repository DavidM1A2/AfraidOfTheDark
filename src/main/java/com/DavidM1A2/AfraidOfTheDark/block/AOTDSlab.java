package com.DavidM1A2.AfraidOfTheDark.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

public abstract class AOTDSlab extends BlockSlab
{
	public static final PropertyBool SEAMLESS_PROP = PropertyBool.create("seamless");
	public static final PropertyEnum VARIANT_PROP = PropertyEnum.create("variant", AOTDSlab.EnumType.class);

	public AOTDSlab(Material material)
	{
		super(material);
		this.setUnlocalizedName("NAME NOT SET");
		this.setCreativeTab(Refrence.AFRAID_OF_THE_DARK);

		IBlockState iblockstate = this.blockState.getBaseState();

		if (this.isDouble())
		{
			iblockstate = iblockstate.withProperty(SEAMLESS_PROP, Boolean.valueOf(false));
		}
		else
		{
			iblockstate = iblockstate.withProperty(HALF_PROP, BlockSlab.EnumBlockHalf.BOTTOM);
		}

		this.setDefaultState(iblockstate.withProperty(VARIANT_PROP, AOTDSlab.EnumType.GRAVEWOOD));
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 * 
	 * @param fortune
	 *            the level of the Fortune enchantment on the player's tool
	 */
	public abstract Item getItemDropped(IBlockState state, Random rand, int fortune);

	public IProperty func_176551_l()
	{
		return VARIANT_PROP;
	}

	@Override
	public String getUnlocalizedName()
	{
		return String.format("tile.%s%s", Refrence.MOD_ID.toLowerCase() + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
		// Format for a block is: tile.modid:blockname.name
	}

	/**
	 * Returns the slab block name with the type associated with it
	 */
	public String getFullSlabName(int p_150002_1_)
	{
		return super.getUnlocalizedName() + "." + AOTDSlab.EnumType.func_176625_a(p_150002_1_).func_176627_c();
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		if (itemIn != Item.getItemFromBlock(ModBlocks.gravewoodDoubleSlab))
		{
			AOTDSlab.EnumType[] aenumtype = AOTDSlab.EnumType.values();
			int i = aenumtype.length;

			for (int j = 0; j < i; ++j)
			{
				AOTDSlab.EnumType enumtype = aenumtype[j];

				list.add(new ItemStack(itemIn, 1, enumtype.func_176624_a()));
			}
		}
	}

	public Object func_176553_a(ItemStack p_176553_1_)
	{
		return AOTDSlab.EnumType.func_176625_a(p_176553_1_.getMetadata() & 7);
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState iblockstate = this.getDefaultState().withProperty(VARIANT_PROP, AOTDSlab.EnumType.func_176625_a(meta & 7));

		if (this.isDouble())
		{
			iblockstate = iblockstate.withProperty(SEAMLESS_PROP, Boolean.valueOf((meta & 8) != 0));
		}
		else
		{
			iblockstate = iblockstate.withProperty(HALF_PROP, (meta & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
		}

		return iblockstate;
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		try
		{
			return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
		}
		catch (IllegalArgumentException e)
		{
			return this.getDefaultState();
		}
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState state)
	{
		byte b0 = 0;
		int i = b0 | ((AOTDSlab.EnumType) state.getValue(VARIANT_PROP)).func_176624_a();

		if (this.isDouble())
		{
			if (((Boolean) state.getValue(SEAMLESS_PROP)).booleanValue())
			{
				i |= 8;
			}
		}
		else if (state.getValue(HALF_PROP) == BlockSlab.EnumBlockHalf.TOP)
		{
			i |= 8;
		}

		return i;
	}

	protected BlockState createBlockState()
	{
		return this.isDouble() ? new BlockState(this, new IProperty[]
		{ SEAMLESS_PROP, VARIANT_PROP }) : new BlockState(this, new IProperty[]
		{ HALF_PROP, VARIANT_PROP });
	}

	// Get the unlocalized name
	protected String getUnwrappedUnlocalizedName(String unlocalizedName)
	{
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
	}

	/**
	 * Get the damage value that this Block should drop
	 */
	public int damageDropped(IBlockState state)
	{
		return ((BlockStoneSlab.EnumType) state.getValue(VARIANT_PROP)).func_176624_a();
	}

	public static enum EnumType implements IStringSerializable
	{
		GRAVEWOOD(0, "gravewood");
		private static final AOTDSlab.EnumType[] field_176640_i = new AOTDSlab.EnumType[values().length];
		private final int number;
		private final String name1;
		private final String name2;

		private EnumType(int number, String name)
		{
			this(number, name, name);
		}

		private EnumType(int number, String name, String name2)
		{
			this.number = number;
			this.name1 = name;
			this.name2 = name2;
		}

		public int func_176624_a()
		{
			return this.number;
		}

		public String toString()
		{
			return this.name1;
		}

		public static AOTDSlab.EnumType func_176625_a(int p_176625_0_)
		{
			if (p_176625_0_ < 0 || p_176625_0_ >= field_176640_i.length)
			{
				p_176625_0_ = 0;
			}

			return field_176640_i[p_176625_0_];
		}

		public String getName()
		{
			return this.name1;
		}

		public String func_176627_c()
		{
			return this.name2;
		}

		static
		{
			AOTDSlab.EnumType[] var0 = values();
			int var1 = var0.length;

			for (int var2 = 0; var2 < var1; ++var2)
			{
				AOTDSlab.EnumType var3 = var0[var2];
				field_176640_i[var3.func_176624_a()] = var3;
			}
		}
	}
}
