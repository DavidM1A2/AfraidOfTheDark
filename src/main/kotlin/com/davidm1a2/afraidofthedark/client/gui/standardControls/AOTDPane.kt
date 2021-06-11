package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.events.*
import com.davidm1a2.afraidofthedark.client.gui.layout.*
import java.awt.Color
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.roundToInt

/**
 * Base class for all GUI containers. Containers are gui components that are made up of other components inside
 */
abstract class AOTDPane (
        offset: Position = AbsolutePosition(0.0, 0.0),
        prefSize: Dimensions = RelativeDimensions(1.0, 1.0),
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
        var calcPadding = padding
        if (calcPadding is RelativeSpacing) calcPadding = calcPadding.toAbsolute(this)
        return width - calcPadding.horizPx
    }

    open fun getInternalHeight(): Double {
        var calcPadding = padding
        if (calcPadding is RelativeSpacing) calcPadding = calcPadding.toAbsolute(this)
        return height - calcPadding.vertPx
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
                GuiGravity.TOP_LEFT, GuiGravity.CENTER_LEFT, GuiGravity.BOTTOM_LEFT -> calcPadding.left + calcMargins.left
                GuiGravity.TOP_CENTER, GuiGravity.CENTER, GuiGravity.BOTTOM_CENTER -> width/2 - (child.width + calcMargins.horizPx)/2 + calcMargins.left
                GuiGravity.TOP_RIGHT, GuiGravity.CENTER_RIGHT, GuiGravity.BOTTOM_RIGHT -> width - child.width - calcMargins.right - calcPadding.right
            }
            val gravityYOffset = when (child.gravity) {
                GuiGravity.TOP_LEFT, GuiGravity.TOP_CENTER, GuiGravity.TOP_RIGHT -> calcPadding.top + calcMargins.top
                GuiGravity.CENTER_LEFT, GuiGravity.CENTER, GuiGravity.CENTER_RIGHT -> height/2 - (child.height + calcMargins.vertPx)/2 + calcMargins.top
                GuiGravity.BOTTOM_LEFT, GuiGravity.BOTTOM_CENTER, GuiGravity.BOTTOM_RIGHT -> height - child.height - calcMargins.bot - calcPadding.bot
            }
            val xOffset = if (child.offset is RelativePosition) internalWidth * child.offset.x else child.offset.x
            val yOffset = if (child.offset is RelativePosition) internalHeight * child.offset.y else child.offset.y
            // Set position
            child.x = (this.x + this.guiOffsetX + gravityXOffset + xOffset).roundToInt()
            child.y = (this.y + this.guiOffsetY + gravityYOffset + yOffset).roundToInt()
            // If it's a pane, have it recalculate its children too
            if (child is AOTDPane) child.calcChildrenBounds()
            // Determine if the subtree of children are in this node's bounds
            determineInBounds(child)
        }
    }

    fun determineInBounds(component: AOTDGuiComponent) {
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
    override fun processMouseMoveInput(event: MouseMoveEvent) {
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
    override fun processMouseScrollInput(event: MouseScrollEvent) {
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
    override fun processKeyInput(event: KeyEvent) {
        // Fire our component's key input
        super.processKeyInput(event)
        // Fire our sub-component's key input events
        this.subComponents.forEach { if (it is AOTDGuiComponentWithEvents) it.processKeyInput(event) }
    }

    override fun invalidate() {
        calcChildrenBounds()
        getChildren().forEach { it.invalidate() }
        super.invalidate()
    }
}