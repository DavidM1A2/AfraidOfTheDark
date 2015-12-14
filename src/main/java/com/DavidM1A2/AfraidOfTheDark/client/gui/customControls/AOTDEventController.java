/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

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
		panel.update(mouseX, mouseY);
	}

	/**
	 * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
	 */
	public void mouseReleased(int mouseX, int mouseY)
	{
		if (panel.isHovered())
		{
			panel.mouseReleased();
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
			panel.mousePressed();
		}
		return false;
	}
}
