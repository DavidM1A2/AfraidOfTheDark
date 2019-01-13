package com.DavidM1A2.afraidofthedark.client.gui.guiScreens;

import com.DavidM1A2.afraidofthedark.client.gui.base.AOTDGuiScreen;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.*;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Journal page UI which is shown when a player opens a page
 */
public class BloodStainedJournalPageGUI extends AOTDGuiScreen
{
	// The complete text that is to be shown on the GUI
	private final String completeText;

	// A partitioned list the "complete text" list to be written on each page
	private final List<String> textOnEachPage = new LinkedList<String>();

	// The text box of the left page
	private AOTDGuiTextBox leftPage;
	private AOTDGuiTextBox rightPage;

	// The button to go forward and backwards
	private AOTDGuiButton forwardButton;
	private AOTDGuiButton backwardButton;

	// The left page number and right page number
	private AOTDGuiLabel leftPageNumber;
	private AOTDGuiLabel rightPageNumber;

	// The 4 possible recipe positions
	private AOTDGuiRecipe topLeftRecipe;
	private AOTDGuiRecipe bottomLeftRecipe;
	private AOTDGuiRecipe topRightRecipe;
	private AOTDGuiRecipe bottomRightRecipe;

	// The bookmark button to go back
	private AOTDGuiButton bookmarkButton;

	// The journal background
	private AOTDGuiPanel journalBackground;

	// The current page we're on
	private int pageNumber = 0;

	// A list of recipes for this research
	private final List<IRecipe> researchRecipes = new ArrayList<>();

	/**
	 * Initializes the entire UI
	 *
	 * @param text The text to place on the page
	 * @param titleText The title of the research
	 * @param relatedItemRecipes The related items to get recipes for
	 */
	public BloodStainedJournalPageGUI(String text, String titleText, Item[] relatedItemRecipes)
	{
		super();

		// Iterate over all items that we want to show recipes for
		for (Item item : relatedItemRecipes)
			// Iterate over all recipes known
			for (IRecipe recipe : CraftingManager.REGISTRY)
				// If the recipe's output is our item store that recipe
				if (recipe.getRecipeOutput().getItem() == item)
					researchRecipes.add(recipe);

		// Store the raw text of the research
		this.completeText = text;

		// Set the width and height of the journal
		int journalWidth = 256;
		int journalHeight = 256;
		// Calculate the x and y corner positions of the page
		int xCornerOfPage = (640 - journalWidth) / 2;
		int yCornerOfPage = (360 - journalHeight) / 2;

		this.journalBackground = new AOTDGuiPanel(xCornerOfPage, yCornerOfPage, journalWidth, journalHeight, false);
	}

	/**
	 * @return False since the inventory key does not close the screen
	 */
	@Override
	public boolean inventoryToCloseGuiScreen()
	{
		return false;
	}

	/**
	 * @return True since the screen should have a gradient background
	 */
	@Override
	public boolean drawGradientBackground()
	{
		return true;
	}
}
