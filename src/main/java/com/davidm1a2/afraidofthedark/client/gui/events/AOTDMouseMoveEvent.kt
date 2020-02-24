package com.davidm1a2.afraidofthedark.client.gui.events

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiComponentWithEvents

/**
 * Class representing any AOTD gui events that have to do with the mouse
 *
 * @constructor initializes all mouse event final fields given a control that fired the event
 * @param source    The control that fired the event
 * @param mouseX    The X position of the mouse when the event was fired
 * @param mouseY    The Y position of the mouse when the event was fired
 * @param eventType The event type that was fired
 */
class AOTDMouseMoveEvent(
    source: AOTDGuiComponentWithEvents,
    val mouseX: Int,
    val mouseY: Int,
    val eventType: EventType
) : AOTDEvent(source) {
    /**
     * Internal enum representing the different mouse event types we can have
     */
    enum class EventType {
        Enter,
        Exit,
        Move,
        Drag
    }
}
