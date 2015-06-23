/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.BookmarkButton;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.ForwardBackwardButtons;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.PageNumberLabel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.TextBox;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ClientData;

public class BloodStainedJournalPageGUI extends GuiScreen
{
	private String textPrevious;
	private String textCurrent;
	private String textNext;
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

	private int xCornerOfPage = 0;
	private int yCornerOfPage = 0;
	private int journalWidth = 0;
	private int journalHeight = 0;

	public BloodStainedJournalPageGUI(final String textNext, final String title)
	{
		// Setup tile and page text. Then add left and right page text boxes
		super();
		this.textNext = textNext;
		this.title = title;
		this.leftPage = new TextBox(this.xCornerOfPage, this.yCornerOfPage, this.journalWidth, this.journalWidth, ClientData.journalFont);
		this.rightPage = new TextBox(this.xCornerOfPage, this.yCornerOfPage, this.journalWidth, this.journalWidth, ClientData.journalFont);
		leftPageLabel = new PageNumberLabel(this.xCornerOfPage, this.yCornerOfPage, this.journalWidth, this.journalHeight, ClientData.journalFont);
		rightPageLabel = new PageNumberLabel(this.xCornerOfPage, this.yCornerOfPage, this.journalWidth, this.journalHeight, ClientData.journalFont);
		this.forwardButton = new ForwardBackwardButtons(15, this.width - 64, this.height - 64, 64, 64, true);
		this.backwardButton = new ForwardBackwardButtons(16, 0, this.height - 64, 64, 64, false);

		this.bookmarkButton = new BookmarkButton(1, 0, (int) (this.yCornerOfPage + this.journalWidth / 2.1), this.width, 40);
		this.journalTexture = new ResourceLocation("afraidofthedark:textures/gui/bloodStainedJournalPage.png");
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.clear();
		this.buttonList.add(this.bookmarkButton);
		this.buttonList.add(this.forwardButton);
		this.buttonList.add(this.backwardButton);

		this.textPrevious = "";
		this.textCurrent = this.textNext.substring(0, this.textNext.length() - leftPage.getExtraText(rightPage.getExtraText(textNext)).length());
		this.textNext = leftPage.getExtraText(rightPage.getExtraText(textNext));
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
		this.drawDefaultBackground();

		this.mc.renderEngine.bindTexture(this.journalTexture);

		// Has the window been resized?
		if ((this.width != this.previousWidth) || (this.height != this.previousHeight))
		{
			// Calculate various variables later used in text box width/height calculation
			this.xCornerOfPage = (this.width - 330) / 2;
			this.yCornerOfPage = (this.height - 330) / 2;

			this.journalWidth = 330;
			this.journalHeight = 330;

			// Set the journal font sizes
			ClientData.journalFont.setFontSize(20, 32, 126, false);
			ClientData.journalTitleFont.setFontSize(32, 32, 126, false);

			int scaledXLeftPageCoord = this.xCornerOfPage + 20;
			int scaledYLeftPageCoord = this.yCornerOfPage + 35;

			int scaledXRightPageCoord = this.xCornerOfPage + 170;
			int scaledYRightPageCoord = this.yCornerOfPage + 35;

			// Set the text box bounds
			this.leftPage.updateBounds(scaledXLeftPageCoord, scaledYLeftPageCoord, this.journalWidth, this.journalHeight - 20);
			this.rightPage.updateBounds(scaledXRightPageCoord, scaledYRightPageCoord, this.journalWidth, this.journalHeight - 20);

			this.leftPageLabel.updateBounds(scaledXLeftPageCoord, scaledYLeftPageCoord, 50, 50);
			this.rightPageLabel.updateBounds(scaledXRightPageCoord, scaledYRightPageCoord, 50, 50);

			this.forwardButton.updateBounds(this.width - 64, this.height - 64);
			this.backwardButton.updateBounds(0, this.height - 64);

			this.bookmarkButton.updateBounds(64, this.height - 40, this.width - 128, 40);

			this.previousWidth = this.width;
			this.previousHeight = this.height;
		}

		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);

		double pageScaleWidth = this.width / 640.0D;
		double pageScaleHeight = this.height / 480.0D;
		double pageScaleCombined = (pageScaleWidth + pageScaleHeight) / 2.0D;

		GL11.glTranslated(this.width / 2, this.height / 2, 0.0D);
		GL11.glScaled(pageScaleCombined, pageScaleCombined, 1.0D);
		GL11.glTranslated(-this.width / 2, -this.height / 2, 0.0D);

		// Draw the journal background
		Gui.drawScaledCustomSizeModalRect(this.xCornerOfPage, this.yCornerOfPage, 0, 0, this.journalWidth, this.journalHeight, this.journalWidth, this.journalHeight, this.journalWidth, this.journalHeight);

		// Draw the title
		ClientData.journalTitleFont.drawString(this.title, this.xCornerOfPage + 15, this.yCornerOfPage + 15, 0xFF800000);
		// Anything the left page can't draw, move to right page
		this.rightPage.drawText(this.leftPage.drawText(this.textCurrent));

		this.leftPageLabel.drawNumber(Integer.toString(this.pageNumber + 1));
		this.rightPageLabel.drawNumber(Integer.toString(this.pageNumber + 2));

		bookmarkButton.drawButton(mc, mouseX, mouseY);

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();

		this.backwardButton.drawButton(mc, mouseX, mouseY);
		this.forwardButton.drawButton(mc, mouseX, mouseY);
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
		this.textPrevious = this.textPrevious + this.textCurrent;
		this.textCurrent = this.textNext.substring(0, this.textNext.length() - leftPage.getExtraText(rightPage.getExtraText(textNext)).length());
		this.textNext = leftPage.getExtraText(rightPage.getExtraText(textNext));
		pageNumber = pageNumber + 2;
	}

	private void rewindPage()
	{
		this.textNext = this.textNext + this.textCurrent;
		this.textCurrent = this.textPrevious.substring(leftPage.getExtraText(rightPage.getExtraText(textPrevious)).length(), this.textPrevious.length());
		this.textPrevious = this.textPrevious.substring(0, textPrevious.length() - textCurrent.length());
		pageNumber = pageNumber - 2;
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
