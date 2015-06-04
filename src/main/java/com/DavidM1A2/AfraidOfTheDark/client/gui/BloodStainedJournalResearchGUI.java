/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.playerData.LoadResearchData;
import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;
import com.DavidM1A2.AfraidOfTheDark.refrence.ResearchTypes;

public class BloodStainedJournalResearchGUI extends GuiClickAndDragable
{
	private static final int RESEARCH_BASE_ID = 1;
	private static int currentID;
	private static final int DISTANCE_BETWEEN_NODES = 75;

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

	// NodeButton is a research
	private NodeButton unbreakableCovenantResearch;
	private NodeButton werewolfExamination;
	private NodeButton crossbow;
	private NodeButton astronomy1;
	private NodeButton vitae1;
	private NodeButton astralSilver;
	private NodeButton darkForest;
	private NodeButton astronomy2;
	private NodeButton igneousArmor;
	private NodeButton igneousGem;
	private NodeButton starMetal;
	private NodeButton vitaeLantern1;
	private NodeButton astralSilverSword;
	private NodeButton cloakOfAgility;

	@Override
	public void initGui()
	{
		// Calculate the various positions of GUI elements on the screen
		BloodStainedJournalResearchGUI.currentID = this.RESEARCH_BASE_ID;
		BloodStainedJournalResearchGUI.baseHeight = (this.height - 256) / 2;
		BloodStainedJournalResearchGUI.baseWidth = (this.width - 256) / 2;
		BloodStainedJournalResearchGUI.xPosScroll = BloodStainedJournalResearchGUI.baseWidth;
		BloodStainedJournalResearchGUI.yPosScroll = BloodStainedJournalResearchGUI.baseHeight;
		BloodStainedJournalResearchGUI.xPosBackground = BloodStainedJournalResearchGUI.baseWidth + 20;
		BloodStainedJournalResearchGUI.yPosBackground = BloodStainedJournalResearchGUI.baseHeight + 20;
		BloodStainedJournalResearchGUI.xPosBaseResearch = (BloodStainedJournalResearchGUI.xPosBackground + 100) - 8;
		BloodStainedJournalResearchGUI.yPosBaseResearch = (BloodStainedJournalResearchGUI.yPosBackground + (BloodStainedJournalResearchGUI.BACKGROUND_WIDTH / 2)) - 8;

		this.setupButtons();
	}

	// To draw the screen we first draw the default GUI background, then the
	// background images. Then we add buttons and a frame.
	@Override
	public void drawScreen(final int i, final int j, final float f)
	{
		this.drawDefaultBackground();
		this.mc.renderEngine.bindTexture(new ResourceLocation("afraidofthedark:textures/gui/BloodStainedJournalResearchBackdrop.png"));
		Gui.drawScaledCustomSizeModalRect(BloodStainedJournalResearchGUI.xPosScroll, BloodStainedJournalResearchGUI.yPosScroll, (this.guiOffsetX * 2) + 384, (this.guiOffsetY * 2) + 768, BloodStainedJournalResearchGUI.BACKGROUND_WIDTH, BloodStainedJournalResearchGUI.BACKGROUND_HEIGHT,
				BloodStainedJournalResearchGUI.BACKGROUND_WIDTH, BloodStainedJournalResearchGUI.BACKGROUND_HEIGHT, 1024, 1024);

		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		final int disWidth = Minecraft.getMinecraft().displayWidth;
		final int disHeight = Minecraft.getMinecraft().displayHeight;
		final int widthScale = Math.round(disWidth / (float) this.width);
		final int heightScale = Math.round(disHeight / (float) this.height);
		GL11.glScissor(disWidth - ((BloodStainedJournalResearchGUI.xPosScroll + BloodStainedJournalResearchGUI.BACKGROUND_WIDTH) * widthScale), disHeight - ((BloodStainedJournalResearchGUI.yPosScroll + BloodStainedJournalResearchGUI.BACKGROUND_HEIGHT) * widthScale),
				BloodStainedJournalResearchGUI.BACKGROUND_WIDTH * heightScale, BloodStainedJournalResearchGUI.BACKGROUND_HEIGHT * heightScale);
		super.drawScreen(i, j, f);
		this.drawLines();
		GL11.glDisable(GL11.GL_SCISSOR_TEST);

		this.mc.renderEngine.bindTexture(new ResourceLocation("afraidofthedark:textures/gui/BloodStainedJournalResearchBackground.png"));
		this.drawTexturedModalRect(BloodStainedJournalResearchGUI.xPosScroll, BloodStainedJournalResearchGUI.yPosScroll, 0, 0, BloodStainedJournalResearchGUI.BACKGROUND_WIDTH, BloodStainedJournalResearchGUI.BACKGROUND_HEIGHT);

		for (Object nodeButton : this.buttonList)
		{
			if (nodeButton instanceof NodeButton)
			{
				NodeButton newNodeButton = (NodeButton) nodeButton;

				if (newNodeButton.isMouseOver())// && LoadResearchData.isResearched(Minecraft.getMinecraft().thePlayer, newNodeButton.getMyType()))
				{
					this.drawString(Minecraft.getMinecraft().fontRendererObj, newNodeButton.getMyType().formattedString(), newNodeButton.xPosition + newNodeButton.height, newNodeButton.yPosition, 0xFF3399);
				}
			}
		}
	}

