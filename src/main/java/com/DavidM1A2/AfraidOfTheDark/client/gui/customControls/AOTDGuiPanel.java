/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.common.utility.Point3D;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

public class AOTDGuiPanel extends AOTDGuiContainer
{
	private final boolean scissorEnabled;

	public AOTDGuiPanel(int x, int y, int width, int height, boolean scissorEnabled)
	{
		super(x, y, width, height);
		this.scissorEnabled = scissorEnabled;
	}

	@Override
	public void draw()
	{
		if (scissorEnabled)
		{
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
			Point3D xyReal = Utility.minecraftToRealScreenCoords(this.getXScaled(), this.getYScaled());
			Point3D widthHeightReal = Utility.minecraftToRealScreenCoords(this.getWidthScaled(), this.getHeightScaled());
			GL11.glScissor(xyReal.getX(), xyReal.getY(), widthHeightReal.getX(), widthHeightReal.getY());
		}
		super.draw();
		if (scissorEnabled)
		{
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
		}
	}
}
