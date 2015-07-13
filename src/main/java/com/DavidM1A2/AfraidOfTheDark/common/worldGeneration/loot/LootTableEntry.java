package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot;

import net.minecraft.item.Item;

public class LootTableEntry
{
	private final IChestGenerator loot;
	private final Item toReplace;
	private final int numberOfItemsToGenerate;

	public LootTableEntry(IChestGenerator loot, Item toReplace, int numberOfItemsToGenerate)
	{
		this.loot = loot;
		this.toReplace = toReplace;
		this.numberOfItemsToGenerate = numberOfItemsToGenerate;
	}

	public IChestGenerator getLoot()
	{
		return this.loot;
	}

	public Item getItemToRepalce()
	{
		return this.toReplace;
	}

	public int numberOfItemsToGenerate()
	{
		return this.numberOfItemsToGenerate;
	}
}
