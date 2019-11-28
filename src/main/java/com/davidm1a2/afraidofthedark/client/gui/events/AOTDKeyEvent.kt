package com.davidm1a2.afraidofthedark.client.gui.events

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiComponentWithEvents

/**
 * Class representing any AOTD gui events that have to do with pressing a key
 *
 * @constructor constructor initializes all final fields
 * @param source    The source of the key event
 * @param key       The key that was pressed
 * @param keyCode   The code of the key that was pressed
 * @param eventType The type of the event that was fired
 */
class AOTDKeyEvent(source: AOTDGuiComponentWithEvents, val key: Char, val keyCode: Int, val eventType: KeyEventType) : AOTDEvent(source)
{
    /**
     * An enum representing the key's event type
     */
    enum class KeyEventType
    {
        Type,
        Press,
        Release
    }
}
