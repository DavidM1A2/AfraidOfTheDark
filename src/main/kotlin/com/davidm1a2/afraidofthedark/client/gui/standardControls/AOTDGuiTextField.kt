package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiContainer
import com.davidm1a2.afraidofthedark.client.gui.base.TextAlignment
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDKeyEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.fontLibrary.TrueTypeFont
import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.util.SharedConstants
import org.lwjgl.glfw.GLFW
import java.awt.Color

/**
 * The GUI text field control to let users enter text
 *
 * @param x      The X location of the top left corner
 * @param y      The Y location of the top left corner
 * @param width  The width of the component
 * @param height The height of the component
 * @param font   The font to use for the text
 * @property background The background image of the text field
 * @property textContainer The text container panel
 * @property textLabel The label which shows the text
 * @property isFocused If the text field is focused or not
 * @property ghostText The text to show when no text is present
 * @property textColor If we were not showing ghost text update the label's color
 * @property text The current text in the text field or empty string if it is ghost text
 */
class AOTDGuiTextField(x: Int, y: Int, width: Int, height: Int, font: TrueTypeFont) :
    AOTDGuiContainer(x, y, width, height) {
    private val background: AOTDGuiImage
    private val textContainer: AOTDGuiPanel
    private val textLabel: AOTDGuiLabel
    var isFocused = false
        private set
    var ghostText = ""
        private set
    var textColor = Color(255, 255, 255)
        private set
    private var text: String = ""

    init {
        // Create our background image
        this.background = AOTDGuiImage(0, 0, width, height, "afraidofthedark:textures/gui/text_field_background.png")

        // Set the text container bounding box to have scissor enabled and contain the label
        this.textContainer = AOTDGuiPanel(5, 5, width - 10, height - 10, true)
        // The text label contains the text field's text
        this.textLabel = AOTDGuiLabel(5, 0, width - 15, height - 10, font)
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
            if (it.eventType == AOTDMouseEvent.EventType.Click) {
                // Make sure it was a left click
                if (it.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON) {
                    // Test if we're hovered or not, if so set it to focused, otherwise set it to not focused
                    setFocused(isHovered)
                }
            }
        }
        // When a key is typed save that information
        this.addKeyListener {
            if (it.eventType == AOTDKeyEvent.KeyEventType.Press) {
                keyTyped(it.getKeyName(), it.key)
            }
        }
    }

    /**
     * Only draw the text field if visible
     */
    override fun draw() {
        if (this.isVisible) {
            super.draw()
        }
    }

    /**
     * Processes a key typed event
     *
     * @param character The character typed
     * @param keyCode   The code of the character typed
     */
    private fun keyTyped(character: String?, keyCode: Int) {
        // Ensure the text field is focused
        if (this.isFocused) {
            // CTRL + A
            if (GuiScreen.isCtrlKeyDown() && keyCode == GLFW.GLFW_KEY_A) {
                // Select all text, not yet implemented
            }
            // CTRL + C
            else if (GuiScreen.isCtrlKeyDown() && keyCode == GLFW.GLFW_KEY_C) {
                // Update the clipboard string
                AOTDGuiUtility.setClipboardString(this.getText())
            }
            // Ctrl + V
            else if (GuiScreen.isCtrlKeyDown() && keyCode == GLFW.GLFW_KEY_V) {
                this.setText("")
                this.addText(SharedConstants.filterAllowedCharacters(AOTDGuiUtility.getClipboardString()))
            }
            // CTRL + X
            else if (GuiScreen.isCtrlKeyDown() && keyCode == GLFW.GLFW_KEY_X) {
                AOTDGuiUtility.setClipboardString(this.getText())
                this.setText("")
            }
            // Regular key typed
            else {
                when (keyCode) {
                    // Backspace removes 1 character
                    GLFW.GLFW_KEY_BACKSPACE -> this.removeChars(1)
                    // Left arrow
                    GLFW.GLFW_KEY_LEFT -> {
                        // Not yet implemented
                    }
                    // Right arrow
                    GLFW.GLFW_KEY_RIGHT -> {
                        // Not yet implemented
                    }
                    // Add the character
                    else -> character?.let { this.addText(SharedConstants.filterAllowedCharacters(it)) }
                }
            }
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
        // Make sure that the text contains valid characters
        @Suppress("NAME_SHADOWING")
        var text = text
        text = SharedConstants.filterAllowedCharacters(text)
        // Now we test if we should show ghost text or not
        // If the control is focused then we don't show ghost text
        if (isFocused) {
            textLabel.text = text + "_"
            textLabel.textColor = textColor
        } else {
            // If the string is empty we show ghost text
            if (text.isEmpty()) {
                textLabel.text = ghostText
                textLabel.textColor = GHOST_TEXT_COLOR
            } else {
                textLabel.text = text
                textLabel.textColor = textColor
            }
        }

        // Update the scroll based on the new text
        updateScroll()
    }

    /**
     * Adds text to the current text in the text field
     *
     * @param text The new text to add
     */
    private fun addText(text: String) {
        // Make sure the text is non-empty
        if (text.isNotEmpty()) {
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
        val textWidth = this.textLabel.font.getWidth(this.textLabel.text) * Constants.TEXT_SCALE_FACTOR
        // If the text width is bigger than the label's width we set the alignment to right so that you see the newest text not the oldest
        if (textWidth > this.textLabel.getWidth()) {
            this.textLabel.textAlignment = TextAlignment.ALIGN_RIGHT
        } else {
            this.textLabel.textAlignment = TextAlignment.ALIGN_LEFT
        }
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
            Minecraft.getInstance().keyboardListener.enableRepeatEvents(true)
            // Set the background to be tinted
            background.color = FOCUSED_COLOR_TINT
            // Get the current text
            val currentText = this.getText()
            // Update our focused variable
            this.isFocused = true
            // Call set text to refresh the text formatting
            this.setText(currentText)
        } else if (wasFocused && !isFocused) {
            // Disable repeat events
            Minecraft.getInstance().keyboardListener.enableRepeatEvents(false)
            // Set the background to be untinted
            background.color = BASE_COLOR_TINT
            // Get the current text
            val currentText = this.getText()
            // Update our focused variable
            this.isFocused = false
            // Call set text to refresh the text formatting
            this.setText(currentText)
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

    companion object {
        // Ghost text color is gray
        private val GHOST_TEXT_COLOR = Color(128, 128, 128)

        // When focused tint gray
        private val FOCUSED_COLOR_TINT = Color(230, 230, 230)

        // When not focused we use a white tint which does nothing
        private val BASE_COLOR_TINT = Color(255, 255, 255)
    }
}
