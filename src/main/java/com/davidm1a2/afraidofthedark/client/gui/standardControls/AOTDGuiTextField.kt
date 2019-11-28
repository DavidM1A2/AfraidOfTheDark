package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiContainer
import com.davidm1a2.afraidofthedark.client.gui.base.TextAlignment
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDKeyEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.fontLibrary.TrueTypeFont
import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.util.ChatAllowedCharacters
import net.minecraft.util.math.MathHelper
import org.apache.commons.lang3.StringUtils
import org.lwjgl.input.Keyboard
import org.lwjgl.util.Color

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
 * @property isShowingGhostText True if ghost text is showing, false otherwise
 */
class AOTDGuiTextField(x: Int, y: Int, width: Int, height: Int, font: TrueTypeFont) : AOTDGuiContainer(x, y, width, height)
{
    private val background: AOTDGuiImage
    private val textContainer: AOTDGuiPanel
    private val textLabel: AOTDGuiLabel
    var isFocused = false
        private set(isFocused)
        {
            // Was focused tells us if we were previously focused
            val wasFocused = this.isFocused
            // If we were not focused and now are focused update the text
            if (!wasFocused && isFocused)
            {
                // Enable repeat events so we can type multiple characters in the box by holding them
                Keyboard.enableRepeatEvents(true)
                // Set the background to be tinted
                this.background.color = FOCUSED_COLOR_TINT
                // Get the current text
                val currentText = this.text
                // Update our focused variable
                field = true
                // Call set text to refresh the text formatting
                this.text = currentText
            }
            // If we were focused and now are not focused update the text
            else if (wasFocused && !isFocused)
            {
                // Disable repeat events
                Keyboard.enableRepeatEvents(false)
                // Set the background to be untinted
                this.background.color = BASE_COLOR_TINT
                // Get the current text
                val currentText = this.text
                // Update our focused variable
                field = false
                // Call set text to refresh the text formatting
                this.text = currentText
            }
        }
    var ghostText = StringUtils.EMPTY
        set(ghostText)
        {
            // Flag telling us if ghost text used to be loaded
            val ghostTextWasLoaded = this.isShowingGhostText
            field = ghostText
            // If the text was ghost text update the ghost text
            if (ghostTextWasLoaded)
            {
                this.textLabel.text = this.ghostText
            }
            else if (StringUtils.isEmpty(this.textLabel.text))
            {
                this.textLabel.text = this.ghostText
                this.textLabel.textColor = GHOST_TEXT_COLOR
            }
        }
    var textColor = Color(255, 255, 255)
        set(color)
        {
            field = color
            if (!this.isShowingGhostText)
            {
                this.textLabel.textColor = this.textColor
            }
        }
    var text: String
        // Return either "" if the text is ghost text or the text otherwise
        get() = if (this.isShowingGhostText)
        {
            StringUtils.EMPTY
        }
        else
        {
            // If we're focused remove the _, otherwise just return the text
            if (this.isFocused)
            {
                this.textLabel.text.substring(0, this.textLabel.text.length - 1)
            }
            else
            {
                this.textLabel.text
            }
        }
        set(text)
        {
            var rawText = text
            // Make sure that the text contains valid characters
            rawText = ChatAllowedCharacters.filterAllowedCharacters(rawText)
            // Now we test if we should show ghost text or not
            if (this.isFocused)
            {
                // If the control is focused then we don't show ghost text
                this.textLabel.text = rawText + "_"
                this.textLabel.textColor = this.textColor
            }
            // If the control is not focused we check if the text is empty to decide if we want to show ghost text or not
            else
            {
                // If the string is empty we show ghost text
                if (StringUtils.isEmpty(rawText))
                {
                    this.textLabel.text = this.ghostText
                    this.textLabel.textColor = GHOST_TEXT_COLOR
                }
                else
                {
                    // If it is not we just show the text
                    this.textLabel.text = rawText
                    this.textLabel.textColor = this.textColor
                }
            }
            // Update the scroll based on the new text
            this.updateScroll()
        }
    private val isShowingGhostText: Boolean
        // Here we use a bit of a hack to detect if we're showing ghost text. We check the color of the text and the string contents.
        // There's an edge case where this fails if the color is gray and the string is exactly the ghost text
        get() = this.textLabel.textColor == GHOST_TEXT_COLOR && this.textLabel.text == this.ghostText

