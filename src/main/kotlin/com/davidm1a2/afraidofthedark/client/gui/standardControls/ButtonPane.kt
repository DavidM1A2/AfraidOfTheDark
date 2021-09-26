package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.events.MouseEvent
import com.davidm1a2.afraidofthedark.client.gui.fontLibrary.TrueTypeFont
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
import com.davidm1a2.afraidofthedark.client.gui.layout.TextAlignment
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.util.SoundEvents
import java.awt.Color

/**
 * Class representing a GUI button to be used by AOTD
 */
open class ButtonPane(
    private val icon: ImagePane?,
    private val iconHovered: ImagePane? = icon,
    silent: Boolean = false,
    margins: Spacing = Spacing(),
    gravity: Gravity = Gravity.TOP_LEFT,
    hoverTexts: Array<String> = emptyArray(),
    padding: Spacing = Spacing(),
    prefSize: Dimensions = Dimensions(Double.MAX_VALUE, Double.MAX_VALUE),
    offset: Position = Position(0.0, 0.0),
    font: TrueTypeFont? = null
) : AOTDPane(offset, prefSize, margins, gravity, hoverTexts, padding) {

    private val label: LabelComponent?

    init {
        // Add our images as child nodes
        this.icon?.let { this.add(it) }
        this.iconHovered?.let { this.add(it) }
        // Create a label to cover the button
        if (font != null) {
            this.label = LabelComponent(font, Dimensions(1.0, 1.0))
            this.add(this.label)
        } else {
            this.label = null
        }

        // If this button is clicked, consume the event so parents don't get it
        this.addMouseListener {
            if (it.eventType == MouseEvent.EventType.Click &&
                it.clickedButton == MouseEvent.LEFT_MOUSE_BUTTON &&
                this.isVisible && this.isHovered && this.inBounds
            ) {
                if (!silent) {
                    entityPlayer.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0f, 1.0f)
                }
                it.consume()
            }
        }
    }

    override fun draw(matrixStack: MatrixStack) {
        if (this.isVisible) {
            super.draw(matrixStack)
            if (iconHovered != null && icon != null) {
                if (this.isHovered) {   // Always end with setting the visibility to true just in case the two icons are the same
                    icon.isVisible = false
                    iconHovered.isVisible = true
                } else {
                    iconHovered.isVisible = false
                    icon.isVisible = true
                }
            }
        }
    }

    // Adds an onClick listener that fires whenever the button is left-clicked
    fun addOnClick(listener: (MouseEvent) -> Unit) {
        this.addMouseListener {
            if (it.eventType == MouseEvent.EventType.Click &&
                it.clickedButton == MouseEvent.LEFT_MOUSE_BUTTON &&
                this.isVisible && this.isHovered && this.inBounds
            ) {
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
