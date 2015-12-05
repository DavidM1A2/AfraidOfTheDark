/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.guiScreens;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.client.settings.Keybindings;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemVitaeLantern;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateLanternState;
import com.DavidM1A2.AfraidOfTheDark.common.utility.NBTHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class VitaeLanternGUI extends GuiScreen
{
	private static final ResourceLocation DIAL = new ResourceLocation("afraidofthedark:textures/gui/vitaeDial.png");
	private static final ResourceLocation POINTER = new ResourceLocation("afraidofthedark:textures/gui/vitaeNeedle.png");
	private static final ResourceLocation GRAY_POINTER = new ResourceLocation("afraidofthedark:textures/gui/vitaeNeedlePrevious.png");

	private int vitaeLanternAngle = 0;

	public VitaeLanternGUI()
	{
		super();
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.clear();

		for (ItemStack itemStack : Minecraft.getMinecraft().thePlayer.inventory.mainInventory)
		{
			if (itemStack != null)
			{
				if (itemStack.getItem() instanceof ItemVitaeLantern)
				{
					if (NBTHelper.getBoolean(itemStack, "isActive"))
					{
						vitaeLanternAngle = MathHelper.floor_double(NBTHelper.getDouble(itemStack, "equalibriumPercentage") * 360.0D);
					}
				}
			}
		}
	}

	/**
	 * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
	 */
	@Override
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

		AfraidOfTheDark.getPacketHandler().sendToServer(new UpdateLanternState(lanternState));

		Minecraft.getMinecraft().thePlayer.closeScreen();
	}

	// To draw the screen we first draw the default GUI background, then the
	// background images. Then we add buttons and a frame.
	@Override
	public void drawScreen(final int i, final int j, final float f)
	{
		super.drawScreen(i, j, f);

		this.mc.renderEngine.bindTexture(DIAL);
		Gui.drawScaledCustomSizeModalRect((this.width - 256) / 2, (this.height - 256) / 2, 0, 0, 2048, 2048, 256, 256, 2048, 2048);

		this.mc.renderEngine.bindTexture(GRAY_POINTER);
		GL11.glTranslated(this.width / 2, this.height / 2, 0.0D);
		GL11.glRotated(-vitaeLanternAngle, 0, 0, 1);
		GL11.glTranslated(-this.width / 2, -this.height / 2, 0.0D);
		Gui.drawScaledCustomSizeModalRect((this.width - 256) / 2, (this.height - 256) / 2, 0, 0, 2048, 2048, 256, 256, 2048, 2048);

		this.mc.renderEngine.bindTexture(POINTER);
		GL11.glTranslated(this.width / 2, this.height / 2, 0.0D);
		int centerOfScreenX = this.width / 2;
		int centerOfScreenY = this.height / 2;
		double angle = Math.toDegrees(Math.atan2(i - centerOfScreenX, j - centerOfScreenY));

		GL11.glRotated(vitaeLanternAngle, 0, 0, 1);
		GL11.glRotated(-angle, 0, 0, 1);
		GL11.glTranslated(-this.width / 2, -this.height / 2, 0.0D);
		Gui.drawScaledCustomSizeModalRect((this.width - 256) / 2, (this.height - 256) / 2, 0, 0, 2048, 2048, 256, 256, 2048, 2048);

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
