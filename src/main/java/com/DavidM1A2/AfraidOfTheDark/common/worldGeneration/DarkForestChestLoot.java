/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.worldGeneration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;

public class DarkForestChestLoot implements IChestGenerator
{
	@Override
	public List<WeightedRandomChestContent> getPossibleItems(Random random)
	{
		ArrayList<WeightedRandomChestContent> toReturn = new ArrayList<WeightedRandomChestContent>();

		ItemStack whiteHelmet = new ItemStack(Items.leather_helmet);
		ItemStack whiteLeggings = new ItemStack(Items.leather_leggings);
		ItemStack whiteChestplate = new ItemStack(Items.leather_chestplate);
		ItemStack whiteBoots = new ItemStack(Items.leather_boots);

		Items.leather_helmet.func_82813_b(whiteHelmet, 0xFFFFFF);
		Items.leather_leggings.func_82813_b(whiteLeggings, 0xFFFFFF);
		Items.leather_chestplate.func_82813_b(whiteChestplate, 0xFFFFFF);
		Items.leather_boots.func_82813_b(whiteBoots, 0xFFFFFF);

		WeightedRandomChestContent clocks = new WeightedRandomChestContent(Items.clock, 0, 1, 1, 10);
		WeightedRandomChestContent bones = new WeightedRandomChestContent(Items.bone, 0, 3, 5, 35);
		WeightedRandomChestContent leatherHelmet = new WeightedRandomChestContent(whiteHelmet, 1, 1, 4);
		WeightedRandomChestContent leatherLeggings = new WeightedRandomChestContent(whiteLeggings, 1, 1, 4);
		WeightedRandomChestContent leatherChestplate = new WeightedRandomChestContent(whiteChestplate, 1, 1, 4);
		WeightedRandomChestContent leatherBoots = new WeightedRandomChestContent(whiteBoots, 1, 1, 4);
		WeightedRandomChestContent cloakOfAgilityResearch = new WeightedRandomChestContent(ModItems.researchScrollCloakOfAgility, 0, 1, 1, 1);

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
