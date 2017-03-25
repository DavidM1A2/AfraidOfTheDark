/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.guiScreens;

import java.awt.Color;
import java.io.IOException;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiButton;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiTextField;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.TextAlignment;
import com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners.AOTDKeyListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners.AOTDMouseListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDKeyEvent;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent.MouseButtonClicked;
import com.DavidM1A2.AfraidOfTheDark.client.settings.ClientData;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModSounds;
import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;

import net.minecraft.init.SoundEvents;
import net.minecraft.util.text.TextComponentString;

public class BloodStainedJournalSignGUI extends AOTDGuiScreen
{
	private AOTDGuiTextField nameSignField;

	public BloodStainedJournalSignGUI()
	{
		AOTDGuiPanel backgroundPanel = new AOTDGuiPanel((640 - 256) / 2, (360 - 256) / 2, 256, 256, false);

		AOTDGuiImage backgroundImage = new AOTDGuiImage(0, 0, 256, 256, "afraidofthedark:textures/gui/blood_stained_journal.png");
		backgroundPanel.add(backgroundImage);

		this.nameSignField = new AOTDGuiTextField(45, 90, 160, 30, ClientData.getTargaMSHandFontSized(45f));
		this.nameSignField.setTextColor(Color.red);
		backgroundPanel.add(this.nameSignField);

		AOTDGuiButton signButton = new AOTDGuiButton(75, 130, 100, 25, ClientData.getTargaMSHandFontSized(45f), "afraidofthedark:textures/gui/sign_button.png", "afraidofthedark:textures/gui/sign_button_hovered.png");
		signButton.setText("Sign");
		signButton.setTextColor(Color.RED);
		signButton.setTextAlignment(TextAlignment.ALIGN_CENTER);
		signButton.addMouseListener(new AOTDMouseListener()
		{
			@Override
			public void mouseClicked(AOTDMouseEvent event)
			{
				if (event.getSource().isHovered() && event.getClickedButton() == MouseButtonClicked.Left)
				{
					entityPlayer.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0f, 1.0f);
					if (BloodStainedJournalSignGUI.this.nameSignField.getText().equals(entityPlayer.getDisplayName().getUnformattedText()))
					{
						// If the player signed their own name and has not started
						// AOTD
						if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getHasStartedAOTD() == false)
						{
							entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).setHasStartedAOTD(true);
							entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).syncHasStartedAOTD();

							entityPlayer.inventory.getStackInSlot(entityPlayer.inventory.currentItem).getTagCompound().setString("owner", entityPlayer.getDisplayName().getUnformattedText());
							entityPlayer.sendMessage(new TextComponentString("What have I done?"));
							entityPlayer.playSound(ModSounds.journalSign, 4.0F, 1.0F);
							entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).unlockResearch(ResearchTypes.AnUnbreakableCovenant, true);
							entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).unlockResearch(ResearchTypes.Crossbow, true);
							entityPlayer.closeScreen();
						}
					}
					else
					{
						if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getHasStartedAOTD() == false)
						{
							entityPlayer.sendMessage(new TextComponentString("*You expect something to happen... but nothing does. Perhaps the name you signed was incorrect?"));
							entityPlayer.closeScreen();
						}
					}
				}
			}
		});
		signButton.addMouseListener(new AOTDMouseListener()
		{
			@Override
			public void mouseEntered(AOTDMouseEvent event)
			{
				entityPlayer.playSound(ModSounds.buttonHover, 0.1f, 0.8f);
			}
		});
		this.nameSignField.addKeyListener(new AOTDKeyListener()
		{
			@Override
			public void keyTyped(AOTDKeyEvent event)
			{
				if (BloodStainedJournalSignGUI.this.nameSignField.isFocused())
					entityPlayer.playSound(ModSounds.keyTyped, 0.4f, 0.8f);
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
