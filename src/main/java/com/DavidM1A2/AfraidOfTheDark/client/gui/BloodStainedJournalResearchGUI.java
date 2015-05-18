/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.playerData.LoadResearchData;
import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;
import com.DavidM1A2.AfraidOfTheDark.research.Research;
import com.DavidM1A2.AfraidOfTheDark.research.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.utility.LogHelper;

public class BloodStainedJournalResearchGUI extends GuiScreen
{
	// IDs of different researches
	private static final int BACKGROUND_IMAGE_ID = 0;
	private static final int RESEARCH_AN_UNBREAKABLE_COVENANT_ID = 1;
	private static final int RESEARCH_WEREWOLF_EXAMINATION_ID = 2;
	private static final int RESEARCH_CROSSBOW_ID = 3;

	private static final ResourceLocation upArrow = new ResourceLocation("afraidofthedark:textures/gui/arrowUp.png");
	private static final ResourceLocation leftArrow = new ResourceLocation("afraidofthedark:textures/gui/arrowLeft.png");
	private static final ResourceLocation rightArrow = new ResourceLocation("afraidofthedark:textures/gui/arrowRight.png");

	// GUI height and width
	private static int baseWidth = 512;
	private static int baseHeight = 512;

	// Current GUI x/y Scrool positions, background positions, and research
	// positions
	private static int xPosScroll;
	private static int yPosScroll;

	private static int xPosBackground;
	private static int yPosBackground;

	private static int xPosBaseResearch;
	private static int yPosBaseResearch;

	private static final int MAX_HEIGHT = 200;
	private static final int MAX_WIDTH = 200;

	// Background will always be 256x256
	private static final int BACKGROUND_HEIGHT = 256;
	private static final int BACKGROUND_WIDTH = 256;

	// Variables for calculating the GUI offset
	private int guiOffsetX = 0;
	private int guiOffsetY = 0;
	private int originalXPosition = 0;
	private int originalYPosition = 0;

	// ReserachBackground is essentially a button that acts as a background
	private ResearchBackground starryBackground;
	// NodeButton is a research
	private NodeButton unbreakableCovenantResearch;
	private NodeButton werewolfExamination;
	private NodeButton crossbow;

	@Override
	public void initGui()
	{
		// Calculate the various positions of GUI elements on the screen
		baseHeight = (this.height - 256) / 2;
		baseWidth = (this.width - 256) / 2;
		xPosScroll = baseWidth;
		yPosScroll = baseHeight;
		xPosBackground = baseWidth + 20;
		yPosBackground = baseHeight + 20;
		xPosBaseResearch = xPosBackground + 100 - 8;
		yPosBaseResearch = yPosBackground + BACKGROUND_WIDTH / 2 - 8;

		// Setup the reserach nodes with an ID and position.
		unbreakableCovenantResearch = new NodeButton(RESEARCH_AN_UNBREAKABLE_COVENANT_ID, xPosBaseResearch, yPosBaseResearch, 0, 0, ResearchTypes.AnUnbreakableCovenant);
		werewolfExamination = new NodeButton(RESEARCH_WEREWOLF_EXAMINATION_ID, xPosBaseResearch, yPosBaseResearch - 90, 32, 0, ResearchTypes.WerewolfExamination);
		crossbow = new NodeButton(RESEARCH_CROSSBOW_ID, xPosBaseResearch + 90, yPosBaseResearch, 64, 0, ResearchTypes.Crossbow);

		// Clear and pre-existing buttons on the GUI and add the new ones
		this.buttonList.clear();
		this.buttonList.add(unbreakableCovenantResearch);
		this.buttonList.add(werewolfExamination);
		this.buttonList.add(crossbow);
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
		mc.renderEngine.bindTexture(new ResourceLocation("afraidofthedark:textures/gui/BloodStainedJournalResearchBackdrop.png"));
		this.drawTexturedModalRect(xPosScroll, yPosScroll, 0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
		super.drawScreen(i, j, f);
		drawLines(guiOffsetX, guiOffsetY);
		mc.renderEngine.bindTexture(new ResourceLocation("afraidofthedark:textures/gui/BloodStainedJournalResearchBackground.png"));
		this.drawTexturedModalRect(xPosScroll, yPosScroll, 0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
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

	// When a button is pressed, open the respective research
	@Override
	protected void actionPerformed(GuiButton button)
	{
		EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;
		Research myResearch = LoadResearchData.getResearch(entityPlayer);
		switch (button.id)
		{
			case BloodStainedJournalResearchGUI.RESEARCH_AN_UNBREAKABLE_COVENANT_ID:
			{
				if (myResearch.getResearch(ResearchTypes.AnUnbreakableCovenant).isResearched())
				{
					Refrence.currentlySelected = ResearchTypes.AnUnbreakableCovenant;
					LogHelper.info(Refrence.currentlySelected);
					entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.BLOOD_STAINED_JOURNAL_PAGE_ID, entityPlayer.worldObj, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
					break;
				}
			}
			case BloodStainedJournalResearchGUI.RESEARCH_WEREWOLF_EXAMINATION_ID:
			{
				if (myResearch.getResearch(ResearchTypes.WerewolfExamination).isResearched())
				{
					Refrence.currentlySelected = ResearchTypes.WerewolfExamination;
					LogHelper.info(Refrence.currentlySelected);
					entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.BLOOD_STAINED_JOURNAL_PAGE_ID, entityPlayer.worldObj, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
					break;
				}
				else if (myResearch.isPreviousResearched(ResearchTypes.WerewolfExamination))
				{
					Refrence.currentlySelected = ResearchTypes.PreWerewolfExamination;
					LogHelper.info(Refrence.currentlySelected);
					entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.BLOOD_STAINED_JOURNAL_PAGE_ID, entityPlayer.worldObj, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
					break;
				}
			}
			case BloodStainedJournalResearchGUI.RESEARCH_CROSSBOW_ID:
			{
				if (myResearch.getResearch(ResearchTypes.Crossbow).isResearched())
				{
					Refrence.currentlySelected = ResearchTypes.Crossbow;
					LogHelper.info(Refrence.currentlySelected);
					entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.BLOOD_STAINED_JOURNAL_PAGE_ID, entityPlayer.worldObj, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
					break;
				}
				break;
			}
		}
	}

	// When you click the mouse move the background
	@Override
	protected void mouseClicked(int xPos, int yPos, int shouldBe0) throws IOException
	{
		originalXPosition = xPos + guiOffsetX;
		originalYPosition = yPos + guiOffsetY;
		super.mouseClicked(xPos, yPos, shouldBe0);
	}

	// When the mouse is dragged, update the GUI accordingly
	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int lastButtonClicked, long timeBetweenClicks)
	{
		guiOffsetX = originalXPosition - mouseX;
		guiOffsetY = originalYPosition - mouseY;

		if (guiOffsetX > MAX_WIDTH)
		{
			guiOffsetX = MAX_WIDTH;
		}
		if (guiOffsetX < -MAX_WIDTH)
		{
			guiOffsetX = -MAX_WIDTH;
		}
		if (guiOffsetY > MAX_HEIGHT)
		{
			guiOffsetY = MAX_HEIGHT;
		}
		if (guiOffsetY < -MAX_HEIGHT)
		{
			guiOffsetY = -MAX_HEIGHT;
		}

		for (Object o : this.buttonList)
		{
			((NodeButton) o).setPosition(guiOffsetX, guiOffsetY);
		}
	}

	// Draw an arrow for the gui
	private void drawLines(int offsetX, int offsetY)
	{
		mc.renderEngine.bindTexture(this.upArrow);
		BloodStainedJournalPageGUI.drawScaledCustomSizeModalRect(xPosBaseResearch - offsetX + 10, yPosBaseResearch - offsetY - 50, 0, 0, 10, 43, 10, 43, 10, 43);
		mc.renderEngine.bindTexture(this.rightArrow);
		BloodStainedJournalPageGUI.drawScaledCustomSizeModalRect(xPosBaseResearch - offsetX + 40, yPosBaseResearch - offsetY + 10, 0, 0, 43, 10, 43, 10, 43, 10);

	}
}
