/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.common.playerData.LoadResearchData;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;

/*
 * A button for researches
 */
public class NodeButton extends GuiButton
{
	private final int ORIGINAL_X_POSITION;
	private final int ORIGINAL_Y_POSITION;
	// Reserach locations for default textures
	private static final ResourceLocation DEFAULT_RESEARCH_BACKGROUND = new ResourceLocation("afraidofthedark:textures/gui/nodeStandard.png");
	private static final ResourceLocation UNKNOWN_RESEARCH = new ResourceLocation("afraidofthedark:textures/gui/QuestionMark.png");
	private final ResearchTypes myType;

	// Each button has a position and offset
	public NodeButton(final int ID, final int xPosition, final int yPosition, final ResearchTypes myType)
	{
		super(ID, xPosition, yPosition, 32, 32, "");
		this.ORIGINAL_X_POSITION = xPosition;
		this.ORIGINAL_Y_POSITION = yPosition;
		this.myType = myType;
	}

	// Set the position of this node
	public void setPosition(final int xPos, final int yPos)
	{
		this.xPosition = this.ORIGINAL_X_POSITION - xPos;
		this.yPosition = this.ORIGINAL_Y_POSITION - yPos;
	}

	// Draw button draws the button using OpenGL
	@Override
	public void drawButton(final Minecraft minecraft, final int mouseX, final int mouseY)
	{

		// Make sure it should be visible
		if (this.visible)
		{
			this.hovered = (mouseX >= this.xPosition) && (mouseY >= this.yPosition) && (mouseX < (this.xPosition + this.width)) && (mouseY < (this.yPosition + this.height));

			// Draw background start:
			minecraft.getTextureManager().bindTexture(NodeButton.DEFAULT_RESEARCH_BACKGROUND);
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
			Gui.drawScaledCustomSizeModalRect(this.xPosition, this.yPosition, 0, 0, 512, 512, this.width, this.height, 512, 512);

			if (LoadResearchData.isResearched(Minecraft.getMinecraft().thePlayer, this.myType))
			{
				this.drawKnownResearch(minecraft);
			}
			else if ((this.myType.getPrevious() != null) && LoadResearchData.isResearched(Minecraft.getMinecraft().thePlayer, this.myType.getPrevious()))
			{
				this.drawAlmostKnownResearch(minecraft);
			}
			else
			{
				this.drawUnknownResearch(minecraft);
			}

			this.mouseDragged(minecraft, mouseX, mouseY);
		}
	}

	public ResearchTypes getMyType()
	{
		return this.myType;
	}

	public int getOriginalXCoord()
	{
		return this.ORIGINAL_X_POSITION;
	}

	public int getOriginalYCoord()
	{
		return this.ORIGINAL_Y_POSITION;
	}

	private void drawKnownResearch(final Minecraft minecraft)
	{
		minecraft.getTextureManager().bindTexture(this.myType.getIcon());
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Gui.drawScaledCustomSizeModalRect(this.xPosition + 2, this.yPosition + 2, 0, 0, 32, 32, this.width, this.height, 32, 32);
	}

	private void drawUnknownResearch(final Minecraft minecraft)
	{
		minecraft.getTextureManager().bindTexture(NodeButton.UNKNOWN_RESEARCH);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Gui.drawScaledCustomSizeModalRect(this.xPosition + 2, this.yPosition + 2, 0, 0, 32, 32, this.width - 4, this.height - 4, 32, 32);
	}

	private void drawAlmostKnownResearch(final Minecraft minecraft)
	{
		minecraft.getTextureManager().bindTexture(this.myType.getIcon());
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Gui.drawScaledCustomSizeModalRect(this.xPosition + 2, this.yPosition + 2, 0, 0, 32, 32, this.width - 4, this.height - 4, 32, 32);

		minecraft.getTextureManager().bindTexture(NodeButton.UNKNOWN_RESEARCH);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Gui.drawScaledCustomSizeModalRect(this.xPosition + 2, this.yPosition + 2, 0, 0, 32, 32, this.width - 4, this.height - 4, 32, 32);
	}
}