	// When the mouse is dragged, update the GUI accordingly
	@Override
	protected void mouseClickMove(final int mouseX, final int mouseY, final int lastButtonClicked, final long timeBetweenClicks)
	{
		super.mouseClickMove(mouseX, mouseY, lastButtonClicked, timeBetweenClicks);
		for (final Object o : this.buttonList)
		{
			((NodeButton) o).setPosition(this.guiOffsetX, this.guiOffsetY);
		}
	}

	// When a button is pressed, open the respective research
	@Override
	protected void actionPerformed(final GuiButton button)
	{
		final EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;
		for (final Object o : this.buttonList)
		{
			final NodeButton current = (NodeButton) o;
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

	// Draw an arrow for the gui
	private void drawLines()
	{
		this.mc.renderEngine.bindTexture(BloodStainedJournalResearchGUI.upArrow);
		Gui.drawScaledCustomSizeModalRect(this.unbreakableCovenantResearch.xPosition + 10, this.unbreakableCovenantResearch.yPosition - 43, 0, 0, 10, 43, 10, 43, 10, 43);
		Gui.drawScaledCustomSizeModalRect(this.werewolfExamination.xPosition + 10, this.werewolfExamination.yPosition - 43, 0, 0, 10, 43, 10, 43, 10, 43);
		Gui.drawScaledCustomSizeModalRect(this.astronomy1.xPosition + 10, this.astronomy1.yPosition - 43, 0, 0, 10, 43, 10, 43, 10, 43);
		Gui.drawScaledCustomSizeModalRect(this.astralSilver.xPosition + 10, this.astralSilver.yPosition - 43, 0, 0, 10, 43, 10, 43, 10, 43);
		Gui.drawScaledCustomSizeModalRect(this.darkForest.xPosition + 10, this.darkForest.yPosition - 43, 0, 0, 10, 43, 10, 43, 10, 43);

		this.mc.renderEngine.bindTexture(BloodStainedJournalResearchGUI.rightArrow);
		Gui.drawScaledCustomSizeModalRect(this.unbreakableCovenantResearch.xPosition + 30, this.unbreakableCovenantResearch.yPosition + 10, 0, 0, 43, 10, 43, 10, 43, 10);
		Gui.drawScaledCustomSizeModalRect(this.astralSilver.xPosition + 30, this.astralSilver.yPosition + 10, 0, 0, 43, 10, 43, 10, 43, 10);
		Gui.drawScaledCustomSizeModalRect(this.astronomy2.xPosition + 30, this.astronomy2.yPosition + 10, 0, 0, 43, 10, 43, 10, 43, 10);

		this.mc.renderEngine.bindTexture(BloodStainedJournalResearchGUI.leftArrow);
		Gui.drawScaledCustomSizeModalRect(this.astronomy1.xPosition - 43, this.astronomy1.yPosition + 10, 0, 0, 43, 10, 43, 10, 43, 10);
		Gui.drawScaledCustomSizeModalRect(this.igneousGem.xPosition - 43, this.igneousGem.yPosition + 10, 0, 0, 43, 10, 43, 10, 43, 10);
		Gui.drawScaledCustomSizeModalRect(this.vitae1.xPosition - 43, this.vitae1.yPosition + 10, 0, 0, 43, 10, 43, 10, 43, 10);
		Gui.drawScaledCustomSizeModalRect(this.astronomy2.xPosition - 43, this.astronomy2.yPosition + 10, 0, 0, 43, 10, 43, 10, 43, 10);
		Gui.drawScaledCustomSizeModalRect(this.werewolfExamination.xPosition - 43, this.werewolfExamination.yPosition + 10, 0, 0, 43, 10, 43, 10, 43, 10);
	}

	private void setupButtons()
	{
		// Setup the research nodes with an ID and position.
		this.unbreakableCovenantResearch = new NodeButton(currentID++, xPosBaseResearch, yPosBaseResearch, 0, 0, ResearchTypes.AnUnbreakableCovenant);
		this.werewolfExamination = new NodeButton(currentID++, xPosBaseResearch, yPosBaseResearch - DISTANCE_BETWEEN_NODES, 32, 0, ResearchTypes.WerewolfExamination);
		this.crossbow = new NodeButton(currentID++, xPosBaseResearch + DISTANCE_BETWEEN_NODES, yPosBaseResearch, 64, 0, ResearchTypes.Crossbow);
		this.astronomy1 = new NodeButton(currentID++, xPosBaseResearch, yPosBaseResearch - (DISTANCE_BETWEEN_NODES * 2), 96, 0, ResearchTypes.AstronomyI);
		this.vitae1 = new NodeButton(currentID++, xPosBaseResearch - DISTANCE_BETWEEN_NODES, yPosBaseResearch - (DISTANCE_BETWEEN_NODES * 2), 128, 0, ResearchTypes.VitaeI);
		this.astralSilver = new NodeButton(currentID++, xPosBaseResearch, yPosBaseResearch - (DISTANCE_BETWEEN_NODES * 3), 160, 0, ResearchTypes.AstralSilver);
		this.astralSilverSword = new NodeButton(currentID++, xPosBaseResearch + DISTANCE_BETWEEN_NODES, yPosBaseResearch - (DISTANCE_BETWEEN_NODES * 3), 160, 0, ResearchTypes.AstralSilverSword);
		this.darkForest = new NodeButton(currentID++, xPosBaseResearch, yPosBaseResearch - (DISTANCE_BETWEEN_NODES * 4), 224, 0, ResearchTypes.DarkForest);
		this.astronomy2 = new NodeButton(currentID++, xPosBaseResearch, yPosBaseResearch - (DISTANCE_BETWEEN_NODES * 5), 0, 32, ResearchTypes.AstronomyII);
		this.igneousArmor = new NodeButton(currentID++, xPosBaseResearch - (DISTANCE_BETWEEN_NODES * 2), yPosBaseResearch - (DISTANCE_BETWEEN_NODES * 5), 32, 32, ResearchTypes.IgneousArmor);
		this.starMetal = new NodeButton(currentID++, xPosBaseResearch + DISTANCE_BETWEEN_NODES, yPosBaseResearch - (DISTANCE_BETWEEN_NODES * 5), 64, 32, ResearchTypes.StarMetal);
		this.vitaeLantern1 = new NodeButton(currentID++, xPosBaseResearch - (DISTANCE_BETWEEN_NODES * 2), yPosBaseResearch - (DISTANCE_BETWEEN_NODES * 2), 96, 32, ResearchTypes.VitaeLanternI);
		this.igneousGem = new NodeButton(currentID++, xPosBaseResearch - DISTANCE_BETWEEN_NODES, yPosBaseResearch - (DISTANCE_BETWEEN_NODES * 5), 32, 32, ResearchTypes.IgneousGem);
		this.cloakOfAgility = new NodeButton(currentID++, xPosBaseResearch - DISTANCE_BETWEEN_NODES, yPosBaseResearch - DISTANCE_BETWEEN_NODES, 32, 32, ResearchTypes.CloakOfAgility);

		// Clear and pre-existing buttons on the GUI and add the new ones
		this.buttonList.clear();
		this.buttonList.add(this.unbreakableCovenantResearch);
		this.buttonList.add(this.werewolfExamination);
		this.buttonList.add(this.crossbow);
		this.buttonList.add(this.astronomy1);
		this.buttonList.add(this.vitae1);
		this.buttonList.add(this.astralSilver);
		this.buttonList.add(this.astralSilverSword);
		this.buttonList.add(this.darkForest);
		this.buttonList.add(this.astronomy2);
		this.buttonList.add(this.igneousArmor);
		this.buttonList.add(this.starMetal);
		this.buttonList.add(this.vitaeLantern1);
		this.buttonList.add(this.igneousGem);
		this.buttonList.add(this.cloakOfAgility);
	}

	@Override
	protected void checkOutOfBounds()
	{
		if (this.guiOffsetX > BloodStainedJournalResearchGUI.MAX_WIDTH)
		{
			this.guiOffsetX = BloodStainedJournalResearchGUI.MAX_WIDTH;
		}
		if (this.guiOffsetX < BloodStainedJournalResearchGUI.MAX_NEGATIVE_WIDTH)
		{
			this.guiOffsetX = BloodStainedJournalResearchGUI.MAX_NEGATIVE_WIDTH;
		}
		if (this.guiOffsetY > BloodStainedJournalResearchGUI.MAX_HEIGHT)
		{
			this.guiOffsetY = BloodStainedJournalResearchGUI.MAX_HEIGHT;
		}
		if (this.guiOffsetY < BloodStainedJournalResearchGUI.MAX_NEGATIVE_HEIGHT)
		{
			this.guiOffsetY = BloodStainedJournalResearchGUI.MAX_NEGATIVE_HEIGHT;
		}
	}
}
