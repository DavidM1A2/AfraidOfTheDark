/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;

public abstract class AOTDGuiButton extends AOTDGuiTextComponent
{
	private final int buttonId;
	private boolean isVisible;
	private boolean isHovered;

	public AOTDGuiButton(int buttonId, int x, int y, int width, int height, TrueTypeFont font)
	{
		super(x, y, width, height, font);
		this.buttonId = buttonId;
		this.setVisible(true);
	}

	/**
	 * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
	 */
	public void mouseDragged(Minecraft mc, int mouseX, int mouseY)
	{
	}

	/**
	 * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
	 */
	public void mouseReleased(int mouseX, int mouseY)
	{
	}

	/**
	 * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent e).
	 */
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
	{
		return this.isVisible() && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.getWidth() && mouseY < this.getY() + this.getHeight();
	}

	public void playPressSound(SoundHandler soundHandlerIn)
	{
		soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
	}

	/**
	 * Whether the mouse cursor is currently over the button.
	 */
	public boolean isMouseOver()
	{
		return this.isHovered;
	}

	public int getId()
	{
		return buttonId;
	}

	public boolean isVisible()
	{
		return isVisible;
	}

	public void setVisible(boolean isVisible)
	{
		this.isVisible = isVisible;
	}

	public boolean isHovered()
	{
		return isHovered;
	}

	public void setHovered(boolean isHovered)
	{
		this.isHovered = isHovered;
	}
}
