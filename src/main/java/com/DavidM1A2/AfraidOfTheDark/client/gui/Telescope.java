package com.DavidM1A2.AfraidOfTheDark.client.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.opengl.GL11;

public class Telescope extends GuiScreen
{
	/*
	 * GUI for the blood stained journal on the initial signing
	 */
	@Override
	public void initGui()
	{
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
