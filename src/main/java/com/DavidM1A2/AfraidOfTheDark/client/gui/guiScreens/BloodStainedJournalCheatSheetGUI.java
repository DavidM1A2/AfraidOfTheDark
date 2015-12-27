/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.guiScreens;

import java.awt.Color;

import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDActionListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiButton;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiComponent;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiScreen;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDPlayerData;

import net.minecraft.util.ChatComponentText;

public class BloodStainedJournalCheatSheetGUI extends AOTDGuiScreen
{
	public BloodStainedJournalCheatSheetGUI()
	{
		AOTDGuiPanel background = new AOTDGuiPanel((640 - 256) / 2, (360 - 256) / 2, 256, 256, false);
		this.getContentPane().add(background);
		AOTDGuiImage backgroundImage = new AOTDGuiImage(0, 0, 256, 256, "textures/gui/bloodStainedJournalCheatSheet.png");
		background.add(backgroundImage);
		AOTDGuiButton confirm = new AOTDGuiButton(30, 200, 80, 20, null, "afraidofthedark:textures/gui/bloodStainedJournalCheatSheetConfirm.png");
		confirm.addActionListener(new AOTDActionListener()
		{
			@Override
			public void actionPerformed(AOTDGuiComponent component, ActionType actionType)
			{
				if (actionType == ActionType.MousePressed)
				{
					if (component.isHovered())
					{
						for (ResearchTypes type : ResearchTypes.values())
						{
							if (!AOTDPlayerData.get(entityPlayer).isResearched(type))
							{
								AOTDPlayerData.get(entityPlayer).unlockResearch(type, false);
							}
						}
						entityPlayer.playSound("gui.button.press", 1.0f, 1.0f);
						entityPlayer.addChatMessage(new ChatComponentText("All researches unlocked."));
						entityPlayer.closeScreen();
					}
				}
				else if (actionType == ActionType.MouseEnterBoundingBox)
				{
					component.setColor(new Color(230, 230, 230));
					entityPlayer.playSound("afraidofthedark:buttonHover", 0.7f, 1.9f);
				}
				else if (actionType == ActionType.MouseExitBoundingBox)
				{
					component.setColor(Color.WHITE);
				}
			}
		});
		AOTDGuiButton deny = new AOTDGuiButton(140, 200, 80, 20, null, "afraidofthedark:textures/gui/bloodStainedJournalCheatSheetDeny.png");
		deny.addActionListener(new AOTDActionListener()
		{
			@Override
			public void actionPerformed(AOTDGuiComponent component, ActionType actionType)
			{
				if (actionType == ActionType.MousePressed)
				{
					if (component.isHovered())
					{
						entityPlayer.playSound("gui.button.press", 1.0f, 1.0f);
						entityPlayer.closeScreen();
					}
				}
				else if (actionType == ActionType.MouseEnterBoundingBox)
				{
					component.setColor(new Color(230, 230, 230));
					entityPlayer.playSound("afraidofthedark:buttonHover", 0.7f, 1.9f);
				}
				else if (actionType == ActionType.MouseExitBoundingBox)
				{
					component.setColor(Color.WHITE);
				}
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
