/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;

public class LootTable
{
	private final LootTableEntry[] lootTableEntries;

	public LootTable(LootTableEntry... lootTables)
	{
		lootTableEntries = lootTables;
	}

	public void generate(TileEntityChest chest)
	{
		LootTableEntry nullEntry = null;
		if (chest == null)
		{
			System.err.println("Error, chest is null.");
			return;
		}
		for (LootTableEntry lootTableEntry : lootTableEntries)
		{
			for (int i = 0; i < chest.getSizeInventory(); i++)
			{
				ItemStack itemStack = chest.getStackInSlot(i);

				if (lootTableEntry.getItemToRepalce() == null)
				{
					nullEntry = lootTableEntry;
				}
				if (itemStack != null && lootTableEntry.getItemToRepalce() == itemStack.getItem())
				{
					chest.clear();
					chest.setLootTable(lootTableEntry.getLoot(), chest.getWorld().rand.nextLong());
					return;
				}
			}
		}

		if (nullEntry != null)
		{
			chest.clear();
			chest.setLootTable(nullEntry.getLoot(), chest.getWorld().rand.nextLong());
		}
	}
}
