package com.davidm1a2.afraidofthedark.client.gui.base;

import java.io.IOException;

/**
 * Special gui screen that keeps track of X and Y gui offsets when dragging the mouse
 */
public abstract class AOTDGuiClickAndDragable extends AOTDGuiScreen
{
    // Variables for calculating the GUI offset
    // The current X and Y gui offsets
    protected int guiOffsetX = 0;
    protected int guiOffsetY = 0;
    // The original X and Y position set before dragging
    protected int originalXPosition = 0;
    protected int originalYPosition = 0;

    /**
     * Called when the mouse is clicked
     *
     * @param xPos        The X position of the mouse
     * @param yPos        The Y position of the mouse
     * @param mouseButton The mouse button that was pressed
     * @throws IOException Required and could be thrown by the base method
     */
    @Override
    protected void mouseClicked(int xPos, int yPos, int mouseButton) throws IOException
    {
        super.mouseClicked(xPos, yPos, mouseButton);

        // Store the original position before dragging when the mouse goes down
        this.originalXPosition = xPos + this.guiOffsetX;
        this.originalYPosition = yPos + this.guiOffsetY;
    }

    /**
     * Called when we drag the mouse
     *
     * @param mouseX            The mouse X position
     * @param mouseY            The mouse Y position
     * @param lastButtonClicked The last button clicked
     * @param timeBetweenClicks The time between the last click
     */
    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int lastButtonClicked, long timeBetweenClicks)
    {
        super.mouseClickMove(mouseX, mouseY, lastButtonClicked, timeBetweenClicks);

        this.guiOffsetX = this.originalXPosition - mouseX;
        this.guiOffsetY = this.originalYPosition - mouseY;

        this.checkOutOfBounds();
    }

    /**
     * We can use this to test if the gui has scrolled out of bounds or not
     */
    protected abstract void checkOutOfBounds();
}
