package com.davidm1a2.afraidofthedark.client.gui.base

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiComponent.Companion.entityPlayer
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiComponent.Companion.fontRenderer
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.Gui
import org.lwjgl.util.Color
import org.lwjgl.util.Point
import org.lwjgl.util.Rectangle
import kotlin.math.roundToInt

/**
 * Base class for all GUI components like labels, buttons, etc
 *
 * @constructor initializes the bounding box *
 * @param x      The X location of the top left corner
 * @param y      The Y location of the top left corner
 * @param width  The width of the component
 * @param height The height of the component
 * @property entityPlayer A reference to the EntityPlayer object that opened the GUI
 * @property fontRenderer A reference to the font renderer that is used to draw fonts
 * @property scaleX The x scale
 * @property scaleY The y scale
 * @property isHovered True if the element is hovered by the mouse, false otherwise
 * @property isVisible True if the element is visible, false otherwise
 * @property boundingBox The raw bounding box of the gui component, this is never changed
 * @property scaledBoundingBox Scaled bounding box of the gui component, this is changed if the scale changes
 * @property color The color that this component should be
 * @property hoverTexts A list of strings to draw when the component is hovered
 */
abstract class AOTDGuiComponent(x: Int, y: Int, width: Int, height: Int)
{
    private val boundingBox: Rectangle = Rectangle(x, y, width, height)
    private val scaledBoundingBox = Rectangle(0, 0, 0, 0)

    var scaleX: Double = 1.0
        private set
    var scaleY: Double = 1.0
        private set
    open var isHovered: Boolean = false
    open var isVisible: Boolean = true
    open var color = Color(255, 255, 255, 255)
    var hoverTexts = emptyArray<String>()

    open var x: Int
        get() = this.boundingBox.x
        set(x)
        {
            this.boundingBox.x = x
            this.updateScaledBounds()
        }
    val xScaled: Int
        get() = this.scaledBoundingBox.x

    open var y: Int
        get() = this.boundingBox.y
        set(y)
        {
            this.boundingBox.y = y
            this.updateScaledBounds()
        }
    val yScaled: Int
        get() = this.scaledBoundingBox.y

    open var width: Int
        get() = this.boundingBox.width
        set(width)
        {
            this.boundingBox.width = width
            this.updateScaledBounds()
        }
    val widthScaled: Int
        get() = this.scaledBoundingBox.width

    open var height: Int
        get() = this.boundingBox.height
        set(height)
        {
            this.boundingBox.height = height
            this.updateScaledBounds()
        }
    val heightScaled: Int
        get() = this.scaledBoundingBox.height

    var hoverText: String
        get() = this.hoverTexts.joinToString("\n")
        set(hoverText)
        {
            this.hoverTexts = hoverText.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        }

    /**
     * Draw function that gets called every frame. This needs to be overridden to draw custom controls
     */
    open fun draw()
    {
        // Draw the bounding box for debug purposes
        // this.drawBoundingBox();
    }

    /**
     * Draws the hover text that appears when we mouse over the control
     */
    open fun drawOverlay()
    {
        // Make sure the control is visible and hovered
        if (this.isVisible && this.isHovered)
        {
            // Find the longest string in the hover texts array
            val maxHoverTextLength = this.hoverTexts.map { fontRenderer.getStringWidth(it) }.max()
            // If it exists, draw the text
            if (maxHoverTextLength != null)
            {
                // Grab the mouse X and Y coordinates to draw at
                val mouseX = AOTDGuiUtility.mouseXInMCCoord
                val mouseY = AOTDGuiUtility.mouseYInMCCoord
                // Draw a background rectangle
                Gui.drawRect(
                    mouseX + 2,
                    mouseY - 2,
                    mouseX + maxHoverTextLength + 7,
                    mouseY + fontRenderer.FONT_HEIGHT * this.hoverTexts.size,
                    Color(140, 0, 0, 0).hashCode()
                )
                // For each hover text in the array draw one line at a time
                for (i in hoverTexts.indices)
                {
                    // Grab the hover text to draw
                    val hoverText = hoverTexts[i]
                    // Draw the string
                    fontRenderer.drawStringWithShadow(
                        hoverText,
                        (mouseX + 5).toFloat(),
                        (mouseY + i * fontRenderer.FONT_HEIGHT).toFloat(),
                        Color(255, 255, 255).hashCode()
                    )
                }
            }
        }
    }

