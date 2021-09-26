package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.events.KeyEvent
import com.davidm1a2.afraidofthedark.client.gui.events.MouseDragEvent
import com.davidm1a2.afraidofthedark.client.gui.events.MouseEvent
import com.davidm1a2.afraidofthedark.client.gui.events.MouseMoveEvent
import com.davidm1a2.afraidofthedark.client.gui.events.MouseScrollEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
import java.awt.Color
import java.awt.Point

/**
 * A base class for any GUI components that can listen for action events like mouse clicks
 */
abstract class AOTDGuiComponentWithEvents(
    offset: Position = Position(0.0, 0.0),
    prefSize: Dimensions = Dimensions(0.0, 0.0),
    margins: Spacing = Spacing(),
    gravity: Gravity = Gravity.TOP_LEFT,
    hoverTexts: Array<String> = emptyArray(),
    color: Color = Color(255, 255, 255, 255)
) : AOTDGuiComponent(offset, prefSize, margins, gravity, hoverTexts, color) {

    private var mouseListeners = mutableListOf<(MouseEvent) -> Unit>()
    private var mouseMoveListeners = mutableListOf<(MouseMoveEvent) -> Unit>()
    private var mouseDragListeners = mutableListOf<(MouseDragEvent) -> Unit>()
    private var mouseScrollListeners = mutableListOf<(MouseScrollEvent) -> Unit>()
    private var keyListeners = mutableListOf<(KeyEvent) -> Unit>()

    init {
        // Add a mouse move listener to this control that allows us to fire off events whenever conditions are met
        this.addMouseMoveListener {
            // If we move the mouse also fire other related events
            if (it.eventType == MouseMoveEvent.EventType.Move) {
                // Grab the source of the event
                val component = it.source
                // Store the flag telling us if the component was hovered
                val wasHovered = component.isHovered
                // Set the hovered flag based on if we intersect the component
                component.isHovered = component.isVisible && component.inBounds && component.intersects(Point(it.mouseX, it.mouseY))
                // Fire mouse enter/exit events if our mouse entered or exited the control
                if (component.isHovered && !wasHovered) {
                    component.processMouseMoveInput(
                        MouseMoveEvent(
                            component,
                            it.mouseX,
                            it.mouseY,
                            MouseMoveEvent.EventType.Enter
                        )
                    )
                }
                if (!component.isHovered && wasHovered) {
                    component.processMouseMoveInput(
                        MouseMoveEvent(
                            component,
                            it.mouseX,
                            it.mouseY,
                            MouseMoveEvent.EventType.Exit
                        )
                    )
                }
            }
        }
    }

    /**
     * Called to process a mouse input event
     *
     * @param event The event to process
     */
    open fun processMouseInput(event: MouseEvent) {
        // If the event is consumed, don't do anything
        if (event.isConsumed) {
            return
        }

        // We set the source to be this component, because we are processing it
        event.source = this

        mouseListeners.forEach { it(event) }
    }

    /**
     * Called to process a mouse move input event
     *
     * @param event The event to process
     */
    open fun processMouseMoveInput(event: MouseMoveEvent) {
        // If the event is consumed, don't do anything
        if (event.isConsumed) {
            return
        }

        // We set the source to be this component, because we are processing it
        event.source = this

        mouseMoveListeners.forEach { it(event) }
    }

    /**
     * Called to process a mouse drag input event
     *
     * @param event The event to process
     */
    open fun processMouseDragInput(event: MouseDragEvent) {
        // If the event is consumed, don't do anything
        if (event.isConsumed) {
            return
        }

        // We set the source to be this component, because we are processing it
        event.source = this

        mouseDragListeners.forEach { it(event) }
    }

    /**
     * Called to process a mouse scroll input event
     *
     * @param event The event to process
     */
    open fun processMouseScrollInput(event: MouseScrollEvent) {
        // If the event is consumed, don't do anything
        if (event.isConsumed) {
            return
        }

        // We set the source to be this component, because we are processing it
        event.source = this

        mouseScrollListeners.forEach { it(event) }
    }

    /**
     * Called to process a key input event
     *
     * @param event The event to process
     */
    open fun processKeyInput(event: KeyEvent) {
        // If the event is consumed, don't do anything
        if (event.isConsumed) {
            return
        }

        // We set the source to be this component, because we are processing it
        event.source = this

        keyListeners.forEach { it(event) }
    }

    /**
     * Adds a mouse listener to this control to be fired whenever a mouse event is raised
     *
     * @param mouseListener The mouse listener to add
     */
    fun addMouseListener(mouseListener: (MouseEvent) -> Unit) {
        this.mouseListeners.add(mouseListener)
    }

    /**
     * Adds a mouse move listener to this control to be fired whenever a mouse move event is raised
     *
     * @param mouseMoveListener The mouse move listener to add
     */
    fun addMouseMoveListener(mouseMoveListener: (MouseMoveEvent) -> Unit) {
        this.mouseMoveListeners.add(mouseMoveListener)
    }

    fun addMouseDragListener(mouseDragListener: (MouseDragEvent) -> Unit) {
        this.mouseDragListeners.add(mouseDragListener)
    }

    /**
     * Adds a mouse scroll listener to this control to be fired whenever a mouse scroll event is raised
     *
     * @param mouseScrollListener The mouse scroll listener to add
     */
    fun addMouseScrollListener(mouseScrollListener: (MouseScrollEvent) -> Unit) {
        this.mouseScrollListeners.add(mouseScrollListener)
    }

    /**
     * Adds a key listener to this control to be fired whenever a key event is raised
     *
     * @param keyListener The key listener to add
     */
    fun addKeyListener(keyListener: (KeyEvent) -> Unit) {
        this.keyListeners.add(keyListener)
    }
}
