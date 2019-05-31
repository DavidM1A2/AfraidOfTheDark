package com.DavidM1A2.afraidofthedark.client.gui.standardControls;

import com.DavidM1A2.afraidofthedark.client.gui.base.AOTDGuiContainer;
import com.DavidM1A2.afraidofthedark.client.gui.base.TextAlignment;
import com.DavidM1A2.afraidofthedark.client.gui.fontLibrary.TrueTypeFont;
import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.util.Color;

/**
 * Class representing a label to be drawn on the GUI
 */
public class AOTDGuiLabel extends AOTDGuiContainer
{
    // The font to draw the text with
    private final TrueTypeFont font;
    // The raw text to draw
    private String text = StringUtils.EMPTY;
    // The actual text to draw within the bounds of the label
    private String fitText = StringUtils.EMPTY;
    // Flag telling the label it needs to update the fit text
    private boolean needsTextUpdate = true;
    // The color to draw the text with
    private Color textColor = new Color(255, 255, 255, 255);
    // Text alignment
    private TextAlignment textAlignment = TextAlignment.ALIGN_LEFT;

    /**
     * Constructor takes an x and y position as well as a font
     *
     * @param x      The X coordinate of the label
     * @param y      The Y coordinate of the label
     * @param width  The width of the label
     * @param height The height of the label
     * @param font   The font to use to draw the label
     */
    public AOTDGuiLabel(int x, int y, int width, int height, TrueTypeFont font)
    {
        super(x, y, width, height);
        this.font = font;
    }

    /**
     * Draw function that gets called every frame. Draw the text
     */
    @Override
    public void draw()
    {
        // If we need a text to size update do that
        if (this.needsTextUpdate)
        {
            this.computeTextForSize();
            this.needsTextUpdate = false;
        }

        // If the label is visible, draw it
        if (this.isVisible())
        {
            // If we don't have a font, we can't draw anything
            if (this.font != null)
            {
                // Compute the x and y positions of the text
                float xCoord = this.getXScaled().floatValue() + (this.textAlignment == TextAlignment.ALIGN_LEFT ? 0 : this.textAlignment == TextAlignment.ALIGN_CENTER ? this.getWidthScaled() / 2f : this.getWidthScaled());
                float yCoord = this.getYScaled().floatValue();

                // TODO: Center align text on the y-axis

                // Draw the string at (x, y) with the correct color and scale
                this.font.drawString(
                        xCoord,
                        yCoord,
                        this.fitText,
                        this.getScaleX().floatValue() * Constants.TEXT_SCALE_FACTOR,
                        this.getScaleY().floatValue() * Constants.TEXT_SCALE_FACTOR,
                        textAlignment,
                        this.textColor);
            }
            super.draw();
        }
    }

    /**
     * Updates the text to draw based on the width and height of the label. If the text is too much then cut it off
     */
    private void computeTextForSize()
    {
        // Can only update text if font is non-null
        if (this.font != null)
        {
            // Test if the text will fit into our label based on height
            float height = (float) (this.font.getHeight() * Constants.TEXT_SCALE_FACTOR * this.getScaleY());
            if (height > this.getHeightScaled())
            {
                this.fitText = StringUtils.EMPTY;
            }
            // If the height is OK shorten the text until it fits into the label
            else
            {
                this.fitText = text;
                // Grab the current width of the text
                float width = (float) (this.font.getWidth(this.fitText) * Constants.TEXT_SCALE_FACTOR * this.getScaleX());
                // If it's too big remove one character at a time until it isn't
                while (width > this.getWidthScaled() && !this.fitText.isEmpty())
                {
                    this.fitText = this.fitText.substring(0, this.fitText.length() - 2);
                    // Grab the current width of the text
                    width = (float) (this.font.getWidth(this.fitText) * Constants.TEXT_SCALE_FACTOR * this.getScaleX());
                }
            }
        }
    }

    /**
     * @return The text of this label
     */
    public String getText()
    {
        return this.text;
    }

    /**
     * @param text Sets the text to be drawn
     */
    public void setText(String text)
    {
        // Set the internal text of the label
        this.text = text;
        this.needsTextUpdate = true;
    }

    /**
     * When we update this control's width also update the drawn text
     *
     * @param width The new component's width
     */
    @Override
    public void setWidth(int width)
    {
        super.setWidth(width);
        this.needsTextUpdate = true;
    }

    /**
     * @return Gets the font to be used while drawing the text
     */
    public TrueTypeFont getFont()
    {
        return this.font;
    }

    /**
     * @return Gets the text's color
     */
    public Color getTextColor()
    {
        return this.textColor;
    }

    /**
     * @param textColor Sets the text's color
     */
    public void setTextColor(Color textColor)
    {
        this.textColor = textColor;
    }

    /**
     * @return Get the text alignment used by the label
     */
    public TextAlignment getTextAlignment()
    {
        return this.textAlignment;
    }

    /**
     * @param textAlignment Text alignment used by the label
     */
    public void setTextAlignment(TextAlignment textAlignment)
    {
        this.textAlignment = textAlignment;
    }
}
