package com.DavidM1A2.afraidofthedark.client.gui.eventListeners;

import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDMouseEvent;

/**
 * Utility class for mouse move listeners with default implementations of all methods. This can be used to reduce the clutter when adding action listeners by
 * reducing the number of empty interface implementations
 */
public class AOTDMouseMoveListener implements IAOTDMouseMoveListener
{
	/**
	 * Called when a mouse is dragged
	 *
	 * @param event The event containing information about the mouse drag
	 */
	@Override
	public void mouseDragged(AOTDMouseEvent event) {}

	/**
	 * Called when a mouse is moved
	 *
	 * @param event The event containing information about the mouse move
	 */
	@Override
	public void mouseMoved(AOTDMouseEvent event) {}
}
