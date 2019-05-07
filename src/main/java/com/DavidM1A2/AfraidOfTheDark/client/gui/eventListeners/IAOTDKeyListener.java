package com.DavidM1A2.afraidofthedark.client.gui.eventListeners;

import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDKeyEvent;

/**
 * Interface to be implemented to add a new key listener to a control
 */
public interface IAOTDKeyListener extends IAOTDEventListener
{
    /**
     * Called when a key is typed (pressed and released)
     *
     * @param event The event containing information about the key press
     */
    void keyTyped(AOTDKeyEvent event);

    /**
     * Called when a key is pressed down
     *
     * @param event The event containing information about the key press
     */
    void keyPressed(AOTDKeyEvent event);

    /**
     * Called when a key is released
     *
     * @param event The event containing information about the key release
     */
    void keyReleased(AOTDKeyEvent event);
}
