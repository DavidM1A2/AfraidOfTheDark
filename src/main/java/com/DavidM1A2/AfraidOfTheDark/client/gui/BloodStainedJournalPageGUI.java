/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

public class BloodStainedJournalPageGUI extends GuiScreen
{
	private final String text;
	private final String title;

	private final TextBox leftPage;
	private final TextBox rightPage;

	private double pageScale = 1.0;

	private int previousWidth = 0;
	private int previousHeight = 0;

	private final ResourceLocation journalTexture;

	private int xCornerOfPage = 0;
	private int yCornerOfPage = 0;
	private int journalWidth = 0;

	public BloodStainedJournalPageGUI(final String text, final String title)
	{
		// Setup tile and page text. Then add left and right page text boxes
		super();
		this.text = text;
		this.title = title;
		this.leftPage = new TextBox(this.xCornerOfPage, this.yCornerOfPage, this.journalWidth, this.journalWidth, Refrence.journalFont);
		this.rightPage = new TextBox(this.xCornerOfPage, this.yCornerOfPage, this.journalWidth, this.journalWidth, Refrence.journalFont);
		this.journalTexture = new ResourceLocation("afraidofthedark:textures/gui/bloodStainedJournalPage.png");
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.clear();
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
	public void drawScreen(final int i, final int j, final float f)
	{
		this.drawDefaultBackground();

		GL11.glColor4f(1, 1, 1, 1);
		this.mc.renderEngine.bindTexture(this.journalTexture);

		// Has the window been resized?
		if ((this.width != this.previousWidth) || (this.height != this.previousHeight))
		{
			// Scale relative to 640x480
			this.pageScale = this.width / 640.0;
			// Calculate various variables later used in text box width/height calculation
			this.xCornerOfPage = (int) ((this.width - (330 * this.pageScale)) / 2);
			this.yCornerOfPage = (int) ((this.height - (330 * this.pageScale)) / 2);
			this.journalWidth = (int) (330 * this.pageScale);

			// Set the journal font sizes
			Refrence.journalFont.setFontSize((int) (this.pageScale * 20), 32, 126, false);
			Refrence.journalTitleFont.setFontSize((int) (this.pageScale * 32), 32, 126, false);

			// Set the text box bounds
			this.leftPage.updateBounds(this.xCornerOfPage + (int) (20 * this.pageScale), this.yCornerOfPage + (int) (35 * this.pageScale), this.journalWidth, this.journalWidth - (this.yCornerOfPage + (int) (35 * this.pageScale)));
			this.rightPage.updateBounds(this.xCornerOfPage + (int) (20 * this.pageScale) + (int) (this.pageScale * (this.width / 2)), this.yCornerOfPage + (int) (35 * this.pageScale), this.journalWidth, this.journalWidth - (this.yCornerOfPage + (int) (35 * this.pageScale)));

			this.previousWidth = this.width;
			this.previousHeight = this.height;
		}

		// Draw the journal background
		Gui.drawScaledCustomSizeModalRect(this.xCornerOfPage, this.yCornerOfPage, 0, 0, this.journalWidth, this.journalWidth, this.journalWidth, this.journalWidth, this.journalWidth, this.journalWidth);

		// Draw the title
		Refrence.journalTitleFont.drawString(this.title, this.xCornerOfPage + (int) (15 * this.pageScale), this.yCornerOfPage + (int) (15 * this.pageScale), 0xFF800000);
		// Anything the left page can't draw, move to right page
		this.rightPage.drawText(this.leftPage.drawText(this.text));

		super.drawScreen(i, j, f);
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
