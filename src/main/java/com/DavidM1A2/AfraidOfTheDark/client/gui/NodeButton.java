/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.playerData.LoadResearchData;
import com.DavidM1A2.AfraidOfTheDark.research.Research;
import com.DavidM1A2.AfraidOfTheDark.research.ResearchTypes;

/*
 * A button for researches
 */
public class NodeButton extends GuiButton
{
	private final int ORIGINAL_X_POSITION;
	private final int ORIGINAL_Y_POSITION;
	// Reserach locations for default textures
	private static final ResourceLocation DEFAULT_RESEARCH_BACKGROUND = new ResourceLocation("afraidofthedark:textures/gui/nodeStandard.png");
	private static final ResourceLocation RESEARCH_ICONS = new ResourceLocation("afraidofthedark:textures/gui/ResearchIcons.png");
	private static final ResourceLocation UNKNOWN_RESEARCH = new ResourceLocation("afraidofthedark:textures/gui/QuestionMark.png");
	private final int iconOffsetX;
	private final int iconOffsetY;
	private final ResearchTypes myType;

	// Each button has a position and offset
	public NodeButton(int ID, int xPosition, int yPosition, int iconOffsetX, int iconOffsetY, ResearchTypes myType)
	{
		super(ID, xPosition, yPosition, 32, 32, "");
		this.ORIGINAL_X_POSITION = xPosition;
		this.ORIGINAL_Y_POSITION = yPosition;
		this.iconOffsetX = iconOffsetX;
		this.iconOffsetY = iconOffsetY;
		this.myType = myType;
	}

	// Set the position of this node
	public void setPosition(int xPos, int yPos)
	{
		this.xPosition = ORIGINAL_X_POSITION - xPos;
		this.yPosition = ORIGINAL_Y_POSITION - yPos;
	}

	// Draw button draws the button using OpenGL
	@Override
	public void drawButton(Minecraft minecraft, int mouseX, int mouseY)
	{
		// Make sure it should be visible
		if (this.visible)
		{
			this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

			// Draw background start:
			minecraft.getTextureManager().bindTexture(DEFAULT_RESEARCH_BACKGROUND);
			if (this.isMouseOver())
			{
				GL11.glColor4f(1.0F, 1.0F, 1.0F, .8F);
			}
			else
			{
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 0, this.width, this.height);

			Research myResearch = LoadResearchData.getResearch(Minecraft.getMinecraft().thePlayer);
			if (myResearch.getResearch(myType).isResearched())
			{
				drawKnownResearch(minecraft);
			}
			else if (myResearch.getResearch(myResearch.getResearch(myType).getPrevious()) != null && myResearch.getResearch(myResearch.getResearch(myType).getPrevious()).isResearched())
			{
				drawAlmostKnownResearch(minecraft);
			}
			else
			{
				drawUnknownResearch(minecraft);
			}

			this.mouseDragged(minecraft, mouseX, mouseY);
		}
	}

	public ResearchTypes getMyType()
	{
		return this.myType;
	}

	private void drawKnownResearch(Minecraft minecraft)
	{
		minecraft.getTextureManager().bindTexture(RESEARCH_ICONS);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		this.drawTexturedModalRect(this.xPosition + 2, this.yPosition + 2, iconOffsetX, iconOffsetY, this.width - 4, this.height - 4);
	}

	private void drawUnknownResearch(Minecraft minecraft)
	{
		minecraft.getTextureManager().bindTexture(UNKNOWN_RESEARCH);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		this.drawTexturedModalRect(this.xPosition + 2, this.yPosition + 2, 0, 0, this.width - 4, this.height - 4);
	}

	private void drawAlmostKnownResearch(Minecraft minecraft)
	{
		minecraft.getTextureManager().bindTexture(RESEARCH_ICONS);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		this.drawTexturedModalRect(this.xPosition + 2, this.yPosition + 2, iconOffsetX, iconOffsetY + 32, this.width - 4, this.height - 4);

		minecraft.getTextureManager().bindTexture(UNKNOWN_RESEARCH);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		this.drawTexturedModalRect(this.xPosition + 2, this.yPosition + 2, 0, 0, this.width - 4, this.height - 4);
	}
}
