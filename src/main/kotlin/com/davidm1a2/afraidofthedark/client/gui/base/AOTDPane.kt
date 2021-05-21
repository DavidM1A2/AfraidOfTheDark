package com.davidm1a2.afraidofthedark.client.gui.base

import com.davidm1a2.afraidofthedark.client.gui.events.*
import com.davidm1a2.afraidofthedark.client.gui.layout.*
import java.awt.Color
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
abstract class AOTDPane (
        offset: Position = AbsolutePosition(0.0, 0.0),
        prefSize: Dimensions = AbsoluteDimensions(0.0, 0.0),
        margins: GuiSpacing = AbsoluteSpacing(),
        gravity: GuiGravity = GuiGravity.TOP_LEFT,
        hoverTexts: Array<String> = emptyArray(),
        var padding: GuiSpacing = AbsoluteSpacing(),
        color: Color = Color(255, 255, 255, 255)) :
    AOTDGuiComponentWithEvents(offset, prefSize, margins, gravity, hoverTexts, color) {

    // Panes can have children
    private val subComponents = CopyOnWriteArrayList<AOTDGuiComponent>()

    // Special offsets so that panes can scroll with children
    var guiOffsetX = 0.0
    var guiOffsetY = 0.0

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
    open fun add(container: AOTDGuiComponent) {
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
    open fun remove(container: AOTDGuiComponent) {
        if (!this.subComponents.contains(container)) {
            return
        }
        this.subComponents.remove(container)
        // Calculate all children's positions and dimensions
        calcChildrenBounds()
    }

    /**
     * Calculates the proper locations and dimensions of all children nodes
     * Default behavior is that of a StackPane (children drawn on top of each other)
     */
    open fun calcChildrenBounds(width: Double = this.width.toDouble(), height: Double = this.height.toDouble()) {
        for (child in subComponents) {
            // Calculate padding and margins
            var calcPadding = padding
            if (calcPadding is RelativeSpacing) calcPadding = calcPadding.toAbsolute(this)
            val internalWidth = width - calcPadding.horizPx
            val internalHeight = height - calcPadding.vertPx
            var calcMargins = child.margins
            if (calcMargins is RelativeSpacing) calcMargins = calcMargins.toAbsolute(child)
            val marginWidth = calcMargins.horizPx
            val marginHeight = calcMargins.vertPx
            // Set dimensions
            child.negotiateDimensions(internalWidth - marginWidth, internalHeight - marginHeight)
            // Calculate position
            val gravityXOffset = when (child.gravity) {
                GuiGravity.TOP_LEFT, GuiGravity.CENTER_LEFT, GuiGravity.BOTTOM_LEFT -> calcPadding.left
                GuiGravity.TOP_CENTER, GuiGravity.CENTER, GuiGravity.BOTTOM_CENTER -> width/2 - (child.width + marginWidth)/2
                GuiGravity.TOP_RIGHT, GuiGravity.CENTER_RIGHT, GuiGravity.BOTTOM_RIGHT -> width - (child.width + marginWidth) - calcPadding.right
            }
            val gravityYOffset = when (child.gravity) {
                GuiGravity.TOP_LEFT, GuiGravity.TOP_CENTER, GuiGravity.TOP_RIGHT -> calcPadding.top
                GuiGravity.CENTER_LEFT, GuiGravity.CENTER, GuiGravity.CENTER_RIGHT -> height/2 - (child.height + marginHeight)/2
                GuiGravity.BOTTOM_LEFT, GuiGravity.BOTTOM_CENTER, GuiGravity.BOTTOM_RIGHT -> height - (child.height + marginHeight) - calcPadding.bot
            }
            val xOffset = if (child.offset is RelativePosition) internalWidth * child.offset.x else child.offset.x
            val yOffset = if (child.offset is RelativePosition) internalHeight * child.offset.y else child.offset.y
            // Set position
            child.x = (this.x + this.guiOffsetX + gravityXOffset + xOffset + calcMargins.left).toInt()
            child.y = (this.y + this.guiOffsetY + gravityYOffset + yOffset + calcMargins.top).toInt()
            // If it's a pane, have it recalculate its children too
            if (child is AOTDPane) child.calcChildrenBounds()
            // Determine if the subtree of children are in this node's bounds
            determineInBounds(child)
        }
    }

    private fun determineInBounds(component: AOTDGuiComponent) {
        if (!component.intersects(this.boundingBox)) component.inBounds = false
        if (component is AOTDPane) {
            for (child in component.getChildren()) {
                determineInBounds(child)
            }
        }
    }

    /**
     * @return Returns the unmodifiable list of sub-components. If you want to add to this list, please use the AOTDGuiContainer.add instead
     */
    fun getChildren(): List<AOTDGuiComponent> {
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
        this.subComponents.forEach { if (it is AOTDGuiComponentWithEvents) it.processMouseInput(event) }
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
        this.subComponents.forEach { if (it is AOTDGuiComponentWithEvents) it.processMouseMoveInput(event) }
    }

    /**
     * Called to process a mouse move input event
     *
     * @param event The event to process
     */
    override fun processMouseDragInput(event: MouseDragEvent) {
        // Fire our component's mouse move input
        super.processMouseDragInput(event)
        // Fire our sub-component's mouse move input events
        this.subComponents.forEach { if (it is AOTDGuiComponentWithEvents) it.processMouseDragInput(event) }
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
        this.subComponents.forEach { if (it is AOTDGuiComponentWithEvents) it.processMouseScrollInput(event) }
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
        this.subComponents.forEach { if (it is AOTDGuiComponentWithEvents) it.processKeyInput(event) }
    }
}
