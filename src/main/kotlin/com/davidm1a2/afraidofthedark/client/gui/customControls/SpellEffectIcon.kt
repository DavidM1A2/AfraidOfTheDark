package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffectInstance
import com.davidm1a2.afraidofthedark.common.utility.round
import net.minecraft.util.text.TranslationTextComponent

/**
 * The icon for a spell effect on the scroll panel
 */
class SpellEffectIcon(effect: SpellEffect) : SpellComponentIcon<SpellEffect>(effect, "effect_holder") {
    init {
        val componentInstance = SpellEffectInstance(effect)
        componentInstance.setDefaults()
        this.hoverTexts = arrayOf(
            effect.getName().string,
            TranslationTextComponent("tooltip.afraidofthedark.gui.spell_crafting.cost", effect.getCost(componentInstance).round(1)).string
        )
    }
}