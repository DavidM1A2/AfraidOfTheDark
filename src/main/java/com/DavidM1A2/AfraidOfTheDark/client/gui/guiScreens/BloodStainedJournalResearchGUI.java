/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.guiScreens;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.client.gui.GuiHandler;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiSpriteSheetImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.SpriteSheetController;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiResearchNodeButton;
import com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners.AOTDMouseListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent;
import com.DavidM1A2.AfraidOfTheDark.client.settings.ClientData;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;

import net.minecraft.util.ResourceLocation;

public class BloodStainedJournalResearchGUI extends AOTDGuiClickAndDragable
{
	private AOTDGuiImage scrollBackground;
	private AOTDGuiImage backgroundBorder;

	private AOTDGuiPanel researchTreeBase;
	private AOTDGuiPanel researchTree;

	private SpriteSheetController nodeConnectorControllerVertical = new SpriteSheetController(500, 20, 60, 180, true, true);
	private SpriteSheetController nodeConnectorControllerHorizontal = new SpriteSheetController(500, 20, 180, 60, true, false);

	public BloodStainedJournalResearchGUI()
	{
		// Background will always be 256x256
		int backgroundHeight = 256;
		int backgroundWidth = 256;
		// Calculate the various positions of GUI elements on the screen
		int xPosScroll = (640 - backgroundWidth) / 2;
		int yPosScroll = (360 - backgroundHeight) / 2;
		int xPosBackground = xPosScroll + 20;
		int yPosBackground = yPosScroll + 20;

		this.guiOffsetX = ClientData.currentBloodStainedJournalX;
		this.guiOffsetY = ClientData.currentBloodStainedJournalY;

		this.researchTreeBase = new AOTDGuiPanel(xPosScroll, yPosScroll, backgroundWidth, backgroundHeight, true);
		this.researchTree = new AOTDGuiPanel(-this.guiOffsetX, -this.guiOffsetY, backgroundWidth, backgroundHeight, false);
		this.getContentPane().add(researchTreeBase);

		this.scrollBackground = new AOTDGuiImage(0, 0, backgroundWidth, backgroundHeight, 1024, 1024, "afraidofthedark:textures/gui/bloodStainedJournalResearchBackdrop.png");
		this.backgroundBorder = new AOTDGuiImage(0, 0, backgroundWidth, backgroundHeight, "afraidofthedark:textures/gui/bloodStainedJournalResearchBackground.png");
		this.scrollBackground.setU(this.guiOffsetX + 384);
		this.scrollBackground.setV(this.guiOffsetY + 768);
		this.researchTreeBase.add(scrollBackground);
		this.researchTreeBase.add(researchTree);
		this.researchTreeBase.add(backgroundBorder);

		AOTDMouseListener nodeListener = new AOTDMouseListener()
		{
			@Override
			public void mouseReleased(AOTDMouseEvent event)
			{
			}

			@Override
			public void mousePressed(AOTDMouseEvent event)
			{
			}

			@Override
			public void mouseExited(AOTDMouseEvent event)
			{
			}

			@Override
			public void mouseEntered(AOTDMouseEvent event)
			{
				if (event.getSource().isVisible())
					entityPlayer.playSound("afraidofthedark:buttonHover", 0.7f, 1.9f);
			}

			@Override
			public void mouseClicked(AOTDMouseEvent event)
			{
				if (event.getSource() instanceof AOTDGuiResearchNodeButton)
				{
					AOTDGuiResearchNodeButton current = (AOTDGuiResearchNodeButton) event.getSource();
					if (current.getParent().getParent().intersects(current))
					{
						if (current.isHovered())
						{
							ClientData.currentlySelected = current.getResearch();
							if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(current.getResearch()))
								entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.BLOOD_STAINED_JOURNAL_PAGE_ID, entityPlayer.worldObj, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
							else if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(current.getResearch().getPrevious()))
								entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.BLOOD_STAINED_JOURNAL_PAGE_PRE_ID, entityPlayer.worldObj, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
						}
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
			researchNode.addMouseListener(nodeListener);
			if (researchNode.getResearch().getPrevious() != null)
			{
				if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(researchNode.getResearch()) || entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).canResearch(researchNode.getResearch()))
				{
					ResearchTypes previous = researchNode.getResearch().getPrevious();
					ResearchTypes current = researchNode.getResearch();
					if (current.getPositionX() < previous.getPositionX())
						this.researchTree.add(new AOTDGuiSpriteSheetImage(xPos + 26, yPos + 9, 54, 14, new ResourceLocation("afraidofthedark:textures/gui/researchHorizontal.png"), this.nodeConnectorControllerHorizontal));
					else if (current.getPositionX() > previous.getPositionX())
						this.researchTree.add(new AOTDGuiSpriteSheetImage(xPos - 50, yPos + 9, 54, 14, new ResourceLocation("afraidofthedark:textures/gui/researchHorizontal.png"), this.nodeConnectorControllerHorizontal));
					else if (current.getPositionY() > previous.getPositionY())
						this.researchTree.add(new AOTDGuiSpriteSheetImage(xPos + 9, yPos + 30, 14, 46, new ResourceLocation("afraidofthedark:textures/gui/researchVertical.png"), this.nodeConnectorControllerVertical));
					else if (current.getPositionY() < previous.getPositionY())
						this.researchTree.add(new AOTDGuiSpriteSheetImage(xPos + 9, yPos - 46, 14, 46, new ResourceLocation("afraidofthedark:textures/gui/researchVertical.png"), this.nodeConnectorControllerVertical));
				}
			}
			buttons[researchType.ordinal()] = researchNode;
		}

		for (AOTDGuiResearchNodeButton nodeButton : buttons)
			this.researchTree.add(nodeButton);

		this.addSpriteSheetController(this.nodeConnectorControllerHorizontal);
		this.addSpriteSheetController(this.nodeConnectorControllerVertical);
	}

	// When the mouse is dragged, update the GUI accordingly
	@Override
	protected void mouseClickMove(final int mouseX, final int mouseY, final int lastButtonClicked, final long timeBetweenClicks)
	{
		super.mouseClickMove(mouseX, mouseY, lastButtonClicked, timeBetweenClicks);

		this.researchTree.setX(-this.guiOffsetX + researchTree.getParent().getX());
		this.researchTree.setY(-this.guiOffsetY + researchTree.getParent().getY());

		scrollBackground.setU(this.guiOffsetX + 384);
		scrollBackground.setV(this.guiOffsetY + 768);
	}

	@Override
	protected void checkOutOfBounds()
	{
		if (this.guiOffsetX > 400)
		{
			this.guiOffsetX = 400;
		}
		if (this.guiOffsetX < -400)
		{
			this.guiOffsetX = -400;
		}
		if (this.guiOffsetY > 0)
		{
			this.guiOffsetY = 0;
		}
		if (this.guiOffsetY < -760)
		{
			this.guiOffsetY = -760;
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
