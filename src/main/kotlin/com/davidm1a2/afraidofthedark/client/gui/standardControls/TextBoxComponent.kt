package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.TextAlignment
import com.davidm1a2.afraidofthedark.client.gui.fontLibrary.TrueTypeFont
import java.awt.Color
import java.util.*

/**
 * A text box control that will have multiple lines of text like a label
 */
class TextBoxComponent(prefSize: Dimensions = Dimensions(1.0, 1.0), private val font: TrueTypeFont, val textAlignment: TextAlignment = TextAlignment.ALIGN_LEFT) :
    AOTDGuiComponentWithEvents(prefSize = prefSize) {
    private var textLines = mutableListOf<String>()
    private var text = ""
    var textColor = Color(255, 255, 255, 255)
    var overflowText = ""
        private set

    /**
     * Draw the text given the width and height as bounds
     */
    override fun draw() {
        if (this.isVisible && this.inBounds) {
            var yCoord = y.toFloat()

            for (line in this.textLines) {
                val lineWidth = this.font.getWidth(line)
                val lineHeight = this.font.getHeight(line)

                val xCoord = x + when (this.textAlignment) {
                        TextAlignment.ALIGN_RIGHT -> lineWidth.toFloat()
                        TextAlignment.ALIGN_CENTER -> lineWidth.toFloat() / 2f
                        else -> 0f
                    }

                this.font.drawString(
                    xCoord,
                    yCoord,
                    line,
                    textAlignment,
                    this.textColor
                )

                yCoord += lineHeight
            }
        }
    }

    /**
     * Sets the text inside the text box
     *
     * @param text The text to use
     */
    fun setText(text: String) {
        this.text = text
        this.textLines.clear()
        val words = StringTokenizer(text, " ")
        var currentLineText = ""

        // Read words into lines that fit inside the text box
        while (words.hasMoreTokens()) {
            var word = words.nextToken()
            // Replace tab characters with spaces since tab characters are buggy to render
            word = word.replace("\t", "   ")
            if (this.font.getWidth("$currentLineText $word") > width) {
                this.textLines.add(currentLineText)
                currentLineText = word
            } else {
                if (currentLineText.isNotEmpty()) currentLineText += " "
                currentLineText += word
            }
        }
        this.textLines.add(currentLineText)

        // Check for overflow
        this.overflowText = ""
        var textHeight = this.font.getHeight(this.textLines.joinToString("\n"))
        while (textHeight > this.height && textLines.isNotEmpty()) {
            overflowText += this.textLines.removeLast() + "\n"
            textHeight = this.font.getHeight(this.textLines.joinToString("\n"))
        }
    }

    override fun invalidate() {
        super.invalidate()
        this.setText(text)
    }

    /**
     * @return Returns the text found inside this text box
     */
    fun getText(): String {
        return text
    }
}
