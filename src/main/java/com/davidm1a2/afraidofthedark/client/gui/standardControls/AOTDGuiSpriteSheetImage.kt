package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiContainer
import com.davidm1a2.afraidofthedark.client.gui.base.SpriteSheetController
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation

/**
 * Class that represents an image that is drawn using a sprite sheet instead of a static texture
 *
 * @param x               The X location of the top left corner
 * @param y               The Y location of the top left corner
 * @param width           The width of the component
 * @param height          The height of the component
 * @param spriteSheet     The sprite sheet to draw from
 * @param sheetController The controller that defines what state the sprite is in
 */
class AOTDGuiSpriteSheetImage(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        private val spriteSheet: ResourceLocation,
        private val sheetController: SpriteSheetController
) : AOTDGuiContainer(x, y, width, height)
{
    /**
     * Draws the sprite sheet on the screen
     */
    override fun draw()
    {
        // Ensure the sprite sheet image is visible
        if (isVisible)
        {
            // Draw any children
            super.draw()
            // Bind the spritesheet texture
            Minecraft.getMinecraft().textureManager.bindTexture(spriteSheet)
            // Grab all fields from the controller
            val percentageToNextFrame = sheetController.percentageTowardsNextFrame
            val currentFrame = sheetController.currentFrame
            val frameWidth = sheetController.frameWidth
            val frameHeight = sheetController.frameHeight
            val totalFrames = sheetController.totalFrames
            // Setup the color tint
            GlStateManager.color(color.red / 255f, color.green / 255f, color.blue / 255f, sheetController.percentageTowardsNextFrame)
            if (sheetController.frameInterpolate)
            {
                // The next frame to interpolate with is either current frame + 1 or 0 if we looped around
                val nextFrame = if (currentFrame == totalFrames) 0 else currentFrame + 1
                // Draw vertically
                if (sheetController.isVertical)
                {
                    Gui.drawScaledCustomSizeModalRect(
                            xScaled,
                            yScaled,
                            0f,
                            currentFrame * frameHeight.toFloat(),
                            frameWidth,
                            frameHeight,
                            widthScaled,
                            heightScaled,
                            frameWidth.toFloat(),
                            frameHeight * totalFrames.toFloat()
                    )
                    GlStateManager.color(color.red / 255f, color.green / 255f, color.blue / 255f, 1 - percentageToNextFrame)
                    Gui.drawScaledCustomSizeModalRect(
                            xScaled,
                            yScaled,
                            0f,
                            nextFrame * frameHeight.toFloat(),
                            frameWidth,
                            frameHeight,
                            widthScaled,
                            heightScaled,
                            frameWidth.toFloat(),
                            frameHeight * totalFrames.toFloat()
                    )
                }
                else
                {
                    Gui.drawScaledCustomSizeModalRect(
                            xScaled,
                            yScaled,
                            currentFrame * frameWidth.toFloat(),
                            0f,
                            frameWidth,
                            frameHeight,
                            widthScaled,
                            heightScaled,
                            frameWidth * totalFrames.toFloat(),
                            frameHeight.toFloat()
                    )
                    GlStateManager.color(color.red / 255f, color.green / 255f, color.blue / 255f, 1 - percentageToNextFrame)
                    Gui.drawScaledCustomSizeModalRect(
                            xScaled,
                            yScaled,
                            nextFrame * frameWidth.toFloat(),
                            0f,
                            frameWidth,
                            frameHeight,
                            widthScaled,
                            heightScaled,
                            frameWidth * totalFrames.toFloat(),
                            frameHeight.toFloat()
                    )
                }
            }
            else
            {
                // Draw vertically
                if (sheetController.isVertical)
                {
                    Gui.drawScaledCustomSizeModalRect(
                            xScaled,
                            yScaled,
                            0f,
                            currentFrame * frameHeight.toFloat(),
                            frameWidth,
                            frameHeight,
                            widthScaled,
                            heightScaled,
                            frameWidth.toFloat(),
                            frameHeight * totalFrames.toFloat()
                    )
                }
                else
                {
                    Gui.drawScaledCustomSizeModalRect(
                            xScaled,
                            yScaled,
                            currentFrame * frameWidth.toFloat(),
                            0f,
                            frameWidth,
                            frameHeight,
                            widthScaled,
                            heightScaled,
                            frameWidth * totalFrames.toFloat(),
                            frameHeight.toFloat()
                    )
                }
            }
        }
    }
}