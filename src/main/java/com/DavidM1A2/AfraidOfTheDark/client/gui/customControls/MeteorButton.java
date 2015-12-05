/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.MeteorTypes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class MeteorButton extends AOTDGuiButtonMovable
{
	private final ResourceLocation METEOR_TEXTURE;
	private final MeteorTypes myType;

	public MeteorButton(final int buttonId, final int x, final int y, final int width, final int height, final MeteorTypes myType)
	{
		super(x, y, width, height);
		this.myType = myType;

		if (myType == MeteorTypes.silver)
			this.METEOR_TEXTURE = new ResourceLocation("afraidofthedark:textures/gui/astralSilverMeteor.png");
		else if (myType == MeteorTypes.starMetal)
			this.METEOR_TEXTURE = new ResourceLocation("afraidofthedark:textures/gui/starMetalMeteor.png");
		else
			this.METEOR_TEXTURE = new ResourceLocation("afraidofthedark:textures/gui/sunstoneMeteor.png");
	}

	// Draw button draws the button using OpenGL
	@Override
	public void draw()
	{
		// Make sure it should be visible
		if (this.isVisible())
		{
			super.draw();

			Minecraft.getMinecraft().getTextureManager().bindTexture(this.METEOR_TEXTURE);
			Gui.drawScaledCustomSizeModalRect(this.getXScaled(), this.getYScaled(), 0, 0, 64, 64, this.getWidthScaled(), this.getHeightScaled(), 64, 64);
		}
	}

	public MeteorTypes getMyType()
	{
		return this.myType;
	}
}
