package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.dragAndDrop.DraggableProducer
import com.davidm1a2.afraidofthedark.client.gui.layout.RelativeDimensions
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect

class SpellEffectIcon(val effect: SpellEffect) :
    ImagePane(effect.icon, DispMode.FIT_TO_PARENT),
    DraggableProducer<SpellEffect> {

    override fun produce(): SpellEffect {
        return effect
    }

    override fun getIcon(): ImagePane {
        val ret = ImagePane(effect.icon, DispMode.FIT_TO_PARENT)
        ret.prefSize = RelativeDimensions(0.1, 0.1)
        return ret
    }
}