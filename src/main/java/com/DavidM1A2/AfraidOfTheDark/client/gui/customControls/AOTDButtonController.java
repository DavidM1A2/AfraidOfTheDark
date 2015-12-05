/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class AOTDButtonController extends GuiButton
{
	private List<AOTDGuiButton> buttons = new ArrayList<AOTDGuiButton>();

	public AOTDButtonController()
	{
		super(0, 0, 0, 0, 0, "");
	}

	public void add(AOTDGuiButton button)
	{
		this.buttons.add(button);
	}

	public List<AOTDGuiButton> getButtons()
	{
		return this.buttons;
	}

	public void clear()
	{
		this.buttons.clear();
	}

	/**
	 * Draws this button to the screen.
	 */
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY)
	{
		for (AOTDGuiButton button : this.buttons)
		{
			button.setHovered(mouseX >= button.getXScaled() && mouseY >= button.getYScaled() && mouseX < button.getXScaled() + button.getWidthScaled() && mouseY < button.getYScaled() + button.getHeightScaled());
			button.mouseDragged(mc, mouseX, mouseY);
		}
	}

	/**
	 * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
	 */
	public void mouseReleased(int mouseX, int mouseY)
	{
		for (AOTDGuiButton button : this.buttons)
		{
			if (button.isHovered())
			{
				button.mouseReleased(mouseX, mouseY);
			}
		}
	}

	/**
	 * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent e).
	 */
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
	{
		for (AOTDGuiButton button : this.buttons)
		{
			if (button.mousePressed(mc, mouseX, mouseY))
			{
				button.playPressSound(mc.getSoundHandler());
				button.mousePressed();
			}
		}
		return false;
	}
}
