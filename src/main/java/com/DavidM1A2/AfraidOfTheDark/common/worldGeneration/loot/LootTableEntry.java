/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot;

import java.util.Random;

import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;

public class LootTableEntry
{
	private final IChestGenerator loot;
	private final Item toReplace;
	private final int numberOfItemsToGenerateMin;
	private final int numberOfItemsToGenerateMax;
	private static Random random = null;

	public LootTableEntry(IChestGenerator loot, Item toReplace, int numberOfItemsToGenerate)
	{
		this.loot = loot;
		this.toReplace = toReplace;
		this.numberOfItemsToGenerateMin = numberOfItemsToGenerate;
		this.numberOfItemsToGenerateMax = numberOfItemsToGenerate;
	}

	public LootTableEntry(IChestGenerator loot, Item toReplace, int numberOfItemsToGenerateMin, int numberOfItemsToGenerateMax)
	{
		this.loot = loot;
		this.toReplace = toReplace;
		this.numberOfItemsToGenerateMin = numberOfItemsToGenerateMin;
		this.numberOfItemsToGenerateMax = numberOfItemsToGenerateMax;
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
		if (random == null)
		{
			random = new Random(MinecraftServer.getServer().worldServerForDimension(0).getSeed());
		}
		return random.nextInt((numberOfItemsToGenerateMax - numberOfItemsToGenerateMin) + 1) + numberOfItemsToGenerateMin;
	}
}
