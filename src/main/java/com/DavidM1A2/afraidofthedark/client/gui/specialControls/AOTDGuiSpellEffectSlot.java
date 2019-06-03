package com.DavidM1A2.afraidofthedark.client.gui.specialControls;

import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffect;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import net.minecraft.client.resources.I18n;

/**
 * Class used to create an effect slot UI component
 */
public class AOTDGuiSpellEffectSlot extends AOTDGuiSpellComponentSlot<SpellEffectEntry, SpellEffect>
{
    /**
     * Constructor initializes the bounding box and spell delivery method
     *
     * @param x           The X location of the top left corner
     * @param y           The Y location of the top left corner
     * @param width       The width of the component
     * @param height      The height of the component
     * @param effectEntry The effect that is in this spell slot
     */
    public AOTDGuiSpellEffectSlot(Integer x, Integer y, Integer width, Integer height, SpellEffectEntry effectEntry)
    {
        super(x, y, width, height, "afraidofthedark:textures/gui/spell_editor/effect_holder.png", effectEntry);
    }

    /**
     * Refreshes the text that gets displayed when the slot is hovered
     */
    @Override
    void refreshHoverText()
    {
        // If the component type is non-null show the effect method and stats, otherwise show the slot is empty
        if (this.getComponentType() != null)
        {
            this.setHoverTexts("Effect (" + I18n.format(this.getComponentType().getRegistryName().toString()) + ")", "Cost: " + this.getComponentInstance().getCost());
        }
        else
        {
            this.setHoverText("Empty effect slot");
        }
    }
}
