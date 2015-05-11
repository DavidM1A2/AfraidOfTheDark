package com.DavidM1A2.AfraidOfTheDark.client.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;
import com.DavidM1A2.AfraidOfTheDark.utility.LogHelper;

public class BloodStainedJournalPageGUI extends GuiScreen
{
	private final GuiLabel mainText;

	public BloodStainedJournalPageGUI()
	{
		super();
		mainText = new GuiLabel(Minecraft.getMinecraft().fontRendererObj, 9001, 50, 50, 300, 300, 0xFF33CC);
		mainText.visible = true;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		buttonList.clear();
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

		GL11.glColor4f(1, 1, 1, 1);
		mc.renderEngine.bindTexture(new ResourceLocation("afraidofthedark:textures/gui/bloodStainedJournalPage.png"));
		double scale = this.width / 640.0;
		LogHelper.info(scale + "   width = " + this.width);
		Refrence.drawTexturedQuadFit((this.width - 400 * scale) / 2, (this.height - 400 * scale) / 2, 400 * scale, 400 * scale, scale);
		mainText.drawString(Minecraft.getMinecraft().fontRendererObj, "Hello World", 50, 50, 0xFF33CC);
		super.drawScreen(i, j, f);
	}

	// If E is typed we close the GUI screen
	@Override
	protected void keyTyped(char character, int iDontKnowWhatThisDoes) throws IOException
	{
		if (character == 'e' || character == 'E')
		{
			Minecraft.getMinecraft().thePlayer.closeScreen();
		}
		super.keyTyped(character, iDontKnowWhatThisDoes);
	}
}
