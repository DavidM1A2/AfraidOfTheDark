/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import java.util.LinkedList;
import java.util.List;

public class AOTDGuiPanel extends AOTDGuiComponent
{
	private final List<AOTDGuiComponent> subComponents;

	public AOTDGuiPanel()
	{
		super();
		subComponents = new LinkedList<AOTDGuiComponent>();
	}

	public AOTDGuiPanel(int x, int y, int width, int height)
	{
		super(x, y, width, height);
		subComponents = new LinkedList<AOTDGuiComponent>();
	}

	public void add(AOTDGuiComponent component)
	{
		component.setX(component.getX() + this.getX());
		component.setY(component.getY() + this.getY());
		this.subComponents.add(component);
	}

	public void remove(AOTDGuiComponent component)
	{
		component.setX(component.getX() - this.getX());
		component.setY(component.getY() - this.getY());
		this.subComponents.remove(component);
	}

	@Override
	public void setX(int x)
	{
		for (AOTDGuiComponent component : this.subComponents)
		{
			component.setX(component.getX() - this.getX() + x);
		}
		super.setX(x);
	}

	@Override
	public void setY(int y)
	{
		for (AOTDGuiComponent component : this.subComponents)
		{
			component.setY(component.getY() - this.getY() + y);
		}
		super.setY(y);
	}

	@Override
	public void setScaleX(double scaleX)
	{
		super.setScaleX(scaleX);
		for (AOTDGuiComponent component : this.subComponents)
		{
			component.setScaleX(scaleX);
		}
	}

	@Override
	public void setScaleY(double scaleY)
	{
		super.setScaleY(scaleY);
		for (AOTDGuiComponent component : this.subComponents)
		{
			component.setScaleY(scaleY);
		}
	}

	@Override
	public void draw()
	{
		super.draw();
		this.drawBoundingBox();
		for (AOTDGuiComponent component : this.subComponents)
		{
			component.draw();
		}
	}
}
