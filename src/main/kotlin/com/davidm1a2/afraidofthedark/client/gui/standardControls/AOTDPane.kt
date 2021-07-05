package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility
import com.davidm1a2.afraidofthedark.client.gui.events.*
import com.davidm1a2.afraidofthedark.client.gui.layout.*
import java.awt.Color
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.roundToInt

/**
 * Base class for all GUI containers. Containers are gui components that are made up of other components inside
 */
abstract class AOTDPane (
    offset: Position = Position(0.0, 0.0),
    prefSize: Dimensions = Dimensions(1.0, 1.0),
    margins: Spacing = Spacing(),
    gravity: Gravity = Gravity.TOP_LEFT,
    hoverTexts: Array<String> = emptyArray(),
    var padding: Spacing = Spacing(),
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
     * Adds a given gui component to the container
     */
    open fun add(container: AOTDGuiComponent) {
        // Add the sub-component
        this.subComponents.add(container)
    }

    /**
     * Removes a given gui component from the container
     */
    open fun remove(container: AOTDGuiComponent) {
        if (!this.subComponents.contains(container)) {
            return
        }
        this.subComponents.remove(container)
    }

    open fun getInternalWidth(): Double {
        return width - padding.getAbsoluteOuter(this).width
    }

    open fun getInternalHeight(): Double {
        return height - padding.getAbsoluteOuter(this).height
    }

    /**
     * Calculates the proper locations and dimensions of all children nodes
     * Default behavior is that of a StackPane (children drawn on top of each other)
     */
    open fun calcChildrenBounds() {
        // Calculate padding
        val calcPadding = padding.getAbsoluteOuter(this)
        val internalWidth = this.getInternalWidth()
        val internalHeight = this.getInternalHeight()

        for (child in subComponents) {
            // Calculate margins
            val calcMargins = child.margins.getAbsoluteOuter(child)
            val marginWidth = calcMargins.width
            val marginHeight = calcMargins.height
            // Set dimensions
            child.negotiateDimensions(internalWidth - marginWidth, internalHeight - marginHeight)
            // Calculate position
            val gravityXOffset = when (child.gravity) {
                Gravity.TOP_LEFT, Gravity.CENTER_LEFT, Gravity.BOTTOM_LEFT -> calcMargins.left
                Gravity.TOP_CENTER, Gravity.CENTER, Gravity.BOTTOM_CENTER -> internalWidth/2 - (child.width + calcMargins.width)/2 + calcMargins.left
                Gravity.TOP_RIGHT, Gravity.CENTER_RIGHT, Gravity.BOTTOM_RIGHT -> internalWidth - child.width - calcMargins.right
            }
            val gravityYOffset = when (child.gravity) {
                Gravity.TOP_LEFT, Gravity.TOP_CENTER, Gravity.TOP_RIGHT -> calcMargins.top
                Gravity.CENTER_LEFT, Gravity.CENTER, Gravity.CENTER_RIGHT -> internalHeight/2 - (child.height + calcMargins.height)/2 + calcMargins.top
                Gravity.BOTTOM_LEFT, Gravity.BOTTOM_CENTER, Gravity.BOTTOM_RIGHT -> internalHeight - child.height - calcMargins.bot
            }
            val offset = child.offset.getAbsolute(this)
            // Set position
            child.x = (this.x + this.guiOffsetX + calcPadding.left + gravityXOffset + offset.x).roundToInt()
            child.y = (this.y + this.guiOffsetY + calcPadding.top + gravityYOffset + offset.y).roundToInt()
            // If it's a pane, have it recalculate its children too
            if (child is AOTDPane) child.calcChildrenBounds()
            // Determine if the subtree of children are in this node's bounds
            determineInBounds(child)
        }
    }

    fun determineInBounds(component: AOTDGuiComponent) {
        if (component is OverlayPane) return    // SPECIAL CASE: OverlayPane extends past it's ancestors' bounds
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
    override fun processMouseInput(event: MouseEvent) {
        // Fire our sub-component's mouse input events
        this.subComponents.forEach { if (it is AOTDGuiComponentWithEvents) it.processMouseInput(event) }
        // Fire our component's mouse input
        super.processMouseInput(event)
    }

    /**
     * Called to process a mouse move input event
     *
     * @param event The event to process
     */
    override fun processMouseMoveInput(event: MouseMoveEvent) {
        // Fire our sub-component's mouse move input events
        this.subComponents.forEach { if (it is AOTDGuiComponentWithEvents) it.processMouseMoveInput(event) }
        // Fire our component's mouse move input
        super.processMouseMoveInput(event)
    }

    /**
     * Called to process a mouse move input event
     *
     * @param event The event to process
     */
    override fun processMouseDragInput(event: MouseDragEvent) {
        // Fire our sub-component's mouse move input events
        this.subComponents.forEach { if (it is AOTDGuiComponentWithEvents) it.processMouseDragInput(event) }
        // Fire our component's mouse move input
        super.processMouseDragInput(event)
    }

    /**
     * Called to process a mouse scroll input event
     *
     * @param event The event to process
     */
    override fun processMouseScrollInput(event: MouseScrollEvent) {
        // Fire our sub-component's mouse scroll input events
        this.subComponents.forEach { if (it is AOTDGuiComponentWithEvents) it.processMouseScrollInput(event) }
        // Fire our component's mouse scroll input
        super.processMouseScrollInput(event)
    }

    /**
     * When we get key input we make sure to fire it over all our children too
     *
     * @param event The event to process
     */
    override fun processKeyInput(event: KeyEvent) {
        // Fire our sub-component's key input events
        this.subComponents.forEach { if (it is AOTDGuiComponentWithEvents) it.processKeyInput(event) }
        // Fire our component's key input
        super.processKeyInput(event)
    }

    override fun invalidate() {
        super.invalidate()
        this.isHovered = this.boundingBox.contains(AOTDGuiUtility.getMouseXInMCCoord(), AOTDGuiUtility.getMouseYInMCCoord())
        calcChildrenBounds()
        getChildren().forEach { it.invalidate() }
    }
}
