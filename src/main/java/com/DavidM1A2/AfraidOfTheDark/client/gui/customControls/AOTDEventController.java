/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDActionListener;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class AOTDEventController extends GuiButton
{
	private final AOTDGuiPanel panel;

	public AOTDEventController(AOTDGuiPanel panel)
	{
		super(0, 0, 0, 0, 0, "");
		this.panel = panel;
	}

	/**
	 * Draws this button to the screen.
	 */
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY)
	{
		panel.onHover(mouseX, mouseY);
		if (panel.isHovered())
		{
			panel.fireEvent(AOTDActionListener.ActionType.MouseHover);
		}
	}

	/**
	 * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
	 */
	public void mouseReleased(int mouseX, int mouseY)
	{
		if (panel.isHovered())
		{
			panel.fireEvent(AOTDActionListener.ActionType.MouseReleased);
		}
	}

	/**
	 * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent e).
	 */
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
	{
		if (panel.isHovered())
		{
			panel.fireEvent(AOTDActionListener.ActionType.MousePressed);
		}
		return false;
	}
}
