package com.davidm1a2.afraidofthedark.client.gui.base

import com.davidm1a2.afraidofthedark.client.gui.events.AOTDKeyEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseScrollEvent
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
 */
abstract class AOTDGuiContainer(
        width: Int,
        height: Int,
        xOffset: Int = 0,
        yOffset: Int = 0,
        margins: AOTDGuiSpacing = AOTDGuiSpacing(),
        gravity: AOTDGuiGravity = AOTDGuiGravity.TOP_LEFT,
        hoverTexts: Array<String> = emptyArray(),
        var padding: AOTDGuiSpacing = AOTDGuiSpacing()) :
        AOTDGuiComponentWithEvents(width, height, xOffset, yOffset, margins, gravity, hoverTexts) {

    private val subComponents = CopyOnWriteArrayList<AOTDGuiContainer>()
    var parent: AOTDGuiContainer? = null

    override var isVisible: Boolean
        get() = super.isVisible
        set(isVisible) {
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
    fun add(container: AOTDGuiContainer) {
        // Set the container's parent
        container.parent = this
        // Add the sub-component
        this.subComponents.add(container)
        // Calculate all children's positions and dimensions
        calcChildrenBounds()
    }

    /**
     * Removes a given gui component to the container, and position it accordingly
     *
     * @param container The container to remove
     */
    fun remove(container: AOTDGuiContainer) {
        if (!this.subComponents.contains(container)) {
            return
        }
        this.subComponents.remove(container)
        // Calculate all children's positions and dimensions
        calcChildrenBounds()
        // Set the container's parent
        container.parent = null
    }

    /**
     * Calculates the proper locations and dimensions of all children nodes
     * Default behavior is that of a StackPane (children drawn on top of each other)
     */
    private fun calcChildrenBounds() {
        for (child in subComponents) {
            // Set position
            val calculatedXOffset = when (child.gravity) {
                AOTDGuiGravity.TOP_LEFT, AOTDGuiGravity.CENTER_LEFT, AOTDGuiGravity.BOTTOM_LEFT -> padding.leftPx
                AOTDGuiGravity.TOP_CENTER, AOTDGuiGravity.CENTER, AOTDGuiGravity.BOTTOM_CENTER -> this.width/2 - child.width/2
                AOTDGuiGravity.TOP_RIGHT, AOTDGuiGravity.CENTER_RIGHT, AOTDGuiGravity.BOTTOM_RIGHT -> this.width - child.width - padding.rightPx
            }
            val calculatedYOffset = when (child.gravity) {
                AOTDGuiGravity.TOP_LEFT, AOTDGuiGravity.TOP_CENTER, AOTDGuiGravity.TOP_RIGHT -> padding.topPx
                AOTDGuiGravity.CENTER_LEFT, AOTDGuiGravity.CENTER, AOTDGuiGravity.CENTER_RIGHT -> this.height/2 - child.height/2
                AOTDGuiGravity.BOTTOM_LEFT, AOTDGuiGravity.BOTTOM_CENTER, AOTDGuiGravity.BOTTOM_RIGHT -> this.height - child.height - padding.botPx
            }
            child.x = this.x + this.xOffset + calculatedXOffset + child.xOffset
            child.y = this.y + this.yOffset + calculatedYOffset + child.yOffset
            // Set dimensions
            val internalWidth = this.width - padding.horizPx
            val internalHeight = this.height - padding.vertPx
            child.negotiateDimensions(internalWidth, internalHeight)
        }
    }

    /**
     * Adjust this component to fit into the given space
     */
    override fun negotiateDimensions(width: Int, height: Int) {
        this.width = width
        this.height = height
        calcChildrenBounds()
    }

    /**
     * @return Returns the unmodifiable list of sub-components. If you want to add to this list, please use the AOTDGuiContainer.add instead
     */
    fun getChildren(): List<AOTDGuiContainer> {
        return subComponents.toList()
    }

    /**
     * Draw function that gets called every frame. We want to draw all sub-components, so do that here
     */
    override fun draw() {
        // Draw our component
        super.draw()
        // Then draw children
        this.subComponents.forEach { it.draw() }
    }

    /**
     * Draws the hover text that appears when we mouse over the control, also draws all sub-child hover text
     */
    override fun drawOverlay() {
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
    override fun processMouseInput(event: AOTDMouseEvent) {
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
    override fun processMouseMoveInput(event: AOTDMouseMoveEvent) {
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
    override fun processMouseScrollInput(event: AOTDMouseScrollEvent) {
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
    override fun processKeyInput(event: AOTDKeyEvent) {
        // Fire our component's key input
        super.processKeyInput(event)
        // Fire our sub-component's key input events
        this.subComponents.forEach { it.processKeyInput(event) }
    }
}
