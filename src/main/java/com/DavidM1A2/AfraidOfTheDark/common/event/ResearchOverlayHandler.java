package com.DavidM1A2.afraidofthedark.common.event;

import com.DavidM1A2.afraidofthedark.client.gui.ResearchAchievedOverlay;
import com.DavidM1A2.afraidofthedark.common.registry.research.Research;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Research overlay handler class is used to show whenever a research is unlocked
 */
public class ResearchOverlayHandler
{
	// The overlay to show researches with
	@SideOnly(Side.CLIENT)
	private final ResearchAchievedOverlay RESEARCH_ACHIEVED_OVERLAY = new ResearchAchievedOverlay();

	/**
	 * Called every tick client side to draw the overlay
	 *
	 * @param event ignored
	 */
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderGameOverlayEventChat(RenderGameOverlayEvent.Chat event)
	{
		RESEARCH_ACHIEVED_OVERLAY.updateResearchAchievedWindow();
	}

	/**
	 * Displays a given research as an overlay
	 *
	 * @param research The research to display
	 */
	@SideOnly(Side.CLIENT)
	public void displayResearch(Research research)
	{
		RESEARCH_ACHIEVED_OVERLAY.displayResearch(research);
	}
}
