package com.davidm1a2.afraidofthedark.client.gui.events;

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiComponentWithEvents;

/**
 * Class representing any AOTD gui events that have to do with pressing a key
 */
public class AOTDKeyEvent extends AOTDEvent
{
    // The key that was pressed to trigger the event
    private final char key;
    // The key code that was pressed to trigger the event
    private final int keyCode;
    // The event type that was fired
    private final KeyEventType eventType;

    /**
     * The AOTD key event constructor initializes all final fields
     *
     * @param source    The source of the key event
     * @param key       The key that was pressed
     * @param keyCode   The code of the key that was pressed
     * @param eventType The type of the event that was fired
     */
    public AOTDKeyEvent(AOTDGuiComponentWithEvents source, char key, int keyCode, KeyEventType eventType)
    {
        super(source);
        this.key = key;
        this.keyCode = keyCode;
        this.eventType = eventType;
    }

    /**
     * @return Getter for the key pressed
     */
    public char getKey()
    {
        return this.key;
    }

    /**
     * @return Getter for the key code pressed
     */
    public int getKeyCode()
    {
        return this.keyCode;
    }

    /**
     * @return Getter for the event type that was fired
     */
    public KeyEventType getEventType()
    {
        return this.eventType;
    }

    /**
     * An enum representing the key's event type
     */
    public enum KeyEventType
    {
        Type,
        Press,
        Release
    }
}
