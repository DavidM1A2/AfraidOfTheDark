package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.client.gui.layout.Spacing
import com.mojang.blaze3d.matrix.MatrixStack
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11

/**
 * Super basic control that just scissors the border and holds gui elements
 */
open class StackPane(
    prefSize: Dimensions = Dimensions(Double.MAX_VALUE, Double.MAX_VALUE),
    offset: Position = Position(0.0, 0.0),
    margins: Spacing = Spacing(),
    gravity: Gravity = Gravity.TOP_LEFT,
    hoverTexts: Array<String> = emptyArray(),
    padding: Spacing = Spacing(),
    private val scissorEnabled: Boolean = false
) :
    AOTDPane(offset, prefSize, margins, gravity, hoverTexts, padding) {

    /**
     * A panel can only be drawn inside of a box that may be scissored
     */
    override fun draw(matrixStack: MatrixStack) {
        // If scissor is enabled we use glScissor to force all drawing to happen inside of a box
        if (scissorEnabled) {
            // Compute the OpenGL X and Y screen coordinates to scissor
            var realX = AOTDGuiUtility.mcToRealScreenCoord(x)
            var realY = AOTDGuiUtility.realScreenYToGLYCoord(AOTDGuiUtility.mcToRealScreenCoord(y + height))
            // Compute the OpenGL width and height to scissor with
            var realWidth = AOTDGuiUtility.mcToRealScreenCoord(width)
            var realHeight = AOTDGuiUtility.mcToRealScreenCoord(height)

            // If open GL scissors is enabled update the x,y width,height to be clamped within the current scissor box
            if (GL11.glIsEnabled(GL11.GL_SCISSOR_TEST)) {
                // Create an int buffer to hold all the current scissor box values
                val buffer = BufferUtils.createIntBuffer(16)
                // Grab the current scissor box values
                GL11.glGetIntegerv(GL11.GL_SCISSOR_BOX, buffer)

                // Grab the old scissor rect values from the buffer
                val oldX = buffer.get()
                val oldY = buffer.get()
                val oldWidth = buffer.get()
                val oldHeight = buffer.get()

                // Clamp the new scissor values within the old scissor box
                realX = realX.coerceIn(oldX, oldX + oldWidth)
                realY = realY.coerceIn(oldY, oldY + oldHeight)
                realWidth = realWidth.coerceIn(0, oldX + oldWidth - realX)
                realHeight = realHeight.coerceIn(0, oldY + oldHeight - realY)

                // Don't draw anything if we're completely outside the original box
                if (realWidth == 0 || realHeight == 0) {
                    return
                }
            }

            // Push the current scissor bit and enable scissor
            GL11.glPushAttrib(GL11.GL_SCISSOR_BIT)
            GL11.glEnable(GL11.GL_SCISSOR_TEST)
            GL11.glScissor(realX, realY, realWidth, realHeight)
        }

        // Draw all sub-components
        super.draw(matrixStack)

        // If scissor was enabled disable it
        if (scissorEnabled) {
            // Disable scissor and pop the old bit
            GL11.glDisable(GL11.GL_SCISSOR_TEST)
            GL11.glPopAttrib()
        }
    }
}
