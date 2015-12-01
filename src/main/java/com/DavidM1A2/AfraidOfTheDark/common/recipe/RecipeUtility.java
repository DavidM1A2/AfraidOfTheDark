/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.recipe;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class RecipeUtility
{
	private static final ResourceLocation CRAFTING_GRID_TEXTURE = new ResourceLocation("afraidofthedark:textures/gui/journalCrafting.png");

	public static void drawCraftingRecipe(int x, int y, ConvertedRecipe recipe)
	{
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		Minecraft.getMinecraft().renderEngine.bindTexture(CRAFTING_GRID_TEXTURE);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 130, 90, 130, 90);

		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		renderItem.zLevel = 100.0F;

		RenderHelper.enableGUIStandardItemLighting();

		if (recipe.getWidth() == -1)
		{
			for (int i = 0; i < recipe.getInput().length; i++)
			{
				if (recipe.getInput()[i] != null)
				{
					RecipeUtility.drawItemStack(recipe.getInput()[i], x + 5 + (i % 3) * 30, y + 5 + 30 * (i > 2 ? 1 : i > 4 ? 2 : 0), recipe.getInput()[i].stackSize);
				}
			}
		}
		else
		{
			for (int i = 0; i < recipe.getHeight(); i++)
			{
				for (int j = 0; j < recipe.getWidth(); j++)
				{
					if (recipe.getInput()[i * recipe.getWidth() + j] != null)
					{
						RecipeUtility.drawItemStack(recipe.getInput()[i * recipe.getWidth() + j], x + 5 + j * 30, y + 5 + i * 30, recipe.getInput()[i * recipe.getWidth() + j].stackSize);
					}
				}
			}
		}

		RecipeUtility.drawItemStack(recipe.getOutput(), x + 105, y + 35, recipe.getOutput().stackSize);

		RenderHelper.disableStandardItemLighting();

		renderItem.zLevel = 0.0F;
	}

	/**
	 * Render an ItemStack. Args : stack, x, y, format
	 */
	private static void drawItemStack(ItemStack stack, int x, int y, int stackSize)
	{
		// Fixed an issue regarding drawing of certain recipes... idk why this exists
		//GlStateManager.translate(0.0F, 0.0F, 32.0F);
		Minecraft.getMinecraft().getRenderItem().zLevel = 200.0F;
		FontRenderer font = null;
		if (stack != null)
			font = stack.getItem().getFontRenderer(stack);
		if (font == null)
			font = Minecraft.getMinecraft().fontRendererObj;
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		renderItem.renderItemAndEffectIntoGUI(stack, x, y);
		renderItem.renderItemOverlayIntoGUI(font, stack, x, y, null);
		renderItem.zLevel = 0.0F;
	}

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
