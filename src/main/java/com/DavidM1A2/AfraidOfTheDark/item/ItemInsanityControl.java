package com.DavidM1A2.AfraidOfTheDark.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.playerData.Insanity;
import com.DavidM1A2.AfraidOfTheDark.utility.LogHelper;

public class ItemInsanityControl extends ItemBase
{
	public ItemInsanityControl()
	{
		super();		
		this.setUnlocalizedName("insanityControl");
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer myPlayer)
	{		
		if (!world.isRemote)
		{
			Insanity.decreaseInsanity(5, myPlayer);		
			LogHelper.info("Insanity Level = " + Insanity.get(myPlayer));
		}
		return itemStack;
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack itemStack, EntityPlayer myPlayer, Entity entity)
	{
		if (!myPlayer.worldObj.isRemote)
		{
			Insanity.increaseInsanity(5, myPlayer);
			LogHelper.info("Insanity Level = " + Insanity.get(myPlayer));
			return true;
		}		
		return true;
	}
}
