/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.initializeMod;

import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.utility.recipe.AOTDDisablableShapedRecipe;
import com.DavidM1A2.AfraidOfTheDark.common.utility.recipe.AOTDDisablableShapelessRecipe;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
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

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.ironBolt, 6), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT)));
		GameRegistry.addRecipe(new AOTDDisablableShapelessRecipe(new ItemStack(ModItems.silverBolt, 6), ResearchTypes.AstralSilver, new ItemStack(ModItems.astralSilverIngot, 1), new ItemStack(ModItems.astralSilverIngot, 1)));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.woodenBolt, 6), "stickWood", "stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.crossbow), "abb", "bcd", "bdc", 'a', new ItemStack(Items.BOW), 'b', new ItemStack(Items.IRON_INGOT), 'c', new ItemStack(Blocks.PLANKS, 1), 'd', new ItemStack(Items.STRING)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.wristCrossbow), "aba", "aca", "aba", 'a', new ItemStack(Items.LEATHER), 'b', new ItemStack(Items.STRING), 'c', new ItemStack(ModItems.crossbow, 1, 0)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.wristCrossbow), "aba", "aca", "aba", 'a', new ItemStack(Items.LEATHER), 'b', new ItemStack(Items.STRING), 'c', new ItemStack(ModItems.crossbow, 1, 3)));

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.gravewoodPlanks, 4), new ItemStack(ModBlocks.gravewood, 1, 0)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.gravewoodStairs, 4), "aab", "abb", "bbb", 'b', new ItemStack(ModBlocks.gravewoodPlanks, 1, 0)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.gravewoodHalfSlab, 6), "   ", "bbb", "   ", 'b', new ItemStack(ModBlocks.gravewoodPlanks, 1, 0)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.gravewoodHalfSlab, 6), "bbb", "   ", "   ", 'b', new ItemStack(ModBlocks.gravewoodPlanks, 1, 0)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.gravewoodHalfSlab, 6), "   ", "   ", "bbb", 'b', new ItemStack(ModBlocks.gravewoodPlanks, 1, 0)));

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.mangrovePlanks, 4), new ItemStack(ModBlocks.mangrove, 1, 0)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.mangroveStairs, 4), "aab", "abb", "bbb", 'b', new ItemStack(ModBlocks.mangrovePlanks, 1, 0)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.mangroveHalfSlab, 6), "   ", "bbb", "   ", 'b', new ItemStack(ModBlocks.mangrovePlanks, 1, 0)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.mangroveHalfSlab, 6), "bbb", "   ", "   ", 'b', new ItemStack(ModBlocks.mangrovePlanks, 1, 0)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.mangroveHalfSlab, 6), "   ", "   ", "bbb", 'b', new ItemStack(ModBlocks.mangrovePlanks, 1, 0)));

		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.igneousHelmet, 1), ResearchTypes.Igneous, "aaa", "a a", 'a', ModItems.igneousGem));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.igneousChestplate, 1), ResearchTypes.Igneous, "a a", "aaa", "aaa", 'a', ModItems.igneousGem));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.igneousLeggings, 1), ResearchTypes.Igneous, "aaa", "a a", "a a", 'a', ModItems.igneousGem));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.igneousBoots, 1), ResearchTypes.Igneous, "a a", "a a", 'a', ModItems.igneousGem));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModBlocks.igneousBlock, 1), ResearchTypes.Igneous, "aaa", "aaa", "aaa", 'a', ModItems.igneousGem));
		GameRegistry.addRecipe(new AOTDDisablableShapelessRecipe(new ItemStack(ModItems.igneousGem, 9), ResearchTypes.Igneous, new ItemStack(ModBlocks.igneousBlock, 1)));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.igneousGem, 1), ResearchTypes.Igneous, "aba", "bab", "aba", 'a', Items.DIAMOND, 'b', ModItems.sunstoneFragment));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.igneousSword, 1), ResearchTypes.Igneous, "b", "b", "a", 'a', Items.IRON_INGOT, 'b', ModItems.igneousGem));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.igneousBolt, 1), ResearchTypes.Igneous, "a", "a", 'a', ModItems.sunstoneFragment));

		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.starMetalHelmet, 1), ResearchTypes.StarMetal, "aaa", "a a", 'a', ModItems.starMetalPlate));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.starMetalChestplate, 1), ResearchTypes.StarMetal, "a a", "aaa", "aaa", 'a', ModItems.starMetalPlate));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.starMetalLeggings, 1), ResearchTypes.StarMetal, "aaa", "a a", "a a", 'a', ModItems.starMetalPlate));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.starMetalBoots, 1), ResearchTypes.StarMetal, "a a", "a a", 'a', ModItems.starMetalPlate));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.starMetalPlate, 1), ResearchTypes.StarMetal, "aaa", "aba", "aaa", 'a', ModItems.starMetalIngot, 'b', Items.IRON_INGOT));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.starMetalPlate, 1), ResearchTypes.StarMetal, "aaa", "aba", "aaa", 'a', ModItems.starMetalIngot, 'b', Items.GOLD_INGOT));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.starMetalBolt, 6), ResearchTypes.StarMetal, "a", "a", 'a', ModItems.starMetalIngot));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.starMetalStaff, 1), ResearchTypes.StarMetal, "a", "a", "a", 'a', ModItems.starMetalPlate));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.starMetalKhopesh, 1), ResearchTypes.StarMetal, " a ", "a  ", " ab", 'a', ModItems.starMetalPlate, 'b', Items.IRON_INGOT));

		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.cloakOfAgility, 1), ResearchTypes.CloakOfAgility, "aba", "cdc", "aba", 'a', Items.LEATHER, 'b', Items.FEATHER, 'c', Items.STRING, 'd', Items.GOLD_INGOT));

		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.telescope), ResearchTypes.AstronomyI.getPrevious(), "ab ", "bcb", " ba", 'a', new ItemStack(Blocks.GLASS, 1, 0), 'b', Items.IRON_INGOT, 'c', Items.DIAMOND));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.sextant), ResearchTypes.AstronomyI, " ab", "cac", " c ", 'a', "stickWood", 'b', new ItemStack(Blocks.GLASS, 1, 0), 'c', Items.IRON_INGOT));

		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.vitaeLantern), ResearchTypes.VitaeI, "aba", "cdc", "aaa", 'a', Items.IRON_INGOT, 'b', new ItemStack(Blocks.IRON_BLOCK, 1, 0), 'c', new ItemStack(Blocks.GLASS, 1, 0), 'd', Items.DIAMOND));

		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModBlocks.vitaeDisenchanter), ResearchTypes.VitaeDisenchanter, "aba", "bcb", "aba", 'a', new ItemStack(ModBlocks.gravewood, 1, 0), 'b', new ItemStack(Blocks.IRON_BLOCK, 1, 0), 'c', new ItemStack(Blocks.DIAMOND_BLOCK, 1,
				0)));

		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModBlocks.voidChest), ResearchTypes.VoidChest, "aba", "bcb", "aba", 'a', new ItemStack(Blocks.OBSIDIAN, 1, 0), 'b', new ItemStack(Blocks.COAL_BLOCK, 1, 0), 'c', new ItemStack(Blocks.ENDER_CHEST, 1, 0)));

		GameRegistry.addRecipe(new AOTDDisablableShapelessRecipe(new ItemStack(Items.DYE, 5, EnumDyeColor.WHITE.getDyeDamage()), ResearchTypes.EnchantedSkeleton, new ItemStack(ModItems.enchantedSkeletonBone)));

		GameRegistry.addRecipe(new AOTDDisablableShapelessRecipe(new ItemStack(ModItems.researchScrollVitae1, 1, 0), ResearchTypes.VitaeI.getPrevious(), new ItemStack(ModItems.researchScrollVitae1, 1, 1), new ItemStack(ModItems.researchScrollVitae1, 1, 2), new ItemStack(
				ModItems.researchScrollVitae1, 1, 3), new ItemStack(ModItems.researchScrollVitae1, 1, 4), new ItemStack(ModItems.researchScrollVitae1, 1, 5)));
		GameRegistry.addRecipe(new AOTDDisablableShapelessRecipe(new ItemStack(ModItems.researchScrollAstronomy2, 1, 0), ResearchTypes.AstronomyII.getPrevious(), new ItemStack(ModItems.researchScrollAstronomy2, 1, 1), new ItemStack(ModItems.researchScrollAstronomy2, 1, 2), new ItemStack(
				ModItems.researchScrollAstronomy2, 1, 3), new ItemStack(ModItems.researchScrollAstronomy2, 1, 4)));

		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.eldritchMetalIngot, 8), ResearchTypes.EldritchDecoration, "aaa", "aba", "aaa", 'a', Items.IRON_INGOT, 'b', Items.ENDER_PEARL));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModBlocks.amorphousEldritchMetal, 8), ResearchTypes.EldritchDecoration, "aaa", "aba", "aaa", 'a', Blocks.GLASS, 'b', ModItems.eldritchMetalIngot));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModBlocks.eldritchStone, 8), ResearchTypes.EldritchDecoration, "aaa", "aba", "aaa", 'a', Blocks.STONE, 'b', ModItems.eldritchMetalIngot));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModBlocks.eldritchObsidian, 8), ResearchTypes.EldritchDecoration, "aaa", "aba", "aaa", 'a', Blocks.OBSIDIAN, 'b', ModItems.eldritchMetalIngot));

		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.bladeOfExhumation, 1), ResearchTypes.BladeOfExhumation, "ab ", "ba ", "  b", 'a', Items.DIAMOND, 'b', ModItems.enchantedSkeletonBone));
		GameRegistry.addRecipe(new AOTDDisablableShapelessRecipe(new ItemStack(ModItems.bladeOfExhumation, 1), ResearchTypes.BladeOfExhumation, Items.DIAMOND, new ItemStack(ModItems.bladeOfExhumation, 1, OreDictionary.WILDCARD_VALUE)));

		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.flaskOfSouls, 1), ResearchTypes.SlayingOfTheWolves, "ada", "bcb", "aba", 'a', ModItems.werewolfBlood, 'b', Blocks.GLASS, 'c', Blocks.DIAMOND_BLOCK, 'd', Items.LEATHER));
		GameRegistry.addRecipe(new AOTDDisablableShapelessRecipe(new ItemStack(ModItems.flaskOfSouls, 1), ResearchTypes.PhylacteryOfSouls, new ItemStack(ModItems.flaskOfSouls, 1, 1), new ItemStack(Items.POTIONITEM, 1, 0)));

		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModBlocks.gnomishMetalPlate, 3), ResearchTypes.GnomishCity, " a ", "bbb", " a ", 'a', ModItems.gnomishMetalIngot, 'b', Blocks.STONE));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModBlocks.gnomishMetalStrut, 3), ResearchTypes.GnomishCity, " b ", "aba", " b ", 'a', ModItems.gnomishMetalIngot, 'b', Blocks.STONE));

		GameRegistry.addRecipe(new AOTDDisablableShapelessRecipe(new ItemStack(ModItems.gnomishMetalIngot), ResearchTypes.GnomishCity, new ItemStack(Items.IRON_INGOT), new ItemStack(Items.POTIONITEM, 1, 0)));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.gnomishMetalIngot, 8), ResearchTypes.GnomishCity, "aaa", "aba", "aaa", 'a', new ItemStack(Items.IRON_INGOT), 'b', new ItemStack(Items.POTIONITEM, 1, 0)));
		GameRegistry.addRecipe(new AOTDDisablableShapelessRecipe(new ItemStack(ModItems.gnomishMetalIngot, 9), ResearchTypes.GnomishCity, new ItemStack(Item.getItemFromBlock(Blocks.IRON_BLOCK)), new ItemStack(Items.POTIONITEM, 1, 0)));
		GameRegistry.addRecipe(new AOTDDisablableShapedRecipe(new ItemStack(ModItems.gnomishMetalIngot, 64), ResearchTypes.GnomishCity, "aaa", "aba", "aaa", 'a', new ItemStack(Item.getItemFromBlock(Blocks.IRON_BLOCK)), 'b', new ItemStack(Items.POTIONITEM, 1, 0)));
	}
}
