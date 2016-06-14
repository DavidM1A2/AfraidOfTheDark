/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners;

import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDKeyEvent;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent;

public class AOTDEventMulticaster implements IAOTDKeyListener, IAOTDMouseListener, IAOTDMouseMoveListener
{
	private AOTDEventListener listener1;
	private AOTDEventListener listener2;

	public AOTDEventMulticaster(AOTDEventListener listener1, AOTDEventListener listener2)
	{
		this.listener1 = listener1;
		this.listener2 = listener2;
	}

	@Override
	public void mouseDragged(AOTDMouseEvent event)
	{
		((IAOTDMouseMoveListener) listener1).mouseDragged(event);
		((IAOTDMouseMoveListener) listener2).mouseDragged(event);
	}

	@Override
	public void mouseMoved(AOTDMouseEvent event)
	{
		((IAOTDMouseMoveListener) listener1).mouseMoved(event);
		((IAOTDMouseMoveListener) listener2).mouseMoved(event);
	}

	@Override
	public void mouseClicked(AOTDMouseEvent event)
	{
		((IAOTDMouseListener) listener1).mouseClicked(event);
		((IAOTDMouseListener) listener2).mouseClicked(event);
	}

	@Override
	public void mousePressed(AOTDMouseEvent event)
	{
		((IAOTDMouseListener) listener1).mousePressed(event);
		((IAOTDMouseListener) listener2).mousePressed(event);
	}

	@Override
	public void mouseReleased(AOTDMouseEvent event)
	{
		((IAOTDMouseListener) listener1).mouseReleased(event);
		((IAOTDMouseListener) listener2).mouseReleased(event);
	}

	@Override
	public void mouseEntered(AOTDMouseEvent event)
	{
		((IAOTDMouseListener) listener1).mouseEntered(event);
		((IAOTDMouseListener) listener2).mouseEntered(event);
	}

	@Override
	public void mouseExited(AOTDMouseEvent event)
	{
		((IAOTDMouseListener) listener1).mouseExited(event);
		((IAOTDMouseListener) listener2).mouseExited(event);
	}

	@Override
	public void keyTyped(AOTDKeyEvent event)
	{
		((IAOTDKeyListener) listener1).keyTyped(event);
		((IAOTDKeyListener) listener2).keyTyped(event);
	}

	@Override
	public void keyPressed(AOTDKeyEvent event)
	{
		((IAOTDKeyListener) listener1).keyPressed(event);
		((IAOTDKeyListener) listener2).keyPressed(event);
	}

	@Override
	public void keyReleased(AOTDKeyEvent event)
	{
		((IAOTDKeyListener) listener1).keyReleased(event);
		((IAOTDKeyListener) listener2).keyReleased(event);
	}

	public static IAOTDKeyListener combineKeyListeners(IAOTDKeyListener first, IAOTDKeyListener second)
	{
		return (IAOTDKeyListener) combine(first, second);
	}

	public static IAOTDMouseListener combineMouseListeners(IAOTDMouseListener first, IAOTDMouseListener second)
	{
		return (IAOTDMouseListener) combine(first, second);
	}

	public static IAOTDMouseMoveListener combineMouseMoveListeners(IAOTDMouseMoveListener first, IAOTDMouseMoveListener second)
	{
		return (IAOTDMouseMoveListener) combine(first, second);
	}

	private static AOTDEventListener combine(AOTDEventListener first, AOTDEventListener second)
	{
		if (first == null)
			return second;
		if (second == null)
			return first;
		return new AOTDEventMulticaster(first, second);
	}
}
