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

	public enum MouseButtonClicked
	{
		Left,
		Right,
		Other;
	}

	private int mouseX;
	private int mouseY;
	private MouseEventType eventType;
	private MouseButtonClicked buttonClicked;
	private boolean isConsumed = false;

	public AOTDMouseEvent(AOTDGuiComponentWithEvents source, int mouseX, int mouseY, MouseButtonClicked buttonClicked, MouseEventType eventType)
	{
		super(source);
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		this.eventType = eventType;
		this.buttonClicked = buttonClicked;
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

	public MouseButtonClicked getClickedButton()
	{
		return this.buttonClicked;
	}
}
