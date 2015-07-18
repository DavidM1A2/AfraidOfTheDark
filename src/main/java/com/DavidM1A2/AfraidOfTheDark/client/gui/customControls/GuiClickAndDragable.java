/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public abstract class GuiClickAndDragable extends GuiScreen
{
	// Variables for calculating the GUI offset
	protected int guiOffsetX = 0;
	protected int guiOffsetY = 0;
	protected int originalXPosition = 0;
	protected int originalYPosition = 0;

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

	// When you click the mouse move the background
	@Override
	protected void mouseClicked(final int xPos, final int yPos, final int shouldBe0) throws IOException
	{
		this.originalXPosition = xPos + this.guiOffsetX;
		this.originalYPosition = yPos + this.guiOffsetY;
		super.mouseClicked(xPos, yPos, shouldBe0);
	}

	// When the mouse is dragged, update the GUI accordingly
	@Override
	protected void mouseClickMove(final int mouseX, final int mouseY, final int lastButtonClicked, final long timeBetweenClicks)
	{
		this.guiOffsetX = this.originalXPosition - mouseX;
		this.guiOffsetY = this.originalYPosition - mouseY;

		this.checkOutOfBounds();
	}

	// Opening a research book DOES NOT pause the game (unlike escape)
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	protected abstract void checkOutOfBounds();
}
