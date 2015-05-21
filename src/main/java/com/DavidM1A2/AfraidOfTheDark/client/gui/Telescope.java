package com.DavidM1A2.AfraidOfTheDark.client.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.packets.TellServerToCreateMeteor;

public class Telescope extends GuiScreen
{
	private GuiButton temp1;

	/*
	 * GUI for the blood stained journal on the initial signing
	 */
	@Override
	public void initGui()
	{
		temp1 = new GuiButton(0, 50, 50, 300, 25, "test12345");
		buttonList.clear();
		buttonList.add(temp1);
	}

	// Opening a research book DOES NOT pause the game (unlike escape)
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	// To draw the screen we first draw the default GUI background, then the
	// background images. Then we add buttons and a frame.
	@Override
	public void drawScreen(int i, int j, float f)
	{
		drawDefaultBackground();
		super.drawScreen(i, j, f);
	}

	// If you press the sign button one of two things happens
	@Override
	public void actionPerformed(GuiButton button)
	{
		EntityPlayer playerWhoPressed = mc.getMinecraft().thePlayer;
		if (button.id == this.temp1.id)
		{
			AfraidOfTheDark.getSimpleNetworkWrapper().sendToServer(new TellServerToCreateMeteor(playerWhoPressed.getPosition().add(0, 20, 0), 5, 5));
		}
	}

	// If E is typed we close the GUI screen
	@Override
	protected void keyTyped(char character, int iDontKnowWhatThisDoes) throws IOException
	{
		if (character == 'e' || character == 'E')
		{
			Minecraft.getMinecraft().thePlayer.closeScreen();
			GL11.glFlush();
		}
		super.keyTyped(character, iDontKnowWhatThisDoes);
	}
}
