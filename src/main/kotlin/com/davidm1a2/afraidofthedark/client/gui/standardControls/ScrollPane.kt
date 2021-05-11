package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiGravity
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import org.lwjgl.BufferUtils
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11

class ScrollPane(val scrollWidth: Int, val scrollHeight: Int) : AOTDGuiPanel(scissorEnabled = true) {
    // Variables for calculating the GUI offset
    // The current X and Y gui offsets
    private var guiOffsetX = 0
    private var guiOffsetY = 0
    private var originalGuiOffsetX = 0
    private var originalGuiOffsetY = 0

    // The original X and Y position set before dragging
    private var originalXPosition = 0
    private var originalYPosition = 0

    init {
        this.gravity = AOTDGuiGravity.TOP_LEFT
        addMouseListener {
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
        addMouseDragListener {
            if (it.clickedButton == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                this.guiOffsetX = originalGuiOffsetX + (it.mouseX - originalXPosition)
                this.guiOffsetY = originalGuiOffsetY + (it.mouseY - originalYPosition)
                this.checkOutOfBounds()
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

    override fun calcChildrenBounds() {
        for (child in getChildren()) {
            // Set dimensions
            child.negotiateDimensions(scrollWidth, scrollHeight)
            // Gravity does not apply when in a scrollpane
            child.x = this.x + this.xOffset + this.guiOffsetX + child.xOffset
            child.y = this.y + this.yOffset + this.guiOffsetY + child.yOffset
        }
    }
}