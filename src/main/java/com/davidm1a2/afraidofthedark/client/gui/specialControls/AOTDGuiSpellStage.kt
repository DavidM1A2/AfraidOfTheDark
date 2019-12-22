package com.davidm1a2.afraidofthedark.client.gui.specialControls

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiContainer
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiButton
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiImage
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.spell.SpellStage

/**
 * Class representing the gui version of a spell stage
 *
 * @param x      The X location of the top left corner
 * @param y      The Y location of the top left corner
 * @param width  The width of the component
 * @param height The height of the component
 * @param spellStage The spell stage that this gui represents
 * @property addNewRow Button for adding new spell stages
 * @property removeRow Button for removing spell stages
 * @property deliveryMethod The delivery method of this spell stage
 * @property effects The 4 effects of this spell stage
 * @property addRunnable to be fired when add is called
 * @property removeRunnable to be fired when remove is called
 */
class AOTDGuiSpellStage(
    x: Int, y: Int, width: Int, height: Int, private val spellStage: SpellStage
) : AOTDGuiContainer(x, y, width, height)
{
    private val addNewRow: AOTDGuiButton
    private val removeRow: AOTDGuiButton
    val deliveryMethod: AOTDGuiSpellDeliveryMethodSlot
    val effects = Array(4)
    {
        // Place the slot at an offset based on the spacing
        AOTDGuiSpellEffectSlot(5 + SLOT_SPACING * (it + 1), 5, height - 25, height - 25)
    }
    var addRunnable: (() -> Unit)? = null
    var removeRunnable: (() -> Unit)? = null

    init
    {
        // Set the background texture of the spell stage, save 14px for add and remove buttons
        val background = AOTDGuiImage(0, 0, width, height - 14, "afraidofthedark:textures/gui/spell_editor/spell_stage_background.png")
        add(background)

        // Create the delivery method slot
        deliveryMethod = AOTDGuiSpellDeliveryMethodSlot(5, 5, height - 25, height - 25)
        add(deliveryMethod)

        // Add a slot for each effect to the UI
        effects.forEach { add(it) }

        // Create two buttons, one to add a new row and one to remove the current row
        addNewRow =
            AOTDGuiButton(0, height - 15, 15, 15, "afraidofthedark:textures/gui/spell_editor/add.png", "afraidofthedark:textures/gui/spell_editor/add_hovered.png")
        addNewRow.setHoverText("Add new spell stage")
        addNewRow.addMouseListener()
        {
            if (it.eventType === AOTDMouseEvent.EventType.Press)
            {
                // If we press the button and it's visible/hovered run the callback
                if (addNewRow.isHovered && addNewRow.isVisible && it.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON)
                {
                    addRunnable?.invoke()
                }
            }
        }
        addNewRow.addMouseMoveListener()
        {
            if (it.eventType === AOTDMouseMoveEvent.EventType.Enter)
            {
                // If the button is hovered and visible play the button hover sound
                if (addNewRow.isHovered && addNewRow.isVisible)
                {
                    entityPlayer.playSound(ModSounds.SPELL_CRAFTING_BUTTON_HOVER, 0.6f, 1.7f)
                }
            }
        }
        add(addNewRow)

        removeRow = AOTDGuiButton(
            15,
            height - 15,
            15,
            15,
            "afraidofthedark:textures/gui/spell_editor/delete.png",
            "afraidofthedark:textures/gui/spell_editor/delete_hovered.png"
        )
        removeRow.setHoverText("Remove spell stage")
        removeRow.addMouseListener()
        {
            if (it.eventType === AOTDMouseEvent.EventType.Press)
            {
                // If we press the button and it's visible/hovered run the callback
                if (removeRow.isHovered && removeRow.isVisible && it.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON)
                {
                    removeRunnable?.invoke()
                }
            }
        }
        removeRow.addMouseMoveListener()
        {
            if (it.eventType === AOTDMouseMoveEvent.EventType.Enter)
            {
                // If the button is hovered and visible play the button hover sound
                if (removeRow.isHovered && removeRow.isVisible)
                {
                    entityPlayer.playSound(ModSounds.SPELL_CRAFTING_BUTTON_HOVER, 0.6f, 1.7f)
                }
            }
        }
        add(removeRow)
    }

    /**
     * Shows the + button
     */
    fun showPlus()
    {
        addNewRow.isVisible = true
    }

    /**
     * Hides the + button
     */
    fun hidePlus()
    {
        addNewRow.isVisible = false
    }

    /**
     * Shows the - button
     */
    fun showMinus()
    {
        removeRow.isVisible = true
    }

    /**
     * Hides the - button
     */
    fun hideMinus()
    {
        removeRow.isVisible = false
    }

    /**
     * Updates this spell stage's slots based on the spell stage.
     */
    fun refresh()
    {
        // Update the delivery method icon based on delivery method
        deliveryMethod.setSpellComponent(spellStage.deliveryInstance)
        // Update each effect slot
        for (i in spellStage.effects.indices)
        {
            effects[i].setSpellComponent(spellStage.effects[i])
        }
    }

    companion object
    {
        // The amount of space between effect/delivery slots
        private const val SLOT_SPACING = 20
    }
}