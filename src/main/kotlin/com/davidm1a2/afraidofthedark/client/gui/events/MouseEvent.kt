package com.davidm1a2.afraidofthedark.client.gui.events

import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiComponentWithEvents
import org.lwjgl.glfw.GLFW

/**
 * Class representing any AOTD gui events that have to do with the mouse
 */
class MouseEvent(
    source: AOTDGuiComponentWithEvents,
    val mouseX: Int,
    val mouseY: Int,
    val clickedButton: Int,
    val eventType: EventType
) : AOTDEvent(source) {
    /**
     * Internal enum representing the different mouse event types we can have
     */
    enum class EventType {
        Click,
        Release
    }

    companion object {
        // The mouse buttons that are standard
        const val LEFT_MOUSE_BUTTON = GLFW.GLFW_MOUSE_BUTTON_LEFT
        const val RIGHT_MOUSE_BUTTON = GLFW.GLFW_MOUSE_BUTTON_RIGHT
        const val MIDDLE_MOUSE_BUTTON = GLFW.GLFW_MOUSE_BUTTON_MIDDLE
    }
}
