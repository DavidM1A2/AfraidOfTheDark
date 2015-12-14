/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDPlayerData;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class AOTDGuiResearchNodeButton extends AOTDGuiButtonMovable
{
	private final ResearchTypes research;
	private static final ResourceLocation UNKNOWN_RESEARCH = new ResourceLocation("afraidofthedark:textures/gui/researchIcons/QuestionMark.png");

	public AOTDGuiResearchNodeButton(int x, int y, ResearchTypes research)
	{
		super(x, y, 32, 32, null, "afraidofthedark:textures/gui/nodeStandard2.png");
		this.research = research;
		this.setVisible(AOTDPlayerData.get(Minecraft.getMinecraft().thePlayer).isResearched(this.research) || AOTDPlayerData.get(Minecraft.getMinecraft().thePlayer).canResearch(this.research));
	}

	@Override
	public void draw()
	{
		if (this.isVisible())
		{
			super.draw();
			if (this.isHovered())
				GlStateManager.color(1.0F, 1.0F, 1.0F, 0.7F);
			else
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

			// Draw the button differently depending on if the research is researched, not research, or almost researched
			if (AOTDPlayerData.get(Minecraft.getMinecraft().thePlayer).isResearched(this.research))
			{
				Minecraft.getMinecraft().getTextureManager().bindTexture(this.research.getIcon());
				Gui.drawScaledCustomSizeModalRect(this.getXScaled() + 2, this.getYScaled() + 2, 0, 0, 32, 32, this.getWidthScaled(), this.getHeightScaled(), 32, 32);
			}
			else
			{
				Minecraft.getMinecraft().getTextureManager().bindTexture(this.research.getIcon());
				Gui.drawScaledCustomSizeModalRect(this.getXScaled() + 2, this.getYScaled() + 2, 0, 0, 32, 32, this.getWidthScaled(), this.getHeightScaled(), 32, 32);
				Minecraft.getMinecraft().getTextureManager().bindTexture(UNKNOWN_RESEARCH);
				Gui.drawScaledCustomSizeModalRect(this.getXScaled() + 2, this.getYScaled() + 2, 0, 0, 32, 32, this.getWidthScaled(), this.getHeightScaled(), 32, 32);
			}
		}
	}

	public ResearchTypes getResearch()
	{
		return this.research;
	}
}
