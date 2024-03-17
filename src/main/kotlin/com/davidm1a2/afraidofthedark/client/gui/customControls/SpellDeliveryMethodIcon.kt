package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethodInstance
import com.davidm1a2.afraidofthedark.common.utility.round
import net.minecraft.network.chat.TranslatableComponent

/**
 * The icon for a spell delivery method on the scroll panel
 */
class SpellDeliveryMethodIcon(deliveryMethod: SpellDeliveryMethod) : SpellComponentIcon<SpellDeliveryMethod>(deliveryMethod, "delivery_method_holder") {
    init {
        // Show the delivery method and stats
        val componentInstance = SpellDeliveryMethodInstance(deliveryMethod)
        componentInstance.setDefaults()
        this.hoverTexts = arrayOf(
            deliveryMethod.getName().string,
            TranslatableComponent("tooltip.afraidofthedark.gui.spell_crafting.cost_multiplier", deliveryMethod.getMultiplicity(componentInstance).round(1)).string,
            TranslatableComponent("tooltip.afraidofthedark.gui.spell_crafting.cost", deliveryMethod.getDeliveryCost(componentInstance).round(1)).string
        )
    }
}