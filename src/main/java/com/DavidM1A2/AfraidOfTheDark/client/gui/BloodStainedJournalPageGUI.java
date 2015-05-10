package com.DavidM1A2.AfraidOfTheDark.client.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class BloodStainedJournalPageGUI extends GuiScreen
{
	public BloodStainedJournalPageGUI()
	{
		super();
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
		mc.renderEngine.bindTexture(new ResourceLocation("afraidofthedark:textures/gui/bloodStainedJournalPage.png"));
		this.drawTexturedModalRect((this.width - 256) / 2, (this.height - 256) / 2, 0, 0, 256, 256);

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
}
