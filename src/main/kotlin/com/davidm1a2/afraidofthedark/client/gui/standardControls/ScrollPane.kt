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
open class ScrollPane(private val scrollWidthRatio: Double,
                      private val scrollHeightRatio: Double,
                      private val persistentOffset: Position? = null,
                      private val maxZoomRatio: Double = 1.0,
                      private val minZoomRatio: Double = 1.0,
                      private val persistentZoomRatio: Double = 1.0,
                      private val zoomSpeed: Double = 0.1) :
    StackPane(scissorEnabled = true) {

    // The current zoom percent
    private var curZoomRatio = persistentZoomRatio.coerceIn(minZoomRatio..maxZoomRatio)

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
            if (this.isHovered) {
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
            }
            // Only check hover on click, not on release or drag
            if (it.eventType == MouseEvent.EventType.Release) {
                if (it.clickedButton == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                    mouseHeld = false
                }
            }
        }
        addMouseDragListener {
            if (it.clickedButton == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                if (mouseHeld) {   // A click must have been detected for a drag to register
                    guiOffsetX = originalGuiOffsetX + (it.mouseX - originalXPosition)
                    guiOffsetY = originalGuiOffsetY + (it.mouseY - originalYPosition)
                    checkOutOfBounds()
                    calcChildrenBounds()
                }
            }
        }
        addMouseScrollListener {
            // Check that this pane is zoom enabled
            if (this.maxZoomRatio > this.minZoomRatio) {
                // Only scroll the pane if it's hovered and the mouse is not being dragged
                if (this.isHovered && !this.mouseHeld) {
                    // Only compute if distance is non-zero
                    if (it.scrollDistance != 0) {
                        // Zoom the panel by the distance amount
                        this.zoom(this.curZoomRatio + it.scrollDistance * zoomSpeed)
                    }
                }
            }
        }
    }

    private fun zoom(ratio: Double) {
        val prevZoomRatio = this.curZoomRatio
        this.curZoomRatio = ratio.coerceIn(this.minZoomRatio..this.maxZoomRatio)
        val scale = this.curZoomRatio/prevZoomRatio
        this.guiOffsetX *= scale
        this.guiOffsetY *= scale
        this.scrollWidth *= scale
        this.scrollHeight *= scale
        this.checkOutOfBounds()
        this.calcChildrenBounds()
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
        this.scrollWidth = this.width * scrollWidthRatio * curZoomRatio
        this.scrollHeight = this.height * scrollHeightRatio * curZoomRatio

        if (persistentOffset != null) {
            guiOffsetX = persistentOffset.getAbsolute(this).x
            guiOffsetY = persistentOffset.getAbsolute(this).y
        }
    }

    fun getCurrentOffset(): Position {
        return Position(guiOffsetX, guiOffsetY, false)
    }

    fun getCurrentZoom(): Double {
        return curZoomRatio
    }
}