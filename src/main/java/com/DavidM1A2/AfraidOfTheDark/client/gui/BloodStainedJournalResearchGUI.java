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

public class BloodStainedJournalResearchGUI extends GuiScreen
{
	// IDs of different researches
	private static final int BACKGROUND_IMAGE_ID = 0;
	private static final int RESEARCH_AN_UNBREAKABLE_COVENANT_ID = 1;
	private static final int RESEARCH_WEREWOLF_EXAMINATION = 2;

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

	// Background will always be 256x256
	private static final int BACKGROUND_HEIGHT = 256;
	private static final int BACKGROUND_WIDTH = 256;

	// Arrow from 1 research to another always is 12x64
	private static final int ARROW_WIDTH = 12;
	private static final int ARROW_HEIGHT = 64;

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
		unbreakableCovenantResearch = new NodeButton(RESEARCH_AN_UNBREAKABLE_COVENANT_ID, xPosBaseResearch, yPosBaseResearch, 0, 0);

		werewolfExamination = new NodeButton(RESEARCH_WEREWOLF_EXAMINATION, xPosBaseResearch, yPosBaseResearch - 90, 32, 0);

		// Clear and pre-existing buttons on the GUI and add the new ones
		this.buttonList.clear();
		this.buttonList.add(unbreakableCovenantResearch);
		this.buttonList.add(werewolfExamination);
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
		if (button.id == this.RESEARCH_AN_UNBREAKABLE_COVENANT_ID)
		{
			Minecraft.getMinecraft().thePlayer.openGui(AfraidOfTheDark.instance, GuiHandler.BLOOD_STAINED_JOURNAL_PAGE_ID, entityPlayer.worldObj, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
		}
		else if (button.id == this.RESEARCH_WEREWOLF_EXAMINATION)
		{

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

		if (guiOffsetX > 100)
		{
			guiOffsetX = 100;
		}
		if (guiOffsetX < -100)
		{
			guiOffsetX = -100;
		}
		if (guiOffsetY > 100)
		{
			guiOffsetY = 100;
		}
		if (guiOffsetY < -100)
		{
			guiOffsetY = -100;
		}

		// Set the position of the GUI node button
		((NodeButton) this.buttonList.get(0)).setPosition(guiOffsetX, guiOffsetY);
		((NodeButton) this.buttonList.get(1)).setPosition(guiOffsetX, guiOffsetY);
	}

	// Draw an arrow for the gui
	private void drawLines(int offsetX, int offsetY)
	{
		mc.renderEngine.bindTexture(new ResourceLocation("afraidofthedark:textures/gui/Arrow.png"));
		this.drawTexturedModalRect(xPosBaseResearch - offsetX + 10, yPosBaseResearch - offsetY - 50, 0, 0, ARROW_WIDTH, ARROW_HEIGHT);
	}
}
