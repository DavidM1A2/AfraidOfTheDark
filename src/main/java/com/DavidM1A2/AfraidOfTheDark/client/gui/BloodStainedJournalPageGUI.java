package com.DavidM1A2.AfraidOfTheDark.client.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

public class BloodStainedJournalPageGUI extends GuiScreen
{
	private final String text;
	private final String title;

	private TextBox leftPage;
	private TextBox rightPage;

	private double pageScale = 1.0;

	private int previousWidth = 0;
	private int previousHeight = 0;

	private final ResourceLocation journalTexture;

	private int xCornerOfPage = 0;
	private int yCornerOfPage = 0;
	private int journalWidth = 0;

	public BloodStainedJournalPageGUI(String text, String title)
	{
		super();
		this.text = text;
		this.title = title;
		leftPage = new TextBox(xCornerOfPage, yCornerOfPage, this.journalWidth, this.journalWidth, Refrence.journalFont);
		rightPage = new TextBox(xCornerOfPage, yCornerOfPage, this.journalWidth, this.journalWidth, Refrence.journalFont);
		journalTexture = new ResourceLocation("afraidofthedark:textures/gui/bloodStainedJournalPage.png");
	}

	@Override
	public void initGui()
	{
		super.initGui();
		buttonList.clear();
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
	public void drawScreen(int i, int j, float f)
	{
		drawDefaultBackground();

		GL11.glColor4f(1, 1, 1, 1);
		mc.renderEngine.bindTexture(journalTexture);

		if (this.width != this.previousWidth || this.height != this.previousHeight)
		{
			pageScale = this.width / 640.0;
			this.xCornerOfPage = (int) ((this.width - 330 * pageScale) / 2);
			this.yCornerOfPage = (int) ((this.height - 330 * pageScale) / 2);
			this.journalWidth = (int) (330 * pageScale);

			Refrence.journalFont.setFontSize((int) (pageScale * 20), 32, 126, false);
			Refrence.journalTitleFont.setFontSize((int) (pageScale * 32), 32, 126, false);

			leftPage.updateBounds(xCornerOfPage + (int) (20 * pageScale), yCornerOfPage + (int) (35 * pageScale), this.journalWidth, this.journalWidth - (yCornerOfPage + (int) (35 * pageScale)));
			rightPage.updateBounds(xCornerOfPage + (int) (20 * pageScale) + (int) (pageScale * (this.width / 2)), yCornerOfPage + (int) (35 * pageScale), this.journalWidth, this.journalWidth - (yCornerOfPage + (int) (35 * pageScale)));

			this.previousWidth = this.width;
			this.previousHeight = this.height;
		}

		this.drawScaledCustomSizeModalRect(xCornerOfPage, yCornerOfPage, 0, 0, journalWidth, journalWidth, journalWidth, journalWidth, journalWidth, journalWidth);

		Refrence.journalTitleFont.drawString(this.title, xCornerOfPage + (int) (15 * pageScale), yCornerOfPage + (int) (15 * pageScale), 0xFF800000);
		// Anything the left page can't draw, move to right page
		rightPage.drawText(leftPage.drawText(text));

		super.drawScreen(i, j, f);
	}

	// If E is typed we close the GUI screen
	@Override
	protected void keyTyped(char character, int iDontKnowWhatThisDoes) throws IOException
	{
		if (character == 'e' || character == 'E')
		{
			Minecraft.getMinecraft().thePlayer.closeScreen();
			GL11.glFlush();
		}
		super.keyTyped(character, iDontKnowWhatThisDoes);
	}
}
