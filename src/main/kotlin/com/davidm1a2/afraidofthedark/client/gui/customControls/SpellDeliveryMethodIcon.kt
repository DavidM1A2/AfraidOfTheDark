package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.dragAndDrop.DraggableProducer
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import net.minecraft.util.text.TranslationTextComponent

/**
 * The icon for a spell delivery method on the scroll panel
 */
class SpellDeliveryMethodIcon(private val deliveryMethod: SpellDeliveryMethod) :
    ImagePane("afraidofthedark:textures/gui/spell_editor/delivery_method_holder.png", DispMode.FIT_TO_PARENT),
    DraggableProducer<SpellDeliveryMethod> {

    init {
        val icon = ImagePane(deliveryMethod.icon, DispMode.FIT_TO_PARENT)
        icon.gravity = Gravity.CENTER
        icon.margins = Spacing(0.08)
        this.add(icon)

        // Show the delivery method and stats
        val componentInstance = SpellComponentInstance(deliveryMethod)
        componentInstance.setDefaults()
        this.hoverTexts = arrayOf(
            deliveryMethod.getName().string,
            TranslationTextComponent("tooltip.afraidofthedark.gui.spell_crafting.cost_multiplier", deliveryMethod.getMultiplicity(componentInstance)).string,
            TranslationTextComponent("tooltip.afraidofthedark.gui.spell_crafting.cost", deliveryMethod.getDeliveryCost(componentInstance)).string
        )
    }

    override fun produce(): SpellDeliveryMethod {
        return deliveryMethod
    }

    override fun getIcon(): ImagePane {
        val icon = ImagePane(deliveryMethod.icon, DispMode.FIT_TO_PARENT)
        icon.prefSize = Dimensions(0.06, 0.06)
        return icon
    }
}