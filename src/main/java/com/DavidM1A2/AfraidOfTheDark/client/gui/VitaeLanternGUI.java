package com.DavidM1A2.AfraidOfTheDark.client.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;

import com.DavidM1A2.AfraidOfTheDark.client.settings.Keybindings;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

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

	}

	// To draw the screen we first draw the default GUI background, then the
	// background images. Then we add buttons and a frame.
	@Override
	public void drawScreen(final int i, final int j, final float f)
	{
		super.drawScreen(i, j, f);

		this.mc.renderEngine.bindTexture(DIAL);
		this.drawTexturedModalRect((this.width - 256) / 2, (this.height - 256) / 2, 0, 0, 256, 256);

		int centerOfScreenX = (this.width - 256) / 2;
		int centerOfScreenY = (this.height - 256) / 2;

		LogHelper.info(centerOfScreenX);
		LogHelper.info(centerOfScreenY);

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
