package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.client.gui.ResearchAchievedOverlay
import com.davidm1a2.afraidofthedark.common.registry.research.Research
import net.minecraftforge.client.event.RenderGameOverlayEvent.Chat
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * Research overlay handler class is used to show whenever a research is unlocked
 */
class ResearchOverlayHandler
{
    // The overlay to show researches with
    @SideOnly(Side.CLIENT)
    private val researchAchievedOverlay = ResearchAchievedOverlay()

    /**
     * Called every tick client side to draw the overlay
     *
     * @param event ignored
     */
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    fun onRenderGameOverlayEventChat(event: Chat)
    {
        researchAchievedOverlay.updateResearchAchievedWindow()
    }

    /**
     * Displays a given research as an overlay
     *
     * @param research The research to display
     */
    @SideOnly(Side.CLIENT)
    fun displayResearch(research: Research)
    {
        researchAchievedOverlay.displayResearch(research)
    }
}