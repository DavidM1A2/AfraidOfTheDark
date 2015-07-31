/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.initializeMod;

import com.DavidM1A2.AfraidOfTheDark.common.recipe.AOTDDisablableShapedRecipe;
import com.DavidM1A2.AfraidOfTheDark.common.recipe.AOTDDisablableShapelessRecipe;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ModRecipes
{
	// Add recipes
	public static void initialize()
	{
		RecipeSorter.register("afraidofthedark:researchShaped", AOTDDisablableShapedRecipe.class, Category.SHAPED, "after:minecraft:shaped");
		RecipeSorter.register("afraidofthedark:researchShapeless", AOTDDisablableShapelessRecipe.class, Category.SHAPELESS, "after:minecraft:shaped");

		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.astralSilverSword, 1), ResearchTypes.AstralSilver, "s", "s", "a", 's', ModItems.astralSilverIngot, 'a', "stickWood"));
		// This can be: "stickWood" or new ItemStack(Items.stick))

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.ironBolt, 6), new ItemStack(Items.iron_ingot), new ItemStack(Items.iron_ingot)));
		GameRegistry.addRecipe(new AOTDDisablableShapelessRecipe(new ItemStack(ModItems.silverBolt, 6), ResearchTypes.AstralSilver, new ItemStack(ModItems.astralSilverIngot, 1), new ItemStack(ModItems.astralSilverIngot, 1)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.woodenBolt, 6), "stickWood", "stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.crossbow), "abb", "bcd", "bdc", 'a', new ItemStack(Items.bow), 'b', new ItemStack(Items.iron_ingot), 'c', new ItemStack(Blocks.planks, 1), 'd', new ItemStack(Items.string)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.wristCrossbow), "aba", "aca", "aba", 'a', new ItemStack(Items.leather), 'b', new ItemStack(Items.string), 'c', new ItemStack(ModItems.crossbow, 1, 0)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.wristCrossbow), "aba", "aca", "aba", 'a', new ItemStack(Items.leather), 'b', new ItemStack(Items.string), 'c', new ItemStack(ModItems.crossbow, 1, 3)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.gravewoodPlanks, 4), new ItemStack(ModBlocks.gravewood, 1, 0)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.gravewoodStairs, 4), "aab", "abb", "bbb", 'b', new ItemStack(ModBlocks.gravewoodPlanks, 1, 0)));

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.gravewoodHalfSlab, 6), "   ", "bbb", "   ", 'b', new ItemStack(ModBlocks.gravewoodPlanks, 1, 0)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.gravewoodHalfSlab, 6), "bbb", "   ", "   ", 'b', new ItemStack(ModBlocks.gravewoodPlanks, 1, 0)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.gravewoodHalfSlab, 6), "   ", "   ", "bbb", 'b', new ItemStack(ModBlocks.gravewoodPlanks, 1, 0)));

		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.igneousHelmet, 1), ResearchTypes.Igneous, "aaa", "a a", 'a', ModItems.igneousGem));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.igneousChestplate, 1), ResearchTypes.Igneous, "a a", "aaa", "aaa", 'a', ModItems.igneousGem));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.igneousLeggings, 1), ResearchTypes.Igneous, "aaa", "a a", "a a", 'a', ModItems.igneousGem));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.igneousBoots, 1), ResearchTypes.Igneous, "a a", "a a", 'a', ModItems.igneousGem));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModBlocks.igneousBlock, 1), ResearchTypes.Igneous, "aaa", "aaa", "aaa", 'a', ModItems.igneousGem));
		GameRegistry.addRecipe(new AOTDDisablableShapelessRecipe(new ItemStack(ModItems.igneousGem, 9), ResearchTypes.Igneous, new ItemStack(ModBlocks.igneousBlock, 1)));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.igneousGem, 1), ResearchTypes.Igneous, "aba", "bab", "aba", 'a', Items.diamond, 'b', ModItems.sunstoneFragment));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.igneousSword, 1), ResearchTypes.Igneous, "b", "b", "a", 'a', Items.iron_ingot, 'b', ModItems.igneousGem));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.igneousBolt, 1), ResearchTypes.Igneous, "a", "a", 'a', ModItems.sunstoneFragment));

		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.starMetalHelmet, 1), ResearchTypes.StarMetal, "aaa", "a a", 'a', ModItems.starMetalPlate));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.starMetalChestplate, 1), ResearchTypes.StarMetal, "a a", "aaa", "aaa", 'a', ModItems.starMetalPlate));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.starMetalLeggings, 1), ResearchTypes.StarMetal, "aaa", "a a", "a a", 'a', ModItems.starMetalPlate));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.starMetalBoots, 1), ResearchTypes.StarMetal, "a a", "a a", 'a', ModItems.starMetalPlate));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.starMetalPlate, 1), ResearchTypes.StarMetal, "aaa", "aba", "aaa", 'a', ModItems.starMetalIngot, 'b', Items.iron_ingot));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.starMetalPlate, 1), ResearchTypes.StarMetal, "aaa", "aba", "aaa", 'a', ModItems.starMetalIngot, 'b', Items.gold_ingot));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.starMetalBolt, 6), ResearchTypes.StarMetal, "a", "a", 'a', ModItems.starMetalIngot));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.starMetalStaff, 1), ResearchTypes.StarMetal, "a", "a", "a", 'a', ModItems.starMetalPlate));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.starMetalKhopesh, 1), ResearchTypes.StarMetal, " a ", "a  ", " ab", 'a', ModItems.starMetalPlate, 'b', Items.iron_ingot));

		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.cloakOfAgility, 1), ResearchTypes.CloakOfAgility, "aba", "cdc", "aba", 'a', Items.leather, 'b', Items.feather, 'c', Items.string, 'd', Items.gold_ingot));

		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.telescope), ResearchTypes.AstronomyI, "ab ", "bcb", " ba", 'a', new ItemStack(Blocks.glass, 1, 0), 'b', Items.iron_ingot, 'c', Items.diamond));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.sextant), ResearchTypes.AstronomyI, " ab", "cac", " c ", 'a', "stickWood", 'b', new ItemStack(Blocks.glass, 1, 0), 'c', Items.iron_ingot));

		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.vitaeLantern), ResearchTypes.VitaeI, "aba", "cdc", "aaa", 'a', Items.iron_ingot, 'b', new ItemStack(Blocks.iron_block, 1, 0), 'c', new ItemStack(Blocks.glass, 1, 0), 'd', Items.diamond));

		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModBlocks.vitaeDisenchanter), ResearchTypes.VitaeDisenchanter, "aba", "bcb", "aba", 'a', new ItemStack(ModBlocks.gravewood, 1, 0), 'b', new ItemStack(Blocks.iron_block, 1, 0), 'c', new ItemStack(Blocks.diamond_block, 1,
				0)));

		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModBlocks.voidChest), ResearchTypes.VoidChest, "aba", "bcb", "aba", 'a', new ItemStack(Blocks.obsidian, 1, 0), 'b', new ItemStack(Blocks.coal_block, 1, 0), 'c', new ItemStack(Blocks.ender_chest, 1, 0)));

		GameRegistry.addRecipe(new AOTDDisablableShapelessRecipe(new ItemStack(Items.dye, 5, EnumDyeColor.WHITE.getDyeColorDamage()), ResearchTypes.EnchantedSkeleton, new ItemStack(ModItems.enchantedSkeletonBone)));

		GameRegistry.addRecipe(new AOTDDisablableShapelessRecipe(new ItemStack(ModItems.researchScrollVitae1, 1, 0), ResearchTypes.VitaeI.getPrevious(), new ItemStack(ModItems.researchScrollVitae1, 1, 1), new ItemStack(ModItems.researchScrollVitae1, 1, 2), new ItemStack(
				ModItems.researchScrollVitae1, 1, 3), new ItemStack(ModItems.researchScrollVitae1, 1, 4), new ItemStack(ModItems.researchScrollVitae1, 1, 5)));
		GameRegistry.addRecipe(new AOTDDisablableShapelessRecipe(new ItemStack(ModItems.researchScrollAstronomy2, 1, 0), ResearchTypes.AstronomyII.getPrevious(), new ItemStack(ModItems.researchScrollAstronomy2, 1, 1), new ItemStack(ModItems.researchScrollAstronomy2, 1, 2), new ItemStack(
				ModItems.researchScrollAstronomy2, 1, 3), new ItemStack(ModItems.researchScrollAstronomy2, 1, 4)));

		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.eldritchMetalIngot, 8), ResearchTypes.EldritchMetal, "aaa", "aba", "aaa", 'a', Items.iron_ingot, 'b', Items.ender_pearl));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModBlocks.eldritchStone, 8), ResearchTypes.EldritchMetal, "aaa", "aba", "aaa", 'a', ModItems.eldritchMetalIngot, 'b', new ItemStack(Blocks.stone)));
		GameRegistry.addRecipe(new AOTDDisablableShapelessRecipe(new ItemStack(ModBlocks.eldritchObsidian, 1), ResearchTypes.EldritchMetal, ModItems.eldritchMetalIngot, Blocks.obsidian));
	}
}
