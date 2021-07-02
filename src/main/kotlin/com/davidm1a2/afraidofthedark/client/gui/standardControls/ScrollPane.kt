package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.events.MouseEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import org.lwjgl.glfw.GLFW

/**
 * A draggable pane that can be scrolled to display different components in the viewport
 *
 * @param scrollWidthRatio The constant ratio of the width of the control to the width of the underlying pane
 * @param scrollWidthRatio The constant ratio of the height of the control to the height of the underlying pane
 * As an example for the ratio constants, a ratio of 2.0 would make the scrollable pane twice the width or height
 * of the actual control, which acts as a viewport.
 */
open class ScrollPane(private val scrollWidthRatio: Double, private val scrollHeightRatio: Double, private var persistentOffset : Position = Position(0.0, 0.0)) : StackPane(scissorEnabled = true) {

    // The current X and Y gui offsets
    private var originalGuiOffsetX = 0.0
    private var originalGuiOffsetY = 0.0

    // The original X and Y position set before dragging
    private var originalXPosition = -1
    private var originalYPosition = -1

    // Whether or not the left mouse button is being held
    private var mouseHeld = false

    // The scaled width and height of the background pane
    var scrollWidth = 0.0
    var scrollHeight = 0.0

    init {
        addMouseListener {
            if (it.source.isHovered) {
                if (it.eventType == MouseEvent.EventType.Click) {
                    if (it.clickedButton == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                        // Store the original position before dragging when the mouse goes down
                        originalXPosition = it.mouseX
                        originalYPosition = it.mouseY
                        originalGuiOffsetX = guiOffsetX
                        originalGuiOffsetY = guiOffsetY
                        mouseHeld = true
                    }
                }
                if (it.eventType == MouseEvent.EventType.Release) {
                    if (it.clickedButton == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                        mouseHeld = false
                    }
                }
            }
        }
        addMouseDragListener {
            if (it.source.isHovered) {
                if (it.clickedButton == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                    if (mouseHeld) {   // A click must have been detected for a drag to register
                        guiOffsetX = originalGuiOffsetX + (it.mouseX - originalXPosition)
                        guiOffsetY = originalGuiOffsetY + (it.mouseY - originalYPosition)
                        checkOutOfBounds()
                        this.invalidate()
                    }
                }
            }
        }
    }

    override fun getInternalWidth(): Double {
        return scrollWidth - padding.getAbsoluteOuter(this).width
    }

    override fun getInternalHeight(): Double {
        return scrollHeight - padding.getAbsoluteOuter(this).height
    }

    /**
     * We can use this to test if the gui has scrolled out of bounds or not
     */
    open fun checkOutOfBounds() {
        if (this.guiOffsetX < width - scrollWidth) this.guiOffsetX = width - scrollWidth
        if (this.guiOffsetY < height - scrollHeight) this.guiOffsetY = height - scrollHeight
        if (this.guiOffsetX > 0) this.guiOffsetX = 0.0
        if (this.guiOffsetY > 0) this.guiOffsetY = 0.0
    }

    override fun negotiateDimensions(width: Double, height: Double) {
        super.negotiateDimensions(width, height)
        this.scrollWidth = this.width * scrollWidthRatio
        this.scrollHeight = this.height * scrollHeightRatio
        guiOffsetX = persistentOffset.getAbsolute(this).x
        guiOffsetY = persistentOffset.getAbsolute(this).y
        checkOutOfBounds()
    }

    fun getCurrentOffset() : Position {
        return Position(guiOffsetX, guiOffsetY, false)
    }
}