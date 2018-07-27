/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners;

import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDKeyEvent;

public interface IAOTDKeyListener extends AOTDEventListener
{
	void keyTyped(AOTDKeyEvent event);

	void keyPressed(AOTDKeyEvent event);

	void keyReleased(AOTDKeyEvent event);
}
