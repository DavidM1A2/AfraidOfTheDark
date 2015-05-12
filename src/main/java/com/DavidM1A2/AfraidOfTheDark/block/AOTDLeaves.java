/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.DavidM1A2.AfraidOfTheDark.refrence.AOTDTreeTypes;
import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;
import com.google.common.base.Predicate;

public abstract class AOTDLeaves extends BlockLeaves
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

	public AOTDLeaves()
	{
		super();
		this.setCreativeTab(Refrence.AFRAID_OF_THE_DARK);
		this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, AOTDTreeTypes.GRAVEWOOD).withProperty(field_176236_b, Boolean.valueOf(true)).withProperty(field_176237_a, Boolean.valueOf(true)));
		this.setGraphicsLevel(true);
		this.setTickRandomly(true);
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
	{
		List<ItemStack> ret = new ArrayList<ItemStack>();
		int meta = this.getMetaFromState(this.getDefaultState());
		ret.add(new ItemStack(this, 1, meta));
		return ret;
	}

	@SideOnly(Side.CLIENT)
	public int getBlockColor()
	{
		return ColorizerFoliage.getFoliageColor(0.5D, 1.0D);
	}

	@SideOnly(Side.CLIENT)
	public int getRenderColor(IBlockState state)
	{
		return ColorizerFoliage.getFoliageColorPine();
	}

	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
	{
		return ColorizerFoliage.getFoliageColorPine();
	}

	@Override
	public EnumType func_176233_b(int p_176233_1_)
	{
		return null;
	}

	// on decay i believe
	@Override
	protected void func_176234_a(World worldIn, BlockPos p_176234_2_, IBlockState p_176234_3_, int p_176234_4_)
	{
		return;
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
	{
		list.add(new ItemStack(itemIn, 1, AOTDTreeTypes.GRAVEWOOD.getMetadata()));
	}

	@Override
	protected ItemStack createStackedBlock(IBlockState state)
	{
		return new ItemStack(Item.getItemFromBlock(this), 1, ((AOTDTreeTypes) state.getValue(VARIANT)).getMetadata());
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(VARIANT, AOTDTreeTypes.getTypeFromMeta(meta)).withProperty(field_176237_a, Boolean.valueOf((meta & 4) == 0)).withProperty(field_176236_b, Boolean.valueOf((meta & 8) > 0));
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state)
	{
		byte b0 = 0;
		int i = b0 | ((AOTDTreeTypes) state.getValue(VARIANT)).getMetadata();

		if (!((Boolean) state.getValue(field_176237_a)).booleanValue())
		{
			i |= 4;
		}

		if (((Boolean) state.getValue(field_176236_b)).booleanValue())
		{
			i |= 8;
		}

		return i;
	}

	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[]
		{ VARIANT, field_176236_b, field_176237_a });
	}

	/**
	 * Get the damage value that this Block should drop
	 */
	@Override
	public int damageDropped(IBlockState state)
	{
		return ((AOTDTreeTypes) state.getValue(VARIANT)).getMetadata();
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer playerIn, BlockPos pos, IBlockState state, TileEntity te)
	{
		if (!worldIn.isRemote && playerIn.getCurrentEquippedItem() != null && playerIn.getCurrentEquippedItem().getItem() == Items.shears)
		{
			playerIn.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
			spawnAsEntity(worldIn, pos, new ItemStack(Item.getItemFromBlock(this), 1, ((AOTDTreeTypes) state.getValue(VARIANT)).getMetadata()));
		}
		else
		{
			super.harvestBlock(worldIn, playerIn, pos, state, te);
		}
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 * 
	 * @param fortune
	 *            the level of the Fortune enchantment on the player's tool
	 */
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return null;
	}

	@Override
	public void updateTick(World world, BlockPos position, IBlockState state, Random random)
	{
		super.updateTick(world, position, state, random);
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
