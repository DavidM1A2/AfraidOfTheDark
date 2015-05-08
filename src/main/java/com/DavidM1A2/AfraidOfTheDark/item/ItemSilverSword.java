/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

import com.DavidM1A2.AfraidOfTheDark.entities.WereWolf.EntityWereWolf;
import com.DavidM1A2.AfraidOfTheDark.playerData.HasStartedAOTD;
import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

// Silversword item which is a sword
public class ItemSilverSword extends ItemSword
{
	public ItemSilverSword()
	{
		super(Refrence.silver);
		this.setUnlocalizedName("silverSword");
		this.setCreativeTab(Refrence.AFRAID_OF_THE_DARK);
	}

	// When left clicking attack from silver weapon damage
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		if (entity instanceof EntityWereWolf)
		{
			if (HasStartedAOTD.get(player))
			{
				entity.attackEntityFrom(Refrence.silverWeapon, 8);
			}
		}
		return false;
	}

	// Set the item name in the game (not the visual name but the refrence name)
	@Override
	public String getUnlocalizedName()
	{
		return String.format("item.%s%s", Refrence.MOD_ID.toLowerCase() + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}

	// Set a stack of items name?
	@Override
	public String getUnlocalizedName(ItemStack itemStack)
	{
		return String.format("item.%s%s", Refrence.MOD_ID.toLowerCase() + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}

	protected String getUnwrappedUnlocalizedName(String unlocalizedName)
	{
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
	}
}
