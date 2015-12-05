/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.common.recipe.ConvertedRecipe;
import com.DavidM1A2.AfraidOfTheDark.common.recipe.RecipeUtility;

public class AOTDGuiRecipe extends AOTDGuiButton
{
	private ConvertedRecipe convertedRecipe;

	public AOTDGuiRecipe(int x, int y, int width, int height)
	{
		super(x, y, width, height, null, null);
	}

	@Override
	public void draw()
	{
		super.draw();
		if (this.convertedRecipe != null)
		{
			GL11.glPushMatrix();
			GL11.glScaled(this.getScaleX(), this.getScaleY(), 1.0f);
			RecipeUtility.drawCraftingRecipe(this.getXScaled(), this.getYScaled(), this.convertedRecipe);
			GL11.glPopMatrix();
		}
	}

	public void setRecipe(ConvertedRecipe recipe)
	{
		this.convertedRecipe = recipe;
	}
}
