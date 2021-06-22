package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.player.ClientPlayerEntity
import net.minecraft.client.gui.FontRenderer
import net.minecraftforge.fml.client.config.GuiUtils
import java.awt.Color
import java.awt.Point
import java.awt.Rectangle
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Base class for all GUI components like labels, buttons, etc
 */
abstract class AOTDGuiComponent(
    var offset: Position = Position(0.0, 0.0),
    var prefSize: Dimensions = Dimensions(Double.MAX_VALUE, Double.MAX_VALUE),
    var margins: Spacing = Spacing(),
    var gravity: Gravity = Gravity.TOP_LEFT,
    var hoverTexts: Array<String> = emptyArray(),
    var color: Color = Color(255, 255, 255, 255)) {

    open var width = 0
    open var height = 0
    open var x = 0
    open var y = 0
    open var isHovered = false
    open var isVisible = true
    open var inBounds = true

    val boundingBox: Rectangle
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
        if (this.isVisible && this.inBounds && this.isHovered) {
            // Grab the mouse X and Y coordinates to draw at
            val mouseX = AOTDGuiUtility.getMouseXInMCCoord()
            val mouseY = AOTDGuiUtility.getMouseYInMCCoord()
            // Get the window width and calculate the distance to the edge of the screen
            val windowWidth = AOTDGuiUtility.getWindowWidthInMCCoords()
            val windowHeight = AOTDGuiUtility.getWindowHeightInMCCoords()
            GuiUtils.drawHoveringText(hoverTexts.toList(), mouseX, mouseY, windowWidth, windowHeight, 200, fontRenderer)
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
    open fun negotiateDimensions(width: Double, height: Double) {
        this.width = if (prefSize.isRelative) (min(prefSize.width, 1.0) * width).roundToInt() else min(prefSize.width, width).roundToInt()
        this.height = if (prefSize.isRelative) (min(prefSize.height, 1.0) * height).roundToInt() else min(prefSize.height, height).roundToInt()
        this.inBounds = true    // Reset the inBounds tag and let ancestor nodes check it again
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
     * Second setter for hover texts, this one assumes the parameter is a '\n' delimited string to be converted into an array
     *
     * @param hoverText The new text to be the hover text of this component
     */
    open fun setHoverText(hoverText: String) {
        hoverTexts = hoverText.split("\n").toTypedArray()
    }

    open fun invalidate() {}

    companion object {
        val fontRenderer: FontRenderer = Minecraft.getInstance().fontRenderer
        val entityPlayer: ClientPlayerEntity
            get() = Minecraft.getInstance().player
    }
}
