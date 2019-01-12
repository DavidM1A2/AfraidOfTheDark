package com.DavidM1A2.afraidofthedark.client.gui.standardControls;

import com.DavidM1A2.afraidofthedark.client.gui.base.AOTDGuiContainer;
import com.DavidM1A2.afraidofthedark.client.gui.base.SpriteSheetController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Class that represents an image that is drawn using a sprite sheet instead of a static texture
 */
public class AOTDGuiSpriteSheetImage extends AOTDGuiContainer
{
	// The sprite sheet to draw from
	private final ResourceLocation spriteSheet;
	// The controller that defines what state the sprite is in
	private final SpriteSheetController sheetController;

	/**
	 * Constructor initializes fields
	 *
	 * @param x The X location of the top left corner
	 * @param y The Y location of the top left corner
	 * @param width The width of the component
	 * @param height The height of the component
	 * @param spriteSheet The sprite sheet to draw from
	 * @param sheetController The controller that defines what state the sprite is in
	 */
	public AOTDGuiSpriteSheetImage(int x, int y, int width, int height, ResourceLocation spriteSheet, SpriteSheetController sheetController)
	{
		super(x, y, width, height);
		this.spriteSheet = spriteSheet;
		this.sheetController = sheetController;
	}

	/**
	 * Draws the sprite sheet on the screen
	 */
	@Override
	public void draw()
	{
		// Ensure the sprite sheet image is visible
		if (this.isVisible())
		{
			// Draw any children
			super.draw();
			// Bind the spritesheet texture
			Minecraft.getMinecraft().getTextureManager().bindTexture(this.spriteSheet);

			// Grab all fields from the controller
			float percentageToNextFrame = this.sheetController.getPercentageTowardsNextFrame();
			int currentFrame = this.sheetController.getCurrentFrame();
			int frameWidth = this.sheetController.getFrameWidth();
			int frameHeight = this.sheetController.getFrameHeight();
			int totalFrames = this.sheetController.getTotalFrames();

			// Setup the color tint
			GlStateManager.color(this.getColor().getRed() / 255f, this.getColor().getGreen() / 255f, this.getColor().getBlue() / 255f, this.sheetController.getPercentageTowardsNextFrame());

			if (this.sheetController.frameInterpolate())
			{
				// The next frame to interpolate with is either current frame + 1 or 0 if we looped around
				int nextFrame = currentFrame == totalFrames ? 0 : currentFrame + 1;

				// Draw vertically
				if (this.sheetController.spriteSheetIsVertical())
				{
					Gui.drawScaledCustomSizeModalRect(this.getXScaled(), this.getYScaled(), 0, currentFrame * frameHeight, frameWidth, frameHeight, this.getWidthScaled(), this.getHeightScaled(), frameWidth, frameHeight * totalFrames);
					GlStateManager.color(this.getColor().getRed() / 255f, this.getColor().getGreen() / 255f, this.getColor().getBlue() / 255f, 1 - percentageToNextFrame);
					Gui.drawScaledCustomSizeModalRect(this.getXScaled(), this.getYScaled(), 0, nextFrame * frameHeight, frameWidth, frameHeight, this.getWidthScaled(), this.getHeightScaled(), frameWidth, frameHeight * totalFrames);
				}
				// Draw horizontally
				else
				{
					Gui.drawScaledCustomSizeModalRect(this.getXScaled(), this.getYScaled(), currentFrame * frameWidth, 0, frameWidth, frameHeight, this.getWidthScaled(), this.getHeightScaled(), frameWidth * totalFrames, frameHeight);
					GlStateManager.color(this.getColor().getRed() / 255f, this.getColor().getGreen() / 255f, this.getColor().getBlue() / 255f, 1 - percentageToNextFrame);
					Gui.drawScaledCustomSizeModalRect(this.getXScaled(), this.getYScaled(), nextFrame * frameWidth, 0, frameWidth, frameHeight, this.getWidthScaled(), this.getHeightScaled(), frameWidth * totalFrames, frameHeight);
				}
			}
			else
			{
				// Draw vertically
				if (this.sheetController.spriteSheetIsVertical())
					Gui.drawScaledCustomSizeModalRect(this.getXScaled(), this.getYScaled(), 0, currentFrame * frameHeight, frameWidth, frameHeight, this.getWidthScaled(), this.getHeightScaled(), frameWidth, frameHeight * totalFrames);
				// Draw horizontally
				else
					Gui.drawScaledCustomSizeModalRect(this.getXScaled(), this.getYScaled(), currentFrame * frameWidth, 0, frameWidth, frameHeight, this.getWidthScaled(), this.getHeightScaled(), frameWidth * totalFrames, frameHeight);
			}
		}
	}
}
