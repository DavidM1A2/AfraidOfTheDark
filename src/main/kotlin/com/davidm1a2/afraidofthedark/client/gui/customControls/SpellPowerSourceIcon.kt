package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.dragAndDrop.DraggableProducer
import com.davidm1a2.afraidofthedark.client.gui.layout.RelativeDimensions
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSource

class SpellPowerSourceIcon(val powerSource: SpellPowerSource) :
    ImagePane(powerSource.icon, DispMode.FIT_TO_PARENT),
    DraggableProducer<SpellPowerSource> {

    override fun produce(): SpellPowerSource {
        return powerSource
    }

    override fun getIcon(): ImagePane {
        val ret = ImagePane(powerSource.icon, DispMode.FIT_TO_PARENT)
        ret.prefSize = RelativeDimensions(0.1, 0.1)
        return ret
    }
}