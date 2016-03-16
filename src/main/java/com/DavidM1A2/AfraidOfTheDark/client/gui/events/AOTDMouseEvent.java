/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.events;

import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiComponentWithEvents;

public class AOTDMouseEvent extends AOTDEvent
{
	public enum MouseEventType
	{
		Click,
		Press,
		Release,
		Enter,
		Exit,
		Move,
		Drag;
	}

	private int mouseX;
	private int mouseY;
	private MouseEventType eventType;
	private boolean isConsumed = false;

	public AOTDMouseEvent(AOTDGuiComponentWithEvents source, int mouseX, int mouseY, MouseEventType eventType)
	{
		super(source);
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		this.eventType = eventType;
	}

	public int getMouseX()
	{
		return this.mouseX;
	}

	public int getMouseY()
	{
		return this.mouseY;
	}

	public MouseEventType getEventType()
	{
		return this.eventType;
	}
}
