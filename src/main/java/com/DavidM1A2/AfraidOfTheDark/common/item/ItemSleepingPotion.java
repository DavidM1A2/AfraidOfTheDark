package com.DavidM1A2.AfraidOfTheDark.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModPotionEffects;
import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItem;

public class ItemSleepingPotion extends AOTDItem
{
	public ItemSleepingPotion()
	{
		super();
		this.setUnlocalizedName("sleepingPotion");
	}

	/**
	 * How long it takes to use or consume an item
	 */
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 32;
	}

	/**
	 * returns the action that specifies what animation to play when the items is being used
	 */
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.DRINK;
	}

	/**
	 * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using the Item before the action is
	 * complete.
	 */
	@Override
	public ItemStack onItemUseFinish(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		if (!world.isRemote)
		{
			entityPlayer.addPotionEffect(new PotionEffect(ModPotionEffects.sleepingPotion.id, 4800, 0, false, true));
		}

		if (!entityPlayer.capabilities.isCreativeMode)
		{
			itemStack.stackSize = itemStack.stackSize - 1;

			if (itemStack.stackSize <= 0)
			{
				return new ItemStack(Items.glass_bottle);
			}

			entityPlayer.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
		}

		return itemStack;
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		entityPlayer.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
		return itemStack;
	}
}
