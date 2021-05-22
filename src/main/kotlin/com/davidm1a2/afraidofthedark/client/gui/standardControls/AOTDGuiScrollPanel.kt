package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDPane
import com.davidm1a2.afraidofthedark.client.gui.layout.AbsoluteDimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.AbsolutePosition
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import java.awt.Point
import java.awt.Rectangle

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
class AOTDGuiScrollPanel(
    width: Double,
    height: Double,
    private val scissorEnabled: Boolean,
    private val scrollBar: HScrollBar
) :
    AOTDPane(prefSize = AbsoluteDimensions(width, height)) {
    var maximumOffset = 0
        set(maximumOffset) {
            field = maximumOffset
            this.lastSliderPosition = -1.0
        }
    private var lastSliderPosition = 0.0
    private var viewport = Rectangle(0, 0, 0, 0)
    var scrollYOffset = 0.0;

    init {
        // When we scroll we want to move the content pane up or down
        this.addMouseScrollListener {
            // Only scroll the pane if it's hovered
            if (this.isHovered) {
                // Only move the handle if scrollDistance is non-zero
                if (it.scrollDistance != 0) {
                    // Move the scroll bar by the distance amount
                    scrollBar.value = (-it.scrollDistance / this.maximumOffset.toFloat() * SCROLL_SPEED).toDouble()
                }
            }
        }
    }

    /**
     * Draws the component
     */
    override fun draw() {
        // If scissor is enabled we use glScissor to force all drawing to happen inside of a box
        if (scissorEnabled) {
            // Compute the OpenGL X and Y screen coordinates to scissor
            var realX = AOTDGuiUtility.mcToRealScreenCoord(x)
            var realY =
                AOTDGuiUtility.realScreenYToGLYCoord(AOTDGuiUtility.mcToRealScreenCoord(y + height))
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

                // Don't draw anything if we're completely ouside the original box
                if (realWidth == 0 || realHeight == 0) {
                    return
                }
            }

            // Push the current scissor bit and enable scissor
            GL11.glPushAttrib(GL11.GL_SCISSOR_BIT)
            GL11.glEnable(GL11.GL_SCISSOR_TEST)
            GL11.glScissor(realX, realY, realWidth, realHeight)
        }

        // If we get a new scroll value update the panel's position
        if (lastSliderPosition != this.scrollBar.value) {
            lastSliderPosition = this.scrollBar.value
            // Update the y position internally by offsetting based on slider percent
            scrollYOffset = (y - (this.maximumOffset * lastSliderPosition))
            for (child in getChildren()) {
                child.offset = AbsolutePosition(child.offset.x, child.offset.y + scrollYOffset)
            }
            // Compute the actual elements in "view"
            // Rectangle realBoundingBox = new Rectangle(this.getXScaled(), (int) (this.originalYPos * this.getScaleY()), this.getWidthScaled(), this.getHeightScaled());
            // If any elements are no longer in "view' hide them, otherwise show them
            // this.getChildren().forEach(child -> child.setVisible(child.intersects(realBoundingBox)));
        }

        // Draw all sub-components
        super.draw()

        // If scissor was enabled disable it
        if (scissorEnabled) {
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
    override fun intersects(point: Point): Boolean {
        return this.viewport.contains(point)
    }

    /**
     * Returns true if the current component's viewport intersects the rectangle, or false if not
     *
     * @param rectangle The point to test intersection with
     * @return True if the point intersects the rectangle, false otherwise
     */
    override fun intersects(rectangle: Rectangle): Boolean {
        return this.viewport.intersects(rectangle)
    }

    companion object {
        // Scroll speed
        private const val SCROLL_SPEED = 1000
    }
}
