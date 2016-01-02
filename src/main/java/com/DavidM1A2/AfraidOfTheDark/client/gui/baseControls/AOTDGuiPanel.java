/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDGuiUtility;

import net.minecraft.client.renderer.GlStateManager;

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

			int realX = AOTDGuiUtility.mcToRealCoord(this.getXScaled());
			int realY = AOTDGuiUtility.realToGLScreenCoords(AOTDGuiUtility.mcToRealCoord(this.getYScaled() + this.getHeightScaled()));
			int realWidth = AOTDGuiUtility.mcToRealCoord(this.getWidthScaled());
			int realHeight = AOTDGuiUtility.mcToRealCoord(this.getHeightScaled());

			GL11.glScissor(realX, realY, realWidth, realHeight);
		}

		super.draw();

		if (scissorEnabled)
		{
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
		}
	}
}
