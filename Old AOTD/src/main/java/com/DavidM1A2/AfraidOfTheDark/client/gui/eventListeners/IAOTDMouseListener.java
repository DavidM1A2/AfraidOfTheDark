/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners;

import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent;

public interface IAOTDMouseListener extends AOTDEventListener
{
	void mouseClicked(AOTDMouseEvent event);

	void mousePressed(AOTDMouseEvent event);

	void mouseReleased(AOTDMouseEvent event);

	void mouseEntered(AOTDMouseEvent event);

	void mouseExited(AOTDMouseEvent event);
}
