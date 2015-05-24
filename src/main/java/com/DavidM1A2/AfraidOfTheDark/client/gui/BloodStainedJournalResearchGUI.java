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
	// IDs of different researches
	private static final int BACKGROUND_IMAGE_ID = 0;
	private static final int RESEARCH_BASE_ID = 1;
	private static final int RESEARCH_AN_UNBREAKABLE_COVENANT_ID = 1;
	private static final int RESEARCH_WEREWOLF_EXAMINATION_ID = 2;
	private static final int RESEARCH_CROSSBOW_ID = 3;
	private static final int RESEARCH_ASTRONOMY_1_ID = 4;
	private static final int RESEARCH_VITAE_1_ID = 5;
	private static final int RESEARCH_ASTRAL_SILVER_ID = 6;
	private static final int RESEARCH_SILVER_INFUSION_ID = 7;
	private static final int RESEARCH_DARK_FOREST_ID = 8;
	private static final int RESEARCH_ASTRONOMY_2_ID = 9;
	private static final int RESEARCH_IGNEOUS_ARMOR_ID = 10;
	private static final int RESEARCH_STAR_METAL_ID = 11;
	private static final int RESEARCH_SANITY_LANTERN_ID = 12;
	private static final int RESEARCH_VITAE_LANTERN_ID = 13;

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

	// ReserachBackground is essentially a button that acts as a background
	private ResearchBackground starryBackground;
	// NodeButton is a research
	private NodeButton unbreakableCovenantResearch;
	private NodeButton werewolfExamination;
	private NodeButton crossbow;
	private NodeButton astronomy1;
	private NodeButton vitae1;
	private NodeButton astralSilver;
	private NodeButton silverInfusion;
	private NodeButton darkForest;
	private NodeButton astronomy2;
	private NodeButton igneousArmor;
	private NodeButton starMetal;
	private NodeButton sanityLantern;
	private NodeButton vitaeLantern1;

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

		this.setupButtons();
	}

	// To draw the screen we first draw the default GUI background, then the
	// background images. Then we add buttons and a frame.
	@Override
	public void drawScreen(int i, int j, float f)
	{
		drawDefaultBackground();
		mc.renderEngine.bindTexture(new ResourceLocation("afraidofthedark:textures/gui/BloodStainedJournalResearchBackdrop.png"));
		Gui.drawScaledCustomSizeModalRect(xPosScroll, yPosScroll, guiOffsetX * 2 + 384, guiOffsetY * 2 + 768, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, BACKGROUND_WIDTH, BACKGROUND_HEIGHT, 1024, 1024);

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

	// When the mouse is dragged, update the GUI accordingly
	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int lastButtonClicked, long timeBetweenClicks)
	{
		super.mouseClickMove(mouseX, mouseY, lastButtonClicked, timeBetweenClicks);
		for (Object o : this.buttonList)
		{
			((NodeButton) o).setPosition(guiOffsetX, guiOffsetY);
		}
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

	// Draw an arrow for the gui
	private void drawLines()
	{
		mc.renderEngine.bindTexture(BloodStainedJournalResearchGUI.upArrow);
		Gui.drawScaledCustomSizeModalRect(this.unbreakableCovenantResearch.xPosition + 10, this.unbreakableCovenantResearch.yPosition - 43, 0, 0, 10, 43, 10, 43, 10, 43);
		Gui.drawScaledCustomSizeModalRect(this.werewolfExamination.xPosition + 10, this.werewolfExamination.yPosition - 43, 0, 0, 10, 43, 10, 43, 10, 43);
		Gui.drawScaledCustomSizeModalRect(this.astronomy1.xPosition + 10, this.astronomy1.yPosition - 43, 0, 0, 10, 43, 10, 43, 10, 43);
		Gui.drawScaledCustomSizeModalRect(this.astralSilver.xPosition + 10, this.astralSilver.yPosition - 43, 0, 0, 10, 43, 10, 43, 10, 43);
		Gui.drawScaledCustomSizeModalRect(this.darkForest.xPosition + 10, this.darkForest.yPosition - 43, 0, 0, 10, 43, 10, 43, 10, 43);
		Gui.drawScaledCustomSizeModalRect(this.sanityLantern.xPosition + 10, this.sanityLantern.yPosition - 43, 0, 0, 10, 43, 10, 43, 10, 43);

		mc.renderEngine.bindTexture(BloodStainedJournalResearchGUI.rightArrow);
		Gui.drawScaledCustomSizeModalRect(this.unbreakableCovenantResearch.xPosition + 30, this.unbreakableCovenantResearch.yPosition + 10, 0, 0, 43, 10, 43, 10, 43, 10);
		Gui.drawScaledCustomSizeModalRect(this.astralSilver.xPosition + 30, this.astralSilver.yPosition + 10, 0, 0, 43, 10, 43, 10, 43, 10);
		Gui.drawScaledCustomSizeModalRect(this.astronomy2.xPosition + 30, this.astronomy2.yPosition + 10, 0, 0, 43, 10, 43, 10, 43, 10);

		mc.renderEngine.bindTexture(BloodStainedJournalResearchGUI.leftArrow);
		Gui.drawScaledCustomSizeModalRect(this.astronomy1.xPosition - 43, this.astronomy1.yPosition + 10, 0, 0, 43, 10, 43, 10, 43, 10);
		Gui.drawScaledCustomSizeModalRect(this.astronomy2.xPosition - 43, this.astronomy2.yPosition + 10, 0, 0, 43, 10, 43, 10, 43, 10);
		Gui.drawScaledCustomSizeModalRect(this.vitae1.xPosition - 43, this.vitae1.yPosition + 10, 0, 0, 43, 10, 43, 10, 43, 10);

	}

	private void setupButtons()
	{
		// Setup the reserach nodes with an ID and position.
		this.unbreakableCovenantResearch = new NodeButton(RESEARCH_BASE_ID, xPosBaseResearch, yPosBaseResearch, 0, 0, ResearchTypes.AnUnbreakableCovenant);
		this.werewolfExamination = new NodeButton(RESEARCH_BASE_ID + 1, xPosBaseResearch, yPosBaseResearch - DISTANCE_BETWEEN_NODES, 32, 0, ResearchTypes.WerewolfExamination);
		this.crossbow = new NodeButton(RESEARCH_BASE_ID + 2, xPosBaseResearch + DISTANCE_BETWEEN_NODES, yPosBaseResearch, 64, 0, ResearchTypes.Crossbow);
		this.astronomy1 = new NodeButton(RESEARCH_BASE_ID + 3, xPosBaseResearch, yPosBaseResearch - DISTANCE_BETWEEN_NODES * 2, 96, 0, ResearchTypes.Astronomy1);
		this.vitae1 = new NodeButton(RESEARCH_BASE_ID + 4, xPosBaseResearch - DISTANCE_BETWEEN_NODES, yPosBaseResearch - DISTANCE_BETWEEN_NODES * 2, 128, 0, ResearchTypes.Vitae1);
		this.astralSilver = new NodeButton(RESEARCH_BASE_ID + 5, xPosBaseResearch, yPosBaseResearch - DISTANCE_BETWEEN_NODES * 3, 160, 0, ResearchTypes.AstralSilver);
		this.silverInfusion = new NodeButton(RESEARCH_BASE_ID + 6, xPosBaseResearch + DISTANCE_BETWEEN_NODES, yPosBaseResearch - DISTANCE_BETWEEN_NODES * 3, 192, 0, ResearchTypes.SilverInfusion);
		this.darkForest = new NodeButton(RESEARCH_BASE_ID + 7, xPosBaseResearch, yPosBaseResearch - DISTANCE_BETWEEN_NODES * 4, 224, 0, ResearchTypes.DarkForest);
		this.astronomy2 = new NodeButton(RESEARCH_BASE_ID + 8, xPosBaseResearch, yPosBaseResearch - DISTANCE_BETWEEN_NODES * 5, 0, 32, ResearchTypes.Astronomy2);
		this.igneousArmor = new NodeButton(RESEARCH_BASE_ID + 9, xPosBaseResearch - DISTANCE_BETWEEN_NODES, yPosBaseResearch - DISTANCE_BETWEEN_NODES * 5, 32, 32, ResearchTypes.IgneousArmor);
		this.starMetal = new NodeButton(RESEARCH_BASE_ID + 10, xPosBaseResearch + DISTANCE_BETWEEN_NODES, yPosBaseResearch - DISTANCE_BETWEEN_NODES * 5, 64, 32, ResearchTypes.StarMetal);
		this.sanityLantern = new NodeButton(RESEARCH_BASE_ID + 11, xPosBaseResearch - DISTANCE_BETWEEN_NODES * 2, yPosBaseResearch - DISTANCE_BETWEEN_NODES * 2, 96, 32, ResearchTypes.SanityLantern);
		this.vitaeLantern1 = new NodeButton(RESEARCH_BASE_ID + 12, xPosBaseResearch - DISTANCE_BETWEEN_NODES * 2, yPosBaseResearch - DISTANCE_BETWEEN_NODES * 3, 128, 32, ResearchTypes.VitaeLantern1);

		// Clear and pre-existing buttons on the GUI and add the new ones
		this.buttonList.clear();
		this.buttonList.add(unbreakableCovenantResearch);
		this.buttonList.add(werewolfExamination);
		this.buttonList.add(crossbow);
		this.buttonList.add(astronomy1);
		this.buttonList.add(vitae1);
		this.buttonList.add(astralSilver);
		this.buttonList.add(silverInfusion);
		this.buttonList.add(darkForest);
		this.buttonList.add(astronomy2);
		this.buttonList.add(igneousArmor);
		this.buttonList.add(starMetal);
		this.buttonList.add(sanityLantern);
		this.buttonList.add(vitaeLantern1);
	}

	@Override
	protected void checkOutOfBounds()
	{
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
	}
}
