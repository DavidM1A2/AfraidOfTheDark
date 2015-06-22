/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.client.settings.Keybindings;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateLanternState;

public class VitaeLanternGUI extends GuiScreen
{
	private static final ResourceLocation DIAL = new ResourceLocation("afraidofthedark:textures/gui/Dial.png");

	public VitaeLanternGUI()
	{
		super();
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.clear();
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		int centerOfScreenX = this.width / 2;
		int centerOfScreenY = this.height / 2;

		double angle = Math.toDegrees(Math.atan2(mouseX - centerOfScreenX, mouseY - centerOfScreenY));
		if (angle < 0)
		{
			angle = angle + 360;
		}
		double lanternState = angle / 360.0;

		AfraidOfTheDark.getSimpleNetworkWrapper().sendToServer(new UpdateLanternState(lanternState));

		Minecraft.getMinecraft().thePlayer.closeScreen();
	}

	// To draw the screen we first draw the default GUI background, then the
	// background images. Then we add buttons and a frame.
	@Override
	public void drawScreen(final int i, final int j, final float f)
	{
		super.drawScreen(i, j, f);

		this.mc.renderEngine.bindTexture(DIAL);
		this.drawTexturedModalRect((this.width - 256) / 2, (this.height - 256) / 2, 0, 0, 256, 256);

		if (!Keyboard.isKeyDown(Keybindings.changeLanternMode.getKeyCode()))
		{
			Minecraft.getMinecraft().thePlayer.closeScreen();
		}
	}

	// Opening a research book DOES NOT pause the game (unlike escape)
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
}
