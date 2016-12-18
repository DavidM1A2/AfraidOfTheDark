/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls;

import net.minecraft.util.math.MathHelper;

public class SpriteSheetController
{
	private final int frameDelayInMillis;
	private final int totalFrames;
	private final int frameWidth;
	private final int frameHeight;
	private final boolean frameInterpolate;
	private final boolean isVertical;

	private float percentageTowardsNextFrame = 0.0f;
	private long lastFrameTime = 0;
	private int currentFrame = 0;
	private int nextFrame = 0;

	public SpriteSheetController(int frameDelayInMillis, int totalFrames, int frameWidth, int frameHeight, boolean frameInterpolate, boolean isVertical)
	{
		this.frameDelayInMillis = frameDelayInMillis;
		this.totalFrames = totalFrames;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.frameInterpolate = frameInterpolate;
		this.isVertical = isVertical;
	}

	public void performUpdate()
	{
		if ((System.currentTimeMillis() - lastFrameTime) > frameDelayInMillis)
		{
			lastFrameTime = System.currentTimeMillis();
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
		this.percentageTowardsNextFrame = MathHelper.clamp_float(1 - ((float) (System.currentTimeMillis() - lastFrameTime) / frameDelayInMillis), 0.0f, 1.0f);
	}

	public boolean frameInterpolate()
	{
		return this.frameInterpolate;
	}

	public float getPercentageTowardsNextFrame()
	{
		return this.percentageTowardsNextFrame;
	}

	public boolean spriteSheetIsVertical()
	{
		return this.isVertical;
	}

	public int getFrameWidth()
	{
		return this.frameWidth;
	}

	public int getFrameHeight()
	{
		return this.frameWidth;
	}

	public int getCurrentFrame()
	{
		return this.currentFrame;
	}

	public int getNextFrame()
	{
		return this.nextFrame;
	}

	public int getTotalFrames()
	{
		return this.totalFrames;
	}
}
