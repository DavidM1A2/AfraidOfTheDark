/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.BookmarkButton;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.ForwardBackwardButtons;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.PageNumberLabel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.TextBox;
import com.DavidM1A2.AfraidOfTheDark.client.settings.ClientData;
import com.DavidM1A2.AfraidOfTheDark.common.utility.ConvertedRecipe;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class BloodStainedJournalPageGUI extends GuiScreen
{
	private final String originalCompleteText;

	private final List<String> textOnEachPage;

	private final String title;

	private final TextBox leftPage;
	private final TextBox rightPage;

	private ForwardBackwardButtons forwardButton;
	private ForwardBackwardButtons backwardButton;

	private PageNumberLabel leftPageLabel;
	private PageNumberLabel rightPageLabel;

	private BookmarkButton bookmarkButton;

	private int previousWidth = 0;
	private int previousHeight = 0;

	private int pageNumber = 0;

	private final ResourceLocation journalTexture;

	private final ResourceLocation journalCraftingGrid;

	private final List<ConvertedRecipe> researchRecipes = new ArrayList<ConvertedRecipe>();

	private int xCornerOfPage = 0;
	private int yCornerOfPage = 0;
	private int journalWidth = 0;
	private int journalHeight = 0;

	public BloodStainedJournalPageGUI(final String textNext, final String title, Item[] relatedItemRecipes)
	{
		// Setup tile and page text. Then add left and right page text boxes
		super();
		this.setupRecipes(relatedItemRecipes);
		this.originalCompleteText = textNext;
		this.title = title;
		this.textOnEachPage = new LinkedList<String>();

		this.leftPage = new TextBox(this.xCornerOfPage, this.yCornerOfPage, this.journalWidth, this.journalWidth, ClientData.journalFont);
		this.rightPage = new TextBox(this.xCornerOfPage, this.yCornerOfPage, this.journalWidth, this.journalWidth, ClientData.journalFont);
		this.leftPageLabel = new PageNumberLabel(this.xCornerOfPage, this.yCornerOfPage, this.journalWidth, this.journalHeight, ClientData.journalFont);
		this.rightPageLabel = new PageNumberLabel(this.xCornerOfPage, this.yCornerOfPage, this.journalWidth, this.journalHeight, ClientData.journalFont);
		this.forwardButton = new ForwardBackwardButtons(15, this.width - 64, this.height - 64, 64, 64, true);
		this.backwardButton = new ForwardBackwardButtons(16, 0, this.height - 64, 64, 64, false);
		this.bookmarkButton = new BookmarkButton(1, 0, (int) (this.yCornerOfPage + this.journalWidth / 2.1), this.width, 40);
		this.journalTexture = new ResourceLocation("afraidofthedark:textures/gui/bloodStainedJournalPage.png");
		this.journalCraftingGrid = new ResourceLocation("afraidofthedark:textures/gui/journalCrafting.png");

		this.updateBounds();
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.clear();
		this.buttonList.add(this.bookmarkButton);
		this.buttonList.add(this.forwardButton);
		this.buttonList.add(this.backwardButton);
	}

	// Opening a research book DOES NOT pause the game (unlike escape)
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	// To draw the screen we first draw the default GUI background, then the
	// background images. Then we add buttons and a frame.
	@Override
	public void drawScreen(final int mouseX, final int mouseY, final float f)
	{
		// Has the window been resized?
		if ((this.width != this.previousWidth) || (this.height != this.previousHeight))
		{
			this.updateBounds();
			this.updateText();
		}

		this.drawScaledJournal(mouseX, mouseY);
	}

	private void setupRecipes(Item[] relatedItemRecipes)
	{
		for (Object recipe : CraftingManager.getInstance().getRecipeList())
		{
			// Is this a recipe?
			if (recipe instanceof IRecipe)
			{
				IRecipe currentRecipe = (IRecipe) recipe;
				for (Item item : relatedItemRecipes)
				{
					// Does this recipe apply to one of our items?
					if (currentRecipe.getRecipeOutput() != null && currentRecipe.getRecipeOutput().getItem() == item)
					{
						// We know at this point that the recipe is for our item
						ConvertedRecipe cleanedRecipe = Utility.getConvertedRecipeFromIRecipe(currentRecipe);

						if (cleanedRecipe != null)
						{
							researchRecipes.add(cleanedRecipe);
						}
						else
						{
							LogHelper.info("Something went wrong in the recipe decoding of the item + " + item.toString());
						}
					}
				}
			}
		}
	}

	private void drawScaledJournal(int mouseX, int mouseY)
	{
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);

		double pageScaleWidth = this.width / 640.0D;
		double pageScaleHeight = this.height / 480.0D;
		double pageScaleCombined = (pageScaleWidth + pageScaleHeight) / 2.0D;

		GL11.glTranslated(this.width / 2, this.height / 2, 0.0D);
		GL11.glScaled(pageScaleCombined, pageScaleCombined, 1.0D);
		GL11.glTranslated(-this.width / 2, -this.height / 2, 0.0D);

		this.mc.renderEngine.bindTexture(this.journalTexture);
		// Draw the journal background

		Gui.drawScaledCustomSizeModalRect(this.xCornerOfPage, this.yCornerOfPage, 0, 0, this.journalWidth, this.journalHeight, this.journalWidth, this.journalHeight, this.journalWidth, this.journalHeight);

		// Draw the title
		ClientData.journalTitleFont.drawString(this.title, this.xCornerOfPage + 15, this.yCornerOfPage + 15, 0xFF800000);

		int adjustedIndexForRecipe = (pageNumber - textOnEachPage.size()) * 2;

		if (Utility.hasIndex(this.textOnEachPage, pageNumber))
		{
			this.leftPage.drawText(this.textOnEachPage.get(pageNumber));
			if (Utility.hasIndex(textOnEachPage, pageNumber + 1))
			{
				this.rightPage.drawText(this.textOnEachPage.get(pageNumber + 1));
			}
			else
			{
				this.rightPage.drawText("");
				if (Utility.hasIndex(researchRecipes, adjustedIndexForRecipe + 2))
				{
					this.drawCraftingRecipe(rightPage.getX() + 10, rightPage.getY(), this.researchRecipes.get(adjustedIndexForRecipe + 2));
				}
				if (Utility.hasIndex(researchRecipes, adjustedIndexForRecipe + 3))
				{
					this.drawCraftingRecipe(rightPage.getX() + 10, rightPage.getY() + 100, this.researchRecipes.get(adjustedIndexForRecipe + 3));
				}
			}
		}
		else
		{
			this.leftPage.drawText("");
			if (Utility.hasIndex(researchRecipes, adjustedIndexForRecipe))
			{
				this.drawCraftingRecipe(leftPage.getX() + 5, leftPage.getY(), this.researchRecipes.get(adjustedIndexForRecipe));
			}
			if (Utility.hasIndex(researchRecipes, adjustedIndexForRecipe + 1))
			{
				this.drawCraftingRecipe(leftPage.getX() + 5, leftPage.getY() + 100, this.researchRecipes.get(adjustedIndexForRecipe + 1));
			}
			if (Utility.hasIndex(researchRecipes, adjustedIndexForRecipe + 2))
			{
				this.drawCraftingRecipe(rightPage.getX() + 10, rightPage.getY(), this.researchRecipes.get(adjustedIndexForRecipe + 2));
			}
			if (Utility.hasIndex(researchRecipes, adjustedIndexForRecipe + 3))
			{
				this.drawCraftingRecipe(rightPage.getX() + 10, rightPage.getY() + 100, this.researchRecipes.get(adjustedIndexForRecipe + 3));
			}
		}

		this.leftPageLabel.drawNumber(Integer.toString(this.pageNumber + 1));
		this.rightPageLabel.drawNumber(Integer.toString(this.pageNumber + 2));

		bookmarkButton.drawButton(mc, mouseX, mouseY);

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();

		this.backwardButton.drawButton(mc, mouseX, mouseY);
		this.forwardButton.drawButton(mc, mouseX, mouseY);
	}

	private void updateBounds()
	{
		this.journalWidth = 330;
		this.journalHeight = 330;

		// Calculate various variables later used in text box width/height calculation
		this.xCornerOfPage = (this.width - journalWidth) / 2;
		this.yCornerOfPage = (this.height - journalHeight) / 2;

		int scaledXLeftPageCoord = this.xCornerOfPage + 20;
		int scaledYLeftPageCoord = this.yCornerOfPage + 35;

		int scaledXRightPageCoord = this.xCornerOfPage + 170;
		int scaledYRightPageCoord = this.yCornerOfPage + 35;

		// Set the text box bounds
		this.leftPage.updateBounds(scaledXLeftPageCoord, scaledYLeftPageCoord, this.journalWidth, this.journalHeight - 50);
		this.rightPage.updateBounds(scaledXRightPageCoord, scaledYRightPageCoord, this.journalWidth, this.journalHeight - 50);

		this.leftPageLabel.updateBounds(scaledXLeftPageCoord, scaledYLeftPageCoord + this.journalHeight - 85, 50, 50);
		this.rightPageLabel.updateBounds(scaledXRightPageCoord + (int) (this.journalWidth * .42), scaledYRightPageCoord + this.journalHeight - 85, 50, 50);

		this.forwardButton.updateBounds(this.width - 64, this.height - 64);
		this.backwardButton.updateBounds(0, this.height - 64);

		this.bookmarkButton.updateBounds(64, this.height - 60, this.width - 128, 40);

		this.previousWidth = this.width;
		this.previousHeight = this.height;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;
		if (button.id == this.bookmarkButton.id)
		{
			entityPlayer.closeScreen();
			entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.BLOOD_STAINED_JOURNAL_ID, entityPlayer.worldObj, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ());
		}
		else if (button.id == this.forwardButton.id)
		{
			advancePage();
		}
		else if (button.id == this.backwardButton.id)
		{
			rewindPage();
		}
	}

	private void advancePage()
	{
		pageNumber = pageNumber + 2;

		if (!Utility.hasIndex(this.textOnEachPage, pageNumber) && !Utility.hasIndex(this.researchRecipes, (pageNumber - this.textOnEachPage.size()) * 2))
		{
			pageNumber = pageNumber - 2;
		}
	}

	private void rewindPage()
	{
		pageNumber = MathHelper.clamp_int(pageNumber - 2, 0, Integer.MAX_VALUE);
	}

	private void updateText()
	{
		this.textOnEachPage.clear();
		String textToDistribute = this.originalCompleteText;
		boolean alternater = true;

		while (!textToDistribute.isEmpty())
		{
			String leftOver;
			if (alternater)
			{
				leftOver = leftPage.getExtraText(textToDistribute);
			}
			else
			{
				leftOver = rightPage.getExtraText(textToDistribute);
			}
			alternater = !alternater;

			String page = textToDistribute.substring(0, textToDistribute.length() - leftOver.length());
			textToDistribute = textToDistribute.substring(textToDistribute.length() - leftOver.length());

			this.textOnEachPage.add(page);
		}
	}

	private void drawCraftingRecipe(int x, int y, ConvertedRecipe recipe)
	{
		this.mc.renderEngine.bindTexture(journalCraftingGrid);
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
					renderItem.func_180450_b(recipe.getInput()[i], x + 5 + i * 30, y + 5 + i % 3);
				}
			}
		}
		else
		{
			for (int i = 0; i < recipe.getHeight(); i++)
			{
				for (int j = 0; j < recipe.getWidth(); j++)
				{
					renderItem.func_180450_b(recipe.getInput()[i * recipe.getWidth() + j], x + 5 + j * 30, y + 5 + i * 30);
				}
			}
		}

		renderItem.func_180450_b(recipe.getOutput(), x + 105, y + 35);

		RenderHelper.disableStandardItemLighting();

		renderItem.zLevel = 0.0F;
	}

	// If E is typed we close the GUI screen
	@Override
	protected void keyTyped(final char character, final int iDontKnowWhatThisDoes) throws IOException
	{
		if ((character == 'e') || (character == 'E'))
		{
			Minecraft.getMinecraft().thePlayer.closeScreen();
			GL11.glFlush();
		}
		super.keyTyped(character, iDontKnowWhatThisDoes);
	}
}
