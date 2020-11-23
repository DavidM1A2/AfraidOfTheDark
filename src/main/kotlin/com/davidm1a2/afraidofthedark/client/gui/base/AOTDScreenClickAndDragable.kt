package com.davidm1a2.afraidofthedark.client.gui.base

import net.minecraft.util.text.ITextComponent
import org.lwjgl.glfw.GLFW
import java.io.IOException
import kotlin.math.roundToInt

/**
 * Special gui screen that keeps track of X and Y gui offsets when dragging the mouse
 *
 * @param name The name of the GUI
 */
abstract class AOTDScreenClickAndDragable(name: ITextComponent) : AOTDScreen(name) {
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
     * @param mouseX The X position of the mouse
     * @param mouseY The Y position of the mouse
     * @param mouseButton The mouse button that was pressed
     * @throws IOException Required and could be thrown by the base method
     */
    override fun mouseClicked(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        val toReturn = super.mouseClicked(mouseX, mouseY, mouseButton)

        if (mouseButton == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            // Store the original position before dragging when the mouse goes down
            this.originalXPosition = mouseX.roundToInt() + this.guiOffsetX
            this.originalYPosition = mouseY.roundToInt() + this.guiOffsetY
        }

        return toReturn
    }

    /**
     * Called when the mouse is dragged
     *
     * @param mouseX The x position of the mouse
     * @param mouseY The y position of the mouse
     * @param lastButtonClicked The mouse button that was dragged
     * @param mouseXTo The position we are dragging the x from
     * @param mouseYTo The position we are dragging the y from
     */
    override fun mouseDragged(mouseX: Double, mouseY: Double, lastButtonClicked: Int, mouseXTo: Double, mouseYTo: Double): Boolean {
        val toReturn = super.mouseDragged(mouseX, mouseY, lastButtonClicked, mouseXTo, mouseYTo)

        if (lastButtonClicked == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            this.guiOffsetX = this.originalXPosition - mouseX.roundToInt()
            this.guiOffsetY = this.originalYPosition - mouseY.roundToInt()

            this.checkOutOfBounds()
        }

        return toReturn
    }

    /**
     * We can use this to test if the gui has scrolled out of bounds or not
     */
    protected abstract fun checkOutOfBounds()
}
