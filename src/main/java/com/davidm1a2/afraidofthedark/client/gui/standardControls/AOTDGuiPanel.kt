package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiContainer
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11

/**
 * Super basic control that just scissors the border and holds gui elements
 *
 * @constructor Initializes the bounding box and if scissors are enabled or not
 * @param x              The X location of the top left corner
 * @param y              The Y location of the top left corner
 * @param width          The width of the component
 * @param height         The height of the component
 * @param scissorEnabled If scissors are enabled when drawing this panel
 */
open class AOTDGuiPanel(x: Int, y: Int, width: Int, height: Int, private val scissorEnabled: Boolean) : AOTDGuiContainer(x, y, width, height)
{
    /**
     * A panel can only be drawn inside of a box that may be scissored
     */
    override fun draw()
    {
        // If scissor is enabled we use glScissor to force all drawing to happen inside of a box
        if (scissorEnabled)
        {
            // Compute the OpenGL X and Y screen coordinates to scissor
            var realX = AOTDGuiUtility.mcToRealScreenCoord(this.getXScaled())
            var realY = AOTDGuiUtility.realScreenYToGLYCoord(AOTDGuiUtility.mcToRealScreenCoord(this.getYScaled() + this.getHeightScaled()))
            // Compute the OpenGL width and height to scissor with
            var realWidth = AOTDGuiUtility.mcToRealScreenCoord(this.getWidthScaled())
            var realHeight = AOTDGuiUtility.mcToRealScreenCoord(this.getHeightScaled())

            // If open GL scissors is enabled update the x,y width,height to be clamped within the current scissor box
            if (GL11.glIsEnabled(GL11.GL_SCISSOR_TEST))
            {
                // Create an int buffer to hold all the current scissor box values
                val buffer = BufferUtils.createIntBuffer(16)
                // Grab the current scissor box values
                GL11.glGetInteger(GL11.GL_SCISSOR_BOX, buffer)

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
                if (realWidth == 0 || realHeight == 0)
                {
                    return
                }
            }

            // Push the current scissor bit and enable scissor
            GL11.glPushAttrib(GL11.GL_SCISSOR_BIT)
            GL11.glEnable(GL11.GL_SCISSOR_TEST)
            GL11.glScissor(realX, realY, realWidth, realHeight)
        }

        // Draw all sub-components
        super.draw()

        // If scissor was enabled disable it
        if (scissorEnabled)
        {
            // Disable scissor and pop the old bit
            GL11.glDisable(GL11.GL_SCISSOR_TEST)
            GL11.glPopAttrib()
        }
    }
}
