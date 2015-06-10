/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.initializeMod;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.DavidM1A2.AfraidOfTheDark.common.recipe.AOTDDisablableShapedRecipe;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;

public class ModRecipes
{
	// Add recipes
	public static void initialize()
	{
		RecipeSorter.INSTANCE.setCategory(AOTDDisablableShapedRecipe.class, Category.SHAPED);

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.astralSilverSword, 1), " s ", " s ", " a ", 's', ModItems.astralSilverIngot, 'a', "stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.astralSilverSword, 1), "s  ", "s  ", "a  ", 's', ModItems.astralSilverIngot, 'a', "stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.astralSilverSword, 1), "  s", "  s", "  a", 's', ModItems.astralSilverIngot, 'a', "stickWood"));
		// This can be: "stickWood" or new ItemStack(Items.stick))

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.ironBolt, 6), new ItemStack(Items.iron_ingot), new ItemStack(Items.iron_ingot)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.silverBolt, 6), new ItemStack(ModItems.astralSilverIngot, 1), new ItemStack(ModItems.astralSilverIngot, 1)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.woodenBolt, 6), "stickWood", "stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.crossbow), "abb", "bcd", "bdc", 'a', new ItemStack(Items.bow), 'b', new ItemStack(Items.iron_ingot), 'c', new ItemStack(Blocks.planks), 'd', new ItemStack(Items.string)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.gravewoodPlanks, 4), new ItemStack(ModBlocks.gravewood, 1)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.gravewoodStairs, 4), "aab", "abb", "bbb", 'b', ModBlocks.gravewoodPlanks));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.gravewoodHalfSlab, 6), "   ", "bbb", "   ", 'b', ModBlocks.gravewoodPlanks));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.gravewoodHalfSlab, 6), "bbb", "   ", "   ", 'b', ModBlocks.gravewoodPlanks));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.gravewoodHalfSlab, 6), "   ", "   ", "bbb", 'b', ModBlocks.gravewoodPlanks));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.igneousHelmet, 1), "aaa", "a a", "   ", 'a', ModItems.igneousGem));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.igneousHelmet, 1), "   ", "aaa", "a a", 'a', ModItems.igneousGem));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.igneousChestplate, 1), "a a", "aaa", "aaa", 'a', ModItems.igneousGem));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.igneousLeggings, 1), "aaa", "a a", "a a", 'a', ModItems.igneousGem));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.igneousBoots, 1), "   ", "a a", "a a", 'a', ModItems.igneousGem));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.igneousBoots, 1), "a a", "a a", "   ", 'a', ModItems.igneousGem));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.igneousBlock, 1), "aaa", "aaa", "aaa", 'a', ModItems.igneousGem));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.igneousGem, 9), new ItemStack(ModBlocks.igneousBlock, 1)));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.starMetalHelmet, 1), "aaa", "a a", "   ", 'a', ModItems.starMetalIngot));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.starMetalHelmet, 1), "   ", "aaa", "a a", 'a', ModItems.starMetalIngot));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.starMetalChestplate, 1), "a a", "aaa", "aaa", 'a', ModItems.starMetalIngot));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.starMetalLeggings, 1), "aaa", "a a", "a a", 'a', ModItems.starMetalIngot));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.starMetalBoots, 1), "   ", "a a", "a a", 'a', ModItems.starMetalIngot));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.starMetalBoots, 1), "a a", "a a", "   ", 'a', ModItems.starMetalIngot));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.igneousGem, 1), "aba", "bab", "aba", 'a', Items.diamond, 'b', ModItems.sunstoneIngot));

		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.cloakOfAgility, 1), ResearchTypes.CloakOfAgility, "aba", "cdc", "aba", 'a', Items.leather, 'b', Items.feather, 'c', Items.string, 'd', Items.gold_ingot));
	}
}
