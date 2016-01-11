/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls;

import org.lwjgl.input.Mouse;

import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDGuiUtility;

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

	public void mouseInputEvent()
	{
		switch (Mouse.getEventButton())
		{
			case 0:
				if (Mouse.getEventButtonState())
				{
					panel.mousePressed();
				}
				else
				{
					panel.mouseReleased();
				}
				break;
			case -1:
				panel.mouseMove(AOTDGuiUtility.getMouseX(), AOTDGuiUtility.getMouseY());
		}
	}
}
