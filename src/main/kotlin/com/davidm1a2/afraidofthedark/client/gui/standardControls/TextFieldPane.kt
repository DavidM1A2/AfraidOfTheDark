package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.events.ITextChangeListener
import com.davidm1a2.afraidofthedark.client.gui.events.KeyEvent
import com.davidm1a2.afraidofthedark.client.gui.events.MouseEvent
import com.davidm1a2.afraidofthedark.client.gui.fontLibrary.TrueTypeFont
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
import com.davidm1a2.afraidofthedark.client.gui.layout.TextAlignment
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SharedConstants
import org.lwjgl.glfw.GLFW
import java.awt.Color

/**
 * The GUI text field control to let users enter text
 */
class TextFieldPane(offset: Position = Position(0.0, 0.0), prefSize: Dimensions = Dimensions(Double.MAX_VALUE, Double.MAX_VALUE), font: TrueTypeFont) :
    AOTDPane(offset, prefSize) {
    private val textChangeListeners = mutableListOf<ITextChangeListener>()
    private val background: ImagePane
    private val textContainer: StackPane
    private val textLabel: LabelComponent
    var isFocused = false
        private set
    var ghostText = ""
        private set
    var textColor = Color(255, 255, 255)
        private set

    init {
        // Create our background image
        this.background = ImagePane(ResourceLocation("afraidofthedark:textures/gui/text_field_background.png"), ImagePane.DispMode.STRETCH)

        // Set the text container bounding box to have scissor enabled and contain the label
        this.textContainer = StackPane(Dimensions(1.0, 1.0), margins = Spacing(0.0, 0.0, 0.1, 0.05), scissorEnabled = true)
        // The text label contains the text field's text
        this.textLabel = LabelComponent(font, Dimensions(1.0, 1.0))
        // Make sure the label doesn't shorten the text inside to fit
        this.textLabel.shortenTextToFit = false

        // Add the text label to the container
        this.textContainer.add(this.textLabel)
        // Add the text container to the background
        this.background.add(this.textContainer)
        // Add the background to this control
        this.add(this.background)

        // Set the control to focused once it's selected
        this.addMouseListener {
            if (it.eventType == MouseEvent.EventType.Click) {
                // Make sure it was a left click
                if (it.clickedButton == MouseEvent.LEFT_MOUSE_BUTTON) {
                    // Test if we're hovered or not, if so set it to focused, otherwise set it to not focused
                    setFocused(isHovered)
                }
            }
        }
        // When a key is typed save that information
        this.addKeyListener {
            if (it.eventType == KeyEvent.KeyEventType.Type) {
                keyTyped(it)
            } else if (it.eventType == KeyEvent.KeyEventType.Press) {
                keyPressed(it)
            }
        }
    }

    /**
     * Only draw the text field if visible
     */
    override fun draw(matrixStack: MatrixStack) {
        if (this.isVisible) {
            super.draw(matrixStack)
        }
    }

    private fun keyPressed(event: KeyEvent) {
        // Ensure the text field is focused
        if (this.isFocused) {
            // CTRL + A
            if (event.hasModifier(KeyEvent.Modifier.CONTROL) && event.key == GLFW.GLFW_KEY_A) {
                // Select all text, not yet implemented
            }
            // CTRL + C
            else if (event.hasModifier(KeyEvent.Modifier.CONTROL) && event.key == GLFW.GLFW_KEY_C) {
                // Update the clipboard string
                Minecraft.getInstance().keyboardHandler.clipboard = this.getText()
            }
            // Ctrl + V
            else if (event.hasModifier(KeyEvent.Modifier.CONTROL) && event.key == GLFW.GLFW_KEY_V) {
                this.setText("")
                this.addText(SharedConstants.filterText(Minecraft.getInstance().keyboardHandler.clipboard))
            }
            // CTRL + X
            else if (event.hasModifier(KeyEvent.Modifier.CONTROL) && event.key == GLFW.GLFW_KEY_X) {
                Minecraft.getInstance().keyboardHandler.clipboard = this.getText()
                this.setText("")
            }
            // Regular key typed
            else {
                when (event.key) {
                    GLFW.GLFW_KEY_BACKSPACE -> this.removeChars(1)
                    GLFW.GLFW_KEY_LEFT -> {
                        // Not yet implemented
                    }
                    GLFW.GLFW_KEY_RIGHT -> {
                        // Not yet implemented
                    }
                }
            }
        }
    }

    private fun keyTyped(event: KeyEvent) {
        // Ensure the text field is focused
        if (this.isFocused) {
            val char = SharedConstants.filterText(event.char.toString())
            this.addText(char)
        }
    }

    /**
     * @return The current text in the text field or empty string if it is ghost text
     */
    fun getText(): String {
        // Return either "" if the text is ghost text or the text otherwise
        return if (isShowingGhostText()) {
            ""
        } else {
            // If we're focused remove the _, otherwise just return the text
            if (isFocused) {
                textLabel.text.substring(0, textLabel.text.length - 1)
            } else {
                textLabel.text
            }
        }
    }

    /**
     * Sets the text of the text field. If the text field is focused an _ is added. If it is not focused either ghost text is shown
     * or the text is added without the _ depending on if text is empty or not
     *
     * @param text The new text to display, or empty string if ghost text should be down
     */
    fun setText(text: String) {
        setTextInternal(text)
    }

    /**
     * Special version of setText which takes previous text as input. The second parameter is used by
     * focus handling code to ensure oldText is correctly passed
     */
    private fun setTextInternal(rawText: String, oldText: String = getText()) {
        // Make sure that the text contains valid characters
        val newText = SharedConstants.filterText(rawText)
        // Now we test if we should show ghost text or not
        // If the control is focused then we don't show ghost text
        if (isFocused) {
            textLabel.text = newText + "_"
            textLabel.textColor = textColor
        } else {
            // If the string is empty we show ghost text
            if (newText.isEmpty()) {
                textLabel.text = ghostText
                textLabel.textColor = GHOST_TEXT_COLOR
            } else {
                textLabel.text = newText
                textLabel.textColor = textColor
            }
        }

        if (newText != oldText) {
            textChangeListeners.forEach { it.apply(oldText, newText) }
        }

        invalidate()
    }

    /**
     * Adds text to the current text in the text field
     *
     * @param text The new text to add
     */
    private fun addText(text: String) {
        // Make sure the text is non-empty
        if (text != "") {
            this.setText(this.getText() + text)
        }
    }

    /**
     * Removes "number" amount of characters from the current string. Should be between 0 ... string.length()
     *
     * @param number The number of characters to remove
     */
    private fun removeChars(number: Int) {
        // Get the current text
        val currentText = this.getText()
        // Clamp the number to be between 0 and the text's length
        val numberClamped = number.coerceIn(0, currentText.length)
        // Set the new text
        this.setText(currentText.substring(0, currentText.length - numberClamped))
    }

    /**
     * Updates the amount the text label should be shifted by to allow for scrolling text
     */
    private fun updateScroll() {
        // Get the text's width and multiply by our scale factor used
        val textWidth = this.textLabel.font.getWidth(this.textLabel.text)
        // If the text width is bigger than the label's width we set the alignment to right so that you see the newest text not the oldest
        if (textWidth > this.textLabel.width) {
            this.textLabel.textAlignment = TextAlignment.ALIGN_RIGHT
        } else {
            this.textLabel.textAlignment = TextAlignment.ALIGN_LEFT
        }
    }

    override fun calcChildrenBounds() {
        super.calcChildrenBounds()
        updateScroll() // Update the alignment every UI update
    }

    /**
     * Sets the control to be focused or not
     *
     * @param isFocused If the control is focused or not
     */
    private fun setFocused(isFocused: Boolean) {
        // Was focused tells us if we were previously focused
        val wasFocused = this.isFocused
        // If we were not focused and now are focused update the text
        if (!wasFocused && isFocused) {
            // Enable repeat events so we can type multiple characters in the box by holding them
            Minecraft.getInstance().keyboardHandler.setSendRepeatsToGui(true)
            // Set the background to be tinted
            background.color = FOCUSED_COLOR_TINT
            // Get the current text
            val currentText = this.getText()
            // Update our focused variable
            this.isFocused = true
            // Call set text to refresh the text formatting
            this.setTextInternal(currentText, currentText)
        } else if (wasFocused && !isFocused) {
            // Disable repeat events
            Minecraft.getInstance().keyboardHandler.setSendRepeatsToGui(false)
            // Set the background to be untinted
            background.color = BASE_COLOR_TINT
            // Get the current text
            val currentText = this.getText()
            // Update our focused variable
            this.isFocused = false
            // Call set text to refresh the text formatting
            this.setTextInternal(currentText, currentText)
        }
    }

    /**
     * @return True if ghost text is showing, false otherwise
     */
    private fun isShowingGhostText(): Boolean {
        // Here we use a bit of a hack to detect if we're showing ghost text. We check the color of the text and the string contents. There's an
        // edge case where this fails if the color is gray and the string is exactly the ghost text
        return textLabel.textColor == GHOST_TEXT_COLOR && textLabel.text == ghostText
    }

    /**
     * Sets the ghost text to use
     *
     * @param ghostText The new ghost text
     */
    fun setGhostText(ghostText: String) {
        // Flag telling us if ghost text used to be loaded
        val ghostTextWasLoaded = this.isShowingGhostText()
        this.ghostText = ghostText

        // If the text was ghost text update the ghost text
        if (ghostTextWasLoaded) {
            textLabel.text = this.ghostText
        } else if (textLabel.text.isEmpty()) {
            textLabel.text = this.ghostText
            textLabel.textColor = GHOST_TEXT_COLOR
        }
    }

    /**
     * Sets the text color
     *
     * @param textColor The new text color
     */
    fun setTextColor(textColor: Color) {
        this.textColor = textColor
        // If we were not showing ghost text update the label's color
        if (!isShowingGhostText()) {
            textLabel.textColor = this.textColor
        }
    }

    fun addTextChangeListener(listener: ITextChangeListener) {
        textChangeListeners.add(listener)
    }

    companion object {
        // Ghost text color is gray
        private val GHOST_TEXT_COLOR = Color(128, 128, 128)

        // When focused tint gray
        private val FOCUSED_COLOR_TINT = Color(230, 230, 230)

        // When not focused we use a white tint which does nothing
        private val BASE_COLOR_TINT = Color(255, 255, 255)
    }
}
