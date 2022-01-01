package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.dragAndDrop.DraggableProducer
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponentInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffect
import net.minecraft.util.text.TranslationTextComponent

/**
 * The icon for a spell effect on the scroll panel
 */
class SpellEffectIcon(private val effect: SpellEffect) :
    ImagePane("afraidofthedark:textures/gui/spell_editor/effect_holder.png", DispMode.FIT_TO_PARENT),
    DraggableProducer<SpellEffect> {

    init {
        val icon = ImagePane(effect.icon, DispMode.FIT_TO_PARENT)
        icon.gravity = Gravity.CENTER
        icon.margins = Spacing(0.08)
        this.add(icon)

        val componentInstance = SpellComponentInstance(effect)
        componentInstance.setDefaults()
        this.hoverTexts = arrayOf(
            effect.getName().string,
            TranslationTextComponent("tooltip.afraidofthedark.gui.spell_crafting.cost", effect.getCost(componentInstance)).string
        )
    }

    override fun produce(): SpellEffect {
        return effect
    }

    override fun getIcon(): ImagePane {
        val icon = ImagePane(effect.icon, DispMode.FIT_TO_PARENT)
        icon.prefSize = Dimensions(0.06, 0.06)
        return icon
    }
}