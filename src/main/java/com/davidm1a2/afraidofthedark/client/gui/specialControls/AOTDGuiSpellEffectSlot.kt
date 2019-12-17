package com.davidm1a2.afraidofthedark.client.gui.specialControls

import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import net.minecraft.client.resources.I18n

/**
 * Class used to create an effect slot UI component
 *
 * @constructor Initializes the bounding box and spell delivery method
 * @param x           The X location of the top left corner
 * @param y           The Y location of the top left corner
 * @param width       The width of the component
 * @param height      The height of the component
 * @param effect The effect that is in this spell slot
 */
class AOTDGuiSpellEffectSlot(x: Int, y: Int, width: Int, height: Int, effect: SpellEffect?) :
    AOTDGuiSpellComponentSlot<SpellEffect>(x, y, width, height, "afraidofthedark:textures/gui/spell_editor/effect_holder.png", effect)
{
    /**
     * Refreshes the text that gets displayed when the slot is hovered
     */
    override fun refreshHoverText()
    {
        // If the component type is non-null show the effect method and stats, otherwise show the slot is empty
        val componentType = this.getComponentType()
        if (componentType != null)
        {
            val componentInstance = this.getComponentInstance()!!
            this.hoverTexts = arrayOf(
                "Effect (${I18n.format(componentType.getUnlocalizedName())})",
                "Cost: ${componentType.getCost(componentInstance)}"
            )
        }
        else
        {
            this.setHoverText("Empty effect slot")
        }
    }
}
