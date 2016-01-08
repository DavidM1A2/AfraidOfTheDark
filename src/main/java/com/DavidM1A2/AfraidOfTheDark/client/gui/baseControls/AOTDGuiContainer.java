/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls;

import java.util.ArrayList;
import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDActionListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDActionListener.ActionType;

public abstract class AOTDGuiContainer extends AOTDGuiComponent
{
	private final List<AOTDGuiContainer> subComponents = new ArrayList<AOTDGuiContainer>();
	private AOTDGuiContainer parent = null;
	private boolean mousePressed = false;
	private boolean mouseReleased = false;
	private boolean mouseMove = false;

	public AOTDGuiContainer(int x, int y, int width, int height)
	{
		super(x, y, width, height);
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

	public void add(AOTDGuiContainer component)
	{
		component.setParent(this);
		component.setX(component.getX() + component.getParent().getX());
		component.setY(component.getY() + component.getParent().getY());
		this.subComponents.add(component);
	}

	public void update(int mouseX, int mouseY)
	{
		boolean wasHovered = this.isHovered();
		this.setHovered(mouseX >= this.getXScaled() && mouseY >= this.getYScaled() && mouseX < this.getXScaled() + this.getWidthScaled() && mouseY < this.getYScaled() + this.getHeightScaled());
		if (this.isHovered())
			this.fireEvent(AOTDActionListener.ActionType.MouseHover);
		if (this.mousePressed)
			this.fireEvent(AOTDActionListener.ActionType.MousePressed);
		if (this.mouseReleased)
			this.fireEvent(AOTDActionListener.ActionType.MouseReleased);
		if (mouseMove)
			this.fireEvent(AOTDActionListener.ActionType.MouseMove);
		if (wasHovered && !this.isHovered())
			this.fireEvent(AOTDActionListener.ActionType.MouseExitBoundingBox);
		if (!wasHovered && this.isHovered())
			this.fireEvent(AOTDActionListener.ActionType.MouseEnterBoundingBox);

		for (AOTDGuiContainer component : this.subComponents)
		{
			component.update(mouseX, mouseY);
		}

		this.mousePressed = false;
		this.mouseReleased = false;
		this.mouseMove = false;
	}

	public void mousePressed()
	{
		this.mousePressed = true;
		for (AOTDGuiContainer component : this.subComponents)
			component.mousePressed();
	}

	public void mouseReleased()
	{
		this.mouseReleased = true;
		for (AOTDGuiContainer component : this.subComponents)
			component.mouseReleased();
	}

	public void mouseMove()
	{
		this.mouseMove = true;
		for (AOTDGuiContainer component : this.subComponents)
			component.mouseMove();
	}

	public void keyPressed()
	{
		this.fireEvent(ActionType.KeyTyped);
		for (AOTDGuiContainer component : this.subComponents)
			component.keyPressed();
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
	public void setX(int x)
	{
		for (AOTDGuiContainer component : this.subComponents)
		{
			int xWithoutTransform = component.getXWithoutParentTransform();
			component.setX(xWithoutTransform + x);
		}
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
