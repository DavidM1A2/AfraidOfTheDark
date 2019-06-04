package com.DavidM1A2.afraidofthedark.client.gui.standardControls;

import com.DavidM1A2.afraidofthedark.client.gui.AOTDGuiUtility;
import com.DavidM1A2.afraidofthedark.client.gui.base.AOTDGuiContainer;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.IntBuffer;

/**
 * Extension of a standard panel that requires a scroll bar to move content up and down
 */
public class AOTDGuiScrollPanel extends AOTDGuiContainer
{
    // True if scissors is enabled, false otherwise
    private final boolean scissorEnabled;
    // The scroll bar that controls this panel's motion
    private final AOTDGuiScrollBar scrollBar;
    // The amount of distance this panel is allowed to scroll
    private int maximumOffset = 0;
    // The last known slider pos is used for determining if the panel should update
    private float lastSliderPosition;
    // The original y position this panel was at before moving because of the slider
    private int originalYPos;

    /**
     * Constructor initializes the bounding box and if scissors are enabled or not
     *
     * @param x              The X location of the top left corner
     * @param y              The Y location of the top left corner
     * @param width          The width of the component
     * @param height         The height of the component
     * @param scissorEnabled If scissors are enabled when drawing this panel
     * @param scrollBar      The scroll bar that controls this panel
     */
    public AOTDGuiScrollPanel(int x, int y, int width, int height, boolean scissorEnabled, AOTDGuiScrollBar scrollBar)
    {
        super(x, y, width, height);
        this.originalYPos = y;
        this.scrollBar = scrollBar;
        this.scissorEnabled = scissorEnabled;
    }

    /**
     * Draws the component
     */
    @Override
    public void draw()
    {
        // If scissor is enabled we use glScissor to force all drawing to happen inside of a box
        if (scissorEnabled)
        {
            // Grab the gui-utility reference
            AOTDGuiUtility guiUtility = AOTDGuiUtility.getInstance();

            // Compute the OpenGL X and Y screen coordinates to scissor
            int realX = guiUtility.mcToRealScreenCoord(this.getXScaled());
            int realY = guiUtility.realScreenYToGLYCoord(guiUtility.mcToRealScreenCoord((int) (this.originalYPos * this.getScaleY()) + this.getHeightScaled()));
            // Compute the OpenGL width and height to scissor with
            int realWidth = guiUtility.mcToRealScreenCoord(this.getWidthScaled());
            int realHeight = guiUtility.mcToRealScreenCoord(this.getHeightScaled());

            // If open GL scissors is enabled update the x,y width,height to be clamped within the current scissor box
            if (GL11.glIsEnabled(GL11.GL_SCISSOR_TEST))
            {
                // Create an int buffer to hold all the current scissor box values
                IntBuffer buffer = BufferUtils.createIntBuffer(16);
                // Grab the current scissor box values
                GL11.glGetInteger(GL11.GL_SCISSOR_BOX, buffer);
                // Grab the old scissor rect values from the buffer
                int oldX = buffer.get();
                int oldY = buffer.get();
                int oldWidth = buffer.get();
                int oldHeight = buffer.get();
                // Clamp the new scissor values within the old scissor box
                realX = MathHelper.clamp(realX, oldX, oldX + oldWidth);
                realY = MathHelper.clamp(realY, oldY, oldY + oldHeight);
                realWidth = MathHelper.clamp(realWidth, 0, oldX + oldWidth - realX);
                realHeight = MathHelper.clamp(realHeight, 0, oldY + oldHeight - realY);
                // Don't draw anything if we're completely ouside the original box
                if (realWidth == 0 || realHeight == 0)
                {
                    return;
                }
            }

            // Push the current scissor bit and enable scissor
            GL11.glPushAttrib(GL11.GL_SCISSOR_BIT);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            GL11.glScissor(realX, realY, realWidth, realHeight);
        }

        // If we get a new scroll value update the panel's position
        if (lastSliderPosition != this.scrollBar.getValue())
        {
            lastSliderPosition = this.scrollBar.getValue();
            // Update the y position internally by offsetting based on slider percent
            super.setY(this.originalYPos - (int) (maximumOffset * lastSliderPosition));
            // Compute the actual elements in "view"
            // Rectangle realBoundingBox = new Rectangle(this.getXScaled(), (int) (this.originalYPos * this.getScaleY()), this.getWidthScaled(), this.getHeightScaled());
            // If any elements are no longer in "view' hide them, otherwise show them
            // this.getChildren().forEach(child -> child.setVisible(child.intersects(realBoundingBox)));
        }

        // Draw all sub-components
        super.draw();

        // If scissor was enabled disable it
        if (scissorEnabled)
        {
            // Disable scissor and pop the old bit
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glPopAttrib();
        }
    }

    /**
     * Sets the new y position of the component, also updates the internal y pos value and forces a redraw by
     * falsifying last slider pos
     *
     * @param y The new y position of the component
     */
    @Override
    public void setY(int y)
    {
        super.setY(y);
        this.originalYPos = y;
        this.lastSliderPosition = -1;
    }

    /**
     * Set the maximum offset this panel can have when being scrolled
     *
     * @param maximumOffset The new max offset
     */
    public void setMaximumOffset(int maximumOffset)
    {
        this.maximumOffset = maximumOffset;
        // Force a recomputation of the panel's position by falsifying the slider's pos
        this.lastSliderPosition = -1;
    }

    /**
     * @return The max number of pixels the panel can slide by slider
     */
    public int getMaximumOffset()
    {
        return maximumOffset;
    }
}
