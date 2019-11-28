package com.davidm1a2.afraidofthedark.client.gui.specialControls

import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethodEntry
import net.minecraft.client.resources.I18n

/**
 * Class used to create a delivery method slot UI component
 *
 * @constructor Initializes the bounding box and spell delivery method
 * @param x                   The X location of the top left corner
 * @param y                   The Y location of the top left corner
 * @param width               The width of the component
 * @param height              The height of the component
 * @param deliveryMethodEntry The delivery method that is in this spell slot
 */
class AOTDGuiSpellDeliveryMethodSlot(x: Int, y: Int, width: Int, height: Int, deliveryMethodEntry: SpellDeliveryMethodEntry?) :
    AOTDGuiSpellComponentSlot<SpellDeliveryMethodEntry, SpellDeliveryMethod>(
        x,
        y,
        width,
        height,
        "afraidofthedark:textures/gui/spell_editor/delivery_method_holder.png",
        deliveryMethodEntry
    )
{
    /**
     * Refreshes the text that gets displayed when the slot is hovered
     */
    override fun refreshHoverText()
    {
        // If the component type is non-null show the delivery method and stats, otherwise show the slot is empty
        if (this.getComponentType() != null)
        {
            this.hoverTexts = arrayOf(
                "Delivery Method (${I18n.format(this.getComponentType()!!.unlocalizedName)})",
                "Cost Multiplier: ${this.getComponentInstance()!!.stageCostMultiplier}",
                "Cost: ${this.getComponentInstance()!!.cost}"
            )
        }
        else
        {
            this.hoverText = "Empty delivery method slot"
        }
    }
}
