package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiContainer
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiGravity
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiSpacing
import com.davidm1a2.afraidofthedark.client.gui.base.TextAlignment
import com.davidm1a2.afraidofthedark.client.gui.fontLibrary.TrueTypeFont
import net.minecraft.util.ResourceLocation
import java.awt.Color
import kotlin.math.min

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
 * @property background The image containing the background texture
 * @property label The label containing the center text
 * @property color The color of the background and text
 */
open class AOTDGuiButton(
        prefWidth: Int,
        prefHeight: Int,
        xOffset: Int = 0,
        yOffset: Int = 0,
        margins: AOTDGuiSpacing = AOTDGuiSpacing(),
        gravity: AOTDGuiGravity = AOTDGuiGravity.TOP_LEFT,
        hoverTexts: Array<String> = emptyArray(),
        padding: AOTDGuiSpacing = AOTDGuiSpacing(),
        private val icon: AOTDGuiImage?,
        private val iconHovered: AOTDGuiImage? = icon,
        font: TrueTypeFont? = null) :
        AOTDGuiContainer(prefWidth, prefHeight, xOffset, yOffset, margins, gravity, hoverTexts, padding) {

    private val label: AOTDGuiLabel?

    init {
        // Add our images as child nodes
        this.icon?.let { this.add(it) }
        this.iconHovered?.let { this.add(it) }
        // Create a label to cover the button
        if (font != null) {
            this.label = AOTDGuiLabel(font)
            this.add(this.label)
        } else {
            this.label = null
        }
    }

    /**
     * Draw function that gets called every frame. Draw the button and i's label
     */
    override fun draw() {
        if (this.isVisible) {
            super.draw()
            iconHovered?.isVisible = this.isHovered
            icon?.isVisible = this.isHovered.not()
        }
    }

    /**
     * Sets the text of the button
     *
     * @param text The text to draw over the button
     */
    fun setText(text: String) {
        this.label?.text = text
    }

    /**
     * Sets the color of the text
     *
     * @param textColor The text color
     */
    fun setTextColor(textColor: Color) {
        this.label?.textColor = textColor
    }

    /**
     * Sets the text alignment for this button
     *
     * @param textAlignment The button's text alignment
     */
    fun setTextAlignment(textAlignment: TextAlignment) {
        this.label?.textAlignment = textAlignment
    }
}
