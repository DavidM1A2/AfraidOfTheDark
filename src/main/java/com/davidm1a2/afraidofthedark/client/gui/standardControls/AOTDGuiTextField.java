package com.davidm1a2.afraidofthedark.client.gui.standardControls;

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility;
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiContainer;
import com.davidm1a2.afraidofthedark.client.gui.base.TextAlignment;
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDKeyEvent;
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent;
import com.davidm1a2.afraidofthedark.client.gui.fontLibrary.TrueTypeFont;
import com.davidm1a2.afraidofthedark.common.constants.Constants;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Color;

/**
 * The GUI text field control to let users enter text
 */
public class AOTDGuiTextField extends AOTDGuiContainer
{
    // Ghost text color is gray
    private static final Color GHOST_TEXT_COLOR = new Color(128, 128, 128);
    // When focused tint gray
    private static final Color FOCUSED_COLOR_TINT = new Color(230, 230, 230);
    // When not focused we use a white tint which does nothing
    private static final Color BASE_COLOR_TINT = new Color(255, 255, 255);
    // The background image of the text field
    private final AOTDGuiImage background;
    // The text container panel
    private final AOTDGuiPanel textContainer;
    // The label which shows the text
    private final AOTDGuiLabel textLabel;
    // If the text field is focused or not
    private boolean isFocused = false;
    // The text to show when no text is present
    private String ghostText = StringUtils.EMPTY;
    // Default text color is white
    private Color textColor = new Color(255, 255, 255);

    /**
     * Constructor initializes the bounding box
     *
     * @param x      The X location of the top left corner
     * @param y      The Y location of the top left corner
     * @param width  The width of the component
     * @param height The height of the component
     * @param font   The font to use for the text
     */
    public AOTDGuiTextField(Integer x, Integer y, Integer width, Integer height, TrueTypeFont font)
    {
        // Call super as usual
        super(x, y, width, height);

        // Create our background image
        this.background = new AOTDGuiImage(0, 0, width, height, "afraidofthedark:textures/gui/text_field_background.png");

        // Set the text container bounding box to have scissor enabled and contain the label
        this.textContainer = new AOTDGuiPanel(5, 5, width - 10, height - 10, true);
        // The text label contains the text field's text
        this.textLabel = new AOTDGuiLabel(5, 0, width - 15, height - 10, font);
        // Make sure the label doesn't shorten the text inside to fit
        this.textLabel.setShortenTextToFit(false);

        // Add the text label to the container
        this.textContainer.add(this.textLabel);
        // Add the text container to the background
        this.background.add(this.textContainer);
        // Add the background to this control
        this.add(this.background);

        // Set the control to focused once it's selected
        this.addMouseListener(event ->
        {
            if (event.getEventType() == AOTDMouseEvent.EventType.Click)
            {
                // Make sure it was a left click
                if (event.getClickedButton() == AOTDMouseEvent.LEFT_MOUSE_BUTTON)
                {
                    // Test if we're hovered or not, if so set it to focused, otherwise set it to not focused
                    AOTDGuiTextField current = AOTDGuiTextField.this;
                    if (current.isHovered())
                    {
                        current.setFocused(true);
                    }
                    else
                    {
                        current.setFocused(false);
                    }
                }
            }
        });
        // When a key is typed save that information
        this.addKeyListener(event ->
        {
            if (event.getEventType() == AOTDKeyEvent.KeyEventType.Type)
            {
                AOTDGuiTextField.this.keyTyped(event.getKey(), event.getKeyCode());
            }
        });
    }

    /**
     * Only draw the text field if visible
     */
    @Override
    public void draw()
    {
        if (this.isVisible())
        {
            super.draw();
        }
    }

    /**
     * Processes a key typed event
     *
     * @param character The character typed
     * @param keyCode   The code of the character typed
     */
    private void keyTyped(char character, int keyCode)
    {
        // Ensure the text field is focused
        if (this.isFocused)
        {
            AOTDGuiUtility guiUtility = AOTDGuiUtility.getInstance();
            // CTRL + A
            if (guiUtility.isCtrlKeyDown() && keyCode == Keyboard.KEY_A)
            {
                // Select all text, not yet implemented
            }
            // CTRL + C
            else if (guiUtility.isCtrlKeyDown() && keyCode == Keyboard.KEY_C)
            {
                // Update the clipboard string
                guiUtility.setClipboardString(this.getText());
            }
            // Ctrl + V
            else if (guiUtility.isCtrlKeyDown() && keyCode == Keyboard.KEY_V)
            {
                this.setText("");
                this.addText(ChatAllowedCharacters.filterAllowedCharacters(guiUtility.getClipboardString()));
            }
            // CTRL + X
            else if (guiUtility.isCtrlKeyDown() && keyCode == Keyboard.KEY_X)
            {
                guiUtility.setClipboardString(this.getText());
                this.setText("");
            }
            // Regular key typed
            else
            {
                switch (keyCode)
                {
                    // Backspace removes 1 character
                    case Keyboard.KEY_BACK:
                        this.removeChars(1);
                        break;
                    // Left arrow
                    case Keyboard.KEY_LEFT:
                        // Not yet implemented
                        break;
                    // Right arrow
                    case Keyboard.KEY_RIGHT:
                        // Not yet implemented
                        break;
                    default:
                        // Add the character
                        if (ChatAllowedCharacters.isAllowedCharacter(character))
                        {
                            this.addText(Character.toString(character));
                        }
                        break;
                }
            }
        }
    }

