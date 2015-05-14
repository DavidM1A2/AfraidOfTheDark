/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

// The background is in fact a button
public class ResearchBackground extends GuiButton
{
	// Texture for the background.
	protected final ResourceLocation buttonTexture;
	private boolean isBeingHeld = false;

	// Offsets
	private int xOffset = 0;
	private int yOffset = 0;

	// Setup the research background
	public ResearchBackground(int ID, int xPosition, int yPosition, int rectWidth, int rectHeight, int xOffset, int yOffset, String texture)
	{
		super(ID, xPosition, yPosition, rectWidth, rectHeight, "");
		buttonTexture = new ResourceLocation(texture);
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	// You can set the offset here
	public void setOffset(int xOffset, int yOffset)
	{
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	// Draw the background
	@Override
	public void drawButton(Minecraft minecraft, int int1, int int2)
	{
		if (this.visible)
		{
			FontRenderer fontrenderer = minecraft.fontRendererObj;
			minecraft.getTextureManager().bindTexture(buttonTexture);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
			this.drawTexturedModalRect(this.xPosition, this.yPosition, xOffset, yOffset, this.width, this.height);
			this.mouseDragged(minecraft, int1, int2);
		}
	}
}
