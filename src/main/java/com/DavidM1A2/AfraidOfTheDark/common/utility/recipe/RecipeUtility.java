/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.utility.recipe;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class RecipeUtility
{
	public static List<ConvertedRecipe> getRecipesForItem(Item item)
	{
		List<ConvertedRecipe> convertedRecipes = new LinkedList<ConvertedRecipe>();
		for (Object recipe : CraftingManager.getInstance().getRecipeList())
		{
			// Is this a recipe?
			if (recipe instanceof IRecipe)
			{
				IRecipe currentRecipe = (IRecipe) recipe;
				// Does this recipe apply to one of our items?
				if (currentRecipe.getRecipeOutput() != null && currentRecipe.getRecipeOutput().getItem() == item)
				{
					// We know at this point that the recipe is for our item
					ConvertedRecipe cleanedRecipe = RecipeUtility.getConvertedRecipeFromIRecipe(currentRecipe);
					if (cleanedRecipe != null)
					{
						convertedRecipes.add(cleanedRecipe);
					}
				}
			}
		}
		return convertedRecipes;
	}

	private static ConvertedRecipe getConvertedRecipeFromIRecipe(IRecipe currentRecipe)
	{
		int width = 0;
		int height = 0;
		ItemStack output = currentRecipe.getRecipeOutput();
		ItemStack[] input = null;

		if (currentRecipe instanceof ShapedRecipes)
		{
			ShapedRecipes shapedRecipe = (ShapedRecipes) currentRecipe;
			width = shapedRecipe.recipeWidth;
			height = shapedRecipe.recipeHeight;
			input = shapedRecipe.recipeItems;
		}
		else if (currentRecipe instanceof ShapedOreRecipe)
		{
			ShapedOreRecipe shapedOreRecipe = (ShapedOreRecipe) currentRecipe;
			Field[] fields = ShapedOreRecipe.class.getDeclaredFields();
			fields[4].setAccessible(true);
			fields[5].setAccessible(true);
			try
			{
				width = (Integer) fields[4].get(shapedOreRecipe);
				height = (Integer) fields[5].get(shapedOreRecipe);// reflection
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			fields[4].setAccessible(false);
			fields[5].setAccessible(false);
			input = new ItemStack[shapedOreRecipe.getInput().length];
			for (int i = 0; i < shapedOreRecipe.getInput().length; i++)
			{
				Object object = shapedOreRecipe.getInput()[i];
				if (object instanceof Item)
				{
					input[i] = new ItemStack((Item) object, 1, 0);
				}
				else if (object instanceof Block)
				{
					input[i] = new ItemStack((Block) object, 1, 0);
				}
				else if (object instanceof ItemStack)
				{
					input[i] = (ItemStack) object;
				}
				else if (object instanceof List)
				{
					// Don't fully support ore dictionary yet
					List<ItemStack> oreDictionaryList = (List<ItemStack>) object;
					if (!oreDictionaryList.isEmpty())
					{
						input[i] = oreDictionaryList.get(0);
					}
				}
			}
		}
		else if (currentRecipe instanceof ShapelessRecipes)
		{
			ShapelessRecipes shapelessRecipe = (ShapelessRecipes) currentRecipe;
			width = -1;
			height = -1;
			List<?> requiredItems = shapelessRecipe.recipeItems;
			input = new ItemStack[requiredItems.size()];
			for (int i = 0; i < requiredItems.size(); i++)
			{
				Object object = requiredItems.get(i);
				if (object instanceof Item)
				{
					input[i] = new ItemStack((Item) object, 1, 0);
				}
				else if (object instanceof Block)
				{
					input[i] = new ItemStack((Block) object, 1, 0);
				}
				else
				{
					input[i] = (ItemStack) object;
				}
			}
		}
		else if (currentRecipe instanceof ShapelessOreRecipe)
		{
			ShapelessOreRecipe shapelessOreRecipe = (ShapelessOreRecipe) currentRecipe;
			width = -1;
			height = -1;
			input = new ItemStack[shapelessOreRecipe.getInput().size()];
			List<Object> requiredItems = shapelessOreRecipe.getInput();
			for (int i = 0; i < requiredItems.size(); i++)
			{
				Object object = requiredItems.get(i);
				if (object instanceof Item)
				{
					input[i] = new ItemStack((Item) object, 1, 0);
				}
				else if (object instanceof Block)
				{
					input[i] = new ItemStack((Block) object, 1, 0);
				}
				else if (object instanceof ItemStack)
				{
					input[i] = (ItemStack) object;
				}
				else if (object instanceof List)
				{
					// Don't fully support ore dictionary yet
					List<ItemStack> oreDictionaryList = (List<ItemStack>) object;
					if (!oreDictionaryList.isEmpty())
					{
						input[i] = oreDictionaryList.get(0);
					}
				}
			}
		}

		ConvertedRecipe cleanedRecipe = new ConvertedRecipe(width, height, output, input);

		for (ItemStack itemStack : cleanedRecipe.getInput())
		{
			if (itemStack != null)
			{
				if (itemStack.getItemDamage() == 32767)
				{
					itemStack.setItemDamage(0);
				}
			}
		}
		if (cleanedRecipe.getOutput().getItemDamage() == 32767)
		{
			cleanedRecipe.getOutput().setItemDamage(0);
		}

		if (width != 0)
		{
			return cleanedRecipe;
		}
		return null;
	}
}
