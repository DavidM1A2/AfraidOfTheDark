package com.DavidM1A2.afraidofthedark.client.gui.standardControls;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Advanced control that displays an entire crafting recipe
 */
public class AOTDGuiRecipe extends AOTDGuiPanel
{
	// The background crafting grid texture
	private final AOTDGuiImage craftingGrid;
	// The item stacks to draw
	private final AOTDGuiItemStack[] guiItemStacks;
	// The itemstack that gets created
	private final AOTDGuiItemStack output;

	/**
	 *	Constructor initializes fields
	 *
	 * @param x The X location of the top left corner
	 * @param y The Y location of the top left corner
	 * @param width  The width of the component
	 * @param height The height of the component
	 * @param recipe The recipe to draw
	 */
	public AOTDGuiRecipe(int x, int y, int width, int height, IRecipe recipe)
	{
		super(x, y, width, height, false);
		// Setup the crafting grid background image
		this.craftingGrid = new AOTDGuiImage(0, 0, width, height, "afraidofthedark:textures/gui/journal_crafting2.png");
		this.add(this.craftingGrid);

		// Create an array of 9 stacks for each of the 9 slots
		this.guiItemStacks = new AOTDGuiItemStack[9];
		// Initialize each of the 9 stacks
		for (int i = 0; i < this.guiItemStacks.length; i++)
			guiItemStacks[i] = new AOTDGuiItemStack(
					5 + (i % 3) * 24,
					6 + 26 * (i / 3),
					(int) (width / 5.0),
					(int) (height / 4.0),
					null,
					true);
		// Initialize the output stack
		output = new AOTDGuiItemStack(83, 31, 24, 24, null, true);

		// Add each stack to the pane to be drawn
		for (AOTDGuiItemStack guiItemStack : this.guiItemStacks)
			this.add(guiItemStack);
		this.add(output);

		// Set the recipe to draw
		this.setRecipe(recipe);
	}

	/**
	 * Called to draw the control, just draws all of its children
	 */
	@Override
	public void draw()
	{
		// If we have no output itemstack we can't draw the recipe
		if (this.isVisible() && this.output.getItemStack() != null)
		{
			super.draw();
		}
	}

	/**
	 * Sets the recipe to be drawn
	 *
	 * @param recipe The recipe to draw
	 */
	public void setRecipe(IRecipe recipe)
	{
		// If the recipe is invalid just return
		if (recipe == null)
		{
			this.setVisible(false);
			return;
		}

		// Show the recipe
		this.setVisible(true);

		// Set all the output stacks to null for now
		for (int i = 0; i < this.guiItemStacks.length; i++)
			this.guiItemStacks[i].setItemStack(null);

		// Update each gui stack with the new ingredient
		for (int i = 0; i < recipe.getIngredients().size(); i++)
			this.guiItemStacks[i].setItemStack(recipe.getIngredients().get(i).getMatchingStacks()[0]);

		// Update the output itemstack
		this.output.setItemStack(recipe.getRecipeOutput());
	}
}
