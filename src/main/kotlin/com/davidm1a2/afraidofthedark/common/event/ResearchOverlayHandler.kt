package com.davidm1a2.afraidofthedark.common.event

import com.davidm1a2.afraidofthedark.client.gui.ResearchAchievedOverlay
import com.davidm1a2.afraidofthedark.common.registry.research.Research
import net.minecraftforge.client.event.RenderGameOverlayEvent.Chat
import net.minecraftforge.eventbus.api.SubscribeEvent

/**
 * Research overlay handler class is used to show whenever a research is unlocked
 */
class ResearchOverlayHandler {
    // The overlay to show researches with
    private val researchAchievedOverlay = ResearchAchievedOverlay()

    /**
     * Called every tick client side to draw the overlay
     *
     * @param event Chat event
     */
    @SubscribeEvent
    fun onRenderGameOverlayEventChat(event: Chat) {
        researchAchievedOverlay.updateResearchAchievedWindow(event.matrixStack)
    }

    /**
     * Displays a given research as an overlay
     *
     * @param research The research to display
     */
    fun displayResearch(research: Research) {
        researchAchievedOverlay.displayResearch(research)
    }
}