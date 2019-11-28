package com.davidm1a2.afraidofthedark.client.gui.base

import com.davidm1a2.afraidofthedark.client.gui.events.AOTDKeyEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseScrollEvent
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Base class for all GUI containers. Containers are gui components that are made up of other components inside
 *
 * @constructor initializes the bounding box
 * @param x      The X location of the top left corner
 * @param y      The Y location of the top left corner
 * @param width  The width of the component
 * @param height The height of the component
 * @property subComponents A list of sub-components found inside this container. Use a CopyOnWriteArrayList so we can modify it while iterating over it. The CopyOnWriteArrayList is very efficient when iterating, but costly when adding/removing elements. We iterate mostly, so it's a good choice here
 * @property parent The parent transform that this container uses as a base
 * @property children The unmodifiable list of sub-components. If you want to add to this list, please use the AOTDGuiContainer.add instead
 * @property xWithoutParentTransform Returns the raw X component of the container
 * @property yWithoutParentTransform Returns the raw Y component of the container
 */
abstract class AOTDGuiContainer(x: Int, y: Int, width: Int, height: Int) : AOTDGuiComponentWithEvents(x, y, width, height)
{
    private val subComponents = CopyOnWriteArrayList<AOTDGuiContainer>()
    var parent: AOTDGuiContainer? = null
    val children: List<AOTDGuiContainer>
        get() = Collections.unmodifiableList(this.subComponents)

    private val xWithoutParentTransform: Int
        get() = if (this.parent == null) this.x else this.x - this.parent!!.x
    private val yWithoutParentTransform: Int
        get() = if (this.parent == null) this.y else this.y - this.parent!!.y

    override var x: Int
        get() = super.x
        set(x)
        {
            // Update all sub-components using the OLD x value of this component
            this.subComponents.forEach { subComponent -> subComponent.x = subComponent.xWithoutParentTransform + x }
            // Then, update the x of the CURRENT component
            super.x = x
        }

    override var y: Int
        get() = super.y
        set(y)
        {
            // Update all sub-components using the OLD y value of this component
            this.subComponents.forEach { subComponent -> subComponent.y = subComponent.yWithoutParentTransform + y }
            // Then, update the y of the CURRENT component
            super.y = y
        }

    override var isVisible: Boolean
        get() = super.isVisible
        set(isVisible)
        {
            // Set our visibility
            super.isVisible = isVisible
            // Sets our sub-components visibility
            this.subComponents.forEach { subContainer -> subContainer.isVisible = isVisible }
        }

    /**
     * Adds a given gui component to the container, and position it accordingly
     *
     * @param container The container to add
     */
    fun add(container: AOTDGuiContainer)
    {
        // Set the container's parent
        container.parent = this
        // Set the X position based on the parent's X
        container.x = container.x + this.x
        // Set the Y position based on the parent's Y
        container.y = container.y + this.y
        // Set the scale based on the parent's scale
        container.setScaleX(this.scaleX)
        container.setScaleY(this.scaleY)
        // Add the sub-component
        this.subComponents.add(container)
    }

    /**
     * Removes a given gui component to the container, and position it accordingly
     *
     * @param container The container to remove
     */
    fun remove(container: AOTDGuiContainer)
    {
        if (!this.subComponents.contains(container))
        {
            return
        }
        this.subComponents.remove(container)
        // Reset the X without any parent transform
        container.x = container.xWithoutParentTransform
        container.y = container.yWithoutParentTransform
        // Set the container's parent
        container.parent = null
    }

    /**
     * Draw function that gets called every frame. We want to draw all sub-components, so do that here
     */
    override fun draw()
    {
        // Draw our component
        super.draw()
        // Then draw children
        this.subComponents.forEach { it.draw() }
    }

    /**
     * Draws the hover text that appears when we mouse over the control, also draws all sub-child hover text
     */
    override fun drawOverlay()
    {
        // Draw our component's hover text
        super.drawOverlay()
        // Draw our children's hover text
        this.subComponents.forEach { it.drawOverlay() }
    }

    /**
     * When we get mouse input we make sure to fire it over all our children too
     *
     * @param event The event to process
     */
    override fun processMouseInput(event: AOTDMouseEvent)
    {
        // Fire our component's mouse input
        super.processMouseInput(event)
        // Fire our sub-component's mouse input events
        this.subComponents.forEach { it.processMouseInput(event) }
    }

    /**
     * Called to process a mouse move input event
     *
     * @param event The event to process
     */
    override fun processMouseMoveInput(event: AOTDMouseMoveEvent)
    {
        // Fire our component's mouse move input
        super.processMouseMoveInput(event)
        // Fire our sub-component's mouse move input events
        this.subComponents.forEach { it.processMouseMoveInput(event) }
    }

    /**
     * Called to process a mouse scroll input event
     *
     * @param event The event to process
     */
    override fun processMouseScrollInput(event: AOTDMouseScrollEvent)
    {
        // Fire our component's mouse scroll input
        super.processMouseScrollInput(event)
        // Fire our sub-component's mouse scroll input events
        this.subComponents.forEach { it.processMouseScrollInput(event) }
    }

    /**
     * When we get key input we make sure to fire it over all our children too
     *
     * @param event The event to process
     */
    override fun processKeyInput(event: AOTDKeyEvent)
    {
        // Fire our component's key input
        super.processKeyInput(event)
        // Fire our sub-component's key input events
        this.subComponents.forEach { it.processKeyInput(event) }
    }

    /**
     * Setter for X and Y scale, also updates the scaled bounding box and all sub-components
     *
     * @param scale The new X and Y scale
     */
    override fun setScaleXAndY(scale: Double)
    {
        // Set our X and Y scale
        super.setScaleXAndY(scale)
        // Sets our sub-components X and Y scale
        this.subComponents.forEach { it.setScaleXAndY(scale) }
    }

    /**
     * Setter for X scale, also updates the scaled bounding box and all sub-components
     *
     * @param scaleX The new X scale to use
     */
    override fun setScaleX(scaleX: Double)
    {
        // Set our X scale
        super.setScaleX(scaleX)
        // Sets our sub-components X scale
        this.subComponents.forEach { it.setScaleX(scaleX) }
    }

    /**
     * Setter for Y scale, also updates the scaled bounding box and all sub-components
     *
     * @param scaleY The new Y scale to use
     */
    override fun setScaleY(scaleY: Double)
    {
        // Set our Y scale
        super.setScaleY(scaleY)
        // Sets our sub-components Y scale
        this.subComponents.forEach { it.setScaleY(scaleY) }
    }
}
