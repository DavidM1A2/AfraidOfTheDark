/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.packets.UpdateAOTDStatus;
import com.DavidM1A2.AfraidOfTheDark.packets.UpdateResearch;
import com.DavidM1A2.AfraidOfTheDark.playerData.HasStartedAOTD;
import com.DavidM1A2.AfraidOfTheDark.playerData.LoadResearchData;

public class BloodStainedJournalSignGUI extends GuiScreen
{
	private static final int SIGN_JOURNAL_BUTTON_ID = 0;
	private GuiTextField signNameHere;

	/*
	 * GUI for the blood stained journal on the initial signing
	 */
	@Override
	public void initGui()
	{
		buttonList.clear();
		buttonList.add(new GuiButton(SIGN_JOURNAL_BUTTON_ID, (this.width / 2) - 75, (this.height / 2) - 10, 150, 20, "Sign The Journal"));
		signNameHere = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 75, this.height / 2 - 35, 150, 20);
		signNameHere.setFocused(true);
		signNameHere.setMaxStringLength(1000);
	}

	// When someone types a key, update the text field
	@Override
	public void keyTyped(char character, int i) throws IOException
	{
		super.keyTyped(character, i);
		signNameHere.textboxKeyTyped(character, i);
	}

	// GUI does not pause the game
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	// Draw the screen (background, then buttons, then the textbox).
	@Override
	public void drawScreen(int i, int j, float f)
	{
		drawDefaultBackground();
		GL11.glColor4f(1, 1, 1, 1);
		mc.renderEngine.bindTexture(new ResourceLocation("afraidofthedark:textures/gui/bloodStainedJournal.png"));
		this.drawTexturedModalRect((this.width - 256) / 2, (this.height - 256) / 2, 0, 0, 256, 256);
		signNameHere.drawTextBox();
		super.drawScreen(i, j, f);
	}

	// If you press the sign button one of two things happens
	@Override
	public void actionPerformed(GuiButton button)
	{
		EntityPlayer playerWhoPressed = mc.getMinecraft().thePlayer;
		switch (button.id)
		{
			case 0:
			{
				if (signNameHere.getText().equals(playerWhoPressed.getDisplayName().getUnformattedText()))
				{
					// If the player signed their own name and has not started
					// AOTD
					if (HasStartedAOTD.get(playerWhoPressed) == false)
					{
						HasStartedAOTD.set(playerWhoPressed, true);
						playerWhoPressed.inventory.getStackInSlot(playerWhoPressed.inventory.currentItem).getTagCompound().setString("owner", playerWhoPressed.getDisplayName().getUnformattedText());
						AfraidOfTheDark.getSimpleNetworkWrapper().sendToServer(new UpdateAOTDStatus(true));
						playerWhoPressed.addChatMessage(new ChatComponentText("§4§oWhat §4§ohave §4§oI §4§odone?"));
						LoadResearchData.get(playerWhoPressed).getResearch().unlockResearch(0);
						AfraidOfTheDark.getSimpleNetworkWrapper().sendToServer(new UpdateResearch(0, true));
						playerWhoPressed.closeScreen();
					}
				}
				else
				{
					if (HasStartedAOTD.get(playerWhoPressed) == false)
					{
						playerWhoPressed.addChatMessage(new ChatComponentText("*You expect something to happen... but nothing does."));
						playerWhoPressed.closeScreen();
					}
				}
			}
		}
	}
}
