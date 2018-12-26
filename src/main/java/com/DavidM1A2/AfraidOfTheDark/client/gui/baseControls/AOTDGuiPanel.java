/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls;

import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDGuiUtility;

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
			int realX = AOTDGuiUtility.mcToRealCoord(this.getXScaled());
			int realY = AOTDGuiUtility.realToGLScreenCoords(AOTDGuiUtility.mcToRealCoord(this.getYScaled() + this.getHeightScaled()));
			int realWidth = AOTDGuiUtility.mcToRealCoord(this.getWidthScaled());
			int realHeight = AOTDGuiUtility.mcToRealCoord(this.getHeightScaled());

			AOTDGuiUtility.beginGLScissor(realX, realY, realWidth, realHeight);
		}

		super.draw();

		if (scissorEnabled)
		{
			AOTDGuiUtility.endGLScissor();
		}
	}
}
