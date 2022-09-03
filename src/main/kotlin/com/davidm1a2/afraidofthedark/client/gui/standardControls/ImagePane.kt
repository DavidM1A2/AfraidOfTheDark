package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.AbstractGui
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Class representing an image to be rendered on the GUI
 */
open class ImagePane : AOTDPane {
    private var textureWidth = -1.0
    private var textureHeight = -1.0
    private var imageTexture: ResourceLocation? = null
    private val displayMode: DispMode
    private var allottedWidth = -1.0
    private var allottedHeight = -1.0
    var u = 0.0f
    var v = 0.0f

    constructor(imageTexture: ResourceLocation? = null, displayMode: DispMode = DispMode.STRETCH) {
        this.imageTexture = imageTexture
        loadTextureDimensions()
        this.displayMode = displayMode
    }

    constructor(imageTexture: String, displayMode: DispMode = DispMode.STRETCH) : this(ResourceLocation(imageTexture), displayMode)

    /**
     * Draws the GUI image given the width and height
     */
    override fun draw(matrixStack: MatrixStack) {
        if (this.isVisible && this.imageTexture != null) {
            matrixStack.pushPose()
            // Enable alpha blending
            RenderSystem.enableBlend()
            // Set our alpha epsilon to 0 (so we get full alpha range)
            RenderSystem.alphaFunc(GL11.GL_GREATER, 0.0F)
            // Set the color
            RenderSystem.color4f(
                this.color.red / 255f,
                this.color.green / 255f,
                this.color.blue / 255f,
                this.color.alpha / 255f
            )
            // Bind the texture to render
            Minecraft.getInstance().textureManager.bind(this.imageTexture!!)
            // Check for invalid texture dimensions
            if (textureHeight > -1 && textureWidth > -1) {
                AbstractGui.blit(matrixStack, x, y, u, v, width, height, width, height)
            }
            matrixStack.popPose()
            // Reset the render system's alpha
            RenderSystem.defaultAlphaFunc()

            // Draw the any children
            super.draw(matrixStack)
        }
    }

    private fun setActualDimensions(width: Double, height: Double) {
        val fitWidth = (if (prefSize.isRelative) prefSize.width * width else prefSize.width).coerceAtMost(width)
        val fitHeight = (if (prefSize.isRelative) prefSize.height * height else prefSize.height).coerceAtMost(height)
        when (displayMode) {
            DispMode.FIT_TO_TEXTURE -> {
                val scaleXRatio = (fitWidth / textureWidth).coerceAtMost(1.0)
                val scaleYRatio = (fitHeight / textureHeight).coerceAtMost(1.0)
                val scaleMinRatio = min(scaleXRatio, scaleYRatio)
                this.width = (textureWidth * scaleMinRatio).roundToInt()
                this.height = (textureHeight * scaleMinRatio).roundToInt()
            }
            DispMode.FIT_TO_PARENT -> {
                val scaleXRatio = fitWidth / textureWidth
                val scaleYRatio = fitHeight / textureHeight
                val scaleMinRatio = min(scaleXRatio, scaleYRatio)
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
    }

    override fun negotiateDimensions(width: Double, height: Double) {
        // Save the allotted dimensions so we can redraw ourselves later without invalidating the whole screen
        this.allottedWidth = width
        this.allottedHeight = height
        // Do the actual resize
        this.setActualDimensions(width, height)
        // Reset the inbounds flag
        this.inBounds = true
    }

    fun updateImageTexture(imageTexture: ResourceLocation?) {
        val textureChanged = imageTexture != this.imageTexture
        this.imageTexture = imageTexture
        if (textureChanged) {
            this.loadTextureDimensions()
            this.setActualDimensions(allottedWidth, allottedHeight)
            this.calcChildrenBounds()
        }
    }

    private fun loadTextureDimensions() {
        if (imageTexture != null) {
            Minecraft.getInstance().textureManager.bind(imageTexture!!)
            textureWidth = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH).toDouble()
            textureHeight = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT).toDouble()
            if (textureWidth.isNaN() || textureHeight.isNaN()) {
                throw IllegalStateException("Texture $imageTexture does not exist")
            }
        } else {
            textureWidth = -1.0
            textureHeight = -1.0
        }
    }

    enum class DispMode {
        FIT_TO_TEXTURE,
        FIT_TO_PARENT,
        STRETCH
    }
}
