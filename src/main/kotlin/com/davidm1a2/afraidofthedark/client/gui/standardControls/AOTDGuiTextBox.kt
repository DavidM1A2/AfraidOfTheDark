package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiContainer
import com.davidm1a2.afraidofthedark.client.gui.base.TextAlignment
import com.davidm1a2.afraidofthedark.client.gui.fontLibrary.TrueTypeFont
import com.davidm1a2.afraidofthedark.common.constants.Constants
import java.awt.Color
import java.util.*
import kotlin.math.floor

/**
 * A text box control that will have multiple lines of text like a label
 *
 * @param x      The X location of the top left corner
 * @param y      The Y location of the top left corner
 * @param width  The width of the component
 * @param height The height of the component
 * @param font   The font to draw text with
 * @property textLines A computed list of text lines
 * @property textColor The color to draw the text with
 * @property overflowText The overflow text that doesn't fit inside this text box
 */
class AOTDGuiTextBox(x: Int, y: Int, width: Int, height: Int, private val font: TrueTypeFont) :
    AOTDGuiContainer(x, y, width, height) {
    private var textLines = mutableListOf<String>()
    var textColor = Color(255, 255, 255, 255)
    var overflowText = ""
        private set

    /**
     * Draw the text given the width and height as bounds
     */
    override fun draw() {
        // Only draw the text if it's visible
        if (this.isVisible) {
            super.draw()
            // Draw each string in the text lines list one at at time
            for (i in this.textLines.indices)
                this.font.drawString(
                    this.getXScaled().toFloat(),
                    this.getYScaled().toFloat() + i * this.font.height * Constants.TEXT_SCALE_FACTOR * this.scaleY.toFloat(),
                    this.textLines[i],
                    this.scaleX.toFloat() * Constants.TEXT_SCALE_FACTOR,
                    this.scaleY.toFloat() * Constants.TEXT_SCALE_FACTOR,
                    TextAlignment.ALIGN_LEFT,
                    this.textColor
                )
        }
    }

    /**
     * Sets the text inside the text box
     *
     * @param text The text to use
     */
    fun setText(text: String) {
        // Clear the original text
        this.textLines.clear()
        // Split the text into words
        val words = StringTokenizer(text, " ")
        // The current line text
        var currentLineText = ""

        // Iterate over all words
        while (words.hasMoreTokens()) {
            // Grab the first word
            var word = words.nextToken()
            // Replace tab characters with spaces since tab characters are buggy to render
            word = word.replace("\t", "   ")
            currentLineText = when {
                // If the line is too long for the current text move to the next line
                this.font.getWidth("$currentLineText $word") * Constants.TEXT_SCALE_FACTOR > this.getWidth() -> {
                    // Store the current line and move on
                    this.textLines.add(currentLineText)
                    // Store the word as the beginning of the next line
                    word
                }
                // Else append to the current line
                currentLineText.isEmpty() -> word
                else -> "$currentLineText $word"
            }
        }
        this.textLines.add(currentLineText)

        // Compute the maximum number of lines that fit vertically inside the text box
        val maxLines = floor(this.getHeight() / (this.font.height * Constants.TEXT_SCALE_FACTOR)).toInt()
        // If the number of lines we have is less than or equal to the max we're OK
        if (textLines.size <= maxLines) {
            this.overflowText = ""
        }
        // If the number of lines is greater than the max then we partition the lines into actual text lines and overflow text
        else {
            val actualText = this.textLines.subList(0, maxLines)
            val spareText = this.textLines.subList(maxLines, this.textLines.size)
            this.textLines = actualText
            this.overflowText = spareText.joinToString(" ")
        }
    }

    /**
     * @return Returns the text found inside this text box
     */
    fun getText(): String {
        return textLines.joinToString(separator = "")
    }
}
