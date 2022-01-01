package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.dragAndDrop.DraggableConsumer
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.utility.round
import net.minecraft.util.text.TranslationTextComponent

/**
 * Class used to create a delivery method slot UI component
 */
class SpellDeliveryMethodSlot(offset: Position, prefSize: Dimensions, spell: Spell, private val stageIndex: Int) :
    SpellComponentSlot<SpellDeliveryMethod>("afraidofthedark:textures/gui/spell_editor/delivery_method_holder.png", offset, prefSize, spell), DraggableConsumer<SpellDeliveryMethod> {
    override fun updateSpell() {
        this.spell.spellStages[stageIndex].deliveryInstance = getComponentInstance()
    }

    override fun refreshHoverText() {
        // If the component type is non-null show the delivery method and stats, otherwise show the slot is empty
        val componentType = this.getComponentType()
        if (componentType != null) {
            val componentInstance = this.getComponentInstance()!!
            this.hoverTexts = arrayOf(
                TranslationTextComponent("tooltip.afraidofthedark.gui.spell_crafting.delivery_method", componentType.getName()).string,
                TranslationTextComponent("tooltip.afraidofthedark.gui.spell_crafting.cost_multiplier", componentType.getMultiplicity(componentInstance).round(1)).string,
                TranslationTextComponent("tooltip.afraidofthedark.gui.spell_crafting.cost", componentType.getDeliveryCost(componentInstance).round(1)).string
            )
        } else {
            this.setHoverText(TranslationTextComponent("tooltip.afraidofthedark.gui.spell_crafting.empty_slot", "delivery method").string)
        }
    }

    override fun consume(data: Any) {
        if (data is SpellDeliveryMethod) {
            val inst = SpellComponentInstance(data)
            inst.setDefaults()
            this.setSpellComponent(inst)
            this.spell.spellStages[stageIndex].deliveryInstance = inst
            this.refreshHoverText()
            invalidate()
        }
    }

    override fun calcChildrenBounds() {
        super.calcChildrenBounds()
        this.refreshHoverText() // Update hover text whenever the component is updated
    }
}
