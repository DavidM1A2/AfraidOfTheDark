/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.events;

import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiComponentWithEvents;

public class AOTDEvent
{
	private AOTDGuiComponentWithEvents source;
	private boolean isConsumed = false;

	public AOTDEvent(AOTDGuiComponentWithEvents source)
	{
		this.source = source;
	}

	public AOTDGuiComponentWithEvents getSource()
	{
		return this.source;
	}

	public void setSource(AOTDGuiComponentWithEvents eventSource)
	{
		this.source = eventSource;
	}

	public void consume()
	{
		this.isConsumed = true;
	}

	public boolean isConsumed()
	{
		return this.isConsumed;
	}
}
