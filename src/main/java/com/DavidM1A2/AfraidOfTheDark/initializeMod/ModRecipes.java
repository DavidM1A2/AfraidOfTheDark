/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.initializeMod;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ModRecipes
{
	// Add recipes
	public static void initialize()
	{
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.silverSword, 1), " s ", " s ", " a ", 's', "ingotSilver", 'a', "stickWood"));
		// This can be: "stickWood" or new ItemStack(Items.stick))

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.ironBolt, 6), new ItemStack(Items.iron_ingot), new ItemStack(Items.iron_ingot)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.silverBolt, 6), "ingotSilver", "ingotSilver"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.woodenBolt, 6), "stickWood", "stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.crossbow), "abb", "bcd", "bdc", 'a', new ItemStack(Items.bow), 'b', new ItemStack(Items.iron_ingot), 'c', new ItemStack(Blocks.planks), 'd', new ItemStack(Items.string)));
	}
}
