package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDPane
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.AbsolutePosition
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.client.gui.layout.RelativeDimensions
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponent
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance

/**
 * Base class for all AOTD gui spell components
 *
 * @constructor Initializes the bounding box
 * @param x      The X location of the top left corner
 * @param y      The Y location of the top left corner
 * @param width  The width of the component
 * @param height The height of the component
 * @property icon The foreground image of the spell slot
 * @property highlight The highlight effect around the spell slot
 * @property componentInstance The instance of this spell component
 */
abstract class SpellComponentSlot<T : SpellComponent<T>>(
    slotBackground: String,
    offset: Position = AbsolutePosition(0.0, 0.0),
    prefSize: Dimensions = RelativeDimensions(1.0, 1.0),
    val spell: Spell
) : AOTDPane(offset, prefSize) {

    private val icon: ImagePane
    private val highlight: ImagePane
    private var componentInstance: SpellComponentInstance<T>? = null

    init {
        // The background image of the spell slot
        val background = ImagePane(slotBackground)
        this.add(background)

        // Add a highlight effect image over the background
        this.highlight = ImagePane("afraidofthedark:textures/gui/spell_editor/highlight.png")
        this.highlight.isVisible = false
        this.add(highlight)

        // Set the icon to be blank
        this.icon = ImagePane("afraidofthedark:textures/gui/spell_editor/blank_slot.png")
        this.icon.isVisible = false
        this.add(icon)

        this.addMouseListener {
            if (it.eventType == AOTDMouseEvent.EventType.Click && it.clickedButton == AOTDMouseEvent.RIGHT_MOUSE_BUTTON) {
                if (this.isHovered && this.inBounds && this.isVisible) {
                    this.setSpellComponent(null)
                }
            }
        }
    }

    /**
     * Sets the component type and icon of this slot from a type
     *
     * @param instance The component instance to show
     */
    fun setSpellComponent(instance: SpellComponentInstance<T>?) {
        // If the component type is null hide the icon and set the instance to null too
        if (instance == null) {
            this.icon.isVisible = false
            this.componentInstance = null
        }
        // If the component type is non-null show the the right icon
        else {
            this.icon.isVisible = true
            this.componentInstance = instance
            this.icon.imageTexture = instance.component.icon
        }
    }

    internal abstract fun refreshHoverText()

    fun getComponentType(): T? {
        return componentInstance?.component
    }

    fun getComponentInstance(): SpellComponentInstance<T>? {
        return componentInstance
    }

    fun setHighlight(highlit: Boolean) {
        this.highlight.isVisible = highlit
    }
}
