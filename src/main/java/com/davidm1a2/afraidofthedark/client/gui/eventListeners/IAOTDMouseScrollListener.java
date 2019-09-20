package com.davidm1a2.afraidofthedark.client.gui.eventListeners;

import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseScrollEvent;

/**
 * Interface to be implemented to add a new mouse scroll listener to a control
 */
public interface IAOTDMouseScrollListener extends IAOTDEventListener
{
    /**
     * Called when a mouse scroll event is fired
     *
     * @param event The event containing information about the mouse move event
     */
    void fire(AOTDMouseScrollEvent event);
}
