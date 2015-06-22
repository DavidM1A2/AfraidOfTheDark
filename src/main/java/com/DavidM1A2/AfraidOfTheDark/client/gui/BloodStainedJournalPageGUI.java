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
import org.lwjgl.util.Point;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customButtons.BookmarkButton;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ClientData;

public class BloodStainedJournalPageGUI extends GuiScreen
{
	private final String text;
	private final String title;

	private final TextBox leftPage;
	private final TextBox rightPage;

	private BookmarkButton bookmarkButton;

	private int previousWidth = 0;
	private int previousHeight = 0;

	private final ResourceLocation journalTexture;

	private int xCornerOfPage = 0;
	private int yCornerOfPage = 0;
	private int journalWidth = 0;
	private int journalHeight = 0;

	public BloodStainedJournalPageGUI(final String text, final String title)
	{
		// Setup tile and page text. Then add left and right page text boxes
		super();
		this.text = text;
		this.title = title;
		this.leftPage = new TextBox(this.xCornerOfPage, this.yCornerOfPage, this.journalWidth, this.journalWidth, ClientData.journalFont);
		this.rightPage = new TextBox(this.xCornerOfPage, this.yCornerOfPage, this.journalWidth, this.journalWidth, ClientData.journalFont);
		this.bookmarkButton = new BookmarkButton(1, 0, (int) (this.yCornerOfPage + this.journalWidth / 2.1), this.width, 40);
		this.journalTexture = new ResourceLocation("afraidofthedark:textures/gui/bloodStainedJournalPage.png");
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.clear();
		this.buttonList.add(this.bookmarkButton);
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

			this.bookmarkButton.updateBounds(0, this.height - 40, this.width, 40);

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
		this.rightPage.drawText(this.leftPage.drawText(this.text));

		super.drawScreen(i, j, f);

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	private static Point centerPointOfRect(Point point)
	{
		return new Point(point.getX() / 2, point.getY() / 2);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (button.id == this.bookmarkButton.id)
		{
			EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;
			entityPlayer.closeScreen();
			entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.BLOOD_STAINED_JOURNAL_ID, entityPlayer.worldObj, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ());
		}
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
