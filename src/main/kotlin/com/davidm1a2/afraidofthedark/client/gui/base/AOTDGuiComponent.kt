package com.davidm1a2.afraidofthedark.client.gui.base

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiComponent.Companion.entityPlayer
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiComponent.Companion.fontRenderer
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.player.ClientPlayerEntity
import net.minecraft.client.gui.AbstractGui
import net.minecraft.client.gui.FontRenderer
import java.awt.Color
import java.awt.Point
import java.awt.Rectangle
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
abstract class AOTDGuiComponent(x: Int, y: Int, width: Int, height: Int) {
    private val boundingBox = Rectangle(x, y, width, height)
    private val scaledBoundingBox = Rectangle(0, 0, 0, 0)

    var scaleX: Double = 1.0
        private set
    var scaleY: Double = 1.0
        private set
    open var isHovered = false
    open var isVisible = true
    open var color = Color(255, 255, 255, 255)
    var hoverTexts = emptyArray<String>()

    /**
     * Draw function that gets called every frame. This needs to be overridden to draw custom controls
     */
    open fun draw() {
        // Draw the bounding box for debug purposes
        // drawBoundingBox()
    }

    /**
     * Draws the hover text that appears when we mouse over the control
     */
    open fun drawOverlay() {
        // Make sure the control is visible and hovered
        if (this.isVisible && this.isHovered) {
            // Find the longest string in the hover texts array
            val maxHoverTextLength = this.hoverTexts.map { fontRenderer.getStringWidth(it) }.maxOrNull()
            // If it exists, draw the text
            if (maxHoverTextLength != null) {
                // Grab the mouse X and Y coordinates to draw at
                val mouseX = AOTDGuiUtility.getMouseXInMCCoord()
                val mouseY = AOTDGuiUtility.getMouseYInMCCoord()

                // Draw a background rectangle
                AbstractGui.fill(
                    mouseX + 2,
                    mouseY - 2,
                    mouseX + maxHoverTextLength + 7,
                    mouseY + fontRenderer.FONT_HEIGHT * this.hoverTexts.size,
                    Color(140, 0, 0, 0).hashCode()
                )

                // For each hover text in the array draw one line at a time
                for (i in hoverTexts.indices) {
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
    fun drawBoundingBox() {
        val whiteColor = Color(255, 255, 255, 255).hashCode()
        AbstractGui.fill(
            this.getXScaled(),
            this.getYScaled(),
            this.getXScaled() + this.getWidthScaled(),
            this.getYScaled() + 1,
            whiteColor
        )
        AbstractGui.fill(
            this.getXScaled(),
            this.getYScaled(),
            this.getXScaled() + 1,
            this.getYScaled() + this.getHeightScaled(),
            whiteColor
        )
        AbstractGui.fill(
            this.getXScaled() + this.getWidthScaled() - 1,
            this.getYScaled(),
            this.getXScaled() + this.getWidthScaled(),
            this.getYScaled() + this.getHeightScaled(),
            whiteColor
        )
        AbstractGui.fill(
            this.getXScaled(),
            this.getYScaled() + this.getHeightScaled() - 1,
            this.getXScaled() + this.getWidthScaled(),
            this.getYScaled() + this.getHeightScaled(),
            whiteColor
        )
    }

    /**
     * Returns true if the current component intersects the other component, or false if not
     *
     * @param other The other gui component to test intersection of
     * @return True if the components intersect, false otherwise
     */
    fun intersects(other: AOTDGuiComponent): Boolean {
        return this.intersects(other.scaledBoundingBox)
    }

    /**
     * Returns true if the current component intersects the point, or false if not
     *
     * @param point The point to test intersection with
     * @return True if the point intersects the rectangle, false otherwise
     */
    open fun intersects(point: Point): Boolean {
        return this.scaledBoundingBox.contains(point)
    }

    /**
     * Returns true if the current component intersects the rectangle, or false if not
     *
     * @param rectangle The point to test intersection with
     * @return True if the point intersects the rectangle, false otherwise
     */
    open fun intersects(rectangle: Rectangle): Boolean {
        return this.scaledBoundingBox.intersects(rectangle)
    }

    /**
     * Setter for X and Y scale, also updates the scaled bounding box
     *
     * @param scale The new X and Y scale
     */
    open fun setScaleXAndY(scale: Double) {
        this.scaleX = scale
        this.scaleY = scale
        this.updateScaledBounds()
    }

    /**
     * Setter for X scale, also updates the scaled bounding box
     *
     * @param scaleX The new X scale to use
     */
    open fun setScaleX(scaleX: Double) {
        this.scaleX = scaleX
        this.updateScaledBounds()
    }

    /**
     * Setter for Y scale, also updates the scaled bounding box
     *
     * @param scaleY The new Y scale to use
     */
    open fun setScaleY(scaleY: Double) {
        this.scaleY = scaleY
        this.updateScaledBounds()
    }

    /**
     * Setter for bounding box X
     *
     * @param x The new x position of the component
     */
    open fun setX(x: Int) {
        boundingBox.x = x
        updateScaledBounds()
    }

    /**
     * @return Getter for the top left corner's X value
     */
    open fun getX(): Int {
        return boundingBox.x
    }

    /**
     * @return Getter for the scaled bounding box's top corner's X value
     */
    open fun getXScaled(): Int {
        return scaledBoundingBox.x
    }

    /**
     * Setter for bounding box Y
     *
     * @param y The new y position of the component
     */
    open fun setY(y: Int) {
        boundingBox.y = y
        updateScaledBounds()
    }

    /**
     * @return Getter for the top left corner's Y value
     */
    open fun getY(): Int {
        return boundingBox.y
    }

    /**
     * @return Getter for the scaled bounding box's top corner's Y value
     */
    open fun getYScaled(): Int {
        return scaledBoundingBox.y
    }

    /**
     * Setter for the width of the component
     *
     * @param width The new component's width
     */
    open fun setWidth(width: Int) {
        boundingBox.width = width
        updateScaledBounds()
    }

    /**
     * @return Getter for the component's width
     */
    open fun getWidth(): Int {
        return boundingBox.width
    }

    /**
     * @return Getter for the component's scaled width
     */
    open fun getWidthScaled(): Int {
        return scaledBoundingBox.width
    }

    /**
     * Setter for the component's height
     *
     * @param height The new height of the component
     */
    open fun setHeight(height: Int) {
        boundingBox.height = height
        updateScaledBounds()
    }

    /**
     * @return Getter for the height of the component
     */
    open fun getHeight(): Int {
        return boundingBox.height
    }

    /**
     * @return Getter for the scaled height of the component
     */
    open fun getHeightScaled(): Int {
        return scaledBoundingBox.height
    }

    /**
     * Updates the scaled bounding box from the current X and Y scale
     */
    open fun updateScaledBounds() {
        // Compute new X, Y, Width, and Height by scaling the original bounding box and saving it into the new bounding box
        val xNew = (this.scaleX * this.boundingBox.x).roundToInt()
        val yNew = (this.scaleY * this.boundingBox.y).roundToInt()
        val widthNew = (this.scaleX * this.boundingBox.width).roundToInt()
        val heightNew = (this.scaleY * this.boundingBox.height).roundToInt()
        this.scaledBoundingBox.setBounds(xNew, yNew, widthNew, heightNew)
    }

    /**
     * Second getter for hover text, this one concatenates the hover texts back together with '\n' characters
     *
     * @return The hover text concatenated together
     */
    open fun getHoverText(): String {
        return hoverTexts.joinToString(separator = "\n")
    }

    /**
     * Second setter for hover texts, this one assumes the parameter is a '\n' delimited string to be converted into an array
     *
     * @param hoverText The new text to be the hover text of this component
     */
    open fun setHoverText(hoverText: String) {
        hoverTexts = hoverText.split("\n").toTypedArray()
    }

    companion object {
        val fontRenderer: FontRenderer = Minecraft.getInstance().fontRenderer
        val entityPlayer: ClientPlayerEntity
            get() = Minecraft.getInstance().player
    }
}
