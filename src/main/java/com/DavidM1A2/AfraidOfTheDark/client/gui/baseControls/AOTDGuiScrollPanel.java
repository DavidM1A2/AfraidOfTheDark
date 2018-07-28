/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls;

import java.awt.Rectangle;

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
			int realX = AOTDGuiUtility.mcToRealCoord(this.getXScaled());
			int realY = AOTDGuiUtility.realToGLScreenCoords(AOTDGuiUtility.mcToRealCoord((int) (ORIGINAL_Y_POS * this.getScaleY() + this.getHeightScaled())));
			int realWidth = AOTDGuiUtility.mcToRealCoord(this.getWidthScaled());
			int realHeight = AOTDGuiUtility.mcToRealCoord(this.getHeightScaled());

			AOTDGuiUtility.beginGLScissor(realX, realY, realWidth, realHeight);
		}

		if (lastSliderPosition != this.scrollSource.getSliderValue())
		{
			lastSliderPosition = this.scrollSource.getSliderValue();
			super.setY(ORIGINAL_Y_POS + (int) (maximumOffset * lastSliderPosition));
			Rectangle realBoundingBox = new Rectangle(this.getXScaled(), ORIGINAL_Y_POS, this.getWidthScaled(), this.getHeightScaled());
			for (AOTDGuiContainer container : this.getChildren())
			{
				if (container.intersects(realBoundingBox))
					container.setVisible(true);
				else
					container.setVisible(false);
			}
		}

		super.draw();

		if (scissorEnabled)
		{
			AOTDGuiUtility.endGLScissor();
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
		this.lastSliderPosition = -1.0f;
		this.maximumOffset = maxOffset;
	}

	public int getMaximumOffset()
	{
		return this.maximumOffset;
	}
}
