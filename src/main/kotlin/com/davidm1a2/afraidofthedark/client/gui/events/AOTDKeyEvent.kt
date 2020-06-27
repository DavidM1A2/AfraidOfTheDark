package com.davidm1a2.afraidofthedark.client.gui.events

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiComponentWithEvents
import org.lwjgl.glfw.GLFW

/**
 * Class representing any AOTD gui events that have to do with pressing a key
 *
 * @constructor constructor initializes all final fields
 * @param source    The source of the key event
 * @param key       The key that was pressed
 * @param modifiers   The modifiers pressed with the key
 * @param eventType The type of the event that was fired
 */
class AOTDKeyEvent(source: AOTDGuiComponentWithEvents, val key: Int, val scanCode: Int, private val modifiers: Int, val eventType: KeyEventType) :
    AOTDEvent(source) {
    fun getKeyName(): String? {
        return GLFW.glfwGetKeyName(key, scanCode)
    }

    fun hasModifier(modifier: Modifier): Boolean {
        return modifiers and modifier.mask > 0
    }

    /**
     * An enum representing the key's event type
     */
    enum class KeyEventType {
        Press,
        Release
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
