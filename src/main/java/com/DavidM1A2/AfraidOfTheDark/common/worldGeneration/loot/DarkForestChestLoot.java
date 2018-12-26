/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration.loot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class DarkForestChestLoot implements IChestGenerator
{
	@Override
	public List<RandomItem> getPossibleItems(Random random)
	{
		ArrayList<RandomItem> toReturn = new ArrayList<RandomItem>();

		ItemStack whiteHelmet = new ItemStack(Items.LEATHER_HELMET);
		ItemStack whiteLeggings = new ItemStack(Items.LEATHER_LEGGINGS);
		ItemStack whiteChestplate = new ItemStack(Items.LEATHER_CHESTPLATE);
		ItemStack whiteBoots = new ItemStack(Items.LEATHER_BOOTS);

		Items.LEATHER_HELMET.setColor(whiteHelmet, 0xFFFFFF);
		Items.LEATHER_LEGGINGS.setColor(whiteLeggings, 0xFFFFFF);
		Items.LEATHER_CHESTPLATE.setColor(whiteChestplate, 0xFFFFFF);
		Items.LEATHER_BOOTS.setColor(whiteBoots, 0xFFFFFF);

		RandomItem clocks = new RandomItem(Items.CLOCK, 10);
		RandomItem bones = new RandomItem(Items.BONE, 35);
		RandomItem leatherHelmet = new RandomItem(whiteHelmet, 4);
		RandomItem leatherLeggings = new RandomItem(whiteLeggings, 4);
		RandomItem leatherChestplate = new RandomItem(whiteChestplate, 4);
		RandomItem leatherBoots = new RandomItem(whiteBoots, 4);
		RandomItem cloakOfAgilityResearch = new RandomItem(ModItems.researchScrollCloakOfAgility, 1);

		toReturn.add(clocks);
		toReturn.add(bones);
		toReturn.add(leatherHelmet);
		toReturn.add(leatherLeggings);
		toReturn.add(leatherChestplate);
		toReturn.add(leatherBoots);
		toReturn.add(cloakOfAgilityResearch);

		return toReturn;
	}
}
