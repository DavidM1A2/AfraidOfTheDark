/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModPotionEffects;
import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItem;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack)
	{
		return true;
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
	public ItemStack onItemUseFinish(ItemStack itemStack, World world, EntityLivingBase entityLiving)
	{
		if (entityLiving instanceof EntityPlayer)
		{
			EntityPlayer entityPlayer = (EntityPlayer) entityLiving;
			if (!world.isRemote)
			{
				entityPlayer.addPotionEffect(new PotionEffect(ModPotionEffects.sleepingPotion, 4800, 0, false, true));
			}

			if (!entityPlayer.capabilities.isCreativeMode)
			{
				itemStack.func_190917_f(-1);

				if (itemStack.func_190916_E() <= 0)
				{
					return new ItemStack(Items.GLASS_BOTTLE);
				}

				entityPlayer.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
			}
		}

		return itemStack;
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityPlayer, EnumHand hand)
	{
		entityPlayer.setActiveHand(hand);
		return ActionResult.<ItemStack> newResult(EnumActionResult.SUCCESS, entityPlayer.getHeldItem(hand));
	}
}
