/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.events;

import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiComponentWithEvents;

public class AOTDKeyEvent extends AOTDEvent
{
	public enum KeyEventType
	{
		Type,
		Press,
		Release;
	}

	private char key;
	private int keyCode;
	private KeyEventType eventType;

	public AOTDKeyEvent(AOTDGuiComponentWithEvents source, char key, int keyCode, KeyEventType eventType)
	{
		super(source);
		this.key = key;
		this.keyCode = keyCode;
		this.eventType = eventType;
	}

	public char getKey()
	{
		return this.key;
	}

	public int getKeyCode()
	{
		return this.keyCode;
	}

	public KeyEventType getEventType()
	{
		return this.eventType;
	}
}
