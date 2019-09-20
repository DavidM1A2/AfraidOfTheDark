package com.davidm1a2.afraidofthedark.client.gui.standardControls;

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility;
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiContainer;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.IntBuffer;

/**
 * Super basic control that just scissors the border and holds gui elements
 */
public class AOTDGuiPanel extends AOTDGuiContainer
{
    // True if scissors is enabled, false otherwise
    private final boolean scissorEnabled;

    /**
     * Constructor initializes the bounding box and if scissors are enabled or not
     *
     * @param x              The X location of the top left corner
     * @param y              The Y location of the top left corner
     * @param width          The width of the component
     * @param height         The height of the component
     * @param scissorEnabled If scissors are enabled when drawing this panel
     */
    public AOTDGuiPanel(int x, int y, int width, int height, boolean scissorEnabled)
    {
        super(x, y, width, height);
        this.scissorEnabled = scissorEnabled;
    }

    /**
     * A panel can only be drawn inside of a box that may be scissored
     */
    @Override
    public void draw()
    {
        // Grab the gui-utility reference
        AOTDGuiUtility guiUtility = AOTDGuiUtility.getInstance();

        // If scissor is enabled we use glScissor to force all drawing to happen inside of a box
        if (scissorEnabled)
        {
            // Compute the OpenGL X and Y screen coordinates to scissor
            int realX = guiUtility.mcToRealScreenCoord(this.getXScaled());
            int realY = guiUtility.realScreenYToGLYCoord(guiUtility.mcToRealScreenCoord(this.getYScaled() + this.getHeightScaled()));
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
}
