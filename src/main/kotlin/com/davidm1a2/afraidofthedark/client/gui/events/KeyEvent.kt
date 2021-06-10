package com.davidm1a2.afraidofthedark.client.gui.events

import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiComponentWithEvents
import org.lwjgl.glfw.GLFW

/**
 * Event fired when a key is pressed, released, or a char is typed. [key] and [scanCode] will be valid for Press and Release events,
 * and [char] will be valid for a Type event. [eventType] indicates what kind of event [source] triggered.
 */
class KeyEvent(
    source: AOTDGuiComponentWithEvents,
    val key: Int,
    val scanCode: Int,
    val char: Char,
    private val modifiers: Int,
    val eventType: KeyEventType
) :
    AOTDEvent(source) {
    fun hasModifier(modifier: Modifier): Boolean {
        return modifiers and modifier.mask > 0
    }

    /**
     * An enum representing the key's event type
     */
    enum class KeyEventType {
        Press,
        Release,
        Type
    }

    enum class Modifier(internal val mask: Int) {
        SHIFT(GLFW.GLFW_MOD_SHIFT),
        CONTROL(GLFW.GLFW_MOD_CONTROL),
        ALT(GLFW.GLFW_MOD_ALT),
        SUPER(GLFW.GLFW_MOD_SUPER),
        CAPS_LOCK(GLFW.GLFW_MOD_CAPS_LOCK),
        NUM_LOCK(GLFW.GLFW_MOD_NUM_LOCK)
    }
}
