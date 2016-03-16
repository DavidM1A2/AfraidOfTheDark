/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners;

import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent;

public interface AOTDMouseMoveListener extends AOTDEventListener
{
	public void mouseDragged(AOTDMouseEvent event);

	public void mouseMoved(AOTDMouseEvent event);
}
