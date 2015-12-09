/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import java.util.LinkedList;
import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDActionListener.ActionType;

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

	public List<AOTDGuiComponent> getComponents()
	{
		return this.subComponents;
	}

	@Override
	public void fireEvent(ActionType actionType)
	{
		super.fireEvent(actionType);
		for (AOTDGuiComponent component : this.subComponents)
		{
			if (component.isHovered())
			{
				component.fireEvent(actionType);
			}
		}
	}

	public void add(AOTDGuiComponent component)
	{
		component.setX(component.getX() + this.getX());
		component.setY(component.getY() + this.getY());
		this.subComponents.add(component);
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
		for (AOTDGuiComponent component : this.subComponents)
		{
			component.draw();
		}
	}

	public void onHover(int mouseX, int mouseY)
	{
		this.setHovered(mouseX >= this.getXScaled() && mouseY >= this.getYScaled() && mouseX < this.getXScaled() + this.getWidthScaled() && mouseY < this.getYScaled() + this.getHeightScaled());
		for (AOTDGuiComponent component : this.subComponents)
		{
			if (component instanceof AOTDGuiPanel)
				((AOTDGuiPanel) component).onHover(mouseX, mouseY);
			else
				component.setHovered(mouseX >= component.getXScaled() && mouseY >= component.getYScaled() && mouseX < component.getXScaled() + component.getWidthScaled() && mouseY < component.getYScaled() + component.getHeightScaled());
		}
	}
}
