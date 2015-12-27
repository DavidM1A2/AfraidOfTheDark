/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls;

import com.DavidM1A2.AfraidOfTheDark.client.TrueTyper.FontLoader;

import net.minecraft.util.ResourceLocation;

public class AOTDGuiLabel extends AOTDGuiTextComponent
{
	com.DavidM1A2.AfraidOfTheDark.client.TrueTyper.TrueTypeFont fontX;

	public AOTDGuiLabel(final int x, final int y, TrueTypeFont font)
	{
		super(x, y, 0, 0, font);
		fontX = FontLoader.createFont(new ResourceLocation("afraidofthedark:fonts/Targa MS Hand.ttf"), 40f, false);
	}

	// Draw the text given the width and height as bounds
	public void draw()
	{
		if (this.isVisible())
		{
			super.draw();
			//GL11.glPushMatrix();
			//GL11.glScaled(this.getScaleX(), this.getScaleY(), 1.0);
			fontX.drawString(this.getXScaled(), this.getYScaled(), this.getText(), 0.3f, 0.3f, com.DavidM1A2.AfraidOfTheDark.client.TrueTyper.TrueTypeFont.ALIGN_LEFT);
			//this.getFont().drawString(this.getX(), this.getY(), this.getText(), 0.3f, 0.3f, this.getTextAlignment());
			//GL11.glPopMatrix();
		}
	}
}
