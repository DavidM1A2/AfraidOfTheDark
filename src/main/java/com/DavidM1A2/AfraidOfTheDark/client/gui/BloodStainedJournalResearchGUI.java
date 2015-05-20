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
import com.DavidM1A2.AfraidOfTheDark.research.ResearchTypes;

public class BloodStainedJournalResearchGUI extends GuiScreen
{
	// IDs of different researches
	private static final int BACKGROUND_IMAGE_ID = 0;
	private static final int RESEARCH_AN_UNBREAKABLE_COVENANT_ID = 1;
	private static final int RESEARCH_WEREWOLF_EXAMINATION_ID = 2;
	private static final int RESEARCH_CROSSBOW_ID = 3;
	private static final int RESEARCH_ASTRONOMY_1_ID = 4;

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

	private static final int MAX_HEIGHT = 0;
	private static final int MAX_WIDTH = 200;
	private static final int MAX_NEGATIVE_HEIGHT = -380;
	private static final int MAX_NEGATIVE_WIDTH = -200;

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
	private NodeButton astronomy1;

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
		werewolfExamination = new NodeButton(RESEARCH_WEREWOLF_EXAMINATION_ID, xPosBaseResearch, yPosBaseResearch - 75, 32, 0, ResearchTypes.WerewolfExamination);
		crossbow = new NodeButton(RESEARCH_CROSSBOW_ID, xPosBaseResearch + 75, yPosBaseResearch, 64, 0, ResearchTypes.Crossbow);
		astronomy1 = new NodeButton(RESEARCH_ASTRONOMY_1_ID, xPosBaseResearch, yPosBaseResearch - 150, 96, 0, ResearchTypes.Astronomy1);

		// Clear and pre-existing buttons on the GUI and add the new ones
		this.buttonList.clear();
		this.buttonList.add(unbreakableCovenantResearch);
		this.buttonList.add(werewolfExamination);
		this.buttonList.add(crossbow);
		this.buttonList.add(astronomy1);
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
		mc.renderEngine.bindTexture(new ResourceLocation("afraidofthedark:textures/gui/BloodStainedJournalResearchBackdrop.png"));
		this.drawScaledCustomSizeModalRect(xPosScroll, yPosScroll, guiOffsetX * 2 + 384, guiOffsetY * 2 + 768, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, 1024, 1024);

		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		int disWidth = Minecraft.getMinecraft().displayWidth;
		int disHeight = Minecraft.getMinecraft().displayHeight;
		int widthScale = Math.round(disWidth / (float) this.width);
		int heightScale = Math.round(disHeight / (float) this.height);
		GL11.glScissor(disWidth - (xPosScroll + BACKGROUND_WIDTH) * widthScale, disHeight - (yPosScroll + BACKGROUND_HEIGHT) * heightScale, BACKGROUND_WIDTH * 3, BACKGROUND_HEIGHT * 3);
		super.drawScreen(i, j, f);
		drawLines();
		GL11.glDisable(GL11.GL_SCISSOR_TEST);

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
		for (Object o : this.buttonList)
		{
			NodeButton current = (NodeButton) o;
			if (current.id == button.id)
			{
				if (LoadResearchData.isResearched(entityPlayer, current.getMyType()))
				{
					Refrence.currentlySelected = current.getMyType();
					entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.BLOOD_STAINED_JOURNAL_PAGE_ID, entityPlayer.worldObj, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
					break;
				}
				else if (LoadResearchData.isResearched(entityPlayer, current.getMyType().getPrevious()))
				{
					Refrence.currentlySelected = ResearchTypes.valueOf("Pre" + current.getMyType());
					entityPlayer.openGui(AfraidOfTheDark.instance, GuiHandler.BLOOD_STAINED_JOURNAL_PAGE_ID, entityPlayer.worldObj, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
					break;
				}
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
		if (guiOffsetX < MAX_NEGATIVE_WIDTH)
		{
			guiOffsetX = MAX_NEGATIVE_WIDTH;
		}
		if (guiOffsetY > MAX_HEIGHT)
		{
			guiOffsetY = MAX_HEIGHT;
		}
		if (guiOffsetY < MAX_NEGATIVE_HEIGHT)
		{
			guiOffsetY = MAX_NEGATIVE_HEIGHT;
		}

		for (Object o : this.buttonList)
		{
			((NodeButton) o).setPosition(guiOffsetX, guiOffsetY);
		}
	}

	// Draw an arrow for the gui
	private void drawLines()
	{
		mc.renderEngine.bindTexture(this.upArrow);
		BloodStainedJournalPageGUI.drawScaledCustomSizeModalRect(unbreakableCovenantResearch.xPosition + 10, unbreakableCovenantResearch.yPosition - 43, 0, 0, 10, 43, 10, 43, 10, 43);
		BloodStainedJournalPageGUI.drawScaledCustomSizeModalRect(this.werewolfExamination.xPosition + 10, this.werewolfExamination.yPosition - 43, 0, 0, 10, 43, 10, 43, 10, 43);
		mc.renderEngine.bindTexture(this.rightArrow);
		BloodStainedJournalPageGUI.drawScaledCustomSizeModalRect(unbreakableCovenantResearch.xPosition + 30, unbreakableCovenantResearch.yPosition + 10, 0, 0, 43, 10, 43, 10, 43, 10);
	}
}
