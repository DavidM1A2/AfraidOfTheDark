package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import net.minecraft.resources.ResourceLocation

open class PopupPane(parent: AOTDPane, background: ResourceLocation, size: Dimensions = Dimensions(0.4, 0.4), gravity: Gravity = Gravity.CENTER) : OverlayPane(parent) {
    private val backgroundPane = ImagePane(background)

    init {
        backgroundPane.prefSize = size
        this.add(backgroundPane)
    }

    override fun add(container: AOTDGuiComponent) {
        backgroundPane.add(container)
    }

    override fun remove(container: AOTDGuiComponent) {
        backgroundPane.remove(container)
    }
}