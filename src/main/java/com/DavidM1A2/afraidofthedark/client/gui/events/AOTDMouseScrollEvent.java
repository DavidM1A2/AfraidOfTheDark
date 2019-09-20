package com.DavidM1A2.afraidofthedark.client.gui.events;

import com.DavidM1A2.afraidofthedark.client.gui.base.AOTDGuiComponentWithEvents;

/**
 * Class representing any AOTD gui events that have to do with the mouse scrolling
 */
public class AOTDMouseScrollEvent extends AOTDEvent
{
    // The distance the scroll wheel has moved, + values indicate forward scroll, - values indicate negative scroll
    private final int scrollDistance;

    /**
     * Constructor initializes the scroll distance and source
     *
     * @param source         The control that fired the event
     * @param scrollDistance The distance the mouse scrolled
     */
    public AOTDMouseScrollEvent(AOTDGuiComponentWithEvents source, int scrollDistance)
    {
        super(source);
        this.scrollDistance = scrollDistance;
    }

    /**
     * @return The distance the mouse scrolled
     */
    public int getScrollDistance()
    {
        return this.scrollDistance;
    }
}
