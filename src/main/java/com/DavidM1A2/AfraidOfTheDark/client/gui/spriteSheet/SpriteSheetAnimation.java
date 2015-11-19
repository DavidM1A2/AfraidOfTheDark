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
	private int totalFrames;
	private int frameWidth;
	private int frameHeight;
	private ResourceLocation spriteSheet;
	private boolean frameInterpolation;

	public SpriteSheetAnimation(ResourceLocation spriteSheet, int frameDelayInMillis, int totalFrames, int frameWidth, int frameHeight, boolean frameInterpolation)
	{
		this.spriteSheet = spriteSheet;
		this.frameCount = 0;
		this.frameDelay = frameDelayInMillis;
		this.currentFrame = 0;
		this.totalFrames = totalFrames;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.frameInterpolation = frameInterpolation;
	}

	public void update()
	{
		if ((System.currentTimeMillis() - frameCount) > frameDelay)
		{
			frameCount = System.currentTimeMillis();
			currentFrame = currentFrame + 1;

			if (currentFrame > totalFrames - 1)
			{
				currentFrame = 0;
			}
		}
	}

	public void draw(int x, int y, int width, int height)
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(spriteSheet);

		if (frameInterpolation)
		{
			float timeSinceLastUpdate = (System.currentTimeMillis() - frameCount);
			float percentageToNextFrame = MathHelper.clamp_float(1 - (timeSinceLastUpdate / frameDelay), 0.0f, 1.0f);

			GlStateManager.color(1.0F, 1.0F, 1.0F, percentageToNextFrame);
			Gui.drawScaledCustomSizeModalRect(x, y, 0, currentFrame * frameHeight, frameWidth, frameHeight, width, height, frameWidth, frameHeight * totalFrames);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1 - percentageToNextFrame);
			int nextFrame = currentFrame + 1;
			if (nextFrame > totalFrames - 1)
			{
				nextFrame = 0;
			}
			Gui.drawScaledCustomSizeModalRect(x, y, 0, nextFrame * frameHeight, frameWidth, frameHeight, width, height, frameWidth, frameHeight * totalFrames);
		}
		else
		{
			Gui.drawScaledCustomSizeModalRect(x, y, 0, currentFrame * frameHeight, frameWidth, frameHeight, width, height, frameWidth, frameHeight * totalFrames);
		}
	}
}
