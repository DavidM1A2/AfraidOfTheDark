/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class AOTDGuiContainer extends AOTDGuiComponentWithEvents
{
	private final List<AOTDGuiContainer> subComponents = new CopyOnWriteArrayList<AOTDGuiContainer>();
	private AOTDGuiContainer parent = null;

	public AOTDGuiContainer(int x, int y, int width, int height)
	{
		super(x, y, width, height);
	}

	public void add(AOTDGuiContainer container)
	{
		container.setParent(this);
		container.setX(container.getX() + container.getParent().getX());
		container.setY(container.getY() + container.getParent().getY());
		container.setScaleX(container.getParent().getScaleX());
		container.setScaleY(container.getParent().getScaleY());
		this.subComponents.add(container);
	}

	public void remove(AOTDGuiContainer container)
	{
		if (!this.subComponents.contains(container))
			return;
		this.subComponents.remove(container);
		container.setParent(null);
	}

	public int getXWithoutParentTransform()
	{
		return (this.parent == null) ? this.getX() : (this.getX() - this.parent.getX());
	}

	public int getYWithoutParentTransform()
	{
		return (this.parent == null) ? this.getY() : (this.getY() - this.parent.getY());
	}

	@Override
	public void draw()
	{
		super.draw();
		for (AOTDGuiContainer component : this.subComponents)
		{
			component.draw();
		}
	}

	@Override
	public void setX(int x)
	{
		// Update all subcomponents using the OLD x value of this component
		for (AOTDGuiContainer component : this.subComponents)
		{
			int xWithoutTransform = component.getXWithoutParentTransform();
			component.setX(xWithoutTransform + x);
		}
		// Set the x of the CURRENT component
		super.setX(x);
	}

	@Override
	public void setY(int y)
	{
		for (AOTDGuiContainer component : this.subComponents)
		{
			int yWithoutTransform = component.getYWithoutParentTransform();
			component.setY(yWithoutTransform + y);
		}
		super.setY(y);
	}

	@Override
	public void update(int mouseX, int mouseY)
	{
		super.update(mouseX, mouseY);
		for (AOTDGuiContainer container : this.subComponents)
			container.update(mouseX, mouseY);
	}

	@Override
	public void mousePressed()
	{
		super.mousePressed();
		for (AOTDGuiContainer container : this.subComponents)
			container.mousePressed();
	}

	@Override
	public void mouseReleased()
	{
		super.mouseReleased();
		for (AOTDGuiContainer container : this.subComponents)
			container.mouseReleased();
	}

	@Override
	public void mouseMove(int mouseX, int mouseY)
	{
		super.mouseMove(mouseX, mouseY);
		for (AOTDGuiContainer container : this.subComponents)
			container.mouseMove(mouseX, mouseY);
	}

	@Override
	public void keyPressed()
	{
		super.keyPressed();
		for (AOTDGuiContainer container : this.subComponents)
			container.keyPressed();
	}

	@Override
	public void setScaleX(double scaleX)
	{
		super.setScaleX(scaleX);
		for (AOTDGuiContainer component : this.subComponents)
			component.setScaleX(scaleX);
	}

	@Override
	public void setScaleY(double scaleY)
	{
		super.setScaleY(scaleY);
		for (AOTDGuiContainer component : this.subComponents)
			component.setScaleY(scaleY);
	}

	public void setParent(AOTDGuiContainer parent)
	{
		this.parent = parent;
	}

	public AOTDGuiContainer getParent()
	{
		return this.parent;
	}
}
