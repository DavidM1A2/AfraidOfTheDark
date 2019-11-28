package com.davidm1a2.afraidofthedark.client.gui.base

import java.io.IOException

/**
 * Special gui screen that keeps track of X and Y gui offsets when dragging the mouse
 */
abstract class AOTDGuiClickAndDragable : AOTDGuiScreen()
{
    // Variables for calculating the GUI offset
    // The current X and Y gui offsets
    protected var guiOffsetX = 0
    protected var guiOffsetY = 0
    // The original X and Y position set before dragging
    private var originalXPosition = 0
    private var originalYPosition = 0

    /**
     * Called when the mouse is clicked
     *
     * @param xPos        The X position of the mouse
     * @param yPos        The Y position of the mouse
     * @param mouseButton The mouse button that was pressed
     * @throws IOException Required and could be thrown by the base method
     */
    override fun mouseClicked(xPos: Int, yPos: Int, mouseButton: Int)
    {
        super.mouseClicked(xPos, yPos, mouseButton)

        // Store the original position before dragging when the mouse goes down
        this.originalXPosition = xPos + this.guiOffsetX
        this.originalYPosition = yPos + this.guiOffsetY
    }

    /**
     * Called when we drag the mouse
     *
     * @param mouseX            The mouse X position
     * @param mouseY            The mouse Y position
     * @param lastButtonClicked The last button clicked
     * @param timeBetweenClicks The time between the last click
     */
    override fun mouseClickMove(mouseX: Int, mouseY: Int, lastButtonClicked: Int, timeBetweenClicks: Long)
    {
        super.mouseClickMove(mouseX, mouseY, lastButtonClicked, timeBetweenClicks)

        this.guiOffsetX = this.originalXPosition - mouseX
        this.guiOffsetY = this.originalYPosition - mouseY

        this.checkOutOfBounds()
    }

    /**
     * We can use this to test if the gui has scrolled out of bounds or not
     */
    protected abstract fun checkOutOfBounds()
}
