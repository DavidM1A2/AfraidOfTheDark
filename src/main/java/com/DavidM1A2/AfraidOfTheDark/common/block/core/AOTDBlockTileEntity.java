/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.block.core;

import javax.annotation.Nullable;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;

public abstract class AOTDBlockTileEntity extends AOTDBlock implements ITileEntityProvider
{
	// See BlockContainer class
	public AOTDBlockTileEntity(Material material)
	{
		super(material);
		this.isBlockContainer = true;
	}

	/**
	 * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
	 */
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		super.breakBlock(worldIn, pos, state);
		worldIn.removeTileEntity(pos);
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
	{
		if (te instanceof IWorldNameable && ((IWorldNameable) te).hasCustomName())
		{
			player.addStat(StatList.getBlockStats(this));
			player.addExhaustion(0.005F);

			if (worldIn.isRemote)
			{
				return;
			}

			int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
			Item item = this.getItemDropped(state, worldIn.rand, i);

			if (item == Items.AIR)
			{
				return;
			}

			ItemStack itemstack = new ItemStack(item, this.quantityDropped(worldIn.rand));
			itemstack.setStackDisplayName(((IWorldNameable) te).getName());
			spawnAsEntity(worldIn, pos, itemstack);
		}
		else
		{
			super.harvestBlock(worldIn, player, pos, state, (TileEntity) null, stack);
		}
	}

	/**
	 * Called on both Client and Server when World#addBlockEvent is called. On the Server, this may perform additional changes to the world, like
	 * pistons replacing the block with an extended base. On the client, the update may involve replacing tile entities, playing sounds, or performing
	 * other visual actions to reflect the server side changes.
	 */
	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
	{
		super.eventReceived(state, worldIn, pos, id, param);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
	}
}
