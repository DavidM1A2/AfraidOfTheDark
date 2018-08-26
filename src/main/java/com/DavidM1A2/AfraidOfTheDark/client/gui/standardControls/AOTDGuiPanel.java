package com.DavidM1A2.afraidofthedark.client.gui.standardControls;

import com.DavidM1A2.afraidofthedark.client.gui.AOTDGuiUtility;
import com.DavidM1A2.afraidofthedark.client.gui.base.AOTDGuiContainer;
import org.lwjgl.opengl.GL11;

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
	 * @param x      The X location of the top left corner
	 * @param y      The Y location of the top left corner
	 * @param width  The width of the component
	 * @param height The height of the component
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
