/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class BookmarkButton extends GuiButton
{
	public BookmarkButton(int buttonId, int x, int y, int widthIn, int heightIn)
	{
		super(buttonId, x, y, widthIn, heightIn, "");
	}

	// Bookmark button has no texture
	@Override
	public void drawButton(final Minecraft minecraft, final int mouseX, final int mouseY)
	{
	}

	// Update x, y, width, and height of a textbox
	public void updateBounds(final int x, final int y, final int width, final int height)
	{
		this.xPosition = x;
		this.yPosition = y;
		this.width = width;
		this.height = height;
	}
}
