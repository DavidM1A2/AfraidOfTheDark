package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.base.*
import com.davidm1a2.afraidofthedark.client.gui.fontLibrary.TrueTypeFont
import com.davidm1a2.afraidofthedark.client.gui.layout.GuiGravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.TextAlignment
import com.davidm1a2.afraidofthedark.common.constants.Constants
import java.awt.Color

/**
 * Class representing a label to be drawn on the GUI
 *
 * @constructor Takes an x and y position as well as a font
 * @param x      The X coordinate of the label
 * @param y      The Y coordinate of the label
 * @param width  The width of the label
 * @param height The height of the label
 * @param font   The font to use to draw the label
 * @property text The raw text to draw
 * @property fitText The actual text to draw within the bounds of the label
 * @property needsTextUpdate Flag telling the label it needs to update the fit text
 * @property textColor The color to draw the text with
 * @property textAlignment Text alignment
 * @property shortenTextToFit True if we should ensure the text fits inside the label by shortening it, false otherwise
 */
class LabelComponent(val font: TrueTypeFont, prefSize: Dimensions, gravity: GuiGravity = GuiGravity.TOP_LEFT) : AOTDGuiComponentWithEvents(prefSize = prefSize, gravity = gravity) {

    var text = ""
        set(text) {
            field = text
            computeTextForSize()
        }
    private var fitText = ""
    private var needsTextUpdate = true
    var textColor = Color(255, 255, 255, 255)
    var textAlignment = TextAlignment.ALIGN_LEFT
    var shortenTextToFit = true

    /**
     * Draw function that gets called every frame. Draw the text
     */
    override fun draw() {
        // If the label is visible, draw it
        if (this.isVisible && this.inBounds) {
            // Compute the x and y positions of the text
            val xCoord =
                x + when (this.textAlignment) {
                    TextAlignment.ALIGN_RIGHT -> this.width.toFloat()
                    TextAlignment.ALIGN_CENTER -> this.width.toFloat() / 2f
                    else -> 0f
                }
            var yCoord = y.toFloat()

            // Center align text on the y-axis
            val spaceLeft = (this.height - this.font.getHeight(this.fitText)).toDouble()
            if (spaceLeft > 0) {
                yCoord += (spaceLeft / 2).toFloat()
            }

            // Draw the string at (x, y) with the correct color and scale
            this.font.drawString(
                xCoord,
                yCoord,
                this.fitText,
                Constants.TEXT_SCALE_FACTOR,
                Constants.TEXT_SCALE_FACTOR,
                textAlignment,
                this.textColor
            )
        }
    }

    override fun negotiateDimensions(width: Double, height: Double) {
        super.negotiateDimensions(width, height)
        computeTextForSize()
    }

    /**
     * Updates the text to draw based on the width and height of the label. If the text is too much then cut it off
     */
    private fun computeTextForSize() {
        if (this.shortenTextToFit) {
            // Test if the text will fit into our label based on height
            if (this.font.getHeight(this.text) > this.height) {
                this.fitText = ""
            }
            // If the height is OK shorten the text until it fits into the label
            else {
                this.fitText = this.text
                // Grab the current width of the text
                var width = this.font.getWidth(this.fitText)
                // If it's too big remove one character at a time until it isn't
                while (width > this.width && this.fitText.length >= 2) {
                    this.fitText = this.fitText.substring(0, this.fitText.length - 2)
                    // Grab the current width of the text
                    width = this.font.getWidth(this.fitText)
                }
            }
        } else {
            this.fitText = this.text
        }
    }
}
