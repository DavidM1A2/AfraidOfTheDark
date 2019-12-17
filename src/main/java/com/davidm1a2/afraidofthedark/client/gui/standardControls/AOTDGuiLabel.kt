package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiContainer
import com.davidm1a2.afraidofthedark.client.gui.base.TextAlignment
import com.davidm1a2.afraidofthedark.client.gui.fontLibrary.TrueTypeFont
import com.davidm1a2.afraidofthedark.common.constants.Constants
import org.apache.commons.lang3.StringUtils
import org.lwjgl.util.Color

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
class AOTDGuiLabel(x: Int, y: Int, width: Int, height: Int, val font: TrueTypeFont) : AOTDGuiContainer(x, y, width, height)
{
    var text = StringUtils.EMPTY
        set(text)
        {
            field = text
            this.needsTextUpdate = true
        }
    private var fitText = StringUtils.EMPTY
    private var needsTextUpdate = true
    var textColor = Color(255, 255, 255, 255)
    var textAlignment = TextAlignment.ALIGN_LEFT
    var shortenTextToFit = true

    /**
     * Draw function that gets called every frame. Draw the text
     */
    override fun draw()
    {
        // If we need a text to size update do that
        if (this.needsTextUpdate)
        {
            this.computeTextForSize()
            this.needsTextUpdate = false
        }

        // If the label is visible, draw it
        if (this.isVisible)
        {
            // Compute the x and y positions of the text
            val xCoord =
                this.getXScaled() + when
                {
                    this.textAlignment === TextAlignment.ALIGN_LEFT -> 0f
                    this.textAlignment === TextAlignment.ALIGN_CENTER -> this.getWidthScaled() / 2f
                    else -> this.getWidthScaled().toFloat()
                }
            var yCoord = this.getYScaled().toFloat()

            // Center align text on the y-axis
            val spaceLeft = (this.getHeight() - this.font.height * Constants.TEXT_SCALE_FACTOR).toDouble()
            if (spaceLeft > 0)
            {
                yCoord = yCoord + (spaceLeft / 2).toFloat()
            }

            // Draw the string at (x, y) with the correct color and scale
            this.font.drawString(
                xCoord,
                yCoord,
                this.fitText,
                this.scaleX.toFloat() * Constants.TEXT_SCALE_FACTOR,
                this.scaleY.toFloat() * Constants.TEXT_SCALE_FACTOR,
                textAlignment,
                this.textColor
            )
            super.draw()
        }
    }

    /**
     * Updates the text to draw based on the width and height of the label. If the text is too much then cut it off
     */
    private fun computeTextForSize()
    {
        if (this.shortenTextToFit)
        {
            // Test if the text will fit into our label based on height
            val height = (this.font.height.toDouble() * Constants.TEXT_SCALE_FACTOR.toDouble() * this.scaleY).toFloat()
            if (height > this.getHeightScaled())
            {
                this.fitText = StringUtils.EMPTY
            }
            // If the height is OK shorten the text until it fits into the label
            else
            {
                this.fitText = this.text
                // Grab the current width of the text
                var width = (this.font.getWidth(this.fitText).toDouble() * Constants.TEXT_SCALE_FACTOR.toDouble() * this.scaleX).toFloat()
                // If it's too big remove one character at a time until it isn't
                while (width > this.getWidthScaled() && this.fitText.isNotEmpty())
                {
                    this.fitText = this.fitText.substring(0, this.fitText.length - 2)
                    // Grab the current width of the text
                    width = (this.font.getWidth(this.fitText).toDouble() * Constants.TEXT_SCALE_FACTOR.toDouble() * this.scaleX).toFloat()
                }
            }
        }
        else
        {
            this.fitText = this.text
        }
    }

    /**
     * Sets the width of label, also update the text
     *
     * @param width The new label width
     */
    override fun setWidth(width: Int)
    {
        super.setWidth(width)
        this.needsTextUpdate = true
    }
}
