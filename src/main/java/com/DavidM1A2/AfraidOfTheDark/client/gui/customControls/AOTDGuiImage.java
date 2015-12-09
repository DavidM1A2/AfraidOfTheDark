/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class AOTDGuiImage extends AOTDGuiComponent
{
	private final ResourceLocation imageTexture;

	public AOTDGuiImage()
	{
		super();
		this.imageTexture = null;
	}

	public AOTDGuiImage(int x, int y, int width, int height, String imageTexture)
	{
		super(x, y, width, height);
		this.imageTexture = new ResourceLocation("afraidofthedark:" + imageTexture);
	}

	@Override
	public void draw()
	{
		super.draw();
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(imageTexture);
		Gui.drawModalRectWithCustomSizedTexture(this.getXScaled(), this.getYScaled(), 0, 0, this.getWidthScaled(), this.getHeightScaled(), this.getWidthScaled(), this.getHeightScaled());
	}
}
