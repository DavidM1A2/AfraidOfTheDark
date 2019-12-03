package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiContainer
import net.minecraft.util.math.MathHelper
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.util.Point
import org.lwjgl.util.Rectangle
import kotlin.math.round

/**
 * Extension of a standard panel that requires a scroll bar to move content up and down
 *
 * @param x              The X location of the top left corner
 * @param originalYPos   The original y position this panel was at before moving because of the slider
 * @param width          The width of the component
 * @param height         The height of the component
 * @param scissorEnabled If scissors are enabled when drawing this panel
 * @param scrollBar      The scroll bar that controls this panel
 * @property maximumOffset The amount of distance this panel is allowed to scroll
 * @property lastSliderPosition The last known slider pos is used for determining if the panel should update
 * @property viewport A rectangle that represents the scroll panel's viewport
 */
class AOTDGuiScrollPanel(x: Int, private var originalYPos: Int, width: Int, height: Int, private val scissorEnabled: Boolean, private val scrollBar: AOTDGuiScrollBar) :
        AOTDGuiContainer(x, originalYPos, width, height)
{
    var maximumOffset = 0
        set(maximumOffset)
        {
            field = maximumOffset
            this.lastSliderPosition = -1f
        }
    private var lastSliderPosition: Float = 0.toFloat()
    private var viewport: Rectangle = Rectangle(0, 0, 0, 0)

    override var y: Int
        get() = super.y
        set(y)
        {
            super.y = y
            this.originalYPos = y
            this.lastSliderPosition = -1f
        }

    init
    {

        // When we scroll we want to move the content pane up or down
        this.addMouseScrollListener()
        {
            // Only scroll the pane if it's hovered
            if (this.isHovered)
            {
                // Only move the handle if scrollDistance is non-zero
                if (it.scrollDistance != 0)
                {
                    // Move the scroll bar by the distance amount
                    scrollBar.moveHandle(round(-it.scrollDistance / this.maximumOffset.toFloat() * PIXELS_SCROLLED_PER_CLICK).toInt(), true)
                }
            }
        }
    }

    /**
     * Draws the component
     */
    override fun draw()
    {
        // If scissor is enabled we use glScissor to force all drawing to happen inside of a box
        if (scissorEnabled)
        {
            // Compute the OpenGL X and Y screen coordinates to scissor
            var realX = AOTDGuiUtility.mcToRealScreenCoord(this.xScaled)
            var realY = AOTDGuiUtility.realScreenYToGLYCoord(AOTDGuiUtility.mcToRealScreenCoord((this.originalYPos * this.scaleY).toInt() + this.heightScaled))
            // Compute the OpenGL width and height to scissor with
            var realWidth = AOTDGuiUtility.mcToRealScreenCoord(this.widthScaled)
            var realHeight = AOTDGuiUtility.mcToRealScreenCoord(this.heightScaled)

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
                realX = MathHelper.clamp(realX, oldX, oldX + oldWidth)
                realY = MathHelper.clamp(realY, oldY, oldY + oldHeight)
                realWidth = MathHelper.clamp(realWidth, 0, oldX + oldWidth - realX)
                realHeight = MathHelper.clamp(realHeight, 0, oldY + oldHeight - realY)
                // Don't draw anything if we're completely ouside the original box
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

        // If we get a new scroll value update the panel's position
        if (lastSliderPosition != this.scrollBar.value)
        {
            lastSliderPosition = this.scrollBar.value
            // Update the y position internally by offsetting based on slider percent
            super.y = this.originalYPos - (this.maximumOffset * lastSliderPosition).toInt()
            // Compute the actual elements in "view"
            // Rectangle realBoundingBox = new Rectangle(this.getXScaled(), (int) (this.originalYPos * this.getScaleY()), this.getWidthScaled(), this.getHeightScaled());
            // If any elements are no longer in "view' hide them, otherwise show them
            // this.getChildren().forEach(child -> child.setVisible(child.intersects(realBoundingBox)));
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

    /**
     * Returns true if the current component's viewport intersects the point, or false if not
     *
     * @param point The point to test intersection with
     * @return True if the point intersects the rectangle, false otherwise
     */
    override fun intersects(point: Point): Boolean
    {
        return this.viewport.contains(point)
    }

    /**
     * Returns true if the current component's viewport intersects the rectangle, or false if not
     *
     * @param rectangle The point to test intersection with
     * @return True if the point intersects the rectangle, false otherwise
     */
    override fun intersects(rectangle: Rectangle): Boolean
    {
        return this.viewport.intersects(rectangle)
    }

    /**
     * Updates the scaled bounding box from the current X and Y scale
     */
    override fun updateScaledBounds()
    {
        super.updateScaledBounds()
        this.viewport = Rectangle(xScaled, (originalYPos * scaleY).toInt(), widthScaled, heightScaled)
    }

    companion object
    {
        // Number of pixels we scroll per mouse wheel click
        private const val PIXELS_SCROLLED_PER_CLICK = 25
    }
}
