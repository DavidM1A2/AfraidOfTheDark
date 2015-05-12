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

	public BloodStainedJournalPageGUI(String text)
	{
		super();
		this.text = text;
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

		double scale;

		GL11.glColor4f(1, 1, 1, 1);
		ResourceLocation texture = new ResourceLocation("afraidofthedark:textures/gui/bloodStainedJournalPage.png");
		mc.renderEngine.bindTexture(texture);
		scale = this.width / 640.0;
		this.drawScaledCustomSizeModalRect((int) ((this.width - 330 * scale) / 2), (int) ((this.height - 330 * scale) / 2), 0, 0, (int) (330 * scale), (int) (330 * scale), (int) (330 * scale), (int) (330 * scale), (float) (330 * scale), (float) (330 * scale));

		this.drawText(((int) ((this.width - 330 * scale) / 2) + 10), ((int) ((this.height - 330 * scale)) / 2) + 10);

		super.drawScreen(i, j, f);
	}

	// If E is typed we close the GUI screen
	@Override
	protected void keyTyped(char character, int iDontKnowWhatThisDoes) throws IOException
	{
		if (character == 'e' || character == 'E')
		{
			Minecraft.getMinecraft().thePlayer.closeScreen();
		}
		super.keyTyped(character, iDontKnowWhatThisDoes);
	}

	private void drawText(int leftPageCoord, int topPageCoord)
	{
		Refrence.aotdFont.drawString(this, text, leftPageCoord, topPageCoord, 0xFF7F00FF);
	}
}
