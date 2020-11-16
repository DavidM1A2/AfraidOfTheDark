package com.davidm1a2.afraidofthedark.client.gui.events

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiComponentWithEvents
import org.lwjgl.glfw.GLFW

/**
 * Class representing any AOTD gui events that have to do with the mouse
 *
 * @constructor initializes all mouse event final fields given a control that fired the event
 * @param source        The control that fired the event
 * @param mouseX        The X position of the mouse when the event was fired
 * @param mouseY        The Y position of the mouse when the event was fired
 * @param clickedButton The mouse button that caused the event
 * @param eventType     The event type that was fired
 */
class AOTDMouseEvent(
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
