package com.DavidM1A2.AfraidOfTheDark.item;

import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public abstract class AOTDSword extends ItemSword
{
	public AOTDSword(ToolMaterial material) 
	{
		super(material);
		this.setCreativeTab(Refrence.AFRAID_OF_THE_DARK);
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
