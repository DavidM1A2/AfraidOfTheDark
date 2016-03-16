/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDGuiUtility;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiButton;
import com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners.AOTDMouseListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDPlayerData;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public class AOTDGuiResearchNodeButton extends AOTDGuiButton
{
	private final ResearchTypes research;
	private static final ResourceLocation UNKNOWN_RESEARCH = new ResourceLocation("afraidofthedark:textures/gui/researchIcons/QuestionMark.png");

	public AOTDGuiResearchNodeButton(int x, int y, ResearchTypes research)
	{
		super(x, y, 32, 32, null, "afraidofthedark:textures/gui/nodeStandard2.png");
		this.research = research;
		this.setVisible(AOTDPlayerData.get(entityPlayer).isResearched(this.research) || AOTDPlayerData.get(entityPlayer).canResearch(this.research));
		this.addMouseListener(new AOTDMouseListener()
		{
			@Override
			public void mouseReleased(AOTDMouseEvent event)
			{
			}

			@Override
			public void mousePressed(AOTDMouseEvent event)
			{
			}

			@Override
			public void mouseExited(AOTDMouseEvent event)
			{
				event.getSource().brightenColor(0.1f);
			}

			@Override
			public void mouseEntered(AOTDMouseEvent event)
			{
				event.getSource().darkenColor(0.1f);
			}

			@Override
			public void mouseClicked(AOTDMouseEvent event)
			{
			}
		});
	}

	@Override
	public void draw()
	{
		if (this.isVisible())
		{
			super.draw();

			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

			// Draw the button differently depending on if the research is researched, not research, or almost researched
			if (AOTDPlayerData.get(entityPlayer).isResearched(this.research))
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

	@Override
	public void drawOverlay()
	{
		if (this.getParent().getParent().intersects(this))
		{
			if (this.isHovered() && AOTDPlayerData.get(entityPlayer).isResearched(this.getResearch()))
			{
				fontRenderer.drawString(this.getResearch().formattedString(), AOTDGuiUtility.getMouseX() + 5, AOTDGuiUtility.getMouseY(), 0xFF3399);
				fontRenderer.drawString(EnumChatFormatting.ITALIC + this.getResearch().getTooltip(), AOTDGuiUtility.getMouseX() + 7, AOTDGuiUtility.getMouseY() + 10, 0xE62E8A);
			}
			else if (this.isHovered() && AOTDPlayerData.get(entityPlayer).canResearch(this.getResearch()))
			{
				fontRenderer.drawString("?", AOTDGuiUtility.getMouseX() + 5, AOTDGuiUtility.getMouseY(), 0xFF3399);
				fontRenderer.drawString(EnumChatFormatting.ITALIC + "Unknown Research", AOTDGuiUtility.getMouseX() + 7, AOTDGuiUtility.getMouseY() + 10, 0xE62E8A);
			}
		}
	}

	public ResearchTypes getResearch()
	{
		return this.research;
	}
}
