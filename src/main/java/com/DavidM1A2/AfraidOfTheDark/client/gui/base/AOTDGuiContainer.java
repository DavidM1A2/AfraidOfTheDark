package com.DavidM1A2.afraidofthedark.client.gui.base;

import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDKeyEvent;
import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDMouseEvent;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class AOTDGuiContainer extends AOTDGuiComponentWithEvents
{
	// A list of sub-components found inside this container
	private final List<AOTDGuiContainer> subComponents = new LinkedList<>();
	// The parent of this container
	private AOTDGuiContainer parent = null;

	/**
	 * Constructor initializes the bounding box
	 *
	 * @param x      The X location of the top left corner
	 * @param y      The Y location of the top left corner
	 * @param width  The width of the component
	 * @param height The height of the component
	 */
	public AOTDGuiContainer(Integer x, Integer y, Integer width, Integer height)
	{
		super(x, y, width, height);
	}

	/**
	 * Adds a given gui component to the container, and position it accordingly
	 *
	 * @param container The container to add
	 */
	public void add(AOTDGuiContainer container)
	{
		// Set the container's parent
		container.setParent(this);
		// Set the X position based on the parent's X
		container.setX(container.getX() + this.getX());
		// Set the Y position based on the parent's Y
		container.setY(container.getY() + this.getY());
		// Set the scale based on the parent's scale
		container.setScaleX(this.getScaleX());
		container.setScaleY(this.getScaleY());
		// Add the sub-component
		this.subComponents.add(container);
	}

	/**
	 * Removes a given gui component to the container, and position it accordingly
	 *
	 * @param container The container to remove
	 */
	public void remove(AOTDGuiContainer container)
	{
		if (!this.subComponents.contains(container))
			return;
		this.subComponents.remove(container);
		// Reset the X without any parent transform
		container.setX(container.getXWithoutParentTransform());
		container.setY(container.getYWithoutParentTransform());
		// Set the container's parent
		container.setParent(null);
	}

	/**
	 * @return Returns the unmodifiable list of sub-components. If you want to add to this list, please use the AOTDGuiContainer.add instead
	 */
	public List<AOTDGuiContainer> getChildren()
	{
		return Collections.unmodifiableList(this.subComponents);
	}

	/**
	 * @return Returns the raw X component of the container
	 */
	public int getXWithoutParentTransform()
	{
		return this.parent == null ? this.getX() : this.getX() - this.parent.getX();
	}

	/**
	 * @return Returns the raw Y component of the container
	 */
	public int getYWithoutParentTransform()
	{
		return this.parent == null ? this.getY() : this.getY() - this.parent.getY();
	}

	/**
	 * Draw function that gets called every frame. We want to draw all sub-components, so do that here
	 */
	@Override
	public void draw()
	{
		// Draw our component
		super.draw();
		// Then draw children
		this.subComponents.forEach(AOTDGuiContainer::draw);
	}

	/**
	 * Draws the hover text that appears when we mouse over the control, also draws all sub-child hover text
	 */
	@Override
	public void drawOverlay()
	{
		// Draw our component's hover text
		super.drawOverlay();
		// Draw our children's hover text
		this.subComponents.forEach(AOTDGuiContainer::drawOverlay);
	}

	/**
	 * Sets the X position of this gui container
	 *
	 * @param x The new x position of the component
	 */
	@Override
	public void setX(int x)
	{
		// Update all sub-components using the OLD x value of this component
		this.subComponents.forEach(subComponent -> subComponent.setX(subComponent.getXWithoutParentTransform() + x));
		// Then, update the x of the CURRENT component
		super.setX(x);
	}

	/**
	 * Sets the Y position of this gui container
	 *
	 * @param y The new y position of the component
	 */
	@Override
	public void setY(int y)
	{
		// Update all sub-components using the OLD y value of this component
		this.subComponents.forEach(subComponent -> subComponent.setY(subComponent.getYWithoutParentTransform() + y));
		// Then, update the y of the CURRENT component
		super.setY(y);
	}

	/**
	 * When we get mouse input we make sure to fire it over all our children too
	 *
	 * @param event The event to process
	 */
	@Override
	public void processMouseInput(AOTDMouseEvent event)
	{
		// Fire our component's mouse input
		super.processMouseInput(event);
		// Fire our sub-component's mouse input events
		this.subComponents.forEach(subContainer -> subContainer.processMouseInput(event));
	}

	/**
	 * When we get key input we make sure to fire it over all our children too
	 *
	 * @param event The event to process
	 */
	@Override
	public void processKeyInput(AOTDKeyEvent event)
	{
		// Fire our component's key input
		super.processKeyInput(event);
		// Fire our sub-component's key input events
		this.subComponents.forEach(subContainer -> subContainer.processKeyInput(event));
	}

	/**
	 * Setter for X and Y scale, also updates the scaled bounding box and all sub-components
	 *
	 * @param scale The new X and Y scale
	 */
	@Override
	public void setScaleXAndY(double scale)
	{
		// Set our X and Y scale
		super.setScaleXAndY(scale);
		// Sets our sub-components X and Y scale
		this.subComponents.forEach(subContainer -> subContainer.setScaleXAndY(scale));
	}

	/**
	 * Setter for X scale, also updates the scaled bounding box and all sub-components
	 *
	 * @param scaleX The new X scale to use
	 */
	@Override
	public void setScaleX(double scaleX)
	{
		// Set our X scale
		super.setScaleX(scaleX);
		// Sets our sub-components X scale
		this.subComponents.forEach(subContainer -> subContainer.setScaleX(scaleX));
	}

	/**
	 * Setter for Y scale, also updates the scaled bounding box and all sub-components
	 *
	 * @param scaleY The new Y scale to use
	 */
	@Override
	public void setScaleY(double scaleY)
	{
		// Set our Y scale
		super.setScaleY(scaleY);
		// Sets our sub-components Y scale
		this.subComponents.forEach(subContainer -> subContainer.setScaleY(scaleY));
	}

	/**
	 * @param parent Sets the parent transform that this container uses as a base
	 */
	public void setParent(AOTDGuiContainer parent)
	{
		this.parent = parent;
	}

	/**
	 * @return Gets the parent transform
	 */
	public AOTDGuiContainer getParent()
	{
		return this.parent;
	}
}
