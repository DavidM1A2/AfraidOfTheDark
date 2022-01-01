package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.events.MouseEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.SpellStage

/**
 * Class representing the gui version of a spell stage
 */
class GuiSpellStage(private val spellStage: SpellStage, val spell: Spell, var componentEditCallback: ((SpellComponentSlot<*>) -> Unit)) :
    AOTDPane(prefSize = Dimensions(1.0, 0.2), margins = Spacing(0.0, 0.15, 0.0, 0.0)) {

    private val slotSize = Dimensions(0.17, 0.7)

    val deliveryMethod: SpellDeliveryMethodSlot
    val effects = Array(4) {
        SpellEffectSlot(Position(0.22 + it * 0.19, 0.15), slotSize, spell, spell.spellStages.indexOf(spellStage), it)
    }

    init {
        // Set the background texture of the spell stage, save 14px for add and remove buttons
        val background = ImagePane("afraidofthedark:textures/gui/spell_editor/spell_stage_background.png")
        this.add(background)

        // Create the delivery method slot
        deliveryMethod = SpellDeliveryMethodSlot(Position(0.04, 0.15), slotSize, spell, spell.spellStages.indexOf(spellStage))
        this.add(deliveryMethod)
        deliveryMethod.addMouseListener {
            if (it.eventType == MouseEvent.EventType.Click && it.clickedButton == MouseEvent.LEFT_MOUSE_BUTTON) {
                if (deliveryMethod.inBounds && deliveryMethod.isHovered && deliveryMethod.isVisible) {
                    componentEditCallback(deliveryMethod)
                }
            }
        }

        // Add a slot for each effect to the UI
        for (effect in effects) {
            this.add(effect)
            effect.addMouseListener {
                if (it.eventType == MouseEvent.EventType.Click && it.clickedButton == MouseEvent.LEFT_MOUSE_BUTTON) {
                    if (effect.inBounds && effect.isHovered && effect.isVisible) {
                        componentEditCallback(effect)
                    }
                }
            }
        }
    }

    fun refresh() {
        // Update the delivery method icon based on delivery method
        deliveryMethod.setSpellComponent(spellStage.deliveryInstance)
        // Update each effect slot
        for (i in spellStage.effects.indices) {
            effects[i].setSpellComponent(spellStage.effects[i])
        }
    }
}