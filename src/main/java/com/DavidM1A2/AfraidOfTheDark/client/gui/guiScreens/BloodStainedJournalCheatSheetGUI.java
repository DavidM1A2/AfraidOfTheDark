/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.guiScreens;

import java.awt.Color;

import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiButton;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners.AOTDMouseListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;

import net.minecraft.util.ChatComponentText;

public class BloodStainedJournalCheatSheetGUI extends AOTDGuiScreen
{
	public BloodStainedJournalCheatSheetGUI()
	{
		AOTDGuiPanel background = new AOTDGuiPanel((640 - 256) / 2, (360 - 256) / 2, 256, 256, false);
		this.getContentPane().add(background);
		AOTDGuiImage backgroundImage = new AOTDGuiImage(0, 0, 256, 256, "afraidofthedark:textures/gui/bloodStainedJournalCheatSheet.png");
		background.add(backgroundImage);
		AOTDGuiButton confirm = new AOTDGuiButton(30, 200, 80, 20, null, "afraidofthedark:textures/gui/bloodStainedJournalCheatSheetConfirm.png");
		confirm.addMouseListener(new AOTDMouseListener()
		{
			@Override
			public void mouseClicked(AOTDMouseEvent event)
			{
				if (event.getSource().isHovered())
				{
					for (ResearchTypes type : ResearchTypes.values())
					{
						if (!entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(type))
						{
							entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).unlockResearch(type, false);
						}
					}
					entityPlayer.playSound("gui.button.press", 1.0f, 1.0f);
					entityPlayer.addChatMessage(new ChatComponentText("All researches unlocked."));
					entityPlayer.closeScreen();
				}
			}

			@Override
			public void mousePressed(AOTDMouseEvent event)
			{
			}

			@Override
			public void mouseReleased(AOTDMouseEvent event)
			{
			}

			@Override
			public void mouseEntered(AOTDMouseEvent event)
			{
				event.getSource().setColor(new Color(230, 230, 230));
				entityPlayer.playSound("afraidofthedark:buttonHover", 0.7f, 1.9f);
			}

			@Override
			public void mouseExited(AOTDMouseEvent event)
			{
				event.getSource().setColor(Color.WHITE);
			}
		});
		AOTDGuiButton deny = new AOTDGuiButton(140, 200, 80, 20, null, "afraidofthedark:textures/gui/bloodStainedJournalCheatSheetDeny.png");
		deny.addMouseListener(new AOTDMouseListener()
		{
			@Override
			public void mouseClicked(AOTDMouseEvent event)
			{
				if (event.getSource().isHovered())
				{
					entityPlayer.playSound("gui.button.press", 1.0f, 1.0f);
					entityPlayer.closeScreen();
				}
			}

			@Override
			public void mousePressed(AOTDMouseEvent event)
			{
			}

			@Override
			public void mouseReleased(AOTDMouseEvent event)
			{
			}

			@Override
			public void mouseEntered(AOTDMouseEvent event)
			{
				event.getSource().setColor(new Color(230, 230, 230));
				entityPlayer.playSound("afraidofthedark:buttonHover", 0.7f, 1.9f);
			}

			@Override
			public void mouseExited(AOTDMouseEvent event)
			{
				event.getSource().setColor(Color.WHITE);
			}
		});
		background.add(confirm);
		background.add(deny);
	}

	@Override
	public boolean inventoryToCloseGuiScreen()
	{
		return true;
	}

	@Override
	public boolean drawGradientBackground()
	{
		return true;
	}
}
