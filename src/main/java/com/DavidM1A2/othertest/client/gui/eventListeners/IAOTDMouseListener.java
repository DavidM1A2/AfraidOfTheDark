package com.DavidM1A2.afraidofthedark.client.gui.eventListeners;

import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDMouseEvent;

/**
 * Interface to be implemented to add a new mouse listener to a control
 */
public interface IAOTDMouseListener extends IAOTDEventListener
{
    /**
     * Called when a mouse button is pressed and released
     *
     * @param event The event containing information about the mouse click
     */
    void mouseClicked(AOTDMouseEvent event);

    /**
     * Called when a mouse button is pressed
     *
     * @param event The event containing information about the mouse press
     */
    void mousePressed(AOTDMouseEvent event);

    /**
     * Called when a mouse button is released
     *
     * @param event The event containing information about the mouse release
     */
    void mouseReleased(AOTDMouseEvent event);

    /**
     * Called when the mouse is moved into the bounding box of a control
     *
     * @param event The event containing information about the mouse entering event
     */
    void mouseEntered(AOTDMouseEvent event);

    /**
     * Called when the mouse is moved out of the bounding box of a control
     *
     * @param event The event containing information about the mouse exiting event
     */
    void mouseExited(AOTDMouseEvent event);
}
