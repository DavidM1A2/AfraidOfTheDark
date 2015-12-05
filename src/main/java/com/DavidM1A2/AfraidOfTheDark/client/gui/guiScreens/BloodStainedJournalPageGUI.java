/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.guiScreens;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDActionListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.GuiHandler;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiButton;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiComponent;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiLabel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiRecipe;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiScreen;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiTextBox;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.TrueTypeFont;
import com.DavidM1A2.AfraidOfTheDark.common.recipe.ConvertedRecipe;
import com.DavidM1A2.AfraidOfTheDark.common.recipe.RecipeUtility;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

public class BloodStainedJournalPageGUI extends AOTDGuiScreen
{
	private final String COMPLETE_TEXT;

	private final List<String> textOnEachPage = new LinkedList<String>();;

	private AOTDGuiTextBox leftPage;
	private AOTDGuiTextBox rightPage;

	private AOTDGuiButton forwardButton;
	private AOTDGuiButton backwardButton;

	private AOTDGuiLabel leftPageNumber;
	private AOTDGuiLabel rightPageNumber;

	private AOTDGuiRecipe topLeftRecipe;
	private AOTDGuiRecipe bottomLeftRecipe;
	private AOTDGuiRecipe topRightRecipe;
	private AOTDGuiRecipe bottomRightRecipe;

	private AOTDGuiButton bookmarkButton;

	private AOTDImage journalBackground;

	private AOTDGuiPanel journal = new AOTDGuiPanel();

	private int pageNumber = 0;

	private final List<ConvertedRecipe> researchRecipes = new ArrayList<ConvertedRecipe>();

	private int xCornerOfPage = 0;
	private int yCornerOfPage = 0;
	private int journalWidth = 0;
	private int journalHeight = 0;

	public BloodStainedJournalPageGUI(final String textNext, final String titleText, Item[] relatedItemRecipes)
	{
		// Setup tile and page text. Then add left and right page text boxes
		super();
		this.setupRecipes(relatedItemRecipes);
		this.COMPLETE_TEXT = textNext.replaceAll(" +", " ");

		this.journalWidth = 256;
		this.journalHeight = 256;
		// Calculate various variables later used in text box width/height calculation
		this.xCornerOfPage = (640 - journalWidth) / 2;
		this.yCornerOfPage = (360 - journalHeight) / 2;

		this.journal.setX(xCornerOfPage);
		this.journal.setY(yCornerOfPage);
		this.journal.setWidth(journalWidth);
		this.journal.setHeight(journalHeight);

		this.journal.add(new AOTDImage(0, 0, journalWidth, journalHeight, "textures/gui/bloodStainedJournalPage.png"));

		AOTDGuiLabel title = new AOTDGuiLabel(5, 15, Utility.createTrueTypeFont("Targa MS Hand", 50f, true));
		title.setText(titleText);
		title.setColor(0xFF4d0000);
		this.journal.add(title);

		TrueTypeFont pageNumberFont = Utility.createTrueTypeFont("Targa MS Hand", 32f, false);
		this.leftPageNumber = new AOTDGuiLabel(8, this.journalHeight - 40, pageNumberFont);
		this.rightPageNumber = new AOTDGuiLabel(230, this.journalHeight - 40, pageNumberFont);
		this.leftPageNumber.setText(Integer.toString(1));
		this.rightPageNumber.setText(Integer.toString(2));
		this.leftPageNumber.setColor(0xFF4d0000);
		this.rightPageNumber.setColor(0xFF4d0000);
		this.journal.add(this.leftPageNumber);
		this.journal.add(this.rightPageNumber);

		TrueTypeFont pageFont = Utility.createTrueTypeFont("Targa MS Hand", 32f, false);
		this.leftPage = new AOTDGuiTextBox(5, 45, this.journalWidth / 2 - 10, this.journalHeight - 80, pageFont);
		this.rightPage = new AOTDGuiTextBox(130, 45, this.journalWidth / 2 - 10, this.journalHeight - 80, pageFont);
		this.updateText();
		this.updatePages();
		this.leftPage.setColor(0xFF4d0000);
		this.rightPage.setColor(0xFF4d0000);
		this.journal.add(this.leftPage);
		this.journal.add(this.rightPage);

		this.bookmarkButton = new AOTDGuiButton(this.journalWidth / 2 - 15, this.journalHeight - 30, 10, 30, null, null);
		this.bookmarkButton.addActionListener(new AOTDActionListener()
		{
			@Override
			public void actionPerformed(AOTDGuiComponent component, AOTDActionListener.ActionType actionType)
			{
				if (actionType == ActionType.MouseClick)
				{
					EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;
					entityPlayer.closeScreen();
					entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.BLOOD_STAINED_JOURNAL_ID, entityPlayer.worldObj, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ());
				}
			}
		});
		this.journal.add(this.bookmarkButton);
		this.getButtonController().add(this.bookmarkButton);

