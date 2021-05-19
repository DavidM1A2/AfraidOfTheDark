package com.davidm1a2.afraidofthedark.client.gui.layout

abstract class GuiSpacing(val top: Double, val bot: Double, val left: Double, val right: Double) {
    val horizPx: Double
        get() = left + right
    val vertPx: Double
        get() = top + bot
}