package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiGravity
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.events.MouseDragEvent
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11
import kotlin.math.min

/**
 * @param scrollWidthRatio The constant ratio of the width of the control to the width of the underlying pane
 * @param scrollWidthRatio The constant ratio of the height of the control to the height of the underlying pane
 * As an example for the ratio constants, a ratio of 2.0 would make the scrollable pane twice the width or height
 * of the actual control, which acts as a viewport.
 */
class ScrollPane(val scrollWidthRatio: Double, val scrollHeightRatio: Double) : AOTDGuiPanel(scissorEnabled = true) {
    // Variables for calculating the GUI offset
    // The current X and Y gui offsets
    private var guiOffsetX = 0
    private var guiOffsetY = 0
    private var originalGuiOffsetX = 0
    private var originalGuiOffsetY = 0

    // The original X and Y position set before dragging
    private var originalXPosition = 0
    private var originalYPosition = 0

    // The scaled width and height of the background pane
    var scrollWidth = 0
    var scrollHeight = 0

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
    private fun checkOutOfBounds() {
        if (this.guiOffsetX < width - scrollWidth) this.guiOffsetX = width - scrollWidth
        if (this.guiOffsetY < height - scrollHeight) this.guiOffsetY = height - scrollHeight
        if (this.guiOffsetX > 0) this.guiOffsetX = 0
        if (this.guiOffsetY > 0) this.guiOffsetY = 0
    }

    override fun negotiateDimensions(width: Int, height: Int) {
        // Calculate new dimensions
        this.width = min(prefWidth, width)
        this.height = min(prefHeight, height)
        // Scale the scroll pane
        this.scrollWidth = (this.scrollWidthRatio * this.width).toInt()
        this.scrollHeight = (this.scrollHeightRatio * this.height).toInt()
        // Calculate children positions now that the scroll dimensions are set
        this.calcChildrenBounds()
    }

    override fun calcChildrenBounds() {
        for (child in getChildren()) {
            // Set dimensions
            child.negotiateDimensions(scrollWidth, scrollHeight)
            // Set position
            val calculatedXOffset = when (child.gravity) {
                AOTDGuiGravity.TOP_LEFT, AOTDGuiGravity.CENTER_LEFT, AOTDGuiGravity.BOTTOM_LEFT -> padding.leftPx
                AOTDGuiGravity.TOP_CENTER, AOTDGuiGravity.CENTER, AOTDGuiGravity.BOTTOM_CENTER -> this.scrollWidth/2 - child.width/2
                AOTDGuiGravity.TOP_RIGHT, AOTDGuiGravity.CENTER_RIGHT, AOTDGuiGravity.BOTTOM_RIGHT -> this.scrollWidth - child.width - padding.rightPx
            }
            val calculatedYOffset = when (child.gravity) {
                AOTDGuiGravity.TOP_LEFT, AOTDGuiGravity.TOP_CENTER, AOTDGuiGravity.TOP_RIGHT -> padding.topPx
                AOTDGuiGravity.CENTER_LEFT, AOTDGuiGravity.CENTER, AOTDGuiGravity.CENTER_RIGHT -> this.scrollHeight/2 - child.height/2
                AOTDGuiGravity.BOTTOM_LEFT, AOTDGuiGravity.BOTTOM_CENTER, AOTDGuiGravity.BOTTOM_RIGHT -> this.scrollHeight - child.height - padding.botPx
            }
            child.x = this.x + this.xOffset + calculatedXOffset + this.guiOffsetX + child.xOffset
            child.y = this.y + this.yOffset + calculatedYOffset + this.guiOffsetY + child.yOffset
        }
    }
}