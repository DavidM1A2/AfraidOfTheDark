package com.davidm1a2.afraidofthedark.client.gui.events

import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiComponentWithEvents

/**
 * Class representing any AOTD gui events that have to do with the mouse
 */
class MouseMoveEvent(
    source: AOTDGuiComponentWithEvents,
    val mouseX: Double,
    val mouseY: Double,
    val eventType: EventType
) : AOTDEvent(source) {
    /**
     * Internal enum representing the different mouse event types we can have
     */
    enum class EventType {
        Enter,
        Exit,
        Move
    }
}
