/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.guiScreens;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDGuiUtility;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.SpriteSheetController;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDKeyEvent;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDKeyEvent.KeyEventType;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent.MouseButtonClicked;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent.MouseEventType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public abstract class AOTDGuiScreen extends GuiScreen
{
	private double guiScale = 1.0f;
	private final AOTDGuiPanel contentPane;
	protected final int INVENTORY_KEYCODE = Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode();
	protected final EntityPlayerSP entityPlayer = Minecraft.getMinecraft().player;
	private List<SpriteSheetController> spriteSheetControllers = new LinkedList<SpriteSheetController>();
	protected final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
	private boolean leftMouseButtonDown = false;

	public AOTDGuiScreen()
	{
		super();
		contentPane = new AOTDGuiPanel(0, 0, 640, 360, false);
	}

	@Override
	public void initGui()
	{
		super.initGui();
		AOTDGuiUtility.updateScaledResolution();
		this.buttonList.clear();

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
		for (SpriteSheetController sheetController : this.spriteSheetControllers)
			sheetController.performUpdate();
		GlStateManager.enableBlend();
		if (this.drawGradientBackground())
			this.drawDefaultBackground();
		this.getContentPane().draw();
		this.getContentPane().drawOverlay();
		super.drawScreen(mouseX, mouseY, partialTicks);
		GlStateManager.disableBlend();
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

	// If E is typed we close the GUI screen
	@Override
	protected void keyTyped(final char character, final int keyCode) throws IOException
	{
		this.getContentPane().processKeyInput(new AOTDKeyEvent(this.getContentPane(), character, keyCode, KeyEventType.Type));
		if (this.inventoryToCloseGuiScreen())
		{
			if ((keyCode == INVENTORY_KEYCODE))
			{
				Minecraft.getMinecraft().player.closeScreen();
				GL11.glFlush();
			}
		}
		super.keyTyped(character, keyCode);
	}

	public void addSpriteSheetController(SpriteSheetController sheetController)
	{
		this.spriteSheetControllers.add(sheetController);
	}

	public void mouseInputEvent()
	{
		switch (Mouse.getEventButton())
		{
			case 0:
				this.processMouseClick(MouseButtonClicked.Left);
				break;
			case 1:
				this.processMouseClick(MouseButtonClicked.Right);
				break;
			case -1:
				contentPane.processMouseInput(new AOTDMouseEvent(contentPane, AOTDGuiUtility.getMouseX(), AOTDGuiUtility.getMouseY(), MouseButtonClicked.Other, MouseEventType.Move));
				if (leftMouseButtonDown)
					contentPane.processMouseInput(new AOTDMouseEvent(contentPane, AOTDGuiUtility.getMouseX(), AOTDGuiUtility.getMouseY(), MouseButtonClicked.Other, MouseEventType.Drag));
				break;
		}
	}

	private void processMouseClick(MouseButtonClicked clickedButton)
	{
		if (Mouse.getEventButtonState())
		{
			leftMouseButtonDown = true;
			contentPane.processMouseInput(new AOTDMouseEvent(contentPane, AOTDGuiUtility.getMouseX(), AOTDGuiUtility.getMouseY(), clickedButton, MouseEventType.Click));
		}
		else
		{
			contentPane.processMouseInput(new AOTDMouseEvent(contentPane, AOTDGuiUtility.getMouseX(), AOTDGuiUtility.getMouseY(), clickedButton, MouseEventType.Release));
			if (leftMouseButtonDown)
			{
				leftMouseButtonDown = false;
				contentPane.processMouseInput(new AOTDMouseEvent(contentPane, AOTDGuiUtility.getMouseX(), AOTDGuiUtility.getMouseY(), clickedButton, MouseEventType.Press));
			}
		}
	}

	public abstract boolean inventoryToCloseGuiScreen();

	public abstract boolean drawGradientBackground();
}
