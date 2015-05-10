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
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

public abstract class AOTDSlab extends BlockSlab
{
	public static final PropertyEnum VARIANT_PROP = PropertyEnum.create("variant", AOTDSlab.EnumType.class);

	public AOTDSlab(Material material)
	{
		super(material);
		this.setUnlocalizedName("NAME NOT SET");
		this.setCreativeTab(Refrence.AFRAID_OF_THE_DARK);
		this.setHardness(2.0F);
		this.setResistance(5.0F);
		this.setStepSound(soundTypeWood);

		IBlockState iblockstate = this.blockState.getBaseState();

		if (!this.isDouble())
		{
			iblockstate = iblockstate.withProperty(HALF_PROP, BlockSlab.EnumBlockHalf.BOTTOM);
		}
		this.useNeighborBrightness = !this.isDouble();

		this.setDefaultState(iblockstate.withProperty(VARIANT_PROP, AOTDSlab.EnumType.GRAVEWOOD));
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 * 
	 * @param fortune
	 *            the level of the Fortune enchantment on the player's tool
	 */
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(ModBlocks.gravewoodHalfSlab);
	}

	@SideOnly(Side.CLIENT)
	public Item getItem(World worldIn, BlockPos pos)
	{
		return Item.getItemFromBlock(ModBlocks.gravewoodHalfSlab);
	}

	/**
	 * Returns the slab block name with the type associated with it
	 */
	@Override
	public String getFullSlabName(int p_150002_1_)
	{
		return super.getUnlocalizedName() + "." + AOTDSlab.EnumType.func_176837_a(p_150002_1_).func_176840_c();
	}

	@Override
	public IProperty func_176551_l()
	{
		return VARIANT_PROP;
	}

	@Override
	public Object func_176553_a(ItemStack itemStack)
	{
		return AOTDSlab.EnumType.func_176837_a(itemStack.getMetadata() & 7);
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
			AOTDSlab.EnumType[] aenumtype = AOTDSlab.EnumType.values();
			int i = aenumtype.length;

			for (int j = 0; j < i; ++j)
			{
				AOTDSlab.EnumType enumtype = aenumtype[j];
				list.add(new ItemStack(itemIn, 1, enumtype.func_176839_a()));
			}
		}
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState iblockstate = this.getDefaultState().withProperty(VARIANT_PROP, AOTDSlab.EnumType.GRAVEWOOD);

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
		int i = b0 | ((AOTDSlab.EnumType) state.getValue(VARIANT_PROP)).func_176839_a();

		if (!this.isDouble() && state.getValue(HALF_PROP) == BlockSlab.EnumBlockHalf.TOP)
		{
			i |= 8;
		}

		return i;
	}

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
		return ((AOTDSlab.EnumType) state.getValue(VARIANT_PROP)).func_176839_a();
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

	public static enum EnumType implements IStringSerializable
	{
		GRAVEWOOD(0, "gravewood");

		private static final AOTDSlab.EnumType[] aotdSlabs = new AOTDSlab.EnumType[values().length];
		private final int meta;
		private final String name1;
		private final String name2;

		private static final String __OBFID = "CL_00002081";

		private EnumType(int meta, String name)
		{
			this(meta, name, name);
		}

		private EnumType(int meta, String name1, String name2)
		{
			this.meta = meta;
			this.name1 = name1;
			this.name2 = name2;
		}

		public int func_176839_a()
		{
			return this.meta;
		}

		@Override
		public String toString()
		{
			return this.name1;
		}

		public static AOTDSlab.EnumType func_176837_a(int metaData)
		{
			if (metaData < 0 || metaData >= aotdSlabs.length)
			{
				metaData = 0;
			}

			return aotdSlabs[metaData];
		}

		@Override
		public String getName()
		{
			return this.name1;
		}

		public String func_176840_c()
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
				aotdSlabs[var3.func_176839_a()] = var3;
			}
		}
	}
}
