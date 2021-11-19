package com.davidm1a2.afraidofthedark.client.event

import com.davidm1a2.afraidofthedark.client.gui.ResearchAchievedOverlay
import com.davidm1a2.afraidofthedark.common.event.ResearchOverlayHandler
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraftforge.client.event.ClientPlayerNetworkEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent.Chat
import net.minecraftforge.eventbus.api.SubscribeEvent

/**
 * Research overlay handler class is used to show whenever a research is unlocked
 */
class ClientResearchOverlayHandler : ResearchOverlayHandler {
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
     * Called when the player logs out of the world. Clear the research overlay
     */
    @SubscribeEvent
    fun onLoggedOutEvent(event: ClientPlayerNetworkEvent.LoggedOutEvent) {
        researchAchievedOverlay.clearResearches()
    }

    override fun displayResearch(research: Research) {
        researchAchievedOverlay.displayResearch(research)
    }

    override fun clearDisplayedResearches() {
        researchAchievedOverlay.clearResearches()
    }
}