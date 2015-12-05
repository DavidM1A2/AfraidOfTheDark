/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import net.minecraft.client.gui.GuiScreen;

public abstract class AOTDGuiScreen extends GuiScreen
{
	private AOTDButtonController buttonController = new AOTDButtonController();
	private double guiScale = 1.0f;
	private AOTDGuiPanel contentPane = new AOTDGuiPanel();

	public AOTDGuiScreen()
	{
		super();
		contentPane.setX(0);
		contentPane.setY(0);
		contentPane.setWidth(640);
		contentPane.setHeight(360);
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.clear();
		this.buttonList.add(buttonController);

		double guiScaleX = this.width / 640D;
		double guiScaleY = this.height / 360D;
		this.guiScale = Math.min(guiScaleX, guiScaleY);

		this.getContentPane().setScaleXAndY(guiScale);
		if (guiScaleX < guiScaleY)
		{
			this.getContentPane().setY((this.height - this.getContentPane().getHeightScaled()) / 2);
		}
		else
		{
			this.getContentPane().setX((this.width - this.getContentPane().getWidthScaled()) / 2);
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.getContentPane().draw();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public AOTDButtonController getButtonController()
	{
		return this.buttonController;
	}

	public void actionPerformed(AOTDGuiButton button)
	{
	}

	public double getGuiScale()
	{
		return this.guiScale;
	}

	public AOTDGuiPanel getContentPane()
	{
		return this.contentPane;
	}

	// Opening a research book DOES NOT pause the game (unlike escape)
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
}