		this.getContentPane().add(this.journal);

		this.forwardButton = new AOTDGuiButton(this.getContentPane().getWidth() - 64, this.getContentPane().getHeight() - 64, 64, 64, null, "afraidofthedark:textures/gui/buttons/forwardButton.png");
		this.backwardButton = new AOTDGuiButton(0, this.getContentPane().getHeight() - 64, 64, 64, null, "afraidofthedark:textures/gui/buttons/backwardButton.png");
		this.forwardButton.addActionListener(new AOTDActionListener()
		{
			@Override
			public void actionPerformed(AOTDGuiComponent component, AOTDActionListener.ActionType actionType)
			{
				if (actionType == ActionType.MouseClick)
				{
					BloodStainedJournalPageGUI.this.advancePage();
				}
			}
		});
		this.backwardButton.addActionListener(new AOTDActionListener()
		{
			@Override
			public void actionPerformed(AOTDGuiComponent component, AOTDActionListener.ActionType actionType)
			{
				if (actionType == ActionType.MouseClick)
				{
					BloodStainedJournalPageGUI.this.rewindPage();
				}
			}
		});
		this.getContentPane().add(this.forwardButton);
		this.getContentPane().add(this.backwardButton);
		this.getButtonController().add(this.forwardButton);
		this.getButtonController().add(this.backwardButton);

