package com.davidm1a2.afraidofthedark.client.gui.events

fun interface ITextChangeListener {
    fun apply(oldText: String, newText: String)
}
