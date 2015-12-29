/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.guiScreens;

import java.awt.Color;
import java.io.IOException;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDActionListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiButton;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiComponent;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiScreen;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiTextField;
import com.DavidM1A2.AfraidOfTheDark.client.trueTypeFont.FontLoader;
import com.DavidM1A2.AfraidOfTheDark.client.trueTypeFont.TrueTypeFont;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;

public class BloodStainedJournalSignGUI extends AOTDGuiScreen
{
	private AOTDGuiTextField nameSignField;
	private static final TrueTypeFont TEXT_FIELD_FONT = FontLoader.createFont(new ResourceLocation("afraidofthedark:fonts/Targa MS Hand.ttf"), 45f, false);
	private static final TrueTypeFont SIGN_BUTTON_FONT = FontLoader.createFont(new ResourceLocation("afraidofthedark:fonts/Targa MS Hand.ttf"), 45f, false);

	public BloodStainedJournalSignGUI()
	{
		AOTDGuiPanel backgroundPanel = new AOTDGuiPanel((640 - 256) / 2, (360 - 256) / 2, 256, 256, false);

		AOTDGuiImage backgroundImage = new AOTDGuiImage(0, 0, 256, 256, "textures/gui/bloodStainedJournal.png");
		backgroundPanel.add(backgroundImage);

		this.nameSignField = new AOTDGuiTextField(45, 90, 160, 30, TEXT_FIELD_FONT);
		this.nameSignField.setTextColor(Color.red);
		backgroundPanel.add(this.nameSignField);

		AOTDGuiButton signButton = new AOTDGuiButton(75, 130, 100, 25, SIGN_BUTTON_FONT, "afraidofthedark:textures/gui/signButton.png", "afraidofthedark:textures/gui/signButtonHovered.png");
		signButton.setText("Sign");
		signButton.setTextColor(Color.RED);
		signButton.addActionListener(new AOTDActionListener()
		{
			@Override
			public void actionPerformed(AOTDGuiComponent component, ActionType actionType)
			{
				if (actionType == ActionType.MousePressed)
				{
					if (component.isHovered())
					{
						entityPlayer.playSound("gui.button.press", 1.0f, 1.0f);
						if (BloodStainedJournalSignGUI.this.nameSignField.getText().equals(entityPlayer.getDisplayName().getUnformattedText()))
						{
							// If the player signed their own name and has not started
							// AOTD
							if (AOTDPlayerData.get(entityPlayer).getHasStartedAOTD() == false)
							{
								AOTDPlayerData.get(entityPlayer).setHasStartedAOTD(true);
								AOTDPlayerData.get(entityPlayer).syncHasStartedAOTD();

								entityPlayer.inventory.getStackInSlot(entityPlayer.inventory.currentItem).getTagCompound().setString("owner", entityPlayer.getDisplayName().getUnformattedText());
								entityPlayer.addChatMessage(new ChatComponentText("What have I done?"));
								entityPlayer.playSound("afraidofthedark:journalSign", 4.0F, 1.0F);
								AOTDPlayerData.get(entityPlayer).unlockResearch(ResearchTypes.AnUnbreakableCovenant, true);
								AOTDPlayerData.get(entityPlayer).unlockResearch(ResearchTypes.Crossbow, true);
								entityPlayer.closeScreen();
							}
						}
						else
						{
							if (AOTDPlayerData.get(entityPlayer).getHasStartedAOTD() == false)
							{
								entityPlayer.addChatMessage(new ChatComponentText("*You expect something to happen... but nothing does."));
								entityPlayer.closeScreen();
							}
						}
					}
				}
			}
		});
		backgroundPanel.add(signButton);

		this.getContentPane().add(backgroundPanel);
	}

	@Override
	protected void keyTyped(char character, int keyCode) throws IOException
	{
		super.keyTyped(character, keyCode);
		if (!this.nameSignField.isFocused())
		{
			if (keyCode == INVENTORY_KEYCODE)
			{
				entityPlayer.closeScreen();
				GL11.glFlush();
			}
		}
	}

	@Override
	public boolean inventoryToCloseGuiScreen()
	{
		return false;
	}

	@Override
	public boolean drawGradientBackground()
	{
		return true;
	}
}
