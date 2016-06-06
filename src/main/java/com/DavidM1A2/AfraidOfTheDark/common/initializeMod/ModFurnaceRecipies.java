/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.initializeMod;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModFurnaceRecipies implements IFuelHandler
{
	private Map<Integer, Integer> itemIDToBurnTime = new HashMap<Integer, Integer>()
	{
		{
			put(Item.getIdFromItem(Item.getItemFromBlock(ModBlocks.gravewood)), 300);
			put(Item.getIdFromItem(Item.getItemFromBlock(ModBlocks.gravewoodHalfSlab)), 150);
			put(Item.getIdFromItem(Item.getItemFromBlock(ModBlocks.gravewoodLeaves)), 25);
			put(Item.getIdFromItem(Item.getItemFromBlock(ModBlocks.gravewoodPlanks)), 300);
			put(Item.getIdFromItem(Item.getItemFromBlock(ModBlocks.gravewoodStairs)), 300);
			put(Item.getIdFromItem(Item.getItemFromBlock(ModBlocks.gravewoodDoubleSlab)), 300);
			put(Item.getIdFromItem(Item.getItemFromBlock(ModBlocks.mangrove)), 300);
			put(Item.getIdFromItem(Item.getItemFromBlock(ModBlocks.mangroveHalfSlab)), 150);
			put(Item.getIdFromItem(Item.getItemFromBlock(ModBlocks.mangroveLeaves)), 25);
			put(Item.getIdFromItem(Item.getItemFromBlock(ModBlocks.mangrovePlanks)), 300);
			put(Item.getIdFromItem(Item.getItemFromBlock(ModBlocks.mangroveStairs)), 300);
			put(Item.getIdFromItem(Item.getItemFromBlock(ModBlocks.mangroveDoubleSlab)), 300);
		}
	};

	public static void initialize()
	{
		// Add one smelting recipie
		GameRegistry.addSmelting(ModBlocks.astralSilverOre, new ItemStack(ModItems.astralSilverIngot), .4F);
		GameRegistry.addSmelting(ModBlocks.gravewood, new ItemStack(Item.getItemById(263), 1, 1), .1F);
		GameRegistry.addSmelting(ModBlocks.mangrove, new ItemStack(Item.getItemById(263), 1, 1), .1F);
		GameRegistry.addSmelting(ModItems.starMetalFragment, new ItemStack(ModItems.starMetalIngot), .5F);
		GameRegistry.registerFuelHandler(new ModFurnaceRecipies());
	}

	@Override
	public int getBurnTime(final ItemStack fuel)
	{
		final int fuelID = Item.getIdFromItem(fuel.getItem());
		if (itemIDToBurnTime.containsKey(fuelID))
			return itemIDToBurnTime.get(fuelID);
		return 0;
	}
}
