package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDPane
import com.davidm1a2.afraidofthedark.client.gui.layout.*
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.common.spell.Spell
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
class AOTDGuiSpellStage(private val spellStage: SpellStage, val spell: Spell) :
    AOTDPane(prefSize = RelativeDimensions(1.0, 0.2), margins = RelativeSpacing(0.0, 0.2, 0.0, 0.0)) {

    val deliveryMethod: SpellDeliveryMethodSlot
    val effects = Array(4) {
        SpellEffectSlot(RelativePosition(0.2 + it * 0.2, 0.125), SLOT_SIZE, spell, spell.spellStages.indexOf(spellStage), it)
    }

    init {
        // Set the background texture of the spell stage, save 14px for add and remove buttons
        val background = ImagePane("afraidofthedark:textures/gui/spell_editor/spell_stage_background.png")
        this.add(background)

        // Create the delivery method slot
        deliveryMethod = SpellDeliveryMethodSlot(RelativePosition(0.0, 0.125), SLOT_SIZE, spell, spell.spellStages.indexOf(spellStage))
        this.add(deliveryMethod)

        // Add a slot for each effect to the UI
        effects.forEach { this.add(it) }
    }

    fun refresh() {
        // Update the delivery method icon based on delivery method
        deliveryMethod.setSpellComponent(spellStage.deliveryInstance)
        // Update each effect slot
        for (i in spellStage.effects.indices) {
            effects[i].setSpellComponent(spellStage.effects[i])
        }
    }

    companion object {
        val SLOT_SIZE = RelativeDimensions(0.19, 0.75)
    }
}