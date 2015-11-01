/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.AOTDTreeTypes;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Refrence;
import com.google.common.base.Predicate;

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

public abstract class AOTDLeaves extends BlockLeaves
{
	// Leaves have a few variants
	public static final PropertyEnum VARIANT = PropertyEnum.create("variant", AOTDTreeTypes.class, new Predicate()
	{
		@Override
		public boolean apply(final Object input)
		{
			final AOTDTreeTypes type = (AOTDTreeTypes) input;
			if (type == AOTDTreeTypes.GRAVEWOOD)
			{
				return true;
			}
			return false;
		}
	});

	// Default is gravewood and decayable
	public AOTDLeaves()
	{
		super();
		this.setCreativeTab(Constants.AFRAID_OF_THE_DARK);
		this.setDefaultState(this.blockState.getBaseState().withProperty(AOTDLeaves.VARIANT, AOTDTreeTypes.GRAVEWOOD).withProperty(BlockLeaves.field_176236_b, Boolean.valueOf(true)).withProperty(BlockLeaves.field_176237_a, Boolean.valueOf(true)));
		this.setTickRandomly(true);
	}

	// When the leaves are sheared
	@Override
	public List<ItemStack> onSheared(final ItemStack item, final IBlockAccess world, final BlockPos pos, final int fortune)
	{
		return new ArrayList<ItemStack>();
	}

	// Always use fancy graphics because reasons (They look way better!)
	@SideOnly(Side.CLIENT)
	public void setGraphicsLevel()
	{
		super.setGraphicsLevel(true);
	}

	// Color of the leaves
	@Override
	@SideOnly(Side.CLIENT)
	public int getBlockColor()
	{
		return ColorizerFoliage.getFoliageColor(0.5D, 1.0D);
	}

	// Leaf render color
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(final IBlockState state)
	{
		return ColorizerFoliage.getFoliageColorPine();
	}

	// What color to multiply these leaves by
	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(final IBlockAccess worldIn, final BlockPos pos, final int renderPass)
	{
		return ColorizerFoliage.getFoliageColorPine();
	}

	// Interface implements it so we need to, but it's unused
	@Override
	public EnumType func_176233_b(final int p_176233_1_)
	{
		return null;
	}

	// on decay i believe (Currently no saplings!)
	@Override
	protected void func_176234_a(final World worldIn, final BlockPos p_176234_2_, final IBlockState p_176234_3_, final int p_176234_4_)
	{
		return;
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

	// Can these leaf types stack?
	@Override
	protected ItemStack createStackedBlock(final IBlockState state)
	{
		return new ItemStack(Item.getItemFromBlock(this), 1, ((AOTDTreeTypes) state.getValue(AOTDLeaves.VARIANT)).getMetadata());
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(final int meta)
	{
		return this.getDefaultState().withProperty(AOTDLeaves.VARIANT, AOTDTreeTypes.getTypeFromMeta(meta)).withProperty(BlockLeaves.field_176237_a, Boolean.valueOf((meta & 4) == 0)).withProperty(BlockLeaves.field_176236_b, Boolean.valueOf((meta & 8) > 0));
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(final IBlockState state)
	{
		final byte b0 = 0;
		int i = b0 | ((AOTDTreeTypes) state.getValue(AOTDLeaves.VARIANT)).getMetadata();

		if (!((Boolean) state.getValue(BlockLeaves.field_176237_a)).booleanValue())
		{
			i |= 4;
		}

		if (((Boolean) state.getValue(BlockLeaves.field_176236_b)).booleanValue())
		{
			i |= 8;
		}

		return i;
	}

	// Create default block state
	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[]
		{ AOTDLeaves.VARIANT, BlockLeaves.field_176236_b, BlockLeaves.field_176237_a });
	}

	/**
	 * Get the damage value that this Block should drop
	 */
	@Override
	public int damageDropped(final IBlockState state)
	{
		return ((AOTDTreeTypes) state.getValue(AOTDLeaves.VARIANT)).getMetadata();
	}

	// When the player harvests the block, what happens?
	@Override
	public void harvestBlock(final World worldIn, final EntityPlayer playerIn, final BlockPos pos, final IBlockState state, final TileEntity te)
	{
		if (!worldIn.isRemote && (playerIn.getCurrentEquippedItem() != null) && (playerIn.getCurrentEquippedItem().getItem() == Items.shears))
		{
			playerIn.triggerAchievement(StatList.mineBlockStatArray[Block.getIdFromBlock(this)]);
			Block.spawnAsEntity(worldIn, pos, new ItemStack(Item.getItemFromBlock(this), 1, ((AOTDTreeTypes) state.getValue(AOTDLeaves.VARIANT)).getMetadata()));
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
	@Override
	public abstract Item getItemDropped(final IBlockState state, final Random rand, final int fortune);

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
