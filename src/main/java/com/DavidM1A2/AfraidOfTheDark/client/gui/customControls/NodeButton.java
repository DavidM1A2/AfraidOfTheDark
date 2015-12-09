/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDPlayerData;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/*
 * A button for researches
 */
public class NodeButton extends AOTDGuiButtonMovable
{
	// Reserach locations for default textures
	private static final ResourceLocation DEFAULT_RESEARCH_BACKGROUND = new ResourceLocation("afraidofthedark:textures/gui/nodeStandard.png");
	private static final ResourceLocation UNKNOWN_RESEARCH = new ResourceLocation("afraidofthedark:textures/gui/researchIcons/QuestionMark.png");
	private final ResearchTypes myType;

	// Each button has a position and offset
	public NodeButton(final int buttonID, final int xPosition, final int yPosition, final ResearchTypes myType)
	{
		super(xPosition, yPosition, 32, 32);
		this.myType = myType;
	}

	// Draw button draws the button using OpenGL
	@Override
	public void draw()
	{
		this.setVisible(AOTDPlayerData.get(Minecraft.getMinecraft().thePlayer).isResearched(this.myType) || AOTDPlayerData.get(Minecraft.getMinecraft().thePlayer).canResearch(this.myType));

		// Make sure it should be visible
		if (this.isVisible())
		{
			super.draw();
			if (this.isHovered())
				GlStateManager.color(1.0F, 1.0F, 1.0F, 0.7F);
			else
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

			Minecraft minecraft = Minecraft.getMinecraft();
			minecraft.getTextureManager().bindTexture(DEFAULT_RESEARCH_BACKGROUND);
			Gui.drawScaledCustomSizeModalRect(this.getXScaled(), this.getYScaled(), 0, 0, 512, 512, this.getWidthScaled(), this.getHeightScaled(), 512, 512);

			// Draw the button differently depending on if the research is researched, not research, or almost researched
			if (AOTDPlayerData.get(Minecraft.getMinecraft().thePlayer).isResearched(this.myType))
			{
				minecraft.getTextureManager().bindTexture(this.myType.getIcon());
				Gui.drawScaledCustomSizeModalRect(this.getXScaled() + 2, this.getYScaled() + 2, 0, 0, 32, 32, this.getWidthScaled(), this.getHeightScaled(), 32, 32);
			}
			else
			{
				minecraft.getTextureManager().bindTexture(this.myType.getIcon());
				Gui.drawScaledCustomSizeModalRect(this.getXScaled() + 2, this.getYScaled() + 2, 0, 0, 32, 32, this.getWidthScaled(), this.getHeightScaled(), 32, 32);
				minecraft.getTextureManager().bindTexture(NodeButton.UNKNOWN_RESEARCH);
				Gui.drawScaledCustomSizeModalRect(this.getXScaled() + 2, this.getYScaled() + 2, 0, 0, 32, 32, this.getWidthScaled(), this.getHeightScaled(), 32, 32);
			}
		}
	}

	public ResearchTypes getMyType()
	{
		return this.myType;
	}
}
