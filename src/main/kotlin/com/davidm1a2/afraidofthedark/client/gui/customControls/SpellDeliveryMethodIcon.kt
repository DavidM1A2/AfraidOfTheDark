package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.dragAndDrop.DraggableProducer
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.RelativeDimensions
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod

class SpellDeliveryMethodIcon(val deliveryMethod: SpellDeliveryMethod) :
    ImagePane(deliveryMethod.icon, DispMode.FIT_TO_PARENT),
    DraggableProducer<SpellDeliveryMethod> {

    override fun produce(): SpellDeliveryMethod {
        return deliveryMethod
    }

    override fun getIcon(): ImagePane {
        val ret = ImagePane(deliveryMethod.icon, DispMode.FIT_TO_PARENT)
        ret.prefSize = RelativeDimensions(0.1, 0.1)
        return ret
    }
}