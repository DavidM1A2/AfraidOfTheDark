package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.AbstractGui
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import java.lang.Long.max
import kotlin.math.floor
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Class representing an animated sprite to be rendered on the GUI
 */
open class SpritePane : AOTDPane {
    private val spritesheetTexture: ResourceLocation
    private val displayMode: ImagePane.DispMode
    private val columns: Int
    private val rows: Int
    private val curAnimation: MutableList<Int> = mutableListOf()
    private var curAnimMode: AnimMode? = null
    private var textureWidth: Double = -1.0
    private var textureHeight: Double = -1.0
    private var frameWidth: Double = -1.0
    private var frameHeight: Double = -1.0
    private var curFrame: Int = 0
    private var curFPS: Double = 24.0
    private var lastTime: Long = 0

    constructor(spritesheetTexture: ResourceLocation, columns: Int, rows: Int, displayMode: ImagePane.DispMode = ImagePane.DispMode.STRETCH) {
        this.spritesheetTexture = spritesheetTexture
        this.displayMode = displayMode
        this.columns = columns
        this.rows = rows
        loadTextureDimensions()
    }

    constructor(spritesheetTexture: String, columns: Int, rows: Int, displayMode: ImagePane.DispMode = ImagePane.DispMode.STRETCH) : this(ResourceLocation(spritesheetTexture), columns, rows, displayMode)

    /**
     * Draws the GUI image given the width and height
     */
    override fun draw(matrixStack: MatrixStack) {
        if (this.isVisible) {
            handleAnimation()

            matrixStack.pushPose()
            // Enable alpha blending
            RenderSystem.enableBlend()
            // Set the color
            RenderSystem.color4f(
                this.color.red / 255f,
                this.color.green / 255f,
                this.color.blue / 255f,
                this.color.alpha / 255f
            )
            // Bind the texture to render
            Minecraft.getInstance().textureManager.bind(this.spritesheetTexture)
            // Check for invalid texture dimensions
            if (textureHeight > -1 && textureWidth > -1) {
                val col = curFrame % columns
                val row = curFrame / columns  // INTENTIONAL integer division
                AbstractGui.blit(matrixStack, x, y, width, height, col.toFloat()/columns*width,
                    row.toFloat()/rows*height, width/columns,
                    height/rows,
                    width, height
                )
            }
            matrixStack.popPose()

            // Draw the any children
            super.draw(matrixStack)
        }
    }

    private fun handleAnimation() {
        if (curAnimation.isNotEmpty()) {
            val msBetweenFrames = 1000 / curFPS
            val time = System.currentTimeMillis()
            val timeDelta = time - lastTime
            if (timeDelta > msBetweenFrames) {
                curFrame = curAnimation.removeFirst()
                if (curAnimMode == AnimMode.LOOP) curAnimation.add(curFrame)
                val overshoot = (timeDelta - msBetweenFrames).toLong()
                lastTime = time - min(overshoot, (msBetweenFrames).toLong())  // Try to "catch up" if we overshot the frame, but not by more than one frame
            }
        }
    }

    fun setFrame(frame: Int) {
        this.curFrame = frame
        this.invalidate()
    }

    fun setAnimation(frames: List<Int>, mode: AnimMode, fps: Double = 24.0) {
        this.curAnimation.clear()
        this.curAnimation.addAll(frames)
        this.curAnimMode = mode
        this.curFPS = fps
    }

    fun stopAnimation() {
        this.curAnimation.clear()
    }

    private fun loadTextureDimensions() {
        Minecraft.getInstance().textureManager.bind(spritesheetTexture)
        textureWidth = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH).toDouble()
        textureHeight = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT).toDouble()
        if (textureWidth.isNaN() || textureHeight.isNaN()) {
            throw IllegalStateException("Texture $spritesheetTexture does not exist")
        }
        frameWidth = textureWidth/columns
        frameHeight = textureHeight/rows
    }

    override fun negotiateDimensions(width: Double, height: Double) {
        val fitWidth = (if (prefSize.isRelative) prefSize.width * width else prefSize.width).coerceAtMost(width)
        val fitHeight = (if (prefSize.isRelative) prefSize.height * height else prefSize.height).coerceAtMost(height)
        when (displayMode) {
            ImagePane.DispMode.FIT_TO_TEXTURE -> {
                val scaleXRatio = (fitWidth / frameWidth).coerceAtMost(1.0)
                val scaleYRatio = (fitHeight / frameHeight).coerceAtMost(1.0)
                val scaleMinRatio = min(scaleXRatio, scaleYRatio)
                this.width = (frameWidth * scaleMinRatio).roundToInt()
                this.height = (frameHeight * scaleMinRatio).roundToInt()
            }
            ImagePane.DispMode.FIT_TO_PARENT -> {
                val scaleXRatio = fitWidth / frameWidth
                val scaleYRatio = fitHeight / frameHeight
                val scaleMinRatio = min(scaleXRatio, scaleYRatio)
                this.width = (frameWidth * scaleMinRatio).roundToInt()
                this.height = (frameHeight * scaleMinRatio).roundToInt()
            }
            ImagePane.DispMode.STRETCH -> {
                val scaleXRatio = fitWidth / frameWidth
                val scaleYRatio = fitHeight / frameHeight
                this.width = (frameWidth * scaleXRatio).roundToInt()
                this.height = (frameHeight * scaleYRatio).roundToInt()
            }
        }
        // Reset the inbounds flag
        inBounds = true
    }

    enum class AnimMode {
        ONE_SHOT,
        LOOP
    }
}
