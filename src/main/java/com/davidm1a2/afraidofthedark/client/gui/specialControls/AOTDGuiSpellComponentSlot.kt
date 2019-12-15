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
 */
abstract class AOTDGuiSpellComponentSlot<T : SpellComponent<T>>(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    slotBackground: String,
    spellComponent: T?
) : AOTDGuiContainer(x, y, width, height)
{
    // The foreground image of the spell slot
    private val icon: AOTDGuiImage
    // The highlight effect around the spell slot
    private val highlight: AOTDGuiImage
    // The type that this spell slot is
    private var component: T? = null
    // The instance of this spell component
    private var componentInstance: SpellComponentInstance<T>? = null

    init
    {
        this.component = spellComponent

        // The background image of the spell slot
        val background = AOTDGuiImage(0, 0, width, height, slotBackground)
        this.add(background)

        // Add a highlight effect image over the background
        this.highlight = AOTDGuiImage(0, 0, width, height, "afraidofthedark:textures/gui/spell_editor/highlight.png")
        this.highlight.isVisible = false
        this.add(highlight)

        // Set the icon to be blank
        this.icon = AOTDGuiImage(0, 0, width, height, "afraidofthedark:textures/gui/spell_editor/blank_slot.png")
        this.icon.isVisible = component != null
        this.add(icon)
    }

    /**
     * Sets the component type and icon of this slot from a type
     *
     * @param instance The component instance to show
     */
    fun setSpellComponent(instance: SpellComponentInstance<T>?)
    {
        // If the component type is null hide the icon and set the instance to null too
        if (instance == null)
        {
            this.icon.isVisible = false
            this.componentInstance = null
        }
        // If the component type is non-null show the the right icon
        else
        {
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
    fun getComponentType(): T?
    {
        return component
    }

    fun getComponentInstance(): SpellComponentInstance<T>?
    {
        return componentInstance
    }

    /**
     * True if the highlight should be shown, false otherwise
     *
     * @param highlit True if the slot is highlit, false otherwise
     */
    fun setHighlight(highlit: Boolean)
    {
        this.highlight.isVisible = highlit
    }
}
