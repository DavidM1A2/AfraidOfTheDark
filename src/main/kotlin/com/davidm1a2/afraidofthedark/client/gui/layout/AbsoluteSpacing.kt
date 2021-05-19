package com.davidm1a2.afraidofthedark.client.gui.layout

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDPane

class AbsoluteSpacing(top: Double, bot: Double, left: Double, right: Double): GuiSpacing(top, bot, left, right) {
    constructor(uniform: Double): this(uniform, uniform, uniform, uniform)
    constructor(): this(0.0)

    fun toRelative(reference: AOTDPane): RelativeSpacing {
        return RelativeSpacing(top / reference.height, bot / reference.height, left / reference.width, right / reference.width)
    }
}