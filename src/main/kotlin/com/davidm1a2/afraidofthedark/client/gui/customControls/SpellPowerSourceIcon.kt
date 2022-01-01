package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.dragAndDrop.DraggableProducer
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
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
        val icon = ImagePane(powerSource.icon, DispMode.FIT_TO_PARENT)
        icon.gravity = Gravity.CENTER
        icon.margins = Spacing(0.08)
        this.add(icon)

        this.hoverTexts = arrayOf(
            powerSource.getName().string,
            TranslationTextComponent("tooltip.afraidofthedark.gui.spell_crafting.cost_meaning", powerSource.getCostOverview()).string
        )
    }

    override fun produce(): SpellPowerSource {
        return powerSource
    }

    override fun getIcon(): ImagePane {
        val icon = ImagePane(powerSource.icon, DispMode.FIT_TO_PARENT)
        icon.prefSize = Dimensions(0.06, 0.06)
        return icon
    }
}