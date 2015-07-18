/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.CustomFont;

public class PageNumberLabel
{
	private int xPosition;
	private int yPosition;
	private int width;
	private int height;
	private final CustomFont myFontRefrence;

	public PageNumberLabel(final int x, final int y, final int width, final int height, final CustomFont myFont)
	{
		// Given x, y, widht, height, and a font we can draw the textbox
		this.xPosition = x;
		this.yPosition = y;
		this.width = width;
		this.height = height;
		this.myFontRefrence = myFont;
	}

	// Draw the text given the width and height as bounds
	public void drawNumber(final String text)
	{
		final int x = this.xPosition;
		final int y = this.yPosition;
		this.myFontRefrence.drawString(text, x, y, 0xFF800000);
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
