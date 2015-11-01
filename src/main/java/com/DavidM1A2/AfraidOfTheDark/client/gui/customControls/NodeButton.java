/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import com.DavidM1A2.AfraidOfTheDark.common.playerData.AOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/*
 * A button for researches
 */
public class NodeButton extends GuiButton
{
	private final int ORIGINAL_X_POSITION;
	private final int ORIGINAL_Y_POSITION;
	// Reserach locations for default textures
	private static final ResourceLocation DEFAULT_RESEARCH_BACKGROUND = new ResourceLocation("afraidofthedark:textures/gui/nodeStandard.png");
	private static final ResourceLocation UNKNOWN_RESEARCH = new ResourceLocation("afraidofthedark:textures/gui/researchIcons/QuestionMark.png");
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
		if (AOTDPlayerData.get(Minecraft.getMinecraft().thePlayer).isResearched(this.myType) || AOTDPlayerData.get(Minecraft.getMinecraft().thePlayer).canResearch(this.myType))
		{
			this.visible = true;
		}
		else
		{
			this.visible = false;
		}

		// Make sure it should be visible
		if (this.visible)
		{
			this.hovered = (mouseX >= this.xPosition) && (mouseY >= this.yPosition) && (mouseX < (this.xPosition + this.width)) && (mouseY < (this.yPosition + this.height));

			// Draw background start:
			minecraft.getTextureManager().bindTexture(NodeButton.DEFAULT_RESEARCH_BACKGROUND);
			if (this.isMouseOver())
			{
				GlStateManager.color(1.0F, 1.0F, 1.0F, 0.8F);
			}
			else
			{
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			}
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.blendFunc(770, 771);
			Gui.drawScaledCustomSizeModalRect(this.xPosition, this.yPosition, 0, 0, 512, 512, this.width, this.height, 512, 512);

			// Draw the button differently depending on if the research is researched, not research, or almost researched
			if (AOTDPlayerData.get(Minecraft.getMinecraft().thePlayer).isResearched(this.myType))
			{
				minecraft.getTextureManager().bindTexture(this.myType.getIcon());
				Gui.drawScaledCustomSizeModalRect(this.xPosition + 2, this.yPosition + 2, 0, 0, 32, 32, this.width, this.height, 32, 32);
			}
			else
			{
				minecraft.getTextureManager().bindTexture(this.myType.getIcon());
				Gui.drawScaledCustomSizeModalRect(this.xPosition + 2, this.yPosition + 2, 0, 0, 32, 32, this.width, this.height, 32, 32);
				minecraft.getTextureManager().bindTexture(NodeButton.UNKNOWN_RESEARCH);
				Gui.drawScaledCustomSizeModalRect(this.xPosition + 2, this.yPosition + 2, 0, 0, 32, 32, this.width, this.height, 32, 32);
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
}
