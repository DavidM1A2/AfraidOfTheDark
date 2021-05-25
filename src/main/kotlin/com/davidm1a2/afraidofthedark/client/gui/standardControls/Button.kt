package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.base.*
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent
import com.davidm1a2.afraidofthedark.client.gui.fontLibrary.TrueTypeFont
import com.davidm1a2.afraidofthedark.client.gui.layout.*
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import java.awt.Color

/**
 * Class representing a GUI button to be used by AOTD
 */
open class Button(
    private val icon: ImagePane?,
    private val iconHovered: ImagePane? = icon,
    private val silent: Boolean = false,
    margins: GuiSpacing = AbsoluteSpacing(),
    gravity: GuiGravity = GuiGravity.TOP_LEFT,
    hoverTexts: Array<String> = emptyArray(),
    padding: GuiSpacing = AbsoluteSpacing(),
    prefSize: Dimensions = AbsoluteDimensions(Double.MAX_VALUE, Double.MAX_VALUE),
    offset: Position = RelativePosition(0.0, 0.0),
    font: TrueTypeFont? = null
) : AOTDPane(offset, prefSize, margins, gravity, hoverTexts, padding) {

    private val label: AOTDGuiLabel?

    init {
        // Add our images as child nodes
        this.icon?.let { this.add(it) }
        this.iconHovered?.let { this.add(it) }
        // Create a label to cover the button
        if (font != null) {
            this.label = AOTDGuiLabel(font, RelativeDimensions(1.0, 1.0))
            this.add(this.label)
        } else {
            this.label = null
        }
        if (!silent) {
            this.addMouseMoveListener {
                if (it.eventType == AOTDMouseMoveEvent.EventType.Enter) {
                    // When hovering the button play the hover sound
                    if (it.source.isHovered && it.source.isVisible) {
                        entityPlayer.playSound(ModSounds.BUTTON_HOVER, 0.6f, 1.7f)
                    }
                }
            }
        }
    }

    override fun draw() {
        if (this.isVisible) {
            super.draw()
            iconHovered?.let { it.isVisible = this.isHovered }
            icon?.let { it.isVisible = this.isHovered.not() }
        }
    }

    // Adds an onClick listener
    fun addOnClick(listener: (AOTDMouseEvent) -> Unit) {
        this.addMouseListener {
            if (it.eventType == AOTDMouseEvent.EventType.Click &&
                it.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON &&
                this.isVisible && this.isHovered && this.inBounds) {
                listener.invoke(it)
            }
        }
    }

    fun setText(text: String) {
        this.label?.text = text
    }

    fun setTextColor(textColor: Color) {
        this.label?.textColor = textColor
    }

    fun setTextAlignment(textAlignment: TextAlignment) {
        this.label?.textAlignment = textAlignment
    }
}
