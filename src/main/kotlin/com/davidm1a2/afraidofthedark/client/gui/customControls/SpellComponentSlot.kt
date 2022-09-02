package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.events.MouseEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponent
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance

/**
 * Base class for all AOTD gui spell components
 */
abstract class SpellComponentSlot<T : SpellComponent<T>>(
    slotBackground: String,
    offset: Position = Position(0.0, 0.0),
    prefSize: Dimensions = Dimensions(1.0, 1.0),
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
        this.icon = ImagePane("afraidofthedark:textures/gui/spell_editor/blank_slot.png", ImagePane.DispMode.FIT_TO_PARENT)
        this.icon.isVisible = false
        this.icon.gravity = Gravity.CENTER
        this.icon.margins = Spacing(0.075)
        this.add(icon)

        this.addMouseListener {
            if (it.eventType == MouseEvent.EventType.Click && it.clickedButton == MouseEvent.RIGHT_MOUSE_BUTTON) {
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
        // If the component type is null hide the icon and set the instance to null too,
        //                       is non-null show the right icon
        if (instance == null) {
            this.icon.isVisible = false
            this.componentInstance = null
        } else {
            this.icon.isVisible = true
            this.componentInstance = instance
            this.icon.updateImageTexture(instance.component.icon)
        }
        updateSpell()
        scheduleFullRedraw()
    }

    internal abstract fun updateSpell()

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
