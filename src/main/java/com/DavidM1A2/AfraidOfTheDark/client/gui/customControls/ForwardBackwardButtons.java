/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class ForwardBackwardButtons extends AOTDGuiButton
{
	private static final ResourceLocation FORWARD_TEXTURE = new ResourceLocation("afraidofthedark:textures/gui/buttons/forwardButton.png");
	private static final ResourceLocation BACKWARD_TEXTURE = new ResourceLocation("afraidofthedark:textures/gui/buttons/backwardButton.png");

	private final boolean isForward;

	public ForwardBackwardButtons(int buttonId, int x, int y, int width, int height, boolean isForward)
	{
		super(buttonId, x, y, width, height, null);
		this.isForward = isForward;
	}

	@Override
	public void draw()
	{
		super.draw();
		if (this.isVisible())
		{
			Minecraft.getMinecraft().getTextureManager().bindTexture(isForward ? FORWARD_TEXTURE : BACKWARD_TEXTURE);
			Gui.drawScaledCustomSizeModalRect(this.getX(), this.getY(), 0, 0, 64, 64, this.getWidth(), this.getHeight(), 64, 64);
		}
	}
}
