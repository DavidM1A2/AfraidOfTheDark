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

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiButton;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiLabel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiScreen;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiTextBox;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.BookmarkButton;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.ForwardBackwardButtons;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.TrueTypeFont;
import com.DavidM1A2.AfraidOfTheDark.common.recipe.ConvertedRecipe;
import com.DavidM1A2.AfraidOfTheDark.common.recipe.RecipeUtility;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class BloodStainedJournalPageGUI extends AOTDGuiScreen
{
	private final String COMPLETE_TEXT;

	private final List<String> textOnEachPage = new LinkedList<String>();;

	private final String TITLE;

	private final AOTDGuiTextBox leftPage;
	private final AOTDGuiTextBox rightPage;

	private final ForwardBackwardButtons forwardButton;
	private final ForwardBackwardButtons backwardButton;

	private final AOTDGuiLabel leftPageLabel;
	private final AOTDGuiLabel rightPageLabel;
	private final AOTDGuiLabel title;

	private final BookmarkButton bookmarkButton;

	private int pageNumber = 0;

	private static final ResourceLocation JOURNAL_TEXTURE = new ResourceLocation("afraidofthedark:textures/gui/bloodStainedJournalPage.png");

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
		this.COMPLETE_TEXT = textNext;
		this.TITLE = title;

		this.journalWidth = 330;
		this.journalHeight = 330;
		// Calculate various variables later used in text box width/height calculation
		this.xCornerOfPage = (640 - journalWidth) / 2;
		this.yCornerOfPage = (360 - journalHeight) / 2;

		TrueTypeFont pageFont = Utility.createTrueTypeFont("Targa MS Hand", 32f, false);
		TrueTypeFont pageNumberFont = Utility.createTrueTypeFont("Targa MS Hand", 32f, false);
		this.leftPage = new AOTDGuiTextBox(this.xCornerOfPage + 25, this.yCornerOfPage + 55, this.journalWidth + 300, this.journalHeight - 50, pageFont);
		this.rightPage = new AOTDGuiTextBox(this.xCornerOfPage + 175, this.yCornerOfPage + 55, this.journalWidth + 300, this.journalHeight - 50, pageFont);
		this.leftPageLabel = new AOTDGuiLabel(this.xCornerOfPage + 8, this.yCornerOfPage + this.journalHeight - 50, pageNumberFont);
		this.rightPageLabel = new AOTDGuiLabel(this.xCornerOfPage + 300, this.yCornerOfPage + this.journalHeight - 50, pageNumberFont);
		this.title = new AOTDGuiLabel(this.xCornerOfPage + 5, this.yCornerOfPage + 15, Utility.createTrueTypeFont("Targa MS Hand", 50f, true));

		this.forwardButton = new ForwardBackwardButtons(15, this.width - 64, this.height - 64, 64, 64, true);
		this.backwardButton = new ForwardBackwardButtons(16, 0, this.height - 64, 64, 64, false);
		this.bookmarkButton = new BookmarkButton(1, 0, (int) (this.yCornerOfPage + this.journalWidth / 2.1), this.width, 40);
	}

	@Override
	public void initGui()
	{
		super.initGui();

		this.getButtonController().clear();
		this.getButtonController().add(this.bookmarkButton);
		this.getButtonController().add(this.forwardButton);
		this.getButtonController().add(this.backwardButton);
		this.leftPageLabel.setColor(0xFFF78181);
		this.rightPageLabel.setColor(0xFFF78181);
		this.leftPageLabel.setText("1");
		this.rightPageLabel.setText("2");
		this.leftPageLabel.setWidth(50);
		this.leftPageLabel.setHeight(50);
		this.rightPageLabel.setWidth(50);
		this.rightPageLabel.setHeight(50);
		this.leftPage.setColor(0xFF4d0000);
		this.rightPage.setColor(0xFF4d0000);
		this.title.setText(TITLE);
		this.title.setColor(0xFF4d0000);

		this.updateBounds();
		this.updateText();
		this.updatePages();

		this.backwardButton.setVisible(this.hasPageBackward());
		this.forwardButton.setVisible(this.hasPageForward());
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
	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks)
	{
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);

		double currentGUIScaleX = this.width / 640.0D;
		double currentGUIScaleY = this.height / 360.0D;
		double currentGUIScale = Math.min(currentGUIScaleX, currentGUIScaleY);
		GL11.glTranslated(currentGUIScale * (-240 + ((this.width - this.xCornerOfPage) / 2)), currentGUIScale * (-180 + ((this.height - this.yCornerOfPage) / 2)), 0.0D);
		GL11.glTranslated(this.width / 2, this.height / 2, 0.0D);
		GL11.glScaled(currentGUIScale, currentGUIScale, 1.0D);
		GL11.glTranslated(-this.width / 2, -this.height / 2, 0.0D);

		this.drawJournal();

		GL11.glPopMatrix();

		super.drawScreen(mouseX, mouseY, partialTicks);
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void drawJournal()
	{
		// Draw the background texture
		this.mc.renderEngine.bindTexture(this.JOURNAL_TEXTURE);
		//Gui.drawScaledCustomSizeModalRect(this.xCornerOfPage, this.yCornerOfPage, 0, 0, this.journalWidth, this.journalHeight, this.journalWidth, this.journalHeight, this.journalWidth, this.journalHeight);
		Gui.drawModalRectWithCustomSizedTexture(this.xCornerOfPage, this.yCornerOfPage, 0, 0, journalWidth, journalHeight, journalWidth, journalHeight);

		// Draw the title
		title.draw();

		// Draw the page contents
		this.drawPageContents();
	}

	private void drawPageContents()
	{
		// 2 Recipes per page, so (page number - number of pages of text) * 2 is the current place to start drawing recipes
		int adjustedIndexForRecipe = (pageNumber - textOnEachPage.size()) * 2;

		if (this.leftPage.getText().equals(""))
		{
			if (Utility.hasIndex(researchRecipes, adjustedIndexForRecipe))
			{
				RecipeUtility.drawCraftingRecipe(leftPage.getX() + 5, leftPage.getY(), this.researchRecipes.get(adjustedIndexForRecipe));
				adjustedIndexForRecipe = adjustedIndexForRecipe + 1;
				if (Utility.hasIndex(researchRecipes, adjustedIndexForRecipe))
				{
					RecipeUtility.drawCraftingRecipe(leftPage.getX() + 5, leftPage.getY() + 100, this.researchRecipes.get(adjustedIndexForRecipe));
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
				RecipeUtility.drawCraftingRecipe(rightPage.getX() + 10, rightPage.getY(), this.researchRecipes.get(adjustedIndexForRecipe));
				adjustedIndexForRecipe = adjustedIndexForRecipe + 1;
				if (Utility.hasIndex(researchRecipes, adjustedIndexForRecipe))
				{
					RecipeUtility.drawCraftingRecipe(rightPage.getX() + 10, rightPage.getY() + 100, this.researchRecipes.get(adjustedIndexForRecipe));
				}
			}
		}
		else
		{
			this.rightPage.draw();
		}

		this.leftPageLabel.draw();
		this.rightPageLabel.draw();
	}

	private void updateBounds()
	{
		this.forwardButton.setX(this.width - 64);
		this.forwardButton.setY(this.height - 64);

		this.backwardButton.setX(0);
		this.backwardButton.setY(this.height - 64);

		this.bookmarkButton.setX(this.width / 2 - 20);
		this.bookmarkButton.setY(this.height - 100);
		this.bookmarkButton.setWidth(40);
		this.bookmarkButton.setHeight(100);
	}

	@Override
	public void actionPerformed(AOTDGuiButton button)
	{
		EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;
		if (button.getId() == this.bookmarkButton.getId())
		{
			entityPlayer.closeScreen();
			entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.BLOOD_STAINED_JOURNAL_ID, entityPlayer.worldObj, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ());
		}
		else if (button.getId() == this.forwardButton.getId() && button.isVisible())
		{
			advancePage();
		}
		else if (button.getId() == this.backwardButton.getId() && button.isVisible())
		{
			rewindPage();
		}
	}

	private void advancePage()
	{
		if (this.hasPageForward())
		{
			pageNumber = pageNumber + 2;
		}
		leftPageLabel.setText(Integer.toString(this.pageNumber + 1));
		rightPageLabel.setText(Integer.toString(this.pageNumber + 2));
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
		leftPageLabel.setText(Integer.toString(this.pageNumber + 1));
		rightPageLabel.setText(Integer.toString(this.pageNumber + 2));
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

			String page = textToDistribute.substring(0, textToDistribute.length() - leftOver.length());
			textToDistribute = textToDistribute.substring(textToDistribute.length() - leftOver.length());

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
