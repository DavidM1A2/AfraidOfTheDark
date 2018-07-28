/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.core;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.DavidM1A2.AfraidOfTheDark.common.reference.Reference;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class AOTDLeaves extends BlockLeaves
{
	// Default is gravewood and decayable
	public AOTDLeaves()
	{
		super();
		this.setCreativeTab(Reference.AFRAID_OF_THE_DARK);
		this.setDefaultState(this.blockState.getBaseState().withProperty(BlockLeaves.CHECK_DECAY, Boolean.TRUE).withProperty(BlockLeaves.DECAYABLE, Boolean.TRUE));
		this.setTickRandomly(true);
		this.leavesFancy = false;
	}

	// When the leaves are sheared
	@Override
	public List<ItemStack> onSheared(final ItemStack item, final IBlockAccess world, final BlockPos pos, final int fortune)
	{
		return NonNullList.withSize(1, new ItemStack(this, 1, 0));
	}

	// Interface implements it so we need to, but it's unused
	@Override
	public EnumType getWoodType(final int p_176233_1_)
	{
		return null;
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
	{
		list.add(new ItemStack(this, 1, 0));
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(final int meta)
	{
		return this.getDefaultState().withProperty(BlockLeaves.DECAYABLE, (meta & 4) == 0).withProperty(BlockLeaves.CHECK_DECAY, (meta & 8) > 0);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(final IBlockState state)
	{
		final byte b0 = 0;
		int i = b0;

		if (!state.getValue(BlockLeaves.DECAYABLE))
		{
			i |= 4;
		}

		if (state.getValue(BlockLeaves.CHECK_DECAY))
		{
			i |= 8;
		}

		return i;
	}

	// Create default block state
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, BlockLeaves.CHECK_DECAY, BlockLeaves.DECAYABLE);
	}

	/**
	 * Get the damage value that this Block should drop
	 */
	@Override
	public int damageDropped(final IBlockState state)
	{
		return 0;
	}

	// When the player harvests the block, what happens?
	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
	{
		if (!worldIn.isRemote && stack.getItem() == Items.SHEARS)
		{
			player.addStat(StatList.getBlockStats(this));
		}
		else
		{
			super.harvestBlock(worldIn, player, pos, state, te, stack);
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
		return String.format("tile.%s%s", Reference.MOD_ID.toLowerCase() + ":", this.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
		// Format for a block is: tile.modid:blockname.name
	}

	// Get the unlocalized name
	protected String getUnwrappedUnlocalizedName(final String unlocalizedName)
	{
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
	}
}
