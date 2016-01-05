/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class AOTDGuiSpriteSheetImage extends AOTDGuiContainer
{
	private final ResourceLocation spriteSheet;
	private final SpriteSheetController sheetController;

	public AOTDGuiSpriteSheetImage(int x, int y, int width, int height, ResourceLocation spriteSheet, SpriteSheetController sheetController)
	{
		super(x, y, width, height);
		this.spriteSheet = spriteSheet;
		this.sheetController = sheetController;
	}

	@Override
	public void draw()
	{
		if (this.isVisible())
		{
			super.draw();
			Minecraft.getMinecraft().getTextureManager().bindTexture(this.spriteSheet);

			float percentageToNextFrame = this.sheetController.getPercentageTowardsNextFrame();
			int currentFrame = this.sheetController.getCurrentFrame();
			int nextFrame = this.sheetController.getNextFrame();
			int frameWidth = this.sheetController.getFrameWidth();
			int frameHeight = this.sheetController.getFrameHeight();
			int totalFrames = this.sheetController.getTotalFrames();

			if (this.sheetController.frameInterpolate())
			{
				GlStateManager.color(1.0F, 1.0F, 1.0F, this.sheetController.getPercentageTowardsNextFrame());
				if (this.sheetController.spriteSheetIsVertical())
				{
					Gui.drawScaledCustomSizeModalRect(this.getXScaled(), this.getYScaled(), 0, currentFrame * frameHeight, frameWidth, frameHeight, this.getWidthScaled(), this.getHeightScaled(), frameWidth, frameHeight * totalFrames);
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1 - percentageToNextFrame);
					Gui.drawScaledCustomSizeModalRect(this.getXScaled(), this.getYScaled(), 0, nextFrame * frameHeight, frameWidth, frameHeight, this.getWidthScaled(), this.getHeightScaled(), frameWidth, frameHeight * totalFrames);
				}
				else
				{
					Gui.drawScaledCustomSizeModalRect(this.getXScaled(), this.getYScaled(), currentFrame * frameWidth, 0, frameWidth, frameHeight, this.getWidthScaled(), this.getHeightScaled(), frameWidth * totalFrames, frameHeight);
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1 - percentageToNextFrame);
					Gui.drawScaledCustomSizeModalRect(this.getXScaled(), this.getYScaled(), nextFrame * frameWidth, 0, frameWidth, frameHeight, this.getWidthScaled(), this.getHeightScaled(), frameWidth * totalFrames, frameHeight);
				}
			}
			else
			{
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				if (this.sheetController.spriteSheetIsVertical())
				{
					Gui.drawScaledCustomSizeModalRect(this.getXScaled(), this.getYScaled(), 0, currentFrame * frameHeight, frameWidth, frameHeight, this.getWidthScaled(), this.getHeightScaled(), frameWidth, frameHeight * totalFrames);
				}
				else
				{
					Gui.drawScaledCustomSizeModalRect(this.getXScaled(), this.getYScaled(), currentFrame * frameWidth, 0, frameWidth, frameHeight, this.getWidthScaled(), this.getHeightScaled(), frameWidth * totalFrames, frameHeight);
				}
			}
		}
	}
}
