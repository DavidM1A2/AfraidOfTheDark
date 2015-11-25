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
	private long frameCount = 0;
	private final int frameDelay;
	private int currentFrame = 0;
	private int nextFrame = 0;
	private final int totalFrames;
	private final int frameWidth;
	private final int frameHeight;
	private final ResourceLocation spriteSheet;
	private final boolean frameInterpolation;
	private final boolean isVertical;
	private float percentageToNextFrame = 0;

	public SpriteSheetAnimation(ResourceLocation spriteSheet, int frameDelayInMillis, int totalFrames, int frameWidth, int frameHeight, boolean frameInterpolation, boolean isVertical)
	{
		this.spriteSheet = spriteSheet;
		this.frameDelay = frameDelayInMillis;
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
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
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
