/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDActionListener;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;

public abstract class AOTDGuiComponent
{
	private double scaleX = 1.0;
	private double scaleY = 1.0;
	private boolean isHovered = false;
	private boolean isVisible = true;
	private Rectangle boundingBox = new Rectangle();
	private Rectangle scaledBoundingBox = new Rectangle();
	private List<AOTDActionListener> actionListeners = new LinkedList<AOTDActionListener>();
	private float[] color = new float[] {1.0f, 1.0f, 1.0f, 1.0f};
	protected final EntityPlayerSP entityPlayer = Minecraft.getMinecraft().thePlayer;

	public AOTDGuiComponent(int x, int y, int width, int height)
	{
		this.boundingBox = new Rectangle(x, y, width, height);
	}

	public void draw()
	{
		GL11.glColor4d(this.getColor()[0], this.getColor()[1], this.getColor()[2], this.getColor()[3]);
		//this.drawBoundingBox();
	}

	public void drawBoundingBox()
	{
		Gui.drawRect(this.getXScaled(), this.getYScaled(), this.getXScaled() + this.getWidthScaled(), this.getYScaled() + 1, 0xFFFFFFFF);
		Gui.drawRect(this.getXScaled(), this.getYScaled(), this.getXScaled() + 1, this.getYScaled() + this.getHeightScaled(), 0xFFFFFFFF);
		Gui.drawRect(this.getXScaled() + this.getWidthScaled() - 1, this.getYScaled(), this.getXScaled() + this.getWidthScaled(), this.getYScaled() + this.getHeightScaled(), 0xFFFFFFFF);
		Gui.drawRect(this.getXScaled(), this.getYScaled() + this.getHeightScaled() - 1, this.getXScaled() + this.getWidthScaled(), this.getYScaled() + this.getHeightScaled(), 0xFFFFFFFF);
		GlStateManager.enableBlend();
	}

	public void addActionListener(AOTDActionListener actionListener)
	{
		this.actionListeners.add(actionListener);
	}

	public void fireEvent(AOTDActionListener.ActionType actionType)
	{
		for (AOTDActionListener actionListener : this.actionListeners)
		{
			actionListener.actionPerformed(this, actionType);
		}
	}

	public boolean intersects(AOTDGuiComponent other)
	{
		if (other == null)
			return false;
		return this.scaledBoundingBox.intersects(other.scaledBoundingBox);
	}

	public void setScaleXAndY(double scale)
	{
		this.setScaleX(scale);
		this.setScaleY(scale);
	}

	public void setScaleX(double scaleX)
	{
		this.scaleX = scaleX;
		this.updateBounds();
	}

	public void setScaleY(double scaleY)
	{
		this.scaleY = scaleY;
		this.updateBounds();
	}

	private void updateBounds()
	{
		int xNew = (int) Math.round(this.scaleX * this.boundingBox.x);
		int yNew = (int) Math.round(this.scaleY * this.boundingBox.y);
		int widthNew = (int) Math.round(this.scaleX * this.boundingBox.width);
		int heightNew = (int) Math.round(this.scaleY * this.boundingBox.height);
		this.scaledBoundingBox.setBounds(xNew, yNew, widthNew, heightNew);
	}

	public double getScaleX()
	{
		return this.scaleX;
	}

	public double getScaleY()
	{
		return this.scaleY;
	}

	public void setX(int x)
	{
		this.boundingBox.x = x;
		this.updateBounds();
	}

	public int getXScaled()
	{
		return this.scaledBoundingBox.x;
	}

	public int getX()
	{
		return this.boundingBox.x;
	}

	public void setY(int y)
	{
		this.boundingBox.y = y;
		this.updateBounds();
	}

	public int getYScaled()
	{
		return this.scaledBoundingBox.y;
	}

	public int getY()
	{
		return this.boundingBox.y;
	}

	public void setWidth(int width)
	{
		this.boundingBox.width = width;
		this.updateBounds();
	}

	public int getWidthScaled()
	{
		return this.scaledBoundingBox.width;
	}

	public int getWidth()
	{
		return this.boundingBox.width;
	}

	public void setHeight(int height)
	{
		this.boundingBox.height = height;
		this.updateBounds();
	}

	public int getHeightScaled()
	{
		return this.scaledBoundingBox.height;
	}

	public int getHeight()
	{
		return this.boundingBox.height;
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

	public void setColor(Color color)
	{
		this.color[0] = color.getRed() / 255.0f;
		this.color[1] = color.getBlue() / 255.0f;
		this.color[2] = color.getGreen() / 255.0f;
		this.color[3] = color.getAlpha() / 255.0f;
	}
	
	public void setColor(float[] color)
	{
		if (color.length != 4)
			return;
		this.color = color;
	}

	public float[] getColor()
	{
		return this.color;
	}
	
	public void brightenColor(float amount)
	{
		this.color[0] = MathHelper.clamp_float(this.color[0] + amount, 0, 255f);
		this.color[1] = MathHelper.clamp_float(this.color[1] + amount, 0, 255f);
		this.color[2] = MathHelper.clamp_float(this.color[2] + amount, 0, 255f);
	}
	
	public void darkenColor(float amount)
	{
		this.color[0] = MathHelper.clamp_float(this.color[0] - amount, 0, 255f);
		this.color[1] = MathHelper.clamp_float(this.color[1] - amount, 0, 255f);
		this.color[2] = MathHelper.clamp_float(this.color[2] - amount, 0, 255f);
	}

	@Override
	public String toString()
	{
		return this.getClass().getSimpleName() + " located at " + this.boundingBox + " with a scaled resolution of " + this.scaledBoundingBox;
	}
}
