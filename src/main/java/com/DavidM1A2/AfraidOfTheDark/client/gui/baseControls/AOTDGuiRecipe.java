/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls;

import com.DavidM1A2.AfraidOfTheDark.common.utility.recipe.ConvertedRecipe;

public class AOTDGuiRecipe extends AOTDGuiPanel
{
	private final AOTDGuiImage CRAFTING_GRID;
	private final AOTDGuiItemStack[] guiItemStacks;
	private final AOTDGuiItemStack output;

	public AOTDGuiRecipe(int x, int y, int width, int height, ConvertedRecipe recipe)
	{
		super(x, y, width, height, false);
		CRAFTING_GRID = new AOTDGuiImage(0, 0, width, height, "afraidofthedark:textures/gui/journalCrafting2.png");
		this.add(CRAFTING_GRID);

		guiItemStacks = new AOTDGuiItemStack[9];
		for (int i = 0; i < this.guiItemStacks.length; i++)
			guiItemStacks[i] = new AOTDGuiItemStack(5 + (i % 3) * 24, 6 + 26 * (i > 5 ? 2 : i > 2 ? 1 : 0), (int) (width / 5.0), (int) (height / 4.0), null, true);
		output = new AOTDGuiItemStack(83, 31, 24, 24, null, true);

		for (int i = 0; i < this.guiItemStacks.length; i++)
			this.add(guiItemStacks[i]);
		this.add(output);

		this.setRecipe(recipe);
	}

	@Override
	public void draw()
	{
		if (this.isVisible())
		{
			if (this.output.getItemStack() != null)
			{
				super.draw();
			}
		}
	}

	public void setRecipe(ConvertedRecipe recipe)
	{
		if (recipe == null)
		{
			this.setVisible(false);
			return;
		}

		this.setVisible(true);

		for (int i = 0; i < this.guiItemStacks.length; i++)
			guiItemStacks[i].setItemStack(null);

		if (recipe.getWidth() == -1)
		{
			for (int i = 0; i < recipe.getInput().length; i++)
				guiItemStacks[i].setItemStack(recipe.getInput()[i]);
		}
		else
		{
			for (int i = 0; i < recipe.getHeight(); i++)
				for (int j = 0; j < recipe.getWidth(); j++)
					guiItemStacks[i * 3 + j].setItemStack(recipe.getInput()[i * recipe.getWidth() + j]);
		}
		this.output.setItemStack(recipe.getOutput());
	}

	@Override
	public void setVisible(boolean isVisible)
	{
		super.setVisible(isVisible);
		this.output.setVisible(isVisible);
		for (AOTDGuiItemStack itemStack : this.guiItemStacks)
			itemStack.setVisible(isVisible);
	}
}
