package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.SpecialTextCharacters
import com.davidm1a2.afraidofthedark.client.gui.fontLibrary.TrueTypeFont
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.TextAlignment
import com.mojang.blaze3d.matrix.MatrixStack
import java.awt.Color
import java.util.StringTokenizer

/**
 * A text box control that will have multiple lines of text like a label
 */
class TextBoxComponent(
    prefSize: Dimensions = Dimensions(1.0, 1.0),
    private val font: TrueTypeFont,
    val textAlignment: TextAlignment = TextAlignment.ALIGN_LEFT
) :
    AOTDGuiComponentWithEvents(prefSize = prefSize) {
    private var textLines = mutableListOf<String>()
    private var text = ""
    var textColor = Color(255, 255, 255, 255)
    var overflowText = ""
        private set

    /**
     * Draw the text given the width and height as bounds
     */
    override fun draw(matrixStack: MatrixStack) {
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

        val textBlocks = text.split(SpecialTextCharacters.NEW_LINE.toString())
        for (textBlock in textBlocks) {
            // Read words into lines that fit inside the text box
            val words = StringTokenizer(textBlock, " ")
            var currentLineText = ""
            while (words.hasMoreTokens()) {
                val word = words.nextToken()
                    .replace(SpecialTextCharacters.SPACE.toString(), " ")
                    .replace(SpecialTextCharacters.TAB.toString(), "   ")

                if (this.font.getWidth("$currentLineText $word") > width) {
                    this.textLines.add(currentLineText)
                    currentLineText = word
                } else {
                    if (currentLineText.isNotEmpty()) currentLineText += " "
                    currentLineText += word
                }
            }
            this.textLines.add(currentLineText)
        }

        // Check for overflow. Start by removing lines that don't fit
        var textHeight = this.font.getHeight(this.textLines.joinToString("\n"))
        while (textHeight > this.height && textLines.isNotEmpty()) {
            textLines.removeLast()
            textHeight = this.font.getHeight(this.textLines.joinToString("\n"))
        }
        // TODO: Clean up this implementation, it's really bad but works. Remove word by word from our overflow text
        this.overflowText = text
        for (textLine in textLines) {
            for (word in textLine.split(" ")) {
                this.overflowText = this.overflowText.substringAfter(word)
            }
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
