package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiComponent
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.resources.ResourceLocation
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
    override fun draw(poseStack: PoseStack) {
        if (this.isVisible && this.imageTexture != null) {
            poseStack.pushPose()
            // Enable alpha blending
            RenderSystem.enableBlend()
            RenderSystem.setShader(GameRenderer::getPositionTexShader)

            // Set the color
            RenderSystem.setShaderColor(
                this.color.red / 255f,
                this.color.green / 255f,
                this.color.blue / 255f,
                this.color.alpha / 255f
            )
            // Bind the texture to render
            RenderSystem.setShaderTexture(0, this.imageTexture!!)
            // Check for invalid texture dimensions
            if (textureHeight > -1 && textureWidth > -1) {
                GuiComponent.blit(poseStack, x, y, u, v, width, height, width, height)
            }
            poseStack.popPose()

            // Draw the any children
            super.draw(poseStack)
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
            Minecraft.getInstance().textureManager.bindForSetup(this.imageTexture!!)
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
