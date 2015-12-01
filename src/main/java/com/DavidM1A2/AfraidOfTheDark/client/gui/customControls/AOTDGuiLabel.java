/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

public class AOTDGuiLabel extends AOTDGuiTextComponent
{
	public AOTDGuiLabel(final int x, final int y, TrueTypeFont font)
	{
		super(x, y, 0, 0, font);
	}

	// Draw the text given the width and height as bounds
	public void draw()
	{
		super.draw();
		this.getFont().drawString(this.getX(), this.getY(), this.getText(), 0.3f, 0.3f, this.getColor());
	}
}
