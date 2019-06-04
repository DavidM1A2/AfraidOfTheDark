package com.DavidM1A2.afraidofthedark.client.gui.specialControls;

import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethodEntry;
import net.minecraft.client.resources.I18n;

/**
 * Class used to create a delivery method slot UI component
 */
public class AOTDGuiSpellDeliveryMethodSlot extends AOTDGuiSpellComponentSlot<SpellDeliveryMethodEntry, SpellDeliveryMethod>
{
    /**
     * Constructor initializes the bounding box and spell delivery method
     *
     * @param x                   The X location of the top left corner
     * @param y                   The Y location of the top left corner
     * @param width               The width of the component
     * @param height              The height of the component
     * @param deliveryMethodEntry The delivery method that is in this spell slot
     */
    public AOTDGuiSpellDeliveryMethodSlot(Integer x, Integer y, Integer width, Integer height, SpellDeliveryMethodEntry deliveryMethodEntry)
    {
        super(x, y, width, height, "afraidofthedark:textures/gui/spell_editor/delivery_method_holder.png", deliveryMethodEntry);
    }

    /**
     * Refreshes the text that gets displayed when the slot is hovered
     */
    @Override
    void refreshHoverText()
    {
        // If the component type is non-null show the delivery method and stats, otherwise show the slot is empty
        if (this.getComponentType() != null)
        {
            this.setHoverTexts("Delivery Method (" + I18n.format(this.getComponentType().getUnlocalizedName()) + ")", "Cost Multiplier: " + this.getComponentInstance().getStageCostMultiplier(), "Cost: " + this.getComponentInstance().getCost());
        }
        else
        {
            this.setHoverText("Empty delivery method slot");
        }
    }
}
