package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.dragAndDrop.DraggableConsumer
import com.davidm1a2.afraidofthedark.client.gui.events.MouseEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import net.minecraft.client.resources.I18n

/**
 * Class used to create a delivery method slot UI component
 */
class SpellDeliveryMethodSlot(offset: Position, prefSize: Dimensions, spell: Spell, val stageIndex: Int) :
    SpellComponentSlot<SpellDeliveryMethod>("afraidofthedark:textures/gui/spell_editor/delivery_method_holder.png", offset, prefSize, spell),
    DraggableConsumer<SpellDeliveryMethod> {

    init {
        this.addMouseListener {
            if (it.eventType == MouseEvent.EventType.Click && it.clickedButton == MouseEvent.RIGHT_MOUSE_BUTTON) {
                if (this.isHovered && this.inBounds && this.isVisible) {
                    this.spell.spellStages[stageIndex].deliveryInstance = null
                }
            }
        }
    }

    override fun refreshHoverText() {
        // If the component type is non-null show the delivery method and stats, otherwise show the slot is empty
        val componentType = this.getComponentType()
        if (componentType != null) {
            val componentInstance = this.getComponentInstance()!!
            this.hoverTexts = arrayOf(
                "Delivery Method (${I18n.get(componentType.getUnlocalizedName())})",
                "Cost Multiplier: %.1f".format(componentType.getStageCostMultiplier(componentInstance)),
                "Cost: %.1f".format(componentType.getCost(componentInstance))
            )
        } else {
            this.setHoverText("Empty delivery method slot")
        }
    }

    override fun consume(data: Any) {
        if (data is SpellDeliveryMethod) {
            val inst = SpellComponentInstance(data)
            inst.setDefaults()
            this.setSpellComponent(inst)
            this.spell.spellStages[stageIndex].deliveryInstance = inst
            this.refreshHoverText()
        }
    }

    override fun calcChildrenBounds() {
        super.calcChildrenBounds()
        this.refreshHoverText() // Update hover text whenever the component is updated
    }
}
