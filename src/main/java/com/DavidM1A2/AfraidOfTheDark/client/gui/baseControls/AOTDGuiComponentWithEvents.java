/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls;

import java.awt.Point;

import com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners.AOTDEventMulticaster;
import com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners.AOTDMouseMoveListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners.IAOTDKeyListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners.IAOTDMouseListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners.IAOTDMouseMoveListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDKeyEvent;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent.MouseButtonClicked;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent.MouseEventType;

public class AOTDGuiComponentWithEvents extends AOTDGuiComponent
{
	private IAOTDKeyListener keyListener;
	private IAOTDMouseListener mouseListener;
	private IAOTDMouseMoveListener mouseMoveListener;

	public AOTDGuiComponentWithEvents(int x, int y, int width, int height)
	{
		super(x, y, width, height);
		this.addMouseMoveListener(new AOTDMouseMoveListener()
		{
			@Override
			public void mouseMoved(AOTDMouseEvent event)
			{
				AOTDGuiComponentWithEvents component = event.getSource();
				boolean wasHovered = component.isHovered();
				component.setHovered(component.intersects(new Point(event.getMouseX(), event.getMouseY())));
				if (component.isHovered() && !wasHovered)
					component.processMouseInput(new AOTDMouseEvent(component, event.getMouseX(), event.getMouseY(), MouseButtonClicked.Other, MouseEventType.Enter));
				if (!component.isHovered() && wasHovered)
					component.processMouseInput(new AOTDMouseEvent(component, event.getMouseX(), event.getMouseY(), MouseButtonClicked.Other, MouseEventType.Exit));
			}
		});
	}

	public void processMouseInput(AOTDMouseEvent event)
	{
		if (event.isConsumed())
			return;
		if (event.getEventType() == MouseEventType.Enter || event.getEventType() == MouseEventType.Exit)
			event.consume();
		event.setSource(this);
		if (mouseMoveListener != null && (event.getEventType() == MouseEventType.Move || event.getEventType() == MouseEventType.Drag))
			switch (event.getEventType())
			{
				case Move:
					mouseMoveListener.mouseMoved(event);
					break;
				case Drag:
					mouseMoveListener.mouseDragged(event);
					break;
				default:
					return;
			}
		if (mouseListener != null)
			switch (event.getEventType())
			{
				case Click:
					mouseListener.mouseClicked(event);
					break;
				case Enter:
					mouseListener.mouseEntered(event);
					break;
				case Exit:
					mouseListener.mouseExited(event);
					break;
				case Press:
					mouseListener.mousePressed(event);
					break;
				case Release:
					mouseListener.mouseReleased(event);
					break;
				default:
					return;
			}
	}

	public void processKeyInput(AOTDKeyEvent event)
	{
		event.setSource(this);
		if (keyListener == null)
			return;
		switch (event.getEventType())
		{
			case Type:
				keyListener.keyTyped(event);
				break;
			case Press:
				keyListener.keyPressed(event);
				break;
			case Release:
				keyListener.keyReleased(event);
				break;
		}
	}

	public void addKeyListener(IAOTDKeyListener keyListener)
	{
		if (keyListener == null)
			return;
		this.keyListener = AOTDEventMulticaster.combineKeyListeners(this.keyListener, keyListener);
	}

	public void addMouseListener(IAOTDMouseListener mouseListener)
	{
		if (mouseListener == null)
			return;
		this.mouseListener = AOTDEventMulticaster.combineMouseListeners(this.mouseListener, mouseListener);
	}

	public void addMouseMoveListener(IAOTDMouseMoveListener mouseMoveListener)
	{
		if (mouseMoveListener == null)
			return;
		this.mouseMoveListener = AOTDEventMulticaster.combineMouseMoveListeners(this.mouseMoveListener, mouseMoveListener);
	}
}
