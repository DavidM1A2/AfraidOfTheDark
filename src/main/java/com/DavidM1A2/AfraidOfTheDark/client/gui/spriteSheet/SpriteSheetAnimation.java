/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.spriteSheet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class SpriteSheetAnimation
{
	private long frameCount;
	private int frameDelay;
	private int currentFrame;
	private int nextFrame;
	private int totalFrames;
	private int frameWidth;
	private int frameHeight;
	private ResourceLocation spriteSheet;
	private boolean frameInterpolation;
	private boolean isVertical;
	private float percentageToNextFrame;

	public SpriteSheetAnimation(ResourceLocation spriteSheet, int frameDelayInMillis, int totalFrames, int frameWidth, int frameHeight, boolean frameInterpolation, boolean isVertical)
	{
		this.spriteSheet = spriteSheet;
		this.frameCount = 0;
		this.frameDelay = frameDelayInMillis;
		this.currentFrame = 0;
		this.totalFrames = totalFrames;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.frameInterpolation = frameInterpolation;
		this.isVertical = isVertical;
	}

	public void update()
	{
		if ((System.currentTimeMillis() - frameCount) > frameDelay)
		{
			frameCount = System.currentTimeMillis();
			currentFrame = currentFrame + 1;
			nextFrame = currentFrame + 1;

			if (currentFrame > totalFrames - 1)
			{
				currentFrame = 0;
			}
			else if (currentFrame > totalFrames - 2)
			{
				nextFrame = 0;
			}
		}
		percentageToNextFrame = MathHelper.clamp_float(1 - ((float) (System.currentTimeMillis() - frameCount) / frameDelay), 0.0f, 1.0f);
	}

	public void draw(int x, int y, int width, int height)
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(spriteSheet);
		if (frameInterpolation)
		{
			GlStateManager.color(1.0F, 1.0F, 1.0F, percentageToNextFrame);
			if (this.isVertical)
			{
				Gui.drawScaledCustomSizeModalRect(x, y, 0, currentFrame * frameHeight, frameWidth, frameHeight, width, height, frameWidth, frameHeight * totalFrames);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1 - percentageToNextFrame);
				Gui.drawScaledCustomSizeModalRect(x, y, 0, nextFrame * frameHeight, frameWidth, frameHeight, width, height, frameWidth, frameHeight * totalFrames);
			}
			else
			{
				Gui.drawScaledCustomSizeModalRect(x, y, currentFrame * frameWidth, 0, frameWidth, frameHeight, width, height, frameWidth * totalFrames, frameHeight);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1 - percentageToNextFrame);
				Gui.drawScaledCustomSizeModalRect(x, y, nextFrame * frameWidth, 0, frameWidth, frameHeight, width, height, frameWidth * totalFrames, frameHeight);
			}
		}
		else
		{
			if (this.isVertical)
			{
				Gui.drawScaledCustomSizeModalRect(x, y, 0, currentFrame * frameHeight, frameWidth, frameHeight, width, height, frameWidth, frameHeight * totalFrames);
			}
			else
			{
				Gui.drawScaledCustomSizeModalRect(x, y, currentFrame * frameWidth, 0, frameWidth, frameHeight, width, height, frameWidth * totalFrames, frameHeight);
			}
		}
	}
}
