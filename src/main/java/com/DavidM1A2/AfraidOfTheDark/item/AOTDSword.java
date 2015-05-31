/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.item;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

public abstract class AOTDSword extends ItemSword
{
	public AOTDSword(final ToolMaterial material)
	{
		super(material);
		this.setCreativeTab(Refrence.AFRAID_OF_THE_DARK);
	}

	// Set the item name in the game (not the visual name but the refrence name)
	@Override
	public String getUnlocalizedName()
	{
		return String.format("item.%s%s", Refrence.MOD_ID.toLowerCase() + ":", this.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}

	// Set a stack of items name?
	@Override
	public String getUnlocalizedName(final ItemStack itemStack)
	{
		return String.format("item.%s%s", Refrence.MOD_ID.toLowerCase() + ":", this.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
	}

	protected String getUnwrappedUnlocalizedName(final String unlocalizedName)
	{
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
	}
}
