package com.davidm1a2.afraidofthedark.client.gui.eventListeners;

import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent;

/**
 * Interface to be implemented to add a new mouse listener to a control
 */
public interface IAOTDMouseListener extends IAOTDEventListener
{
    /**
     * Called when a mouse button event is fired
     *
     * @param event The event containing information about the mouse event
     */
    void fire(AOTDMouseEvent event);
}
