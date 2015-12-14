/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import org.lwjgl.opengl.GL11;

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
		if (this.isVisible())
		{
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			GL11.glPushMatrix();
			GL11.glScaled(this.getScaleX(), this.getScaleY(), 1.0);
			this.getFont().drawString(this.getX(), this.getY(), this.getText(), 0.3f, 0.3f, this.getColor());
			GL11.glPopMatrix();
		}
	}
}
