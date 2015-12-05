/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.Gui;

public abstract class AOTDGuiComponent
{
	private int x;
	private int y;
	private int xPosScaled = 0;
	private int yPosScaled = 0;
	private int width;
	private int height;
	private int widthScaled = 0;
	private int heightScaled = 0;
	private String name;
	private double scaleX = 1.0;
	private double scaleY = 1.0;

	public AOTDGuiComponent()
	{
		this.x = 0;
		this.y = 0;
		this.width = 0;
		this.height = 0;
	}

	public AOTDGuiComponent(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void draw()
	{
		GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
	}

	public void drawBoundingBox()
	{
		Gui.drawRect(this.getXScaled(), this.getYScaled(), this.getXScaled() + this.getWidthScaled(), this.getYScaled() + 1, 0xFFFFFFFF);
		Gui.drawRect(this.getXScaled(), this.getYScaled(), this.getXScaled() + 1, this.getYScaled() + this.getHeightScaled(), 0xFFFFFFFF);
		Gui.drawRect(this.getXScaled() + this.getWidthScaled() - 1, this.getYScaled(), this.getXScaled() + this.getWidthScaled(), this.getYScaled() + this.getHeightScaled(), 0xFFFFFFFF);
		Gui.drawRect(this.getXScaled(), this.getYScaled() + this.getHeightScaled() - 1, this.getXScaled() + this.getWidthScaled(), this.getYScaled() + this.getHeightScaled(), 0xFFFFFFFF);
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name.isEmpty() ? Integer.toString(this.hashCode()) : this.name;
	}

	public void setScaleXAndY(double scale)
	{
		this.setScaleX(scale);
		this.setScaleY(scale);
	}

	public void setScaleX(double scaleX)
	{
		this.scaleX = scaleX;
		this.calculateNewWidthAndX();
	}

	public void setScaleY(double scaleY)
	{
		this.scaleY = scaleY;
		this.calculateNewHeightAndY();
	}

	private void calculateNewWidthAndX()
	{
		this.xPosScaled = (int) Math.round(this.scaleX * this.x);
		this.widthScaled = (int) Math.round(this.scaleX * this.width);
	}

	private void calculateNewHeightAndY()
	{
		this.yPosScaled = (int) Math.round(this.scaleY * this.y);
		this.heightScaled = (int) Math.round(this.scaleY * this.height);
	}

	public double getScaleX()
	{
		return this.scaleX;
	}

	public double getScaleY()
	{
		return this.scaleX;
	}

	public void setX(int x)
	{
		this.x = x;
		this.calculateNewWidthAndX();
	}

	public int getXScaled()
	{
		return this.xPosScaled;
	}

	public int getX()
	{
		return this.x;
	}

	public void setY(int y)
	{
		this.y = y;
		this.calculateNewHeightAndY();
	}

	public int getYScaled()
	{
		return this.yPosScaled;
	}

	public int getY()
	{
		return this.y;
	}

	public void setWidth(int width)
	{
		this.width = width;
		this.calculateNewWidthAndX();
	}

	public int getWidthScaled()
	{
		return widthScaled;
	}

	public int getWidth()
	{
		return this.width;
	}

	public void setHeight(int height)
	{
		this.height = height;
		this.calculateNewHeightAndY();
	}

	public int getHeightScaled()
	{
		return this.heightScaled;
	}

	public int getHeight()
	{
		return this.height;
	}
}
