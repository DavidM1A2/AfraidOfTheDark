/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls;

import com.DavidM1A2.AfraidOfTheDark.client.trueTypeFont.TrueTypeFont;

public class AOTDGuiLabel extends AOTDGuiTextComponent
{
	private int maxStrLength = Integer.MAX_VALUE;
	private String actualStringToDraw;

	public AOTDGuiLabel(final int x, final int y, TrueTypeFont font)
	{
		super(x, y, 0, 0, font);
	}

	// Draw the text given the width and height as bounds
	public void draw()
	{
		if (this.isVisible())
		{
			super.draw();
			this.drawText(this.getXScaled(), this.getYScaled());
		}
	}

	public void setMaxStringLength(int strLen)
	{
		maxStrLength = strLen;
	}
}
