package com.davidm1a2.afraidofthedark.client.gui.events;

import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiComponentWithEvents;

/**
 * Class representing any AOTD gui events that have to do with the mouse
 */
public class AOTDMouseMoveEvent extends AOTDEvent
{
    // The mouse's X position when the event was fired
    private final int mouseX;
    // The mouse's Y position when the event was fired
    private final int mouseY;
    // The type of mouse event that was fired
    private final EventType eventType;

    /**
     * The constructor initializes all mouse event final fields given a control that fired the event
     *
     * @param source    The control that fired the event
     * @param mouseX    The X position of the mouse when the event was fired
     * @param mouseY    The Y position of the mouse when the event was fired
     * @param eventType The event type that was fired
     */
    public AOTDMouseMoveEvent(AOTDGuiComponentWithEvents source, int mouseX, int mouseY, EventType eventType)
    {
        super(source);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.eventType = eventType;
    }

    /**
     * @return The X position of the mouse when the event was fired
     */
    public int getMouseX()
    {
        return this.mouseX;
    }

    /**
     * @return The Y position of the mouse when the event was fired
     */
    public int getMouseY()
    {
        return this.mouseY;
    }

    /**
     * @return The event type that the event was fired with
     */
    public EventType getEventType()
    {
        return this.eventType;
    }

    /**
     * Internal enum representing the different mouse event types we can have
     */
    public enum EventType
    {
        Enter,
        Exit,
        Move,
        Drag
    }
}
