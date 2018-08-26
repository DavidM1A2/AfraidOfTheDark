package com.DavidM1A2.afraidofthedark.client.gui.eventListeners;

import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDMouseEvent;

/**
 * Interface to be implemented to add a new mouse moving listener to a control
 */
public interface IAOTDMouseMoveListener extends IAOTDEventListener
{
	/**
	 * Called when a mouse is dragged
	 *
	 * @param event The event containing information about the mouse drag
	 */
	void mouseDragged(AOTDMouseEvent event);

	/**
	 * Called when a mouse is moved
	 *
	 * @param event The event containing information about the mouse move
	 */
	void mouseMoved(AOTDMouseEvent event);
}
