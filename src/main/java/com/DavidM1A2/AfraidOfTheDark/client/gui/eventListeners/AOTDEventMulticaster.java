/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners;

import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDKeyEvent;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent;

public class AOTDEventMulticaster implements AOTDKeyListener, AOTDMouseListener, AOTDMouseMoveListener
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
		((AOTDMouseMoveListener) listener1).mouseDragged(event);
		((AOTDMouseMoveListener) listener2).mouseDragged(event);
	}

	@Override
	public void mouseMoved(AOTDMouseEvent event)
	{
		((AOTDMouseMoveListener) listener1).mouseMoved(event);
		((AOTDMouseMoveListener) listener2).mouseMoved(event);
	}

	@Override
	public void mouseClicked(AOTDMouseEvent event)
	{
		((AOTDMouseListener) listener1).mouseClicked(event);
		((AOTDMouseListener) listener2).mouseClicked(event);
	}

	@Override
	public void mousePressed(AOTDMouseEvent event)
	{
		((AOTDMouseListener) listener1).mousePressed(event);
		((AOTDMouseListener) listener2).mousePressed(event);
	}

	@Override
	public void mouseReleased(AOTDMouseEvent event)
	{
		((AOTDMouseListener) listener1).mouseReleased(event);
		((AOTDMouseListener) listener2).mouseReleased(event);
	}

	@Override
	public void mouseEntered(AOTDMouseEvent event)
	{
		((AOTDMouseListener) listener1).mouseEntered(event);
		((AOTDMouseListener) listener2).mouseEntered(event);
	}

	@Override
	public void mouseExited(AOTDMouseEvent event)
	{
		((AOTDMouseListener) listener1).mouseExited(event);
		((AOTDMouseListener) listener2).mouseExited(event);
	}

	@Override
	public void keyTyped(AOTDKeyEvent event)
	{
		((AOTDKeyListener) listener1).keyTyped(event);
		((AOTDKeyListener) listener2).keyTyped(event);
	}

	@Override
	public void keyPressed(AOTDKeyEvent event)
	{
		((AOTDKeyListener) listener1).keyPressed(event);
		((AOTDKeyListener) listener2).keyPressed(event);
	}

	@Override
	public void keyReleased(AOTDKeyEvent event)
	{
		((AOTDKeyListener) listener1).keyReleased(event);
		((AOTDKeyListener) listener2).keyReleased(event);
	}

	public static AOTDKeyListener combineKeyListeners(AOTDKeyListener first, AOTDKeyListener second)
	{
		return (AOTDKeyListener) combine(first, second);
	}

	public static AOTDMouseListener combineMouseListeners(AOTDMouseListener first, AOTDMouseListener second)
	{
		return (AOTDMouseListener) combine(first, second);
	}

	public static AOTDMouseMoveListener combineMouseMoveListeners(AOTDMouseMoveListener first, AOTDMouseMoveListener second)
	{
		return (AOTDMouseMoveListener) combine(first, second);
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
