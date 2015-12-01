/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import org.lwjgl.opengl.GL11;

public abstract class AOTDGuiComponent
{
	private int x;
	private int y;
	private int width;
	private int height;
	private String name;

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

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name.isEmpty() ? Integer.toString(this.hashCode()) : this.name;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getX()
	{
		return this.x;
	}

	public void setY(int y)
	{
		this.y = y;
	}

	public int getY()
	{
		return this.y;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public int getWidth()
	{
		return this.width;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public int getHeight()
	{
		return this.height;
	}
}
