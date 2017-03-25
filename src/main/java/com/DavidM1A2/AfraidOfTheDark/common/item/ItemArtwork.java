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
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemArtwork extends AOTDItem
{
	public ItemArtwork()
	{
		super();
		this.setHasSubtypes(true);
		this.setUnlocalizedName("artwork");
		this.setRegistryName("artwork");
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> subItems)
	{
		for (int i = 0; i < AOTDArt.values().length; i++)
			subItems.add(new ItemStack(item, 1, i));
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer entityPlayer, World world, BlockPos blockPos, EnumHand hand, EnumFacing side, float facing, float hitX, float hitZ)
	{
		ItemStack itemStack = entityPlayer.getHeldItem(hand);

		if (side != EnumFacing.DOWN && side != EnumFacing.UP && entityPlayer.canPlayerEdit(blockPos, side, itemStack))
		{
			BlockPos offsetted = blockPos.offset(side);
			AOTDArt artwork = artFromItemstack(itemStack);
			if (artwork != null)
			{
				EntityArtwork painting = new EntityArtwork(world, offsetted, side, artwork);
				if (painting.onValidSurface())
				{
					if (!entityPlayer.world.isRemote)
					{
						painting.playPlaceSound();
						world.spawnEntity(painting);
					}
					itemStack.shrink(1);
					return EnumActionResult.SUCCESS;
				}
			}
		}
		return EnumActionResult.FAIL;
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
