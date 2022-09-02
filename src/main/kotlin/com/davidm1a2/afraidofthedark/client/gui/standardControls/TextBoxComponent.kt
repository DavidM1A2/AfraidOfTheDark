package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.fontLibrary.TrueTypeFont
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.TextAlignment
import com.mojang.blaze3d.matrix.MatrixStack
import java.awt.Color

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

        if (width <= 0 || height <= 0) {
            overflowText = text
            return
        }

        if (text.isEmpty()) {
            overflowText = ""
            return
        }

        var currentWord = ""
        var currentLineText = ""
        val lineEndings = mutableListOf<Int>()
        text.forEachIndexed { index, char ->
            when (char) {
                '\t' -> throw IllegalStateException("Tabs are not supported for now by text boxes. Use 3 spaces instead")
                // Done with the current word AND line
                '\n' -> {
                    this.textLines.add("$currentLineText$currentWord")
                    lineEndings.add(index + 1)
                    currentLineText = ""
                    currentWord = ""
                }
                // Done with the current word
                ' ' -> {
                    if (this.font.getWidth("$currentLineText$currentWord ") > width) {
                        this.textLines.add(currentLineText)
                        lineEndings.add(index - currentWord.length)
                        currentLineText = "$currentWord "
                        currentWord = ""
                    } else {
                        currentLineText = "$currentLineText$currentWord "
                        currentWord = ""
                    }
                }
                // Add to the current word
                else -> {
                    currentWord = "$currentWord$char"
                    if (this.font.getWidth("$currentLineText$currentWord") > width) {
                        if (currentLineText.isEmpty()) {
                            throw IllegalStateException("Could not lay out text '$currentWord' in text box because the word was too long and couldn't be split")
                        }
                        this.textLines.add(currentLineText)
                        lineEndings.add(index - currentWord.length + 1)
                        currentLineText = ""
                    }
                }
            }
        }
        val remainder = "$currentLineText$currentWord"
        if (remainder.isNotEmpty()) {
            this.textLines.add(remainder)
            lineEndings.add(text.length)
        }

        // Check for overflow. Start by removing lines that don't fit
        while (this.font.getHeight(this.textLines.joinToString("\n")) > this.height) {
            textLines.removeLast()
        }

        this.overflowText = text.substring(lineEndings[textLines.size - 1])
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
