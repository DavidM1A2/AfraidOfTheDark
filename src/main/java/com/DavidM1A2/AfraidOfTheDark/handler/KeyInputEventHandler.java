/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.handler;

import com.DavidM1A2.AfraidOfTheDark.refrence.Key;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;

public class KeyInputEventHandler
{
	private static Key getPressedKeybinding()
	{
		// if (Keybindings.changeMode.isPressed())
		{
			return Key.CHANGE_MODE;
		}
		// return Key.UNKNOWN;
	}

	@SubscribeEvent
	public void handleKeyInputEvent(InputEvent.KeyInputEvent event)
	{
		// LogHelper.info(getPressedKeybinding());
	}
}
