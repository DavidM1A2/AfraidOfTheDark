/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item.core;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public abstract class AOTDItemWithCooldownStatic extends AOTDItemWithCooldownPerItem
{
	public AOTDItemWithCooldownStatic()
	{
		super();
		this.setMaxStackSize(1);
	}

	@Override
	public void setOnCooldown(ItemStack itemStack, EntityPlayer entityPlayer)
	{
		for (ItemStack similarItemStack : this.getAllSimilarItems(entityPlayer, itemStack))
			super.setOnCooldown(similarItemStack, entityPlayer);
	}

	private List<ItemStack> getAllSimilarItems(EntityPlayer entityPlayer, ItemStack itemStackOriginal)
	{
		List<ItemStack> similarItems = new LinkedList<ItemStack>();
		if (itemStackOriginal != null)
			for (ItemStack itemStack : entityPlayer.inventory.mainInventory)
				if (itemStack != null && itemStack.getItem().equals(itemStackOriginal.getItem()))
					similarItems.add(itemStack);
		return similarItems;
	}
}
