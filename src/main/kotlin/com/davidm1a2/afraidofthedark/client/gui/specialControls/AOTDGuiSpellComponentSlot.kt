package com.davidm1a2.afraidofthedark.client.gui.specialControls

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiContainer
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiImage
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
abstract class AOTDGuiSpellComponentSlot<T : SpellComponent<T>>(
    width: Int,
    height: Int,
    slotBackground: String
) : AOTDGuiContainer(width, height) {
    private val icon: AOTDGuiImage
    private val highlight: AOTDGuiImage
    private var componentInstance: SpellComponentInstance<T>? = null

    init {
        // The background image of the spell slot
        val background = AOTDGuiImage(slotBackground)
        this.add(background)

        // Add a highlight effect image over the background
        this.highlight = AOTDGuiImage("afraidofthedark:textures/gui/spell_editor/highlight.png")
        this.highlight.isVisible = false
        this.add(highlight)

        // Set the icon to be blank
        this.icon = AOTDGuiImage("afraidofthedark:textures/gui/spell_editor/blank_slot.png")
        this.icon.isVisible = false
        this.add(icon)
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
        // Update the hover text based on the slot
        this.refreshHoverText()
    }

    /**
     * Refreshes the text that gets displayed when the slot is hovered
     */
    internal abstract fun refreshHoverText()

    /**
     * @return The component type of this slot
     */
    fun getComponentType(): T? {
        return componentInstance?.component
    }

    /**
     * @return The component instance of this slot
     */
    fun getComponentInstance(): SpellComponentInstance<T>? {
        return componentInstance
    }

    /**
     * True if the highlight should be shown, false otherwise
     *
     * @param highlit True if the slot is highlit, false otherwise
     */
    fun setHighlight(highlit: Boolean) {
        this.highlight.isVisible = highlit
    }
}