    /**
     * Utility function to draw the bounding box of the GUI component, useful for debug only
     */
    fun drawBoundingBox()
    {
        val whiteColor = Color(255, 255, 255, 255).hashCode()
        Gui.drawRect(this.xScaled, this.yScaled, this.xScaled + this.widthScaled, this.yScaled + 1, whiteColor)
        Gui.drawRect(this.xScaled, this.yScaled, this.xScaled + 1, this.yScaled + this.heightScaled, whiteColor)
        Gui.drawRect(this.xScaled + this.widthScaled - 1, this.yScaled, this.xScaled + this.widthScaled, this.yScaled + this.heightScaled, whiteColor)
        Gui.drawRect(this.xScaled, this.yScaled + this.heightScaled - 1, this.xScaled + this.widthScaled, this.yScaled + this.heightScaled, whiteColor)
    }

    /**
     * Returns true if the current component intersects the other component, or false if not
     *
     * @param other The other gui component to test intersection of
     * @return True if the components intersect, false otherwise
     */
    fun intersects(other: AOTDGuiComponent): Boolean
    {
        return this.intersects(other.scaledBoundingBox)
    }

    /**
     * Returns true if the current component intersects the point, or false if not
     *
     * @param point The point to test intersection with
     * @return True if the point intersects the rectangle, false otherwise
     */
    open fun intersects(point: Point): Boolean
    {
        return this.scaledBoundingBox.contains(point)
    }

    /**
     * Returns true if the current component intersects the rectangle, or false if not
     *
     * @param rectangle The point to test intersection with
     * @return True if the point intersects the rectangle, false otherwise
     */
    open fun intersects(rectangle: Rectangle): Boolean
    {
        return this.scaledBoundingBox.intersects(rectangle)
    }

    /**
     * Setter for X and Y scale, also updates the scaled bounding box
     *
     * @param scale The new X and Y scale
     */
    open fun setScaleXAndY(scale: Double)
    {
        this.scaleX = scale
        this.scaleY = scale
        this.updateScaledBounds()
    }

    /**
     * Setter for X scale, also updates the scaled bounding box
     *
     * @param scaleX The new X scale to use
     */
    open fun setScaleX(scaleX: Double)
    {
        this.scaleX = scaleX
        this.updateScaledBounds()
    }

    /**
     * Setter for Y scale, also updates the scaled bounding box
     *
     * @param scaleY The new Y scale to use
     */
    open fun setScaleY(scaleY: Double)
    {
        this.scaleY = scaleY
        this.updateScaledBounds()
    }

    /**
     * Updates the scaled bounding box from the current X and Y scale
     */
    open fun updateScaledBounds()
    {
        // Compute new X, Y, Width, and Height by scaling the original bounding box and saving it into the new bounding box
        val xNew = (this.scaleX * this.boundingBox.x).roundToInt()
        val yNew = (this.scaleY * this.boundingBox.y).roundToInt()
        val widthNew = (this.scaleX * this.boundingBox.width).roundToInt()
        val heightNew = (this.scaleY * this.boundingBox.height).roundToInt()
        this.scaledBoundingBox.setBounds(xNew, yNew, widthNew, heightNew)
    }

    companion object
    {
        val entityPlayer: EntityPlayerSP = Minecraft.getMinecraft().player
        val fontRenderer: FontRenderer = Minecraft.getMinecraft().fontRenderer
    }
}
