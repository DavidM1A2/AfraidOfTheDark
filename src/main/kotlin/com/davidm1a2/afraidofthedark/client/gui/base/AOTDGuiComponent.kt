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
 */
abstract class AOTDGuiComponent(
        open var width: Int,
        open var height: Int,
        open var xOffset: Int = 0,
        open var yOffset: Int = 0,
        var margins: AOTDGuiSpacing = AOTDGuiSpacing(),
        var gravity: AOTDGuiGravity = AOTDGuiGravity.TOP_LEFT,
        var hoverTexts: Array<String> = emptyArray()) {

    open var x = 0
    open var y = 0
    open var isHovered = false
    open var isVisible = true
    open var color = Color(255, 255, 255, 255)

    private val boundingBox: Rectangle
        get() = Rectangle(x, y, width, height)

    /**
     * Draw function that gets called every frame. This needs to be overridden to draw custom controls
     */
    open fun draw() {}

    /**
     * Draws the hover text that appears when we mouse over the control
     */
    open fun drawOverlay() {
        // Make sure the control is visible and hovered
        if (this.isVisible && this.isHovered) {
            // Grab the mouse X and Y coordinates to draw at
            val mouseX = AOTDGuiUtility.getMouseXInMCCoord()
            val mouseY = AOTDGuiUtility.getMouseYInMCCoord()
            // Get the window width and calculate the distance to the edge of the screen
            val windowWidth = AOTDGuiUtility.getWindowWidthInMCCoords()
            val distToEdge = windowWidth - (mouseX+5)
            // Apply text wrapping to the hover text
            val fittedHoverTexts = fitText(distToEdge, hoverTexts)
            // Find the longest string in the hover texts array
            val maxHoverTextLength = fittedHoverTexts.map { fontRenderer.getStringWidth(it) }.maxOrNull()
            // If it exists, draw the text
            if (maxHoverTextLength != null) {

                // Draw a background rectangle
                AbstractGui.fill(
                    mouseX + 2,
                    mouseY - 2,
                    mouseX + maxHoverTextLength + 7,
                    mouseY + fontRenderer.FONT_HEIGHT * fittedHoverTexts.size,
                    Color(140, 0, 0, 0).hashCode()
                )

                // For each hover text in the array draw one line at a time
                for (i in fittedHoverTexts.indices) {
                    // Grab the hover text to draw
                    val hoverText = fittedHoverTexts[i]
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
     * Fits an array of strings within a given width using text wrapping
     */
    private fun fitText(width: Int, text: Array<String>): Array<String> {
        var ret = emptyArray<String>()
        for (l in text) {
            var line = l
            var breakIndex = -1
            var lastBreak: Int
            var w: Int
            while (line.isNotEmpty()) {
                lastBreak = breakIndex
                // Find the next break
                breakIndex = line.indexOf(' ', lastBreak+1)
                // Determine width of the string up to the break
                // If there's no break, we use the full width of the string
                w = fontRenderer.getStringWidth(line.substring(0, if (breakIndex == -1) line.length else breakIndex))

                if (breakIndex == -1 || w > width) { // No breaks left OR break is after the max width
                    when {
                        w < width -> {  // Line does not need to be wrapped
                            ret += line
                            line = ""
                        }
                        lastBreak != -1 -> {  // If the previous line break exists (last break before max width)
                            ret += line.substring(0, lastBreak)
                            line = line.substring(lastBreak+1)
                        }
                        breakIndex != -1 -> { // If a break exists, but after the max width
                            ret += line.substring(0, breakIndex)
                            line = line.substring(breakIndex+1)
                        }
                        else -> {    // Line is too long, but no break exists
                            ret += line
                            line = ""
                        }
                    }
                    breakIndex = -1 // Reset the index
                }
            }
        }
        return ret
    }

    /**
     * Returns true if the current component intersects the other component, or false if not
     *
     * @param other The other gui component to test intersection of
     * @return True if the components intersect, false otherwise
     */
    fun intersects(other: AOTDGuiComponent): Boolean {
        return this.intersects(other.boundingBox)
    }

    /**
     * Adjust this component to fit into the given space
     * Default behavior takes up maximum allowed space
     */
    open fun negotiateDimensions(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    /**
     * Returns true if the current component intersects the point, or false if not
     *
     * @param point The point to test intersection with
     * @return True if the point intersects the rectangle, false otherwise
     */
    open fun intersects(point: Point): Boolean {
        return this.boundingBox.contains(point)
    }

    /**
     * Returns true if the current component intersects the rectangle, or false if not
     *
     * @param rectangle The point to test intersection with
     * @return True if the point intersects the rectangle, false otherwise
     */
    open fun intersects(rectangle: Rectangle): Boolean {
        return this.boundingBox.intersects(rectangle)
    }

    /**
     * Updates the bounding box
     */
    open fun updateBounds() {
        this.boundingBox.setBounds(this.x, this.y, this.width, this.height)
    }

    /**
     * Second setter for hover texts, this one assumes the parameter is a '\n' delimited string to be converted into an array
     *
     * @param hoverText The new text to be the hover text of this component
     */
    open fun setHoverText(hoverText: String) {
        hoverTexts = hoverText.split("\n").toTypedArray()
    }

    fun getXWithOffset() = x + xOffset

    fun getYWithOffset() = y + yOffset

    companion object {
        val fontRenderer: FontRenderer = Minecraft.getInstance().fontRenderer
        val entityPlayer: ClientPlayerEntity
            get() = Minecraft.getInstance().player
    }
}
