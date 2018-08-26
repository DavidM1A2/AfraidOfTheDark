package com.DavidM1A2.afraidofthedark.client.gui.events;

import com.DavidM1A2.afraidofthedark.client.gui.base.AOTDGuiComponentWithEvents;

/**
 * Class representing any AOTD gui events that have to do with the mouse
 */
public class AOTDMouseEvent extends AOTDEvent
{
	/**
	 * Internal enum representing the different mouse event types we can have
	 */
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

	/**
	 * Internal enum representing different mouse buttons that can be clicked
	 */
	public enum MouseButtonClicked
	{
		Left,
		Right,
		Other;
	}

	// The mouse's X position when the event was fired
	private final int mouseX;
	// The mouse's Y position when the event was fired
	private final int mouseY;
	// The type of mouse event that was fired
	private final MouseEventType eventType;
	// The mouse button that fired the event
	private final MouseButtonClicked buttonClicked;

	/**
	 * The constructor initializes all mouse event final fields given a control that fired the event
	 *
	 * @param source The control that fired the event
	 * @param mouseX The X position of the mouse when the event was fired
	 * @param mouseY The Y position of the mouse when the event was fired
	 * @param buttonClicked The mouse button that caused the event
	 * @param eventType The event type that was fired
	 */
	public AOTDMouseEvent(AOTDGuiComponentWithEvents source, int mouseX, int mouseY, MouseButtonClicked buttonClicked, MouseEventType eventType)
	{
		super(source);
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		this.eventType = eventType;
		this.buttonClicked = buttonClicked;
	}

	/**
	 * @return The X position of the mouse when the event was fired
	 */
	public int getMouseX()
	{
		return this.mouseX;
	}

	/**
	 * @return The Y position of the mouse when the event was fired
	 */
	public int getMouseY()
	{
		return this.mouseY;
	}

	/**
	 * @return The event type that the event was fired with
	 */
	public MouseEventType getEventType()
	{
		return this.eventType;
	}

	/**
	 * @return The button that triggered the event
	 */
	public MouseButtonClicked getClickedButton()
	{
		return this.buttonClicked;
	}
}
