package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiContainer
import com.davidm1a2.afraidofthedark.client.gui.base.SpriteSheetController
import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.AbstractGui
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
) : AOTDGuiContainer(x, y, width, height) {
    /**
     * Draws the sprite sheet on the screen
     */
    override fun draw() {
        // Ensure the sprite sheet image is visible
        if (isVisible) {
            // Draw any children
            super.draw()

            // Bind the spritesheet texture
            Minecraft.getInstance().textureManager.bindTexture(spriteSheet)

            // Grab all fields from the controller
            val percentageToNextFrame = sheetController.percentageTowardsNextFrame
            val currentFrame = sheetController.currentFrame
            val frameWidth = sheetController.frameWidth
            val frameHeight = sheetController.frameHeight
            val totalFrames = sheetController.totalFrames

            if (sheetController.frameInterpolate) {
                // Setup the color tint
                GlStateManager.color4f(
                    color.red / 255f,
                    color.green / 255f,
                    color.blue / 255f,
                    sheetController.percentageTowardsNextFrame
                )

                // The next frame to interpolate with is either current frame + 1 or 0 if we looped around
                val nextFrame = if (currentFrame == totalFrames) 0 else currentFrame + 1

                // Draw vertically
                if (sheetController.isVertical) {
                    AbstractGui.blit(
                        getXWithOffset(),
                        getYWithOffset(),
                        width,
                        height,
                        0f,
                        (currentFrame * frameHeight).toFloat(),
                        frameWidth,
                        frameHeight,
                        frameWidth,
                        frameHeight * totalFrames
                    )
                    GlStateManager.color4f(
                        color.red / 255f,
                        color.green / 255f,
                        color.blue / 255f,
                        1 - percentageToNextFrame
                    )
                    AbstractGui.blit(
                        getXWithOffset(),
                        getYWithOffset(),
                        width,
                        height,
                        0f,
                        (nextFrame * frameHeight).toFloat(),
                        frameWidth,
                        frameHeight,
                        frameWidth,
                        frameHeight * totalFrames
                    )
                } else {
                    AbstractGui.blit(
                        getXWithOffset(),
                        getYWithOffset(),
                        width,
                        height,
                        (currentFrame * frameWidth).toFloat(),
                        0f,
                        frameWidth,
                        frameHeight,
                        frameWidth * totalFrames,
                        frameHeight
                    )
                    GlStateManager.color4f(
                        color.red / 255f,
                        color.green / 255f,
                        color.blue / 255f,
                        1 - percentageToNextFrame
                    )
                    AbstractGui.blit(
                        getXWithOffset(),
                        getYWithOffset(),
                        width,
                        height,
                        (nextFrame * frameWidth).toFloat(),
                        0f,
                        frameWidth,
                        frameHeight,
                        frameWidth * totalFrames,
                        frameHeight
                    )
                }
            } else {
                // Setup the color tint
                GlStateManager.color3f(
                    color.red / 255f,
                    color.green / 255f,
                    color.blue / 255f
                )

                // Draw vertically
                if (sheetController.isVertical) {
                    AbstractGui.blit(
                        getXWithOffset(),
                        getYWithOffset(),
                        width,
                        height,
                        0f,
                        (currentFrame * frameHeight).toFloat(),
                        frameWidth,
                        frameHeight,
                        frameWidth,
                        frameHeight * totalFrames
                    )
                } else {
                    AbstractGui.blit(
                        getXWithOffset(),
                        getYWithOffset(),
                        width,
                        height,
                        (currentFrame * frameWidth).toFloat(),
                        0f,
                        frameWidth,
                        frameHeight,
                        frameWidth * totalFrames,
                        frameHeight
                    )
                }
            }
        }
    }
}