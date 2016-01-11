/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls;

import java.util.LinkedList;
import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDActionListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDActionListener.ActionType;

public class AOTDGuiComponentWithEvents extends AOTDGuiComponent
{
	private List<AOTDActionListener> actionListeners = new LinkedList<AOTDActionListener>();

	public AOTDGuiComponentWithEvents(int x, int y, int width, int height)
	{
		super(x, y, width, height);
	}

	public void update(int mouseX, int mouseY)
	{
		if (this.isHovered())
			this.fireEvent(AOTDActionListener.ActionType.MouseHover);
	}

	public void mousePressed()
	{
		this.fireEvent(AOTDActionListener.ActionType.MousePressed);
	}

	public void mouseReleased()
	{
		this.fireEvent(AOTDActionListener.ActionType.MouseReleased);
	}

	public void mouseMove(int mouseX, int mouseY)
	{
		boolean wasHovered = this.isHovered();
		this.setHovered(mouseX >= this.getXScaled() && mouseY >= this.getYScaled() && mouseX < this.getXScaled() + this.getWidthScaled() && mouseY < this.getYScaled() + this.getHeightScaled());
		this.fireEvent(AOTDActionListener.ActionType.MouseMove);
		if (wasHovered && !this.isHovered())
			this.fireEvent(AOTDActionListener.ActionType.MouseExitBoundingBox);
		if (!wasHovered && this.isHovered())
			this.fireEvent(AOTDActionListener.ActionType.MouseEnterBoundingBox);
	}

	public void keyPressed()
	{
		this.fireEvent(ActionType.KeyTyped);
	}

	public void fireEvent(AOTDActionListener.ActionType actionType)
	{
		for (AOTDActionListener actionListener : this.actionListeners)
		{
			actionListener.actionPerformed(this, actionType);
		}
	}

	public void addActionListener(AOTDActionListener actionListener)
	{
		this.actionListeners.add(actionListener);
	}
}
