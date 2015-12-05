/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDActionListener;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class AOTDGuiButton extends AOTDGuiTextComponent
{
	private boolean isVisible;
	private boolean isHovered;
	private List<AOTDActionListener> actionListeners = new LinkedList<AOTDActionListener>();
	private ResourceLocation icon;

	public AOTDGuiButton()
	{
		super();
		this.setVisible(false);
		this.icon = null;
	}

	public AOTDGuiButton(int x, int y, int width, int height, TrueTypeFont font, String icon)
	{
		super(x, y, width, height, font);
		this.setVisible(true);
		if (icon == null || icon.isEmpty())
			this.icon = null;
		else
			this.icon = new ResourceLocation(icon);
	}

	@Override
	public void draw()
	{
		if (icon != null)
		{
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.blendFunc(770, 771);
			if (this.isVisible())
			{
				Minecraft.getMinecraft().getTextureManager().bindTexture(this.icon);
				Gui.drawScaledCustomSizeModalRect(this.getXScaled(), this.getYScaled(), 0, 0, this.getWidth(), this.getWidth(), this.getWidthScaled(), this.getHeightScaled(), this.getWidth(), this.getWidth());
			}
		}
		if (this.isHovered())
		{
			for (AOTDActionListener actionListener : this.actionListeners)
			{
				actionListener.actionPerformed(this, AOTDActionListener.ActionType.MouseHover);
			}
		}
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

	public void mousePressed()
	{
		for (AOTDActionListener actionListener : this.actionListeners)
		{
			actionListener.actionPerformed(this, AOTDActionListener.ActionType.MouseClick);
		}
	}

	/**
	 * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent e).
	 */
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
	{
		return this.isVisible() && mouseX >= this.getXScaled() && mouseY >= this.getYScaled() && mouseX < this.getXScaled() + this.getWidthScaled() && mouseY < this.getYScaled() + this.getHeightScaled();
	}

	public void playPressSound(SoundHandler soundHandlerIn)
	{
		soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
	}

	public void addActionListener(AOTDActionListener actionListener)
	{
		this.actionListeners.add(actionListener);
	}

	/**
	 * Whether the mouse cursor is currently over the button.
	 */
	public boolean isMouseOver()
	{
		return this.isHovered;
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
