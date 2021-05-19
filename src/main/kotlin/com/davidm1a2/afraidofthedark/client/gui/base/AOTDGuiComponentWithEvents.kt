package com.davidm1a2.afraidofthedark.client.gui.base

import com.davidm1a2.afraidofthedark.client.gui.events.*
import com.davidm1a2.afraidofthedark.client.gui.layout.*
import java.awt.Color
import java.awt.Point

/**
 * A base class for any GUI components that can listen for action events like mouse clicks
 *
 * @constructor initializes the bounding box
 * @param x      The X location of the top left corner
 * @param y      The Y location of the top left corner
 * @param width  The width of the component
 * @param height The height of the component
 * @property mouseListeners The mouse listeners of this component
 * @property mouseMoveListeners The mouse move listeners of this component
 * @property mouseScrollListeners The mouse scroll listeners of this component
 * @property keyListeners The key listeners of this component
 */
abstract class AOTDGuiComponentWithEvents(
        offset: Position = AbsolutePosition(0.0, 0.0),
        prefSize: Dimensions = AbsoluteDimensions(0.0, 0.0),
        margins: GuiSpacing = AbsoluteSpacing(),
        gravity: GuiGravity = GuiGravity.TOP_LEFT,
        hoverTexts: Array<String> = emptyArray(),
        color: Color = Color(255, 255, 255, 255)) :
    AOTDGuiComponent(offset, prefSize, margins, gravity, hoverTexts, color) {

    private var mouseListeners = mutableListOf<(AOTDMouseEvent) -> Unit>()
    private var mouseMoveListeners = mutableListOf<(AOTDMouseMoveEvent) -> Unit>()
    private var mouseDragListeners = mutableListOf<(MouseDragEvent) -> Unit>()
    private var mouseScrollListeners = mutableListOf<(AOTDMouseScrollEvent) -> Unit>()
    private var keyListeners = mutableListOf<(AOTDKeyEvent) -> Unit>()

    init {
        // Add a mouse move listener to this control that allows us to fire off events whenever conditions are met
        this.addMouseMoveListener {
            // If we move the mouse also fire other related events
            if (it.eventType == AOTDMouseMoveEvent.EventType.Move) {
                // Grab the source of the event
                val component = it.source
                // Store the flag telling us if the component was hovered
                val wasHovered = component.isHovered
                // Set the hovered flag based on if we intersect the component
                component.isHovered = component.isVisible && component.inBounds && component.intersects(Point(it.mouseX, it.mouseY))
                // Fire mouse enter/exit events if our mouse entered or exited the control
                if (component.isHovered && !wasHovered) {
                    component.processMouseMoveInput(
                        AOTDMouseMoveEvent(
                            component,
                            it.mouseX,
                            it.mouseY,
                            AOTDMouseMoveEvent.EventType.Enter
                        )
                    )
                }
                if (!component.isHovered && wasHovered) {
                    component.processMouseMoveInput(
                        AOTDMouseMoveEvent(
                            component,
                            it.mouseX,
                            it.mouseY,
                            AOTDMouseMoveEvent.EventType.Exit
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
    open fun processMouseInput(event: AOTDMouseEvent) {
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
    open fun processMouseMoveInput(event: AOTDMouseMoveEvent) {
        // If the event is consumed, don't do anything
        if (event.isConsumed) {
            return
        }

        // If the event is an enter or exit event, consume the event. This is because an enter and exit event can only happen to a single control at a time
        if (event.eventType == AOTDMouseMoveEvent.EventType.Enter || event.eventType == AOTDMouseMoveEvent.EventType.Exit) {
            event.consume()
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
    open fun processMouseScrollInput(event: AOTDMouseScrollEvent) {
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
    open fun processKeyInput(event: AOTDKeyEvent) {
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
    fun addMouseListener(mouseListener: (AOTDMouseEvent) -> Unit) {
        this.mouseListeners.add(mouseListener)
    }

    /**
     * Adds a mouse move listener to this control to be fired whenever a mouse move event is raised
     *
     * @param mouseMoveListener The mouse move listener to add
     */
    fun addMouseMoveListener(mouseMoveListener: (AOTDMouseMoveEvent) -> Unit) {
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
    fun addMouseScrollListener(mouseScrollListener: (AOTDMouseScrollEvent) -> Unit) {
        this.mouseScrollListeners.add(mouseScrollListener)
    }

    /**
     * Adds a key listener to this control to be fired whenever a key event is raised
     *
     * @param keyListener The key listener to add
     */
    fun addKeyListener(keyListener: (AOTDKeyEvent) -> Unit) {
        this.keyListeners.add(keyListener)
    }
}
