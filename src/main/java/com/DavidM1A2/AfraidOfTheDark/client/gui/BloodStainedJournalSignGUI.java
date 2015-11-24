/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDPlayerData;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;

public class BloodStainedJournalSignGUI extends GuiScreen
{
	private static final int SIGN_JOURNAL_BUTTON_ID = 0;
	private GuiTextField signNameHere;
	private static final ResourceLocation background = new ResourceLocation("afraidofthedark:textures/gui/bloodStainedJournal.png");

	/*
	 * GUI for the blood stained journal on the initial signing
	 */
	@Override
	public void initGui()
	{
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(BloodStainedJournalSignGUI.SIGN_JOURNAL_BUTTON_ID, (this.width / 2) - 75, (this.height / 2) - 10, 150, 20, "Sign The Journal"));
		this.signNameHere = new GuiTextField(2, this.fontRendererObj, (this.width / 2) - 75, (this.height / 2) - 35, 150, 20);
		this.signNameHere.setFocused(true);
		this.signNameHere.setMaxStringLength(1000);
	}

	// When someone types a key, update the text field
	@Override
	public void keyTyped(final char character, final int i) throws IOException
	{
		super.keyTyped(character, i);
		this.signNameHere.textboxKeyTyped(character, i);
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
		this.mc.renderEngine.bindTexture(background);
		this.drawTexturedModalRect((this.width - 256) / 2, (this.height - 256) / 2, 0, 0, 256, 256);
		this.signNameHere.drawTextBox();
		super.drawScreen(i, j, f);
	}

	// If you press the sign button one of two things happens
	@Override
	public void actionPerformed(final GuiButton button)
	{
		final EntityPlayer playerWhoPressed = Minecraft.getMinecraft().thePlayer;
		switch (button.id)
		{
			case 0:
			{
				if (this.signNameHere.getText().equals(playerWhoPressed.getDisplayName().getUnformattedText()))
				{
					// If the player signed their own name and has not started
					// AOTD
					if (AOTDPlayerData.get(playerWhoPressed).getHasStartedAOTD() == false)
					{
						AOTDPlayerData.get(playerWhoPressed).setHasStartedAOTD(true);
						AOTDPlayerData.get(playerWhoPressed).syncHasStartedAOTD();

						playerWhoPressed.inventory.getStackInSlot(playerWhoPressed.inventory.currentItem).getTagCompound().setString("owner", playerWhoPressed.getDisplayName().getUnformattedText());
						playerWhoPressed.addChatMessage(new ChatComponentText("What have I done?"));
						playerWhoPressed.playSound("afraidofthedark:journalSign", 4.0F, 1.0F);
						AOTDPlayerData.get(playerWhoPressed).unlockResearch(ResearchTypes.AnUnbreakableCovenant, true);
						AOTDPlayerData.get(playerWhoPressed).unlockResearch(ResearchTypes.Crossbow, true);
						playerWhoPressed.closeScreen();
					}
				}
				else
				{
					if (AOTDPlayerData.get(playerWhoPressed).getHasStartedAOTD() == false)
					{
						playerWhoPressed.addChatMessage(new ChatComponentText("*You expect something to happen... but nothing does."));
						playerWhoPressed.closeScreen();
					}
				}
			}
		}
	}
}
