/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class BookmarkButton extends AOTDGuiButton
{
	private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation("afraidofthedark:textures/gui/buttons/backToResearchButton.png");

	public BookmarkButton(int buttonId, int x, int y, int width, int height)
	{
		super(buttonId, x, y, width, height, null);
	}

	/**
	 * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent e).
	 */
	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
	{
		return this.isMouseOver();
	}
}
