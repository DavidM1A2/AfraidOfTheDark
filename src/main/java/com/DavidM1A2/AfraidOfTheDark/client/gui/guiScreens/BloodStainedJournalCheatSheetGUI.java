/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.guiScreens;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiScreen;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDPlayerData;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class BloodStainedJournalCheatSheetGUI extends AOTDGuiScreen
{
	private static final int YES_ID = 0;
	private static final int NO_ID = 1;
	private GuiTextField information;

	/*
	 * GUI for the blood stained journal on the initial signing
	 */
	@Override
	public void initGui()
	{
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(BloodStainedJournalCheatSheetGUI.YES_ID, (this.width / 2) - 75, (this.height / 2) - 35, 150, 20, "Okay"));
		this.buttonList.add(new GuiButton(BloodStainedJournalCheatSheetGUI.NO_ID, (this.width / 2) - 75, (this.height / 2) - 10, 150, 20, "No Thanks"));
	}

	// GUI does not pause the game
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	// Draw the screen (background, then buttons, then the textbox).
	@Override
	public void drawScreen(final int i, final int j, final float f)
	{
		GL11.glColor4f(1, 1, 1, 1);
		this.drawDefaultBackground();
		super.drawScreen(i, j, f);

		this.fontRendererObj.drawString("Pressing Okay will unlock all researches in this mod, ", (this.width / 2) - 120, (this.height / 2) - 70, 0xFFFFFF);
		this.fontRendererObj.drawString("including any side-effects that come with those researches.", (this.width / 2) - 140, (this.height / 2) - 60, 0xFFFFFF);
		this.fontRendererObj.drawString("Continue?", (this.width / 2) - 20, (this.height / 2) - 50, 0xFFFFFF);
	}

	// If you press the sign button one of two things happens
	@Override
	public void actionPerformed(final GuiButton button)
	{
		final EntityPlayer playerWhoPressed = Minecraft.getMinecraft().thePlayer;
		switch (button.id)
		{
			case YES_ID:
			{
				for (ResearchTypes type : ResearchTypes.values())
				{
					if (!AOTDPlayerData.get(playerWhoPressed).isResearched(type))
					{
						AOTDPlayerData.get(Minecraft.getMinecraft().thePlayer).unlockResearch(type, false);
					}
				}
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("All researches unlocked."));
				Minecraft.getMinecraft().thePlayer.closeScreen();
				break;
			}
			case NO_ID:
			{
				Minecraft.getMinecraft().thePlayer.closeScreen();
				break;
			}
		}
	}

	@Override
	public boolean inventoryToCloseGuiScreen()
	{
		return true;
	}
}
