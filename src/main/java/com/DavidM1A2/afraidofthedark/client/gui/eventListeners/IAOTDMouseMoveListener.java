package com.DavidM1A2.afraidofthedark.client.gui.eventListeners;

import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent;

/**
 * Interface to be implemented to add a new mouse moving listener to a control
 */
public interface IAOTDMouseMoveListener extends IAOTDEventListener
{
    /**
     * Called when a mouse move event is fired
     *
     * @param event The event containing information about the mouse move event
     */
    void fire(AOTDMouseMoveEvent event);
}
