/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui;

import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiComponent;

public interface AOTDActionListener
{
	public enum ActionType
	{
		MouseClick,
		MouseHover;
	}

	public abstract void actionPerformed(AOTDGuiComponent component, ActionType actionType);
}
