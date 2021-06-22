package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.dragAndDrop.DraggableProducer
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import net.minecraft.client.resources.I18n

/**
 * The icon for a spell effect on the scroll panel
 */
class SpellEffectIcon(val effect: SpellEffect) :
    ImagePane("afraidofthedark:textures/gui/spell_editor/effect_holder.png", DispMode.FIT_TO_PARENT),
    DraggableProducer<SpellEffect> {

    init {
        this.add(ImagePane(effect.icon, DispMode.STRETCH))
        val componentInstance = SpellComponentInstance(effect)
        componentInstance.setDefaults()
        this.hoverTexts = arrayOf(
            I18n.format(effect.getUnlocalizedName()),
            "Cost: %.1f".format(effect.getCost(componentInstance))
        )
    }

    override fun produce(): SpellEffect {
        return effect
    }

    override fun getIcon(): ImagePane {
        val ret = ImagePane(effect.icon, DispMode.FIT_TO_PARENT)
        ret.prefSize = Dimensions(0.1, 0.1)
        return ret
    }
}