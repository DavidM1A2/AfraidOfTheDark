/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.handler;

import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiScreen;

import net.minecraftforge.client.event.GuiScreenEvent.MouseInputEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiEventHandler
{
	@SubscribeEvent
	public void onMouseInputEvent(MouseInputEvent.Pre event)
	{
		if (event.gui instanceof AOTDGuiScreen)
		{
			((AOTDGuiScreen) event.gui).getEventController().mouseInputEvent();
		}
	}
}
