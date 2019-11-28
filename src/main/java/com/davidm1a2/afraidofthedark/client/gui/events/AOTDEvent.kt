package com.davidm1a2.afraidofthedark.client.gui.events

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiComponentWithEvents

/**
 * Base class for all AOTD gui events
 *
 * @constructor initializes the event with a source component that fired the event
 * @param source The source component that triggered this event
 * @property isConsumed A flag to tell us if the event is consumed
 */
open class AOTDEvent(var source: AOTDGuiComponentWithEvents)
{
    var isConsumed = false
        private set

    /**
     * Consumes the event preventing further processing
     */
    fun consume()
    {
        this.isConsumed = true
    }
}
