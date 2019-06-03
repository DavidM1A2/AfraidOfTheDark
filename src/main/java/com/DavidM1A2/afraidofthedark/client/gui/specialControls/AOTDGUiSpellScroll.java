package com.DavidM1A2.afraidofthedark.client.gui.specialControls;

import com.DavidM1A2.afraidofthedark.client.gui.base.AOTDGuiContainer;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.*;
import com.DavidM1A2.afraidofthedark.client.settings.ClientData;
import com.DavidM1A2.afraidofthedark.common.constants.ModRegistries;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethodEntry;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import com.DavidM1A2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSourceEntry;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.util.Color;


/**
 * Compliment control to the tablet, allows players to click spell components up
 */
public class AOTDGUiSpellScroll extends AOTDGuiContainer
{
    /**
     * Constructor initializes the bounding box
     *
     * @param x      The X location of the top left corner
     * @param y      The Y location of the top left corner
     * @param width  The width of the component
     * @param height The height of the component
     */
    public AOTDGUiSpellScroll(Integer x, Integer y, Integer width, Integer height)
    {
        super(x, y, width, height);

        // Create the base panel to attach all of our components to
        AOTDGuiPanel scroll = new AOTDGuiPanel(0, 0, width, height, false);

        // Add the background scroll texture image
        AOTDGuiImage backgroundScroll = new AOTDGuiImage(0, 0, width - 20, height, "afraidofthedark:textures/gui/spell_editor/effect_list_scroll.png");
        scroll.add(backgroundScroll);

        // Add a scroll bar to the right of the scroll
        AOTDGuiScrollBar componentScrollBar = new AOTDGuiScrollBar(backgroundScroll.getWidth(), 50, 13, height - 100);
        scroll.add(componentScrollBar);

        // Add a scroll panel to the scroll
        AOTDGuiScrollPanel componentScrollPanel = new AOTDGuiScrollPanel(40, 50, 120, 175, true, componentScrollBar);
        scroll.add(componentScrollPanel);

        // The current component index we're adding to the scroll
        final int COMPONENTS_PER_LINE = 5;
        int currentComponent = 0;

        // Create the power source label
        AOTDGuiLabel powerSourceHeading = new AOTDGuiLabel(5 + 24 * (currentComponent % 5), 5 + 24 * (currentComponent / 5), 120, 20, ClientData.getInstance().getTargaMSHandFontSized(46f));
        powerSourceHeading.setTextColor(new Color(140, 35, 206));
        powerSourceHeading.setText("Power Sources");
        componentScrollPanel.add(powerSourceHeading);
        currentComponent = currentComponent + COMPONENTS_PER_LINE;

        // Go over all power sources and add a slot for each
        for (SpellPowerSourceEntry powerSourceEntry : ModRegistries.SPELL_POWER_SOURCES)
        {
            AOTDGuiSpellPowerSourceSlot powerSource = new AOTDGuiSpellPowerSourceSlot(5 + 24 * (currentComponent % 5), 5 + 24 * (currentComponent / 5), 20, 20, powerSourceEntry);
            componentScrollPanel.add(powerSource);
            currentComponent = currentComponent + 1;
        }

        // Round the current component to a multiple of COMPONENTS_PER_LINE
        currentComponent = MathHelper.roundUp(currentComponent, COMPONENTS_PER_LINE);

        // Create the effect label
        AOTDGuiLabel effectHeading = new AOTDGuiLabel(5 + 24 * (currentComponent % 5), 5 + 24 * (currentComponent / 5), 120, 20, ClientData.getInstance().getTargaMSHandFontSized(46f));
        effectHeading.setTextColor(new Color(140, 35, 206));
        effectHeading.setText("Effects");
        componentScrollPanel.add(effectHeading);
        currentComponent = currentComponent + COMPONENTS_PER_LINE;

        // Go over all effects and add a slot for each
        for (SpellEffectEntry effectEntry : ModRegistries.SPELL_EFFECTS)
        {
            AOTDGuiSpellEffectSlot effect = new AOTDGuiSpellEffectSlot(5 + 24 * (currentComponent % 5), 5 + 24 * (currentComponent / 5), 20, 20, effectEntry);
            componentScrollPanel.add(effect);
            currentComponent = currentComponent + 1;
        }

        // Round the current component to a multiple of COMPONENTS_PER_LINE
        currentComponent = MathHelper.roundUp(currentComponent, COMPONENTS_PER_LINE);

        // Create the delivery method label
        AOTDGuiLabel deliveryMethodHeading = new AOTDGuiLabel(5 + 24 * (currentComponent % 5), 5 + 24 * (currentComponent / 5), 120, 20, ClientData.getInstance().getTargaMSHandFontSized(46f));
        deliveryMethodHeading.setTextColor(new Color(140, 35, 206));
        deliveryMethodHeading.setText("Delivery Methods");
        componentScrollPanel.add(deliveryMethodHeading);
        currentComponent = currentComponent + COMPONENTS_PER_LINE;

        // Go over all delivery methods and add a slot for each
        for (SpellDeliveryMethodEntry deliveryMethodEntry : ModRegistries.SPELL_DELIVERY_METHODS)
        {
            AOTDGuiSpellDeliveryMethodSlot deliveryMethod = new AOTDGuiSpellDeliveryMethodSlot(5 + 24 * (currentComponent % 5), 5 + 24 * (currentComponent / 5), 20, 20, deliveryMethodEntry);
            componentScrollPanel.add(deliveryMethod);
            currentComponent = currentComponent + 1;
        }

        // Update the scroll offset accordingly
        int offset = currentComponent / 6 - 5;
        offset = offset > 0 ? offset : 0;
        componentScrollPanel.setMaximumOffset(offset * 30);

        // Add the scroll to the component
        this.add(scroll);
    }
}
