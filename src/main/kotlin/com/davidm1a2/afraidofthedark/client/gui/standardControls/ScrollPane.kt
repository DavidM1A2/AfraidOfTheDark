package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import org.lwjgl.glfw.GLFW

/**
 * @param scrollWidthRatio The constant ratio of the width of the control to the width of the underlying pane
 * @param scrollWidthRatio The constant ratio of the height of the control to the height of the underlying pane
 * As an example for the ratio constants, a ratio of 2.0 would make the scrollable pane twice the width or height
 * of the actual control, which acts as a viewport.
 */
class ScrollPane(val scrollWidthRatio: Double, val scrollHeightRatio: Double) : StackPane(scissorEnabled = true) {
    // Variables for calculating the GUI offset
    // The current X and Y gui offsets
    private var originalGuiOffsetX = 0.0
    private var originalGuiOffsetY = 0.0

    // The original X and Y position set before dragging
    private var originalXPosition = 0
    private var originalYPosition = 0

    // The scaled width and height of the background pane
    var scrollWidth = 0.0
    var scrollHeight = 0.0

    init {
        addMouseListener {
            if (it.source.isHovered) {
                if (it.eventType == AOTDMouseEvent.EventType.Click) {
                    if (it.clickedButton == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                        // Store the original position before dragging when the mouse goes down
                        this.originalXPosition = it.mouseX
                        this.originalYPosition = it.mouseY
                        this.originalGuiOffsetX = guiOffsetX
                        this.originalGuiOffsetY = guiOffsetY
                    }
                }
            }
        }
        addMouseDragListener {
            if (it.source.isHovered) {
                if (it.clickedButton == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                    this.guiOffsetX = originalGuiOffsetX + (it.mouseX - originalXPosition)
                    this.guiOffsetY = originalGuiOffsetY + (it.mouseY - originalYPosition)
                    this.checkOutOfBounds()
                }
            }
        }
    }

    /**
     * We can use this to test if the gui has scrolled out of bounds or not
     */
    fun checkOutOfBounds() {
        if (this.guiOffsetX < width - scrollWidth) this.guiOffsetX = width - scrollWidth
        if (this.guiOffsetY < height - scrollHeight) this.guiOffsetY = height - scrollHeight
        if (this.guiOffsetX > 0) this.guiOffsetX = 0.0
        if (this.guiOffsetY > 0) this.guiOffsetY = 0.0
    }

    override fun calcChildrenBounds(width: Double, height: Double) {
        super.calcChildrenBounds(scrollWidth, scrollHeight)
    }

    override fun negotiateDimensions(width: Double, height: Double) {
        super.negotiateDimensions(width, height)
        this.scrollWidth = this.width * scrollWidthRatio
        this.scrollHeight = this.height * scrollHeightRatio
    }
}