package com.DavidM1A2.afraidofthedark.client.gui.eventListeners;

import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDKeyEvent;

/**
 * Utility class for key listeners with default implementations of all methods. This can be used to reduce the clutter when adding action listeners by
 * reducing the number of empty interface implementations
 */
public abstract class AOTDKeyListener implements IAOTDKeyListener
{
	/**
	 * Called when a key is typed (pressed and released)
	 *
	 * @param event The event containing information about the key press
	 */
	@Override
	public void keyTyped(AOTDKeyEvent event) {}

	/**
	 * Called when a key is pressed down
	 *
	 * @param event The event containing information about the key press
	 */
	@Override
	public void keyPressed(AOTDKeyEvent event) {}

	/**
	 * Called when a key is released
	 *
	 * @param event The event containing information about the key release
	 */
	@Override
	public void keyReleased(AOTDKeyEvent event) {}
}
