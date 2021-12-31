package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.dragAndDrop.DraggableProducer
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSource
import net.minecraft.util.text.TranslationTextComponent

/**
 * The icon for a spell power source on the scroll panel
 */
class SpellPowerSourceIcon(private val powerSource: SpellPowerSource) :
    ImagePane("afraidofthedark:textures/gui/spell_editor/power_source_holder.png", DispMode.FIT_TO_PARENT),
    DraggableProducer<SpellPowerSource> {

    init {
        this.add(ImagePane(powerSource.icon, DispMode.STRETCH))
        this.hoverTexts = arrayOf(
            powerSource.getName().string,
            TranslationTextComponent("tooltip.afraidofthedark.gui.spell_crafting.cost_meaning", powerSource.getCostOverview()).string
        )
    }

    override fun produce(): SpellPowerSource {
        return powerSource
    }

    override fun getIcon(): ImagePane {
        val ret = ImagePane(powerSource.icon, DispMode.FIT_TO_PARENT)
        ret.prefSize = Dimensions(0.1, 0.1)
        return ret
    }
}