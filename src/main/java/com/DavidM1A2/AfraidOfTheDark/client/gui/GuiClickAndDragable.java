package com.DavidM1A2.AfraidOfTheDark.client.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.opengl.GL11;

public abstract class GuiClickAndDragable extends GuiScreen
{
	// Variables for calculating the GUI offset
	protected int guiOffsetX = 0;
	protected int guiOffsetY = 0;
	protected int originalXPosition = 0;
	protected int originalYPosition = 0;

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

	// When you click the mouse move the background
	@Override
	protected void mouseClicked(int xPos, int yPos, int shouldBe0) throws IOException
	{
		originalXPosition = xPos + guiOffsetX;
		originalYPosition = yPos + guiOffsetY;
		super.mouseClicked(xPos, yPos, shouldBe0);
	}

	// When the mouse is dragged, update the GUI accordingly
	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int lastButtonClicked, long timeBetweenClicks)
	{
		guiOffsetX = originalXPosition - mouseX;
		guiOffsetY = originalYPosition - mouseY;

		checkOutOfBounds();
	}

	// Opening a research book DOES NOT pause the game (unlike escape)
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	protected abstract void checkOutOfBounds();
}
