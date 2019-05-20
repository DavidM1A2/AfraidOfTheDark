package com.DavidM1A2.afraidofthedark.client.gui.events;

import com.DavidM1A2.afraidofthedark.client.gui.base.AOTDGuiComponentWithEvents;

/**
 * Base class for all AOTD gui events
 */
public class AOTDEvent
{
    // The source component that triggered this event
    private AOTDGuiComponentWithEvents source;
    // A flag to tell us if the event is consumed
    private boolean isConsumed = false;

    /**
     * Constructor initializes the event with a source component that fired the event
     *
     * @param source The component that fired the event
     */
    public AOTDEvent(AOTDGuiComponentWithEvents source)
    {
        this.source = source;
    }

    /**
     * @return Returns the source of the event
     */
    public AOTDGuiComponentWithEvents getSource()
    {
        return this.source;
    }

    /**
     * Sets the source of this event
     *
     * @param source The component that fired this event
     */
    public void setSource(AOTDGuiComponentWithEvents source)
    {
        this.source = source;
    }

    /**
     * Consumes the event preventing further processing
     */
    public void consume()
    {
        this.isConsumed = true;
    }

    /**
     * @return True if the event was consumed, false otherwise
     */
    public boolean isConsumed()
    {
        return this.isConsumed;
    }
}
