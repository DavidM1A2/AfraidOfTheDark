/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot;

import java.util.Random;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class LootTableEntry
{
	private final ResourceLocation loot;
	private final Item toReplace;
	private static Random random = null;

	public LootTableEntry(ResourceLocation loot, Item toReplace)
	{
		this.loot = loot;
		this.toReplace = toReplace;
	}

	public ResourceLocation getLoot()
	{
		return this.loot;
	}

	public Item getItemToRepalce()
	{
		return this.toReplace;
	}
}
