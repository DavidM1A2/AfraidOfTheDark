package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.dragAndDrop.DraggableProducer
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.RelativeDimensions
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import net.minecraft.client.resources.I18n

class SpellDeliveryMethodIcon(val deliveryMethod: SpellDeliveryMethod) :
    ImagePane("afraidofthedark:textures/gui/spell_editor/delivery_method_holder.png", DispMode.FIT_TO_PARENT),
    DraggableProducer<SpellDeliveryMethod> {

    init {
        this.add(ImagePane(deliveryMethod.icon, DispMode.STRETCH))
        // Show the delivery method and stats
        val componentInstance = SpellComponentInstance(deliveryMethod)
        componentInstance.setDefaults()
        this.hoverTexts = arrayOf(
            "Delivery Method (${I18n.format(deliveryMethod.getUnlocalizedName())})",
            "Cost Multiplier: %.1f".format(deliveryMethod.getStageCostMultiplier(componentInstance)),
            "Cost: %.1f".format(deliveryMethod.getCost(componentInstance))
        )
    }

    override fun produce(): SpellDeliveryMethod {
        return deliveryMethod
    }

    override fun getIcon(): ImagePane {
        val ret = ImagePane(deliveryMethod.icon, DispMode.FIT_TO_PARENT)
        ret.prefSize = RelativeDimensions(0.1, 0.1)
        return ret
    }
}