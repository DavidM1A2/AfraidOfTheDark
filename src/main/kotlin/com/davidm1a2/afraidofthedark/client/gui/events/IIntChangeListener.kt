package com.davidm1a2.afraidofthedark.client.gui.events

fun interface IIntChangeListener {
    fun apply(oldVal: Int, newVal: Int)
}