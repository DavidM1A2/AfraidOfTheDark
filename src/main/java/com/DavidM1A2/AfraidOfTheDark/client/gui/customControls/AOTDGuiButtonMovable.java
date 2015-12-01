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

	public AOTDGuiButtonMovable(int buttonId, int x, int y, int width, int height)
	{
		super(buttonId, x, y, width, height, null);
		this.ORIGINAL_X_POSITION = x;
		this.ORIGINAL_Y_POSITION = y;
	}

	public void offset(int xOffset, int yOffset)
	{
		this.setX(ORIGINAL_X_POSITION - xOffset);
		this.setY(ORIGINAL_Y_POSITION - yOffset);
	}
}
