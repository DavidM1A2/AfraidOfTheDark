/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

public abstract class AOTDGuiButtonMovable extends AOTDGuiButton
{
	protected final int ORIGINAL_X_POSITION;
	protected final int ORIGINAL_Y_POSITION;

	public AOTDGuiButtonMovable(int x, int y, int width, int height)
	{
		super(x, y, width, height, null, "");
		this.ORIGINAL_X_POSITION = x;
		this.ORIGINAL_Y_POSITION = y;
	}

	public AOTDGuiButtonMovable(int x, int y, int width, int height, TrueTypeFont font, String icon)
	{
		super(x, y, width, height, font, icon);
		this.ORIGINAL_X_POSITION = x;
		this.ORIGINAL_Y_POSITION = y;
	}

	public void offset(int xOffset, int yOffset)
	{
		this.setX(ORIGINAL_X_POSITION - xOffset);
		this.setY(ORIGINAL_Y_POSITION - yOffset);
	}
}
