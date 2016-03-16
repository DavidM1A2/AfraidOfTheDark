/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners;

import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent;

public interface AOTDMouseListener extends AOTDEventListener
{
	public void mouseClicked(AOTDMouseEvent event);

	public void mousePressed(AOTDMouseEvent event);

	public void mouseReleased(AOTDMouseEvent event);

	public void mouseEntered(AOTDMouseEvent event);

	public void mouseExited(AOTDMouseEvent event);
}
