/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners;

import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDKeyEvent;

public interface AOTDKeyListener extends AOTDEventListener
{
	public void keyTyped(AOTDKeyEvent event);

	public void keyPressed(AOTDKeyEvent event);

	public void keyReleased(AOTDKeyEvent event);
}
