package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility
import com.davidm1a2.afraidofthedark.client.gui.TextureDimensionsCache
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiContainer
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDImageDispMode
import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.AbstractGui
import net.minecraft.util.ResourceLocation
import java.lang.Math.round
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Class representing an image to be rendered on the GUI
 *
 * @param x             The X location of the top left corner
 * @param y             The Y location of the top left corner
 * @param width         The width of the component
 * @param height        The height of the component
 * @param imageTexture  The texture of the image
 * @param textureWidth  The width of the image png texture
 * @param textureHeight The height of the image png texture
 * @property u the x value to start drawing from inside the texture
 * @property v the y value to start drawing from inside the texture
 */
class AOTDGuiImage(
        var imageTexture: ResourceLocation,
        var displayMode: AOTDImageDispMode = AOTDImageDispMode.STRETCH) :
        AOTDGuiContainer() {

    constructor(imageTexture: String, displayMode: AOTDImageDispMode = AOTDImageDispMode.STRETCH): this(ResourceLocation(imageTexture), displayMode)

    var u = 0.0f
    var v = 0.0f
    var textureWidth = TextureDimensionsCache.cache.get(imageTexture).width
    var textureHeight = TextureDimensionsCache.cache.get(imageTexture).height

    /**
     * Draws the GUI image given the width and height
     */
    override fun draw() {
        if (this.isVisible) {
            GlStateManager.pushMatrix()
            // Enable alpha blending
            GlStateManager.enableBlend()
            // Set the color
            GlStateManager.color4f(
                this.color.red / 255f,
                this.color.green / 255f,
                this.color.blue / 255f,
                this.color.alpha / 255f
            )
            // Bind the texture to render
            Minecraft.getInstance().textureManager.bindTexture(this.imageTexture)
            // Check for invalid texture dimensions
            if (textureHeight > -1 && textureWidth > -1) {
                AbstractGui.blit(x, y, 0, u, v, width, height, width, height)
            }
            GlStateManager.popMatrix()

            // Draw the any children
            super.draw()
        }
    }

    override fun negotiateDimensions(width: Int, height: Int) {
        when (displayMode) {
            AOTDImageDispMode.FIT_TO_SIZE -> {
                val scaleXRatio = (width/textureWidth.toDouble()).coerceAtMost(1.0)
                val scaleYRatio = (height/textureHeight.toDouble()).coerceAtMost(1.0)
                val scaleMinRatio = scaleXRatio.coerceAtMost(scaleYRatio)
                this.width = (textureWidth * scaleMinRatio).roundToInt()
                this.height = (textureHeight * scaleMinRatio).roundToInt()
            }
            AOTDImageDispMode.FIT_TO_PARENT -> {
                val scaleXRatio = width/textureWidth.toDouble()
                val scaleYRatio = height/textureHeight.toDouble()
                val scaleMinRatio = scaleXRatio.coerceAtMost(scaleYRatio)
                this.width = (textureWidth * scaleMinRatio).roundToInt()
                this.height = (textureHeight * scaleMinRatio).roundToInt()
            }
            AOTDImageDispMode.STRETCH -> {
                val scaleXRatio = width/textureWidth.toDouble()
                val scaleYRatio = height/textureHeight.toDouble()
                this.width = (textureWidth * scaleXRatio).roundToInt()
                this.height = (textureHeight * scaleYRatio).roundToInt()
            }
        }
    }
}
