package com.DavidM1A2.afraidofthedark.client.gui.eventListeners;

import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDMouseEvent;

/**
 * Utility class for mouse listeners with default implementations of all methods. This can be used to reduce the clutter when adding action listeners by
 * reducing the number of empty interface implementations
 */
public abstract class AOTDMouseListener implements IAOTDMouseListener
{
	/**
	 * Called when a mouse button is pressed and released
	 *
	 * @param event The event containing information about the mouse click
	 */
	@Override
	public void mouseClicked(AOTDMouseEvent event) {}

	/**
	 * Called when a mouse button is pressed
	 *
	 * @param event The event containing information about the mouse press
	 */
	@Override
	public void mousePressed(AOTDMouseEvent event) {}

	/**
	 * Called when a mouse button is released
	 *
	 * @param event The event containing information about the mouse release
	 */
	@Override
	public void mouseReleased(AOTDMouseEvent event) {}

	/**
	 * Called when the mouse is moved into the bounding box of a control
	 *
	 * @param event The event containing information about the mouse entering event
	 */
	@Override
	public void mouseEntered(AOTDMouseEvent event) {}

	/**
	 * Called when the mouse is moved out of the bounding box of a control
	 *
	 * @param event The event containing information about the mouse exiting event
	 */
	@Override
	public void mouseExited(AOTDMouseEvent event) {}
}
