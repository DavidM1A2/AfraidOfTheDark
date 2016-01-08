/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDGuiUtility;

public class AOTDGuiScrollPanel extends AOTDGuiContainer
{
	private AOTDGuiScrollBar scrollSource;
	private final boolean scissorEnabled;
	private int maximumOffset = 0;
	private float lastSliderPosition = 0;
	private int ORIGINAL_Y_POS = 0;

	public AOTDGuiScrollPanel(int x, int y, int width, int height, boolean scissorEnabled, AOTDGuiScrollBar scrollSource)
	{
		super(x, y, width, height);
		ORIGINAL_Y_POS = y;
		this.scrollSource = scrollSource;
		this.scissorEnabled = scissorEnabled;
	}

	@Override
	public void draw()
	{
		if (scissorEnabled)
		{
			GL11.glEnable(GL11.GL_SCISSOR_TEST);

			int realX = AOTDGuiUtility.mcToRealCoord(this.getXScaled());
			int realY = AOTDGuiUtility.realToGLScreenCoords(AOTDGuiUtility.mcToRealCoord((int) (ORIGINAL_Y_POS * this.getScaleY() + this.getHeightScaled())));
			int realWidth = AOTDGuiUtility.mcToRealCoord(this.getWidthScaled());
			int realHeight = AOTDGuiUtility.mcToRealCoord(this.getHeightScaled());

			GL11.glScissor(realX, realY, realWidth, realHeight);
		}

		if (lastSliderPosition != this.scrollSource.getSliderValue())
		{
			lastSliderPosition = this.scrollSource.getSliderValue();
			super.setY(ORIGINAL_Y_POS + (int) (maximumOffset * lastSliderPosition));
		}

		super.draw();

		if (scissorEnabled)
		{
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
		}
	}

	@Override
	public void setY(int y)
	{
		ORIGINAL_Y_POS = y;
		super.setY(y);
	}

	public void setMaximumOffset(int maxOffset)
	{
		this.maximumOffset = maxOffset;
	}

	public int getMaximumOffset()
	{
		return this.maximumOffset;
	}
}
