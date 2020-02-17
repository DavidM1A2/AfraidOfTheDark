package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiContainer
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation

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
class AOTDGuiImage(x: Int, y: Int, width: Int, height: Int, var imageTexture: ResourceLocation, private var textureWidth: Int = -1, private var textureHeight: Int = -1) :
        AOTDGuiContainer(x, y, width, height)
{
    var u = 0
    var v = 0

    /**
     * Constructor initializes the bounding box and the image texture
     *
     * @param x            The X location of the top left corner
     * @param y            The Y location of the top left corner
     * @param width        The width of the component
     * @param height       The height of the component
     * @param imageTexture The texture of the image
     * @param textureWidth  The width of the image png texture
     * @param textureHeight The height of the image png texture
     */
    constructor(x: Int, y: Int, width: Int, height: Int, imageTexture: String, textureWidth: Int = -1, textureHeight: Int = -1) : this(
            x,
            y,
            width,
            height,
            ResourceLocation(imageTexture),
            textureWidth,
            textureHeight
    )

    /**
     * Draws the GUI image given the width and height
     */
    override fun draw()
    {
        if (this.isVisible)
        {
            GlStateManager.pushMatrix()
            // Enable alpha blending
            GlStateManager.enableBlend()
            // Set the color
            GlStateManager.color(this.color.red / 255f, this.color.green / 255f, this.color.blue / 255f, this.color.alpha / 255f)
            // Bind the texture to render
            Minecraft.getMinecraft().textureManager.bindTexture(this.imageTexture)
            // If the texture width and height are both -1, then we assume the image's size is this control's size
            if (textureHeight == -1 || textureWidth == -1)
            {
                Gui.drawModalRectWithCustomSizedTexture(
                        this.getXScaled(),
                        this.getYScaled(),
                        this.u.toFloat(),
                        this.v.toFloat(),
                        this.getWidthScaled(),
                        this.getHeightScaled(),
                        this.getWidthScaled().toFloat(),
                        this.getHeightScaled().toFloat()
                )
            }
            else
            {
                Gui.drawScaledCustomSizeModalRect(
                        this.getXScaled(),
                        this.getYScaled(),
                        this.u.toFloat(),
                        this.v.toFloat(),
                        this.getWidth(),
                        this.getHeight(),
                        this.getWidthScaled(),
                        this.getHeightScaled(),
                        this.textureWidth.toFloat(),
                        this.textureHeight.toFloat()
                )
            }
            GlStateManager.popMatrix()

            // Draw the any children
            super.draw()
        }
    }

    /**
     * @return Getter for maximum texture width. If we don't have a texture width use this control's width as the image's width
     */
    fun getMaxTextureWidth(): Int
    {
        return if (textureWidth == -1)
        {
            getWidth()
        }
        else
        {
            textureWidth
        }
    }

    /**
     * @return Getter for maximum texture height. If we don't have a texture height use this control's height as the image's height
     */
    fun getMaxTextureHeight(): Int
    {
        return if (textureHeight == -1)
        {
            getHeight()
        }
        else
        {
            textureHeight
        }
    }
}
