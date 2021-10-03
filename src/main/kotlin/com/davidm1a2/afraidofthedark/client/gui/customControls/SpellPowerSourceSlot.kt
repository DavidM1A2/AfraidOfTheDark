package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.dragAndDrop.DraggableConsumer
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSource

/**
 * Class used to create a power source slot UI component
 */
class SpellPowerSourceSlot(offset: Position, prefSize: Dimensions, spell: Spell) :
    SpellComponentSlot<SpellPowerSource>("afraidofthedark:textures/gui/spell_editor/power_source_holder.png", offset, prefSize, spell),
    DraggableConsumer<SpellPowerSource> {

    /**
     * Refreshes the text that gets displayed when the slot is hovered
     */
    override fun refreshHoverText() {
        // If the component type is non-null show the power source and stats, otherwise show the slot is empty
        val componentType = this.getComponentType()
        if (componentType != null) {
            this.hoverTexts = arrayOf(
                "Power Source (${componentType.getName().string})",
                "Cost Meaning: ${componentType.getCostDescription().string}"
            )
        } else {
            this.setHoverText("Empty power source slot")
        }
    }

    /**
     * Implements drag and drop capabilities
     */
    override fun consume(data: Any) {
        if (data is SpellPowerSource) {
            val inst = SpellComponentInstance(data)
            inst.setDefaults()
            this.setSpellComponent(inst)
            this.spell.powerSource = inst
            this.refreshHoverText()
        }
    }

    override fun calcChildrenBounds() {
        super.calcChildrenBounds()
        this.refreshHoverText() // Update hover text whenever the component is updated
    }
}