    init
    {
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
        this.addMouseListener()
        {
            if (it.eventType === AOTDMouseEvent.EventType.Click)
            {
                // Make sure it was a left click
                if (it.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON)
                {
                    // Test if we're hovered or not, if so set it to focused, otherwise set it to not focused
                    isFocused = isHovered
                }
            }
        }
        // When a key is typed save that information
        this.addKeyListener()
        {
            if (it.eventType === AOTDKeyEvent.KeyEventType.Type)
            {
                keyTyped(it.key, it.keyCode)
            }
        }
    }

    /**
     * Only draw the text field if visible
     */
    override fun draw()
    {
        if (this.isVisible)
        {
            super.draw()
        }
    }

    /**
     * Processes a key typed event
     *
     * @param character The character typed
     * @param keyCode   The code of the character typed
     */
    private fun keyTyped(character: Char, keyCode: Int)
    {
        // Ensure the text field is focused
        if (this.isFocused)
        {
            // CTRL + A
            if (AOTDGuiUtility.isCtrlKeyDown && keyCode == Keyboard.KEY_A)
            {
                // Select all text, not yet implemented
            }
            // CTRL + C
            else if (AOTDGuiUtility.isCtrlKeyDown && keyCode == Keyboard.KEY_C)
            {
                // Update the clipboard string
                AOTDGuiUtility.clipboardString = this.text
            }
            // Ctrl + V
            else if (AOTDGuiUtility.isCtrlKeyDown && keyCode == Keyboard.KEY_V)
            {
                this.text = ""
                this.addText(ChatAllowedCharacters.filterAllowedCharacters(AOTDGuiUtility.clipboardString))
            }
            // CTRL + X
            else if (AOTDGuiUtility.isCtrlKeyDown && keyCode == Keyboard.KEY_X)
            {
                AOTDGuiUtility.clipboardString = this.text
                this.text = ""
            }
            // Regular key typed
            else
            {
                when (keyCode)
                {
                    // Backspace removes 1 character
                    Keyboard.KEY_BACK -> this.removeChars(1)
                    // Left arrow
                    Keyboard.KEY_LEFT ->
                    {
                        // Not yet implemented
                    }
                    // Right arrow
                    Keyboard.KEY_RIGHT ->
                    {
                        // Not yet implemented
                    }
                    else ->
                        if (ChatAllowedCharacters.isAllowedCharacter(character))
                        {
                            // Add the character
                            this.addText(character.toString())
                        }
                }
            }
        }
    }

    /**
     * Adds text to the current text in the text field
     *
     * @param text The new text to add
     */
    private fun addText(text: String)
    {
        // Make sure the text is non-empty
        if (!StringUtils.isEmpty(text))
        {
            this.text = this.text + text
        }
    }

    /**
     * Removes "number" amount of characters from the current string. Should be between 0 ... string.length()
     *
     * @param number The number of characters to remove
     */
    private fun removeChars(number: Int)
    {
        // Clamp the number to be between 0 and the text's length
        val numberClamped = MathHelper.clamp(number, 0, this.text.length)
        // Get the current text
        val currentText = this.text
        // Set the new text
        this.text = currentText.substring(0, currentText.length - numberClamped)
    }

    /**
     * Updates the amount the text label should be shifted by to allow for scrolling text
     */
    private fun updateScroll()
    {
        // Get the text's width and multiply by our scale factor used
        val textWidth = this.textLabel.font.getWidth(this.textLabel.text) * Constants.TEXT_SCALE_FACTOR
        // If the text width is bigger than the label's width we set the alignment to right so that you see the newest text not the oldest
        if (textWidth > this.textLabel.width)
        {
            this.textLabel.textAlignment = TextAlignment.ALIGN_RIGHT
        }
        else
        {
            this.textLabel.textAlignment = TextAlignment.ALIGN_LEFT
        }
    }

    companion object
    {
        // Ghost text color is gray
        private val GHOST_TEXT_COLOR = Color(128, 128, 128)
        // When focused tint gray
        private val FOCUSED_COLOR_TINT = Color(230, 230, 230)
        // When not focused we use a white tint which does nothing
        private val BASE_COLOR_TINT = Color(255, 255, 255)
    }
}
