package com.davidm1a2.afraidofthedark.client.gui.eventListeners;

import com.davidm1a2.afraidofthedark.client.gui.events.AOTDKeyEvent;

/**
 * Interface to be implemented to add a new key listener to a control
 */
public interface IAOTDKeyListener extends IAOTDEventListener
{
    /**
     * Called when a key is interacted with
     *
     * @param event The event containing information about the key press
     */
    void fire(AOTDKeyEvent event);
}
