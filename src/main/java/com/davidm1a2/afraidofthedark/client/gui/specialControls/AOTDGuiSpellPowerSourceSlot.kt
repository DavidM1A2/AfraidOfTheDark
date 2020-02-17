package com.davidm1a2.afraidofthedark.client.gui.specialControls

import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSource
import net.minecraft.client.resources.I18n

/**
 * Class used to create a power source slot UI component
 *
 * @constructor Initializes the bounding box and spell power source
 * @param x                The X location of the top left corner
 * @param y                The Y location of the top left corner
 * @param width            The width of the component
 * @param height           The height of the component
 */
class AOTDGuiSpellPowerSourceSlot(x: Int, y: Int, width: Int, height: Int) :
        AOTDGuiSpellComponentSlot<SpellPowerSource>(x, y, width, height, "afraidofthedark:textures/gui/spell_editor/power_source_holder.png")
{
    /**
     * Refreshes the text that gets displayed when the slot is hovered
     */
    override fun refreshHoverText()
    {
        // If the component type is non-null show the power source and stats, otherwise show the slot is empty
        val componentType = this.getComponentType()
        if (componentType != null)
        {
            this.hoverTexts = arrayOf(
                    "Power Source (${I18n.format(componentType.getUnlocalizedName())})",
                    "Cost Meaning: ${componentType.getCostDescription()}"
            )
        }
        else
        {
            this.setHoverText("Empty power source slot")
        }
    }
}