		this.forwardButton.setVisible(this.hasPageForward());
		this.backwardButton.setVisible(this.hasPageBackward());
	}

	// To draw the screen we first draw the default GUI background, then the
	// background images. Then we add buttons and a frame.
	@Override
	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks)
	{
		GL11.glEnable(GL11.GL_BLEND);
		super.drawScreen(mouseX, mouseY, partialTicks);
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void drawPageContents()
	{
		// 2 Recipes per page, so (page number - number of pages of text) * 2 is the current place to start drawing recipes
		int adjustedIndexForRecipe = (pageNumber - textOnEachPage.size()) * 2;

		if (this.leftPage.getText().equals(""))
		{
			if (Utility.hasIndex(researchRecipes, adjustedIndexForRecipe))
			{
				RecipeUtility.drawCraftingRecipe(leftPage.getXScaled() + 5, leftPage.getYScaled(), this.researchRecipes.get(adjustedIndexForRecipe));
				adjustedIndexForRecipe = adjustedIndexForRecipe + 1;
				if (Utility.hasIndex(researchRecipes, adjustedIndexForRecipe))
				{
					RecipeUtility.drawCraftingRecipe(leftPage.getXScaled() + 5, leftPage.getYScaled() + 100, this.researchRecipes.get(adjustedIndexForRecipe));
					adjustedIndexForRecipe = adjustedIndexForRecipe + 1;
				}
			}
		}
		else
		{
			adjustedIndexForRecipe = adjustedIndexForRecipe + 2;
			this.leftPage.draw();
		}

		if (this.rightPage.getText().equals(""))
		{
			if (Utility.hasIndex(researchRecipes, adjustedIndexForRecipe))
			{
				RecipeUtility.drawCraftingRecipe(rightPage.getXScaled() + 10, rightPage.getYScaled(), this.researchRecipes.get(adjustedIndexForRecipe));
				adjustedIndexForRecipe = adjustedIndexForRecipe + 1;
				if (Utility.hasIndex(researchRecipes, adjustedIndexForRecipe))
				{
					RecipeUtility.drawCraftingRecipe(rightPage.getXScaled() + 10, rightPage.getYScaled() + 100, this.researchRecipes.get(adjustedIndexForRecipe));
				}
			}
		}
		else
		{
			this.rightPage.draw();
		}
	}

	private void advancePage()
	{
		if (this.hasPageForward())
		{
			pageNumber = pageNumber + 2;
		}
		this.leftPageNumber.setText(Integer.toString(this.pageNumber + 1));
		this.rightPageNumber.setText(Integer.toString(this.pageNumber + 2));
		this.backwardButton.setVisible(this.hasPageBackward());
		this.forwardButton.setVisible(this.hasPageForward());
		this.updatePages();
	}

	private void rewindPage()
	{
		if (this.hasPageBackward())
		{
			pageNumber = pageNumber - 2;
		}
		this.leftPageNumber.setText(Integer.toString(this.pageNumber + 1));
		this.rightPageNumber.setText(Integer.toString(this.pageNumber + 2));
		this.backwardButton.setVisible(this.hasPageBackward());
		this.forwardButton.setVisible(this.hasPageForward());
		this.updatePages();
	}

	private boolean hasPageBackward()
	{
		return pageNumber != 0;
	}

	private boolean hasPageForward()
	{
		return !(!Utility.hasIndex(this.textOnEachPage, pageNumber + 2) && !Utility.hasIndex(this.researchRecipes, (pageNumber + 2 - this.textOnEachPage.size()) * 2));
	}

	private void updatePages()
	{
		if (Utility.hasIndex(this.textOnEachPage, pageNumber))
			this.leftPage.setText(this.textOnEachPage.get(pageNumber));
		else
			this.leftPage.setText("");
		if (Utility.hasIndex(textOnEachPage, pageNumber + 1))
			this.rightPage.setText(this.textOnEachPage.get(pageNumber + 1));
		else
			this.rightPage.setText("");
	}

	private void updateText()
	{
		this.textOnEachPage.clear();
		String textToDistribute = this.COMPLETE_TEXT;
		boolean alternater = true;

		while (!textToDistribute.isEmpty() && !textToDistribute.equals(" "))
		{
			String leftOver = alternater ? leftPage.getOverflowText(textToDistribute) : rightPage.getOverflowText(textToDistribute);
			alternater = !alternater;

			String page = textToDistribute.substring(0, textToDistribute.length() - leftOver.length() - 1);
			textToDistribute = textToDistribute.substring(textToDistribute.length() - leftOver.length() - 1);

			this.textOnEachPage.add(page);
		}
	}

	// If E is typed we close the GUI screen
	@Override
	protected void keyTyped(final char character, final int keyCode) throws IOException
	{
		if ((character == 'e') || (character == 'E'))
		{
			EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;
			entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.BLOOD_STAINED_JOURNAL_ID, entityPlayer.worldObj, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
		}
		else if ((character == 'a') || (character == 'A') || (keyCode == Keyboard.KEY_LEFT))
		{
			this.rewindPage();
		}
		else if ((character == 'd') || (character == 'D') || (keyCode == Keyboard.KEY_RIGHT))
		{
			this.advancePage();
		}
		super.keyTyped(character, keyCode);
	}

	private void setupRecipes(Item[] relatedItemRecipes)
	{
		for (Item nextItem : relatedItemRecipes)
		{
			researchRecipes.addAll(RecipeUtility.getRecipesForItem(nextItem));
		}
	}
}
