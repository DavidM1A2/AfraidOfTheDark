/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.initializeMod;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModFurnaceRecipies implements IFuelHandler
{
	public static void initialize()
	{
		// Add one smelting recipie
		GameRegistry.addSmelting(ModBlocks.astralSilverOre, new ItemStack(ModItems.silverIngot), .4F);
		GameRegistry.addSmelting(ModBlocks.gravewood, new ItemStack(Item.getItemById(263), 1, 1), .1F);
		GameRegistry.registerFuelHandler(new ModFurnaceRecipies());
	}

	@Override
	public int getBurnTime(final ItemStack fuel)
	{
		final int fuelID = Item.getIdFromItem(fuel.getItem());
		if (fuelID == Item.getIdFromItem(Item.getItemFromBlock(ModBlocks.gravewood)))
		{
			return 300;
		}
		else if (fuelID == Item.getIdFromItem(Item.getItemFromBlock(ModBlocks.gravewoodHalfSlab)))
		{
			return 150;
		}
		else if (fuelID == Item.getIdFromItem(Item.getItemFromBlock(ModBlocks.gravewoodLeaves)))
		{
			return 25;
		}
		else if (fuelID == Item.getIdFromItem(Item.getItemFromBlock(ModBlocks.gravewoodPlanks)))
		{
			return 300;
		}
		else if (fuelID == Item.getIdFromItem(Item.getItemFromBlock(ModBlocks.gravewoodStairs)))
		{
			return 300;
		}
		else if (fuelID == Item.getIdFromItem(Item.getItemFromBlock(ModBlocks.gravewoodDoubleSlab)))
		{
			return 300;
		}
		return 0;
	}
}
