package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiContainer
import com.davidm1a2.afraidofthedark.client.gui.base.TextAlignment
import com.davidm1a2.afraidofthedark.client.gui.fontLibrary.TrueTypeFont
import net.minecraft.util.ResourceLocation
import org.lwjgl.util.Color

/**
 * Class representing a GUI button to be used by AOTD
 *
 * @constructor Sets the buttons location, scale, font, icon path. No hovered icon is provided
 * @param x      The X location of the top left corner
 * @param y      The Y location of the top left corner
 * @param width  The width of the component
 * @param height The height of the component
 * @param font   The font to be used to draw the button's text
 * @param icon   The icon to use for the background of the button
 * @property
 */
open class AOTDGuiButton(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        private val icon: ResourceLocation,
        private val iconHovered: ResourceLocation,
        font: TrueTypeFont? = null
) : AOTDGuiContainer(x, y, width, height)
{
    // The image containing the background texture
    private val background: AOTDGuiImage
    // The label containing the center text
    private val label: AOTDGuiLabel?

    override var color: Color
        get() = super.color
        set(tint)
        {
            super.color = tint
            this.background.color = tint
        }

    init
    {
        // Create a background image for the button
        this.background = AOTDGuiImage(0, 0, width, height, icon)
        this.background.color = Color(255, 255, 255)
        this.add(background)

        // Create a label to cover the button
        if (font != null)
        {
            this.label = AOTDGuiLabel(0, 0, width, height, font)
            this.add(this.label)
        }
        else
        {
            this.label = null
        }
    }

    /**
     * Constructor sets the buttons location, scale, font, icon and hovered icon paths
     *
     * @param x           The X location of the top left corner
     * @param y           The Y location of the top left corner
     * @param width       The width of the component
     * @param height      The height of the component
     * @param font        The font to be used to draw the button's text
     * @param icon        The icon to use for the background of the button
     * @param iconHovered The icon to use for the background of the button when the button is hovered
     */
    constructor(x: Int, y: Int, width: Int, height: Int, icon: String, iconHovered: String = icon, font: TrueTypeFont? = null) : this(
            x,
            y,
            width,
            height,
            ResourceLocation(icon),
            ResourceLocation(iconHovered),
            font
    )

    /**
     * Draw function that gets called every frame. Draw the button and i's label
     */
    override fun draw()
    {
        if (this.isVisible)
        {
            super.draw()
            this.background.imageTexture = if (this.isHovered) this.iconHovered else this.icon
        }
    }

    /**
     * Sets the text of the button
     *
     * @param text The text to draw over the button
     */
    fun setText(text: String)
    {
        this.label?.text = text
    }

    /**
     * Sets the color of the text
     *
     * @param textColor The text color
     */
    fun setTextColor(textColor: Color)
    {
        this.label?.textColor = textColor
    }

    /**
     * Sets the text alignment for this button
     *
     * @param textAlignment The button's text alignment
     */
    fun setTextAlignment(textAlignment: TextAlignment)
    {
        this.label?.textAlignment = textAlignment
    }
}
