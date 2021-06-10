package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDPane
import com.davidm1a2.afraidofthedark.client.gui.events.MouseEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.*
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.SpellStage

/**
 * Class representing the gui version of a spell stage
 */
class GuiSpellStage(private val spellStage: SpellStage, val spell: Spell, var componentEditCallback: ((SpellComponentSlot<*>) -> Unit)?) :
    AOTDPane(prefSize = RelativeDimensions(1.0, 0.2), margins = RelativeSpacing(0.0, 0.15, 0.0, 0.0)) {

    private val slotSize = RelativeDimensions(0.16, 0.65)

    val deliveryMethod: SpellDeliveryMethodSlot
    val effects = Array(4) {
        SpellEffectSlot(RelativePosition(0.25 + it * 0.18, 0.175), slotSize, spell, spell.spellStages.indexOf(spellStage), it)
    }

    init {
        // Set the background texture of the spell stage, save 14px for add and remove buttons
        val background = ImagePane("afraidofthedark:textures/gui/spell_editor/spell_stage_background.png")
        this.add(background)

        // Create the delivery method slot
        deliveryMethod = SpellDeliveryMethodSlot(RelativePosition(0.06, 0.175), slotSize, spell, spell.spellStages.indexOf(spellStage))
        this.add(deliveryMethod)
        deliveryMethod.addMouseListener {
            if (it.eventType == MouseEvent.EventType.Click && it.clickedButton == MouseEvent.LEFT_MOUSE_BUTTON) {
                if (deliveryMethod.inBounds && deliveryMethod.isHovered && deliveryMethod.isVisible) {
                    componentEditCallback?.invoke(deliveryMethod)
                }
            }
        }

        // Add a slot for each effect to the UI
        for (effect in effects) {
            this.add(effect)
            effect.addMouseListener {
                if (it.eventType == MouseEvent.EventType.Click && it.clickedButton == MouseEvent.LEFT_MOUSE_BUTTON) {
                    if (effect.inBounds && effect.isHovered && effect.isVisible) {
                        componentEditCallback?.invoke(effect)
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