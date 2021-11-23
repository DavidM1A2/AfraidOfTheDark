package com.davidm1a2.afraidofthedark.client.gui.layout

import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiComponent
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDPane

class Spacing(val top: Double, val bot: Double, val left: Double, val right: Double, val isRelative: Boolean = true) {
    val width = left + right
    val height = top + bot

    constructor(uniform: Double, isRelative: Boolean = true) : this(uniform, uniform, uniform, uniform, isRelative)
    constructor() : this(0.0)

    fun getAbsoluteOuter(reference: AOTDGuiComponent): Spacing {
        return if (isRelative) {
            Spacing(
                top * reference.height,
                bot * reference.height,
                left * reference.width,
                right * reference.width,
                false
            )
        } else {
            Spacing(top, bot, left, right, false)
        }
    }

    fun getAbsoluteInner(reference: AOTDPane): Spacing {
        return if (isRelative) {
            Spacing(
                top * reference.getInternalHeight(),
                bot * reference.getInternalHeight(),
                left * reference.getInternalWidth(),
                right * reference.getInternalWidth(),
                false
            )
        } else {
            Spacing(top, bot, left, right, false)
        }
    }
}