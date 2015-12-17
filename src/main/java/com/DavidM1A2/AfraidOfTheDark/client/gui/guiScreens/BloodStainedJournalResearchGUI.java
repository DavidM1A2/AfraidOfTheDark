/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.guiScreens;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDActionListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.GuiHandler;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiClickAndDragable;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiComponent;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiResearchNodeButton;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiSpriteSheetImage;
import com.DavidM1A2.AfraidOfTheDark.client.settings.ClientData;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDPlayerData;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public class BloodStainedJournalResearchGUI extends AOTDGuiClickAndDragable
{
	private AOTDGuiImage scrollBackground;
	private AOTDGuiImage backgroundBorder;

	private AOTDGuiPanel researchTreeBase;
	private AOTDGuiPanel researchTree;

	public BloodStainedJournalResearchGUI()
	{
		// Background will always be 256x256
		int backgroundHeight = 256;
		int backgroundWidth = 256;
		// Calculate the various positions of GUI elements on the screen
		int baseWidth = (640 - backgroundWidth) / 2;
		int baseHeight = (360 - backgroundHeight) / 2;
		int xPosScroll = baseWidth;
		int yPosScroll = baseHeight;
		int xPosBackground = baseWidth + 20;
		int yPosBackground = baseHeight + 20;

		this.guiOffsetX = ClientData.currentBloodStainedJournalX;
		this.guiOffsetY = ClientData.currentBloodStainedJournalY;

		this.researchTreeBase = new AOTDGuiPanel(xPosScroll, yPosScroll, backgroundWidth, backgroundHeight, true);
		this.researchTree = new AOTDGuiPanel(-this.guiOffsetX, -this.guiOffsetY, backgroundWidth, backgroundHeight, false);
		this.getContentPane().add(researchTreeBase);

		this.scrollBackground = new AOTDGuiImage(0, 0, backgroundWidth, backgroundHeight, 1024, 1024, "textures/gui/bloodStainedJournalResearchBackdrop.png");
		this.backgroundBorder = new AOTDGuiImage(0, 0, backgroundWidth, backgroundHeight, "textures/gui/bloodStainedJournalResearchBackground.png");
		this.scrollBackground.setU((this.guiOffsetX * 2) + 384);
		this.scrollBackground.setV((this.guiOffsetY * 2) + 768);
		this.researchTreeBase.add(scrollBackground);
		this.researchTreeBase.add(researchTree);
		this.researchTreeBase.add(backgroundBorder);

		AOTDActionListener nodeListener = new AOTDActionListener()
		{
			@Override
			public void actionPerformed(AOTDGuiComponent component, AOTDActionListener.ActionType actionType)
			{
				if (component instanceof AOTDGuiResearchNodeButton)
				{
					AOTDGuiResearchNodeButton current = (AOTDGuiResearchNodeButton) component;
					if (current.getParent().getParent().intersects(current))
					{
						if (actionType == ActionType.MousePressed)
						{
							EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;
							ClientData.currentlySelected = current.getResearch();
							if (AOTDPlayerData.get(entityPlayer).isResearched(current.getResearch()))
								entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.BLOOD_STAINED_JOURNAL_PAGE_ID, entityPlayer.worldObj, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
							else if (AOTDPlayerData.get(entityPlayer).isResearched(current.getResearch().getPrevious()))
								entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.BLOOD_STAINED_JOURNAL_PAGE_PRE_ID, entityPlayer.worldObj, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
						}
						else if (actionType == ActionType.MouseHover)
						{
							if (current.getParent().getParent().intersects(current))
							{
								if (current.isHovered() && AOTDPlayerData.get(Minecraft.getMinecraft().thePlayer).isResearched(current.getResearch()))
								{
									fontRendererObj.drawString(current.getResearch().formattedString(), current.getXScaled() + current.getHeightScaled(), current.getYScaled(), 0xFF3399);
									fontRendererObj.drawString(EnumChatFormatting.ITALIC + current.getResearch().getTooltip(), current.getXScaled() + current.getHeightScaled() + 2, current.getYScaled() + 10, 0xE62E8A);
								}
								else if (current.isHovered() && AOTDPlayerData.get(Minecraft.getMinecraft().thePlayer).canResearch(current.getResearch()))
								{
									fontRendererObj.drawString("?", current.getXScaled() + current.getHeightScaled(), current.getYScaled(), 0xFF3399);
									fontRendererObj.drawString(EnumChatFormatting.ITALIC + "Unknown Research", current.getXScaled() + current.getHeightScaled() + 2, current.getYScaled() + 10, 0xE62E8A);
								}
							}
						}
						else if (actionType == ActionType.MouseEnterBoundingBox)
							Minecraft.getMinecraft().thePlayer.playSound("afraidofthedark:buttonHover", 0.7f, 1.9f);
					}
				}
			}
		};

		int distanceBetweenNodes = 75;
		AOTDGuiResearchNodeButton[] buttons = new AOTDGuiResearchNodeButton[ResearchTypes.values().length];
		for (ResearchTypes researchType : ResearchTypes.values())
		{
			int xPos = backgroundWidth / 2 - 16 + distanceBetweenNodes * researchType.getPositionX();
			int yPos = backgroundHeight - 50 - distanceBetweenNodes * researchType.getPositionY();
			AOTDGuiResearchNodeButton researchNode = new AOTDGuiResearchNodeButton(xPos, yPos, researchType);
			researchNode.addActionListener(nodeListener);
			if (researchNode.getResearch().getPrevious() != null)
			{
				if (AOTDPlayerData.get(Minecraft.getMinecraft().thePlayer).isResearched(researchNode.getResearch()) || AOTDPlayerData.get(Minecraft.getMinecraft().thePlayer).canResearch(researchNode.getResearch()))
				{
					ResearchTypes previous = researchNode.getResearch().getPrevious();
					ResearchTypes current = researchNode.getResearch();
					if (current.getPositionX() < previous.getPositionX())
						this.researchTree.add(new AOTDGuiSpriteSheetImage(xPos + 26, yPos + 9, 54, 14, new ResourceLocation("afraidofthedark:textures/gui/researchHorizontal.png"), 500, 20, 180, 60, true, false));
					else if (current.getPositionX() > previous.getPositionX())
						this.researchTree.add(new AOTDGuiSpriteSheetImage(xPos - 50, yPos + 9, 54, 14, new ResourceLocation("afraidofthedark:textures/gui/researchHorizontal.png"), 500, 20, 180, 60, true, false));
					else if (current.getPositionY() > previous.getPositionY())
						this.researchTree.add(new AOTDGuiSpriteSheetImage(xPos + 9, yPos + 30, 14, 46, new ResourceLocation("afraidofthedark:textures/gui/researchVertical.png"), 500, 20, 60, 180, true, true));
					else if (current.getPositionY() < previous.getPositionY())
						this.researchTree.add(new AOTDGuiSpriteSheetImage(xPos + 9, yPos - 46, 14, 46, new ResourceLocation("afraidofthedark:textures/gui/researchVertical.png"), 500, 20, 60, 180, true, true));
				}
			}
			buttons[researchType.ordinal()] = researchNode;
		}

		for (AOTDGuiResearchNodeButton nodeButton : buttons)
			this.researchTree.add(nodeButton);
	}

	// When the mouse is dragged, update the GUI accordingly
	@Override
	protected void mouseClickMove(final int mouseX, final int mouseY, final int lastButtonClicked, final long timeBetweenClicks)
	{
		super.mouseClickMove(mouseX, mouseY, lastButtonClicked, timeBetweenClicks);

		this.researchTree.setX(-this.guiOffsetX + researchTree.getParent().getX());
		this.researchTree.setY(-this.guiOffsetY + researchTree.getParent().getY());

		scrollBackground.setU((this.guiOffsetX * 2) + 384);
		scrollBackground.setV((this.guiOffsetY * 2) + 768);
	}

	@Override
	protected void checkOutOfBounds()
	{
		if (this.guiOffsetX > 200)
		{
			this.guiOffsetX = 200;
		}
		if (this.guiOffsetX < -200)
		{
			this.guiOffsetX = -200;
		}
		if (this.guiOffsetY > 0)
		{
			this.guiOffsetY = 0;
		}
		if (this.guiOffsetY < -380)
		{
			this.guiOffsetY = -380;
		}
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	@Override
	public void onGuiClosed()
	{
		ClientData.currentBloodStainedJournalX = guiOffsetX;
		ClientData.currentBloodStainedJournalY = guiOffsetY;
		super.onGuiClosed();
	}
}
