package com.davidm1a2.afraidofthedark.client.gui.specialControls

import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry
import net.minecraft.client.resources.I18n

/**
 * Class used to create an effect slot UI component
 *
 * @constructor Initializes the bounding box and spell delivery method
 * @param x           The X location of the top left corner
 * @param y           The Y location of the top left corner
 * @param width       The width of the component
 * @param height      The height of the component
 * @param effectEntry The effect that is in this spell slot
 */
class AOTDGuiSpellEffectSlot(x: Int, y: Int, width: Int, height: Int, effectEntry: SpellEffectEntry?) :
    AOTDGuiSpellComponentSlot<SpellEffectEntry, SpellEffect>(x, y, width, height, "afraidofthedark:textures/gui/spell_editor/effect_holder.png", effectEntry)
{
    /**
     * Refreshes the text that gets displayed when the slot is hovered
     */
    override fun refreshHoverText()
    {
        // If the component type is non-null show the effect method and stats, otherwise show the slot is empty
        if (this.getComponentType() != null)
        {
            this.hoverTexts = arrayOf("Effect (${I18n.format(this.getComponentType()!!.unlocalizedName)})", "Cost: ${this.getComponentInstance()!!.cost}")
        }
        else
        {
            this.hoverText = "Empty effect slot"
        }
    }
}
