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
		Gui.drawScaledCustomSizeModalRect(BloodStainedJournalResearchGUI.xPosScroll, BloodStainedJournalResearchGUI.yPosScroll, (this.guiOffsetX * 2) + 384, (this.guiOffsetY * 2) + 768, BloodStainedJournalResearchGUI.BACKGROUND_WIDTH, BloodStainedJournalResearchGUI.BACKGROUND_HEIGHT, BloodStainedJournalResearchGUI.BACKGROUND_WIDTH, BloodStainedJournalResearchGUI.BACKGROUND_HEIGHT, 1024, 1024);

		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		final int disWidth = Minecraft.getMinecraft().displayWidth;
		final int disHeight = Minecraft.getMinecraft().displayHeight;
		final int widthScale = Math.round(disWidth / (float) this.width);
		final int heightScale = Math.round(disHeight / (float) this.height);
		GL11.glScissor(disWidth - ((BloodStainedJournalResearchGUI.xPosScroll + BloodStainedJournalResearchGUI.BACKGROUND_WIDTH) * widthScale), disHeight - ((BloodStainedJournalResearchGUI.yPosScroll + BloodStainedJournalResearchGUI.BACKGROUND_HEIGHT) * widthScale), BloodStainedJournalResearchGUI.BACKGROUND_WIDTH * heightScale, BloodStainedJournalResearchGUI.BACKGROUND_HEIGHT * heightScale);
		super.drawScreen(i, j, f);
		this.drawLines();
		GL11.glDisable(GL11.GL_SCISSOR_TEST);

		this.mc.renderEngine.bindTexture(new ResourceLocation("afraidofthedark:textures/gui/BloodStainedJournalResearchBackground.png"));
		this.drawTexturedModalRect(BloodStainedJournalResearchGUI.xPosScroll, BloodStainedJournalResearchGUI.yPosScroll, 0, 0, BloodStainedJournalResearchGUI.BACKGROUND_WIDTH, BloodStainedJournalResearchGUI.BACKGROUND_HEIGHT);
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
		Gui.drawScaledCustomSizeModalRect(this.sanityLantern.xPosition + 10, this.sanityLantern.yPosition - 43, 0, 0, 10, 43, 10, 43, 10, 43);

		this.mc.renderEngine.bindTexture(BloodStainedJournalResearchGUI.rightArrow);
		Gui.drawScaledCustomSizeModalRect(this.unbreakableCovenantResearch.xPosition + 30, this.unbreakableCovenantResearch.yPosition + 10, 0, 0, 43, 10, 43, 10, 43, 10);
		Gui.drawScaledCustomSizeModalRect(this.astralSilver.xPosition + 30, this.astralSilver.yPosition + 10, 0, 0, 43, 10, 43, 10, 43, 10);
		Gui.drawScaledCustomSizeModalRect(this.astronomy2.xPosition + 30, this.astronomy2.yPosition + 10, 0, 0, 43, 10, 43, 10, 43, 10);

		this.mc.renderEngine.bindTexture(BloodStainedJournalResearchGUI.leftArrow);
		Gui.drawScaledCustomSizeModalRect(this.astronomy1.xPosition - 43, this.astronomy1.yPosition + 10, 0, 0, 43, 10, 43, 10, 43, 10);
		Gui.drawScaledCustomSizeModalRect(this.astronomy2.xPosition - 43, this.astronomy2.yPosition + 10, 0, 0, 43, 10, 43, 10, 43, 10);
		Gui.drawScaledCustomSizeModalRect(this.vitae1.xPosition - 43, this.vitae1.yPosition + 10, 0, 0, 43, 10, 43, 10, 43, 10);

	}

	private void setupButtons()
	{
		// Setup the reserach nodes with an ID and position.
		this.unbreakableCovenantResearch = new NodeButton(BloodStainedJournalResearchGUI.RESEARCH_BASE_ID, BloodStainedJournalResearchGUI.xPosBaseResearch, BloodStainedJournalResearchGUI.yPosBaseResearch, 0, 0, ResearchTypes.AnUnbreakableCovenant);
		this.werewolfExamination = new NodeButton(BloodStainedJournalResearchGUI.RESEARCH_BASE_ID + 1, BloodStainedJournalResearchGUI.xPosBaseResearch, BloodStainedJournalResearchGUI.yPosBaseResearch - BloodStainedJournalResearchGUI.DISTANCE_BETWEEN_NODES, 32, 0, ResearchTypes.WerewolfExamination);
		this.crossbow = new NodeButton(BloodStainedJournalResearchGUI.RESEARCH_BASE_ID + 2, BloodStainedJournalResearchGUI.xPosBaseResearch + BloodStainedJournalResearchGUI.DISTANCE_BETWEEN_NODES, BloodStainedJournalResearchGUI.yPosBaseResearch, 64, 0, ResearchTypes.Crossbow);
		this.astronomy1 = new NodeButton(BloodStainedJournalResearchGUI.RESEARCH_BASE_ID + 3, BloodStainedJournalResearchGUI.xPosBaseResearch, BloodStainedJournalResearchGUI.yPosBaseResearch - (BloodStainedJournalResearchGUI.DISTANCE_BETWEEN_NODES * 2), 96, 0, ResearchTypes.Astronomy1);
		this.vitae1 = new NodeButton(BloodStainedJournalResearchGUI.RESEARCH_BASE_ID + 4, BloodStainedJournalResearchGUI.xPosBaseResearch - BloodStainedJournalResearchGUI.DISTANCE_BETWEEN_NODES, BloodStainedJournalResearchGUI.yPosBaseResearch - (BloodStainedJournalResearchGUI.DISTANCE_BETWEEN_NODES * 2), 128, 0, ResearchTypes.Vitae1);
		this.astralSilver = new NodeButton(BloodStainedJournalResearchGUI.RESEARCH_BASE_ID + 5, BloodStainedJournalResearchGUI.xPosBaseResearch, BloodStainedJournalResearchGUI.yPosBaseResearch - (BloodStainedJournalResearchGUI.DISTANCE_BETWEEN_NODES * 3), 160, 0, ResearchTypes.AstralSilver);
		this.silverInfusion = new NodeButton(BloodStainedJournalResearchGUI.RESEARCH_BASE_ID + 6, BloodStainedJournalResearchGUI.xPosBaseResearch + BloodStainedJournalResearchGUI.DISTANCE_BETWEEN_NODES, BloodStainedJournalResearchGUI.yPosBaseResearch - (BloodStainedJournalResearchGUI.DISTANCE_BETWEEN_NODES * 3), 192, 0, ResearchTypes.SilverInfusion);
		this.darkForest = new NodeButton(BloodStainedJournalResearchGUI.RESEARCH_BASE_ID + 7, BloodStainedJournalResearchGUI.xPosBaseResearch, BloodStainedJournalResearchGUI.yPosBaseResearch - (BloodStainedJournalResearchGUI.DISTANCE_BETWEEN_NODES * 4), 224, 0, ResearchTypes.DarkForest);
		this.astronomy2 = new NodeButton(BloodStainedJournalResearchGUI.RESEARCH_BASE_ID + 8, BloodStainedJournalResearchGUI.xPosBaseResearch, BloodStainedJournalResearchGUI.yPosBaseResearch - (BloodStainedJournalResearchGUI.DISTANCE_BETWEEN_NODES * 5), 0, 32, ResearchTypes.Astronomy2);
		this.igneousArmor = new NodeButton(BloodStainedJournalResearchGUI.RESEARCH_BASE_ID + 9, BloodStainedJournalResearchGUI.xPosBaseResearch - BloodStainedJournalResearchGUI.DISTANCE_BETWEEN_NODES, BloodStainedJournalResearchGUI.yPosBaseResearch - (BloodStainedJournalResearchGUI.DISTANCE_BETWEEN_NODES * 5), 32, 32, ResearchTypes.IgneousArmor);
		this.starMetal = new NodeButton(BloodStainedJournalResearchGUI.RESEARCH_BASE_ID + 10, BloodStainedJournalResearchGUI.xPosBaseResearch + BloodStainedJournalResearchGUI.DISTANCE_BETWEEN_NODES, BloodStainedJournalResearchGUI.yPosBaseResearch - (BloodStainedJournalResearchGUI.DISTANCE_BETWEEN_NODES * 5), 64, 32, ResearchTypes.StarMetal);
		this.sanityLantern = new NodeButton(BloodStainedJournalResearchGUI.RESEARCH_BASE_ID + 11, BloodStainedJournalResearchGUI.xPosBaseResearch - (BloodStainedJournalResearchGUI.DISTANCE_BETWEEN_NODES * 2), BloodStainedJournalResearchGUI.yPosBaseResearch - (BloodStainedJournalResearchGUI.DISTANCE_BETWEEN_NODES * 2), 96, 32, ResearchTypes.SanityLantern);
		this.vitaeLantern1 = new NodeButton(BloodStainedJournalResearchGUI.RESEARCH_BASE_ID + 12, BloodStainedJournalResearchGUI.xPosBaseResearch - (BloodStainedJournalResearchGUI.DISTANCE_BETWEEN_NODES * 2), BloodStainedJournalResearchGUI.yPosBaseResearch - (BloodStainedJournalResearchGUI.DISTANCE_BETWEEN_NODES * 3), 128, 32, ResearchTypes.VitaeLantern1);

		// Clear and pre-existing buttons on the GUI and add the new ones
		this.buttonList.clear();
		this.buttonList.add(this.unbreakableCovenantResearch);
		this.buttonList.add(this.werewolfExamination);
		this.buttonList.add(this.crossbow);
		this.buttonList.add(this.astronomy1);
		this.buttonList.add(this.vitae1);
		this.buttonList.add(this.astralSilver);
		this.buttonList.add(this.silverInfusion);
		this.buttonList.add(this.darkForest);
		this.buttonList.add(this.astronomy2);
		this.buttonList.add(this.igneousArmor);
		this.buttonList.add(this.starMetal);
		this.buttonList.add(this.sanityLantern);
		this.buttonList.add(this.vitaeLantern1);
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
