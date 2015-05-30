/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.playerData.Insanity;
import com.DavidM1A2.AfraidOfTheDark.utility.LogHelper;

public class ItemInsanityControl extends AOTDItem
{
	// Set the item name
	public ItemInsanityControl()
	{
		super();
		this.setUnlocalizedName("insanityControl");
	}

	// When rightclicking + holding shift, decrease insanity, else increase it
	@Override
	public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer myPlayer)
	{
		if (!world.isRemote)
		{
			if (myPlayer.isSneaking())
			{
				Insanity.addInsanity(-5, myPlayer);
				LogHelper.info("Insanity Level = " + Insanity.get(myPlayer));
			}
			else
			{
				Insanity.addInsanity(5, myPlayer);
				LogHelper.info("Insanity Level = " + Insanity.get(myPlayer));
			}
		}
		return itemStack;
	}
}
