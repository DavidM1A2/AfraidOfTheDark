/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class AOTDGuiImage extends AOTDGuiContainer
{
	private final ResourceLocation imageTexture;
	private int u = 0;
	private int v = 0;
	private final int textureWidth;
	private final int textureHeight;

	public AOTDGuiImage(int x, int y, int width, int height, int textureHeight, int textureWidth, String imageTexture)
	{
		super(x, y, width, height);
		this.imageTexture = new ResourceLocation("afraidofthedark:" + imageTexture);
		this.textureHeight = textureHeight;
		this.textureWidth = textureWidth;
	}

	public AOTDGuiImage(int x, int y, int width, int height, String imageTexture)
	{
		super(x, y, width, height);
		this.imageTexture = new ResourceLocation("afraidofthedark:" + imageTexture);
		this.textureHeight = -1;
		this.textureWidth = -1;
	}

	@Override
	public void draw()
	{
		if (this.isVisible())
		{
			super.draw();
			GL11.glEnable(GL11.GL_BLEND);
			Minecraft.getMinecraft().getTextureManager().bindTexture(imageTexture);
			if (textureHeight == -1 || textureWidth == -1)
				Gui.drawModalRectWithCustomSizedTexture(this.getXScaled(), this.getYScaled(), u, v, this.getWidthScaled(), this.getHeightScaled(), this.getWidthScaled(), this.getHeightScaled());
			else
				Gui.drawModalRectWithCustomSizedTexture(this.getXScaled(), this.getYScaled(), u, v, this.getWidthScaled(), this.getHeightScaled(), textureWidth, textureHeight);
			GlStateManager.enableBlend();
		}
	}

	public void setU(int u)
	{
		this.u = u;
	}

	public void setV(int v)
	{
		this.v = v;
	}
}