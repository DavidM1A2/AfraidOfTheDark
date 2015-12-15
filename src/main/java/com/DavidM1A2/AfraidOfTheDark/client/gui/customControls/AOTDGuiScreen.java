/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public abstract class AOTDGuiScreen extends GuiScreen
{
	private final AOTDEventController eventController;
	private double guiScale = 1.0f;
	private final AOTDGuiPanel contentPane;

	public AOTDGuiScreen()
	{
		super();
		contentPane = new AOTDGuiPanel(0, 0, 640, 360, false);
		eventController = new AOTDEventController(contentPane);
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.clear();
		this.buttonList.add(eventController);

		double guiScaleX = this.width / 640D;
		double guiScaleY = this.height / 360D;
		this.guiScale = Math.min(guiScaleX, guiScaleY);

		this.getContentPane().setScaleXAndY(guiScale);
		if (guiScaleX < guiScaleY)
		{
			this.getContentPane().setX(0);
			this.getContentPane().setY((this.height - this.getContentPane().getHeightScaled()) / 2);
		}
		else
		{
			this.getContentPane().setX((this.width - this.getContentPane().getWidthScaled()) / 2);
			this.getContentPane().setY(0);
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		GlStateManager.enableBlend();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.getContentPane().draw();
		super.drawScreen(mouseX, mouseY, partialTicks);
		GlStateManager.disableBlend();
	}

	public AOTDEventController getEventController()
	{
		return this.eventController;
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
