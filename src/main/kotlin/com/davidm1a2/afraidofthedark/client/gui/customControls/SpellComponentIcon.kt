package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.dragAndDrop.DraggableProducer
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.common.spell.component.SpellComponent

abstract class SpellComponentIcon<T : SpellComponent<T>>(
    private val component: T,
    backgroundTexture: String
) : ImagePane("afraidofthedark:textures/gui/spell_editor/$backgroundTexture.png", DispMode.FIT_TO_PARENT), DraggableProducer<T> {
    init {
        val icon = ImagePane(component.icon, DispMode.FIT_TO_PARENT)
        icon.gravity = Gravity.CENTER
        icon.margins = Spacing(0.08)
        this.add(icon)
    }

    override fun produce(): T {
        return component
    }

    override fun getIcon(): ImagePane {
        val dragIcon = ImagePane(component.icon, DispMode.FIT_TO_PARENT)
        dragIcon.prefSize = Dimensions(0.06, 0.06)
        return dragIcon
    }
}