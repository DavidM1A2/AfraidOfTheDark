package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.utility.round
import net.minecraft.util.text.TranslationTextComponent

/**
 * The icon for a spell delivery method on the scroll panel
 */
class SpellDeliveryMethodIcon(deliveryMethod: SpellDeliveryMethod) : SpellComponentIcon<SpellDeliveryMethod>(deliveryMethod, "delivery_method_holder") {
    init {
        // Show the delivery method and stats
        val componentInstance = SpellComponentInstance(deliveryMethod)
        componentInstance.setDefaults()
        this.hoverTexts = arrayOf(
            deliveryMethod.getName().string,
            TranslationTextComponent("tooltip.afraidofthedark.gui.spell_crafting.cost_multiplier", deliveryMethod.getMultiplicity(componentInstance).round(1)).string,
            TranslationTextComponent("tooltip.afraidofthedark.gui.spell_crafting.cost", deliveryMethod.getDeliveryCost(componentInstance).round(1)).string
        )
    }
}