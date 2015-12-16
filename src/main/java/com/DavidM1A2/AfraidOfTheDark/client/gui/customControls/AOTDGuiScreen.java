/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDGuiUtility;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.common.MinecraftForge;

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
		AOTDGuiUtility.updateScaledResolution();
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

	/**
	 * Causes the screen to lay out its subcomponents again. This is the equivalent of the Java call Container.validate()
	 */
	@Override
	public void setWorldAndResolution(Minecraft mc, int width, int height)
	{
		this.mc = mc;
		this.itemRender = mc.getRenderItem();
		this.fontRendererObj = mc.fontRendererObj;
		this.width = width;
		this.height = height;
		if (!MinecraftForge.EVENT_BUS.post(new InitGuiEvent.Pre(this, this.buttonList)))
		{
			this.buttonList.clear();
			this.initGui();
		}
		MinecraftForge.EVENT_BUS.post(new InitGuiEvent.Post(this, this.buttonList));
	}
}
