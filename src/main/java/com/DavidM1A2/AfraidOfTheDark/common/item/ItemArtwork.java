/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.entities.EntityArtwork.EntityArtwork;
import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItem;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDArt;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemArtwork extends AOTDItem
{
	public ItemArtwork()
	{
		super();
		this.setHasSubtypes(true);
		this.setUnlocalizedName("artwork");
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subItems)
	{
		for (int i = 0; i < AOTDArt.values().length; i++)
			subItems.add(new ItemStack(item, 1, i));
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 */
	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (side == EnumFacing.DOWN)
			return false;
		else if (side == EnumFacing.UP)
			return false;
		else
		{
			BlockPos offsetted = blockPos.offset(side);
			if (!entityPlayer.canPlayerEdit(offsetted, side, itemStack))
				return false;
			else
			{
				AOTDArt artwork = artFromItemstack(itemStack);
				if (artwork == null)
					return false;

				EntityArtwork painting = new EntityArtwork(world, offsetted, side, artwork);
				if (painting.onValidSurface())
				{
					if (!world.isRemote)
						world.spawnEntityInWorld(painting);

					itemStack.stackSize--;
				}
				return true;
			}
		}
	}

	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> tooltip, boolean advanced)
	{
		AOTDArt artwork = artFromItemstack(itemStack);
		if (artwork != null)
			tooltip.add("Painting: " + artwork.formattedString());
	}

	private AOTDArt artFromItemstack(ItemStack itemStack)
	{
		if (itemStack.getMetadata() < AOTDArt.values().length)
			return AOTDArt.values()[itemStack.getMetadata()];
		return null;
	}
}
