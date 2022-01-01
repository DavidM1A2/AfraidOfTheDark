package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSource
import net.minecraft.util.text.TranslationTextComponent

/**
 * The icon for a spell power source on the scroll panel
 */
class SpellPowerSourceIcon(powerSource: SpellPowerSource) : SpellComponentIcon<SpellPowerSource>(powerSource, "power_source_holder") {
    init {
        this.hoverTexts = arrayOf(
            powerSource.getName().string,
            TranslationTextComponent("tooltip.afraidofthedark.gui.spell_crafting.cost_meaning", powerSource.getCostOverview()).string
        )
    }
}