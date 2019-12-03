package com.davidm1a2.afraidofthedark.client.gui.specialControls

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiContainer
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiImage
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponent
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentEntry

/**
 * Base class for all AOTD gui spell components
 *
 * @constructor Initializes the bounding box
 * @param x      The X location of the top left corner
 * @param y      The Y location of the top left corner
 * @param width  The width of the component
 * @param height The height of the component
 */
abstract class AOTDGuiSpellComponentSlot<T : SpellComponentEntry<T, V>, V : SpellComponent>(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        slotBackground: String,
        componentInstance: T?
) : AOTDGuiContainer(x, y, width, height)
{
    // The foreground image of the spell slot
    private val icon: AOTDGuiImage
    // The highlight effect around the spell slot
    private val highlight: AOTDGuiImage
    // The type that this spell slot is
    private var componentType: T? = null
    // An instance of the component type
    private var componentInstance: V? = null

    init
    {
        // The background image of the spell slot
        val background = AOTDGuiImage(0, 0, width, height, slotBackground)
        this.add(background)

        // Add a highlight effect image over the background
        this.highlight = AOTDGuiImage(0, 0, width, height, "afraidofthedark:textures/gui/spell_editor/highlight.png")
        this.highlight.isVisible = false
        this.add(highlight)

        // Set the icon to be blank
        this.icon = AOTDGuiImage(0, 0, width, height, "afraidofthedark:textures/gui/spell_editor/blank_slot.png")
        this.add(icon)

        this.setComponentType(componentInstance)
    }

    /**
     * Sets the component type and icon of this slot from a type
     *
     * @param componentType The component type to use
     */
    fun setComponentType(componentType: T?)
    {
        this.componentType = componentType
        // If the component type is null hide the icon and set the instance to null too
        if (this.componentType == null)
        {
            this.icon.isVisible = false
            this.componentInstance = null
        }
        // If the component type is non-null show the the right icon and create a new instance of the type
        else
        {
            this.icon.imageTexture = this.componentType!!.icon
            this.icon.isVisible = true
            this.componentInstance = this.componentType!!.newInstance()
        }
        // Update the hover text based on the slot
        this.refreshHoverText()
    }

    /**
     * Sets the component instance and icon of this slot from an instance
     *
     * @param componentInstance The component instance to use
     */
    fun setComponentInstance(componentInstance: V?)
    {
        this.componentInstance = componentInstance
        // If the instance is null hide the icon and set the type to null too
        if (this.componentInstance == null)
        {
            this.icon.isVisible = false
            this.componentType = null
        }
        // If the instance is non-null set the component type and icon
        else
        {
            this.componentType = this.componentInstance!!.getEntryRegistryType<V, T>()
            this.icon.imageTexture = componentType!!.icon
            this.icon.isVisible = true
        }
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
        return componentType
    }

    /**
     * @return The instance of this component type
     */
    fun getComponentInstance(): V?
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
