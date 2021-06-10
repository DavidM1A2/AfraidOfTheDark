package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.layout.RelativeDimensions
import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.AbstractGui
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import kotlin.math.roundToInt

/**
 * Class representing an image to be rendered on the GUI
 */
open class ImagePane(
    var imageTexture: ResourceLocation,
    var displayMode: DispMode = DispMode.STRETCH
) : AOTDPane() {
    var u = 0.0f
    var v = 0.0f
    var textureWidth: Double
    var textureHeight: Double

    init {
        Minecraft.getInstance().textureManager.bindTexture(imageTexture)
        textureWidth = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH).toDouble()
        textureHeight = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT).toDouble()
    }

    constructor(imageTexture: String, displayMode: DispMode = DispMode.STRETCH) : this(ResourceLocation(imageTexture), displayMode)

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
                AbstractGui.blit(x, y, u, v, width, height, width, height)
            }
            GlStateManager.popMatrix()

            // Draw the any children
            super.draw()
        }
    }

    override fun negotiateDimensions(width: Double, height: Double) {
        val calcPrefWidth = if (prefSize is RelativeDimensions) prefSize.width * width else prefSize.width
        val calcPrefHeight = if (prefSize is RelativeDimensions) prefSize.height * height else prefSize.height
        val fitWidth = if (width < calcPrefWidth) width else calcPrefWidth
        val fitHeight = if (height < calcPrefHeight) height else calcPrefHeight
        when (displayMode) {
            DispMode.FIT_TO_TEXTURE -> {
                val scaleXRatio = (fitWidth / textureWidth).coerceAtMost(1.0)
                val scaleYRatio = (fitHeight / textureHeight).coerceAtMost(1.0)
                val scaleMinRatio = scaleXRatio.coerceAtMost(scaleYRatio)
                this.width = (textureWidth * scaleMinRatio).roundToInt()
                this.height = (textureHeight * scaleMinRatio).roundToInt()
            }
            DispMode.FIT_TO_PARENT -> {
                val scaleXRatio = fitWidth / textureWidth
                val scaleYRatio = fitHeight / textureHeight
                val scaleMinRatio = scaleXRatio.coerceAtMost(scaleYRatio)
                this.width = (textureWidth * scaleMinRatio).roundToInt()
                this.height = (textureHeight * scaleMinRatio).roundToInt()
            }
            DispMode.STRETCH -> {
                val scaleXRatio = fitWidth / textureWidth
                val scaleYRatio = fitHeight / textureHeight
                this.width = (textureWidth * scaleXRatio).roundToInt()
                this.height = (textureHeight * scaleYRatio).roundToInt()
            }
        }
        // Reset the inbounds flag
        inBounds = true
    }

    enum class DispMode {
        FIT_TO_TEXTURE,
        FIT_TO_PARENT,
        STRETCH
    }
}