    /**
     * @return The current text in the text field or empty string if it is ghost text
     */
    public String getText()
    {
        // Return either "" if the text is ghost text or the text otherwise
        if (this.isShowingGhostText())
        {
            return StringUtils.EMPTY;
        }
        else
        {
            // If we're focused remove the _, otherwise just return the text
            if (this.isFocused)
            {
                return this.textLabel.getText().substring(0, this.textLabel.getText().length() - 1);
            }
            else
            {
                return this.textLabel.getText();
            }
        }
    }

    /**
     * Sets the text of the text field. If the text field is focused an _ is added. If it is not focused either ghost text is shown
     * or the text is added without the _ depending on if text is empty or not
     *
     * @param text The new text to display, or empty string if ghost text should be down
     */
    public void setText(String text)
    {
        // Make sure that the text contains valid characters
        text = ChatAllowedCharacters.filterAllowedCharacters(text);
        // Now we test if we should show ghost text or not
        // If the control is focused then we don't show ghost text
        if (this.isFocused)
        {
            this.textLabel.setText(text + "_");
            this.textLabel.setTextColor(textColor);
        }
        // If the control is not focused we check if the text is empty to decide if we want to show ghost text or not
        else
        {
            // If the string is empty we show ghost text
            if (StringUtils.isEmpty(text))
            {
                this.textLabel.setText(ghostText);
                this.textLabel.setTextColor(GHOST_TEXT_COLOR);
            }
            // If it is not we just show the text
            else
            {
                this.textLabel.setText(text);
                this.textLabel.setTextColor(textColor);
            }
        }
        // Update the scroll based on the new text
        this.updateScroll();
    }

    /**
     * Adds text to the current text in the text field
     *
     * @param text The new text to add
     */
    public void addText(String text)
    {
        // Make sure the text is non-empty
        if (!StringUtils.isEmpty(text))
        {
            this.setText(this.getText() + text);
        }
    }

    /**
     * Removes "number" amount of characters from the current string. Should be between 0 ... string.length()
     *
     * @param number The number of characters to remove
     */
    public void removeChars(int number)
    {
        // Clamp the number to be between 0 and the text's length
        number = MathHelper.clamp(number, 0, this.getText().length());
        // Get the current text
        String currentText = this.getText();
        // Set the new text
        this.setText(currentText.substring(0, currentText.length() - number));
    }

    /**
     * Updates the amount the text label should be shifted by to allow for scrolling text
     */
    private void updateScroll()
    {
        // Get the text's width and multiply by our scale factor used
        float textWidth = this.textLabel.getFont().getWidth(this.textLabel.getText()) * Constants.TEXT_SCALE_FACTOR;
        // If the text width is bigger than the label's width we set the alignment to right so that you see the newest text not the oldest
        if (textWidth > this.textLabel.getWidth())
        {
            this.textLabel.setTextAlignment(TextAlignment.ALIGN_RIGHT);
        }
        else
        {
            this.textLabel.setTextAlignment(TextAlignment.ALIGN_LEFT);
        }
    }

    /**
     * @return The ghost text to be shown if the text field is empty
     */
    public String getGhostText()
    {
        return this.ghostText;
    }

    /**
     * Sets the ghost text to use
     *
     * @param ghostText The new ghost text
     */
    public void setGhostText(String ghostText)
    {
        // Flag telling us if ghost text used to be loaded
        boolean ghostTextWasLoaded = this.isShowingGhostText();
        this.ghostText = ghostText;
        // If the text was ghost text update the ghost text
        if (ghostTextWasLoaded)
        {
            this.textLabel.setText(this.ghostText);
        }
        else if (StringUtils.isEmpty(this.textLabel.getText()))
        {
            this.textLabel.setText(this.ghostText);
            this.textLabel.setTextColor(GHOST_TEXT_COLOR);
        }
    }

    /**
     * @return True if ghost text is showing, false otherwise
     */
    private boolean isShowingGhostText()
    {
        // Here we use a bit of a hack to detect if we're showing ghost text. We check the color of the text and the string contents. There's an
        // edge case where this fails if the color is gray and the string is exactly the ghost text
        return this.textLabel.getTextColor().equals(GHOST_TEXT_COLOR) && this.textLabel.getText().equals(this.ghostText);
    }

    /**
     * @return True if the control is focused, false otherwise
     */
    public boolean isFocused()
    {
        return this.isFocused;
    }

    /**
     * Sets the control to be focused or not
     *
     * @param isFocused If the control is focused or not
     */
    private void setFocused(boolean isFocused)
    {
        // Was focused tells us if we were previously focused
        boolean wasFocused = this.isFocused;
        // If we were not focused and now are focused update the text
        if (!wasFocused && isFocused)
        {
            // Enable repeat events so we can type multiple characters in the box by holding them
            Keyboard.enableRepeatEvents(true);
            // Set the background to be tinted
            this.background.setColor(FOCUSED_COLOR_TINT);
            // Get the current text
            String currentText = this.getText();
            // Update our focused variable
            this.isFocused = true;
            // Call set text to refresh the text formatting
            this.setText(currentText);
        }
        // If we were focused and now are not focused update the text
        else if (wasFocused && !isFocused)
        {
            // Disable repeat events
            Keyboard.enableRepeatEvents(false);
            // Set the background to be untinted
            this.background.setColor(BASE_COLOR_TINT);
            // Get the current text
            String currentText = this.getText();
            // Update our focused variable
            this.isFocused = false;
            // Call set text to refresh the text formatting
            this.setText(currentText);
        }
    }

    /**
     * @return The color of the text
     */
    public Color getTextColor()
    {
        return this.textColor;
    }

    /**
     * Sets the text color
     *
     * @param color The new text color
     */
    public void setTextColor(Color color)
    {
        this.textColor = color;
        // If we were not showing ghost text update the label's color
        if (!this.isShowingGhostText())
        {
            this.textLabel.setTextColor(this.textColor);
        }
    }
}
