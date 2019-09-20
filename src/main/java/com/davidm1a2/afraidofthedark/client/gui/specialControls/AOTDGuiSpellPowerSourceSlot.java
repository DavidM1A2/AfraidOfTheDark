package com.davidm1a2.afraidofthedark.client.gui.specialControls;

import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSource;
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSourceEntry;
import net.minecraft.client.resources.I18n;

/**
 * Class used to create a power source slot UI component
 */
public class AOTDGuiSpellPowerSourceSlot extends AOTDGuiSpellComponentSlot<SpellPowerSourceEntry, SpellPowerSource>
{
    /**
     * Constructor initializes the bounding box and spell power source
     *
     * @param x                The X location of the top left corner
     * @param y                The Y location of the top left corner
     * @param width            The width of the component
     * @param height           The height of the component
     * @param powerSourceEntry The power source that powers this spell slot
     */
    public AOTDGuiSpellPowerSourceSlot(Integer x, Integer y, Integer width, Integer height, SpellPowerSourceEntry powerSourceEntry)
    {
        super(x, y, width, height, "afraidofthedark:textures/gui/spell_editor/power_source_holder.png", powerSourceEntry);
    }

    /**
     * Refreshes the text that gets displayed when the slot is hovered
     */
    @Override
    void refreshHoverText()
    {
        // If the component type is non-null show the power source and stats, otherwise show the slot is empty
        if (this.getComponentType() != null)
        {
            this.setHoverTexts("Power Source (" + I18n.format(this.getComponentType().getUnlocalizedName()) + ")", "Cost Meaning: " + this.getComponentInstance().getCostDescription());
        }
        else
        {
            this.setHoverText("Empty power source slot");
        }
    }
}
