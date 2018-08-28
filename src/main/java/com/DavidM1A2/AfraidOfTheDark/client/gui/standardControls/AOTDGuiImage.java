package com.DavidM1A2.afraidofthedark.client.gui.standardControls;

import com.DavidM1A2.afraidofthedark.client.gui.base.AOTDGuiContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * Class representing an image to be rendered on the GUI
 */
public class AOTDGuiImage extends AOTDGuiContainer
{
	// The resource location that contains the texture to draw
	private final ResourceLocation imageTexture;
	// The U and V representing the starting top left position inside the image to begin drawing from
	private int u = 0;
	private int v = 0;
	// The image texture's width and height
	private final int textureWidth;
	private final int textureHeight;

	/**
	 * Constructor initializes the bounding box and the image texture
	 *
	 * @param x The X location of the top left corner
	 * @param y The Y location of the top left corner
	 * @param width The width of the component
	 * @param height The height of the component
	 * @param textureWidth The width of the image png texture
	 * @param textureHeight The height of the image png texture
	 * @param imageTexture The texture of the image
	 */
	public AOTDGuiImage(Integer x, Integer y, Integer width, Integer height, Integer textureWidth, Integer textureHeight, String imageTexture)
	{
		super(x, y, width, height);
		this.imageTexture = new ResourceLocation(imageTexture);
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
	}

	/**
	 * Constructor initializes the bounding box and the image texture
	 *
	 * @param x The X location of the top left corner
	 * @param y The Y location of the top left corner
	 * @param width The width of the component
	 * @param height The height of the component
	 * @param imageTexture The texture of the image
	 */
	public AOTDGuiImage(int x, int y, int width, int height, String imageTexture)
	{
		super(x, y, width, height);
		this.imageTexture = new ResourceLocation(imageTexture);
		this.textureHeight = -1;
		this.textureWidth = -1;
	}

	/**
	 * Draws the GUI image given the width and height
	 */
	@Override
	public void draw()
	{
		if (this.isVisible())
		{
			super.draw();
			GlStateManager.pushMatrix();
			// Enable alpha blending
			GlStateManager.enableBlend();
			// Bind the texture to render
			Minecraft.getMinecraft().getTextureManager().bindTexture(this.imageTexture);
			// If the texture width and height are both -1, then we assume the image's size is this control's size
			if (textureHeight == -1 || textureWidth == -1)
				Gui.drawModalRectWithCustomSizedTexture(this.getXScaled(), this.getYScaled(), this.u, this.v, this.getWidthScaled(), this.getHeightScaled(), this.getWidthScaled(), this.getHeightScaled());
			else
				Gui.drawScaledCustomSizeModalRect(this.getXScaled(), this.getYScaled(), this.u, this.v, this.getWidth(), this.getHeight(), this.getWidthScaled(), this.getHeightScaled(), this.textureWidth, this.textureHeight);
			GlStateManager.popMatrix();
		}
	}

	/**
	 * @param u Sets the x value to start drawing from inside the texture
	 */
	public void setU(int u)
	{
		this.u = u;
	}

	/**
	 * @return Gets the x value to start drawing from inside the texture
	 */
	public int getU()
	{
		return this.u;
	}

	/**
	 * @param v Sets the y value to start drawing from inside the texture
	 */
	public void setV(int v)
	{
		this.v = v;
	}

	/**
	 * @return Gets the y value to start drawing from inside the texture
	 */
	public int getV()
	{
		return this.v;
	}

	/**
	 * @return Getter for maximum texture width. If we don't have a texture width use this control's width as the image's width
	 */
	public int getMaxTextureWidth()
	{
		if (textureWidth == -1)
			return this.getWidth();
		else
			return this.textureWidth;
	}

	/**
	 * @return Getter for maximum texture height. If we don't have a texture height use this control's height as the image's height
	 */
	public int getMaxTextureHeight()
	{
		if (textureHeight == -1)
			return this.getHeight();
		else
			return this.textureHeight;
	}
}
