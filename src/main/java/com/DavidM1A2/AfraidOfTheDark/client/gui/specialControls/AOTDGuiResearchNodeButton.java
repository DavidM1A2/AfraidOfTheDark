package com.DavidM1A2.afraidofthedark.client.gui.specialControls;

import com.DavidM1A2.afraidofthedark.client.gui.AOTDGuiUtility;
import com.DavidM1A2.afraidofthedark.client.gui.eventListeners.AOTDMouseListener;
import com.DavidM1A2.afraidofthedark.client.gui.events.AOTDMouseEvent;
import com.DavidM1A2.afraidofthedark.client.gui.standardControls.AOTDGuiButton;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.research.IAOTDPlayerResearch;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.research.base.Research;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Button that represents a research in the research GUI
 */
public class AOTDGuiResearchNodeButton extends AOTDGuiButton
{
	// Icon used by buttons that have unknown icons
	private static final ResourceLocation UNKNOWN_RESEARCH = new ResourceLocation("afraidofthedark:textures/gui/research_icons/question_mark.png");

	// The research that this button represents
	private final Research research;

	// The player's research for fast querying
	private final IAOTDPlayerResearch playerResearch;

	/**
	 * Constructor initializes the button based on the selected research
	 *
	 * @param x The x coordinate of the button
	 * @param y The y coordinate of the button
	 * @param research The research that this button represents
	 */
	public AOTDGuiResearchNodeButton(int x, int y, Research research)
	{
		super(x, y, 32, 32, null, "afraidofthedark:textures/gui/node_standard2.png");
		this.research = research;

		// Make the button visible if the research is either researched or can be researched show it
		this.playerResearch = entityPlayer.getCapability(ModCapabilities.PLAYER_RESEARCH, null);
		this.setVisible(playerResearch.isResearched(this.research) || playerResearch.canResearch(this.research));

		// The mouse listener tests when the mouse enters the button, if it does the button is brightened, otherwise it is darkened
		this.addMouseListener(new AOTDMouseListener()
		{
			@Override
			public void mouseExited(AOTDMouseEvent event)
			{
				event.getSource().brightenColor(15);
			}

			@Override
			public void mouseEntered(AOTDMouseEvent event)
			{
				event.getSource().darkenColor(15);
			}
		});
	}

	/**
	 * Draws the node button
	 */
	@Override
	public void draw()
	{
		if (this.isVisible())
		{
			super.draw();

			// Ensure our color is white to draw with
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

			// Draw the researches icon on the button
			Minecraft.getMinecraft().getTextureManager().bindTexture(this.research.getIcon());
			Gui.drawScaledCustomSizeModalRect(this.getXScaled() + 2, this.getYScaled() + 2, 0, 0, 32, 32, this.getWidthScaled(), this.getHeightScaled(), 32, 32);

			// If the player has not researched the research then show the question mark over top
			if (!this.playerResearch.isResearched(this.research))
			{
				Minecraft.getMinecraft().getTextureManager().bindTexture(UNKNOWN_RESEARCH);
				Gui.drawScaledCustomSizeModalRect(this.getXScaled() + 2, this.getYScaled() + 2, 0, 0, 32, 32, this.getWidthScaled(), this.getHeightScaled(), 32, 32);
			}
		}
	}

	/**
	 * Called to draw the name of the research when the button is hovered
	 */
	@Override
	public void drawOverlay()
	{
		super.drawOverlay();

		// If the button intersects the pane it's in then allow for drawing hover text
		if (this.getParent().getParent().intersects(this) && this.isHovered())
		{
			Integer mouseX = AOTDGuiUtility.getInstance().getMouseXInMCCoord();
			Integer mouseY = AOTDGuiUtility.getInstance().getMouseYInMCCoord();

			// If the research is researched show the name of the research when hovered
			if (playerResearch.isResearched(this.research))
			{
				fontRenderer.drawString(research.getLocalizedName(), mouseX + 5, mouseY, 0xFF3399);
				fontRenderer.drawString(ChatFormatting.ITALIC + this.getResearch().getTooltip(), mouseX + 7, mouseY + 10, 0xE62E8A);
			}
			// If the research can be researched show a ? and unknown research when hovered
			else if (playerResearch.canResearch(this.research))
			{
				fontRenderer.drawString("?", mouseX + 5, mouseY, 0xFF3399);
				fontRenderer.drawString(ChatFormatting.ITALIC + "Unknown Research", mouseX + 7, mouseY + 10, 0xE62E8A);
			}
		}
	}

	/**
	 * @return Returns the research that this button represents
	 */
	public Research getResearch()
	{
		return this.research;
	}
}
