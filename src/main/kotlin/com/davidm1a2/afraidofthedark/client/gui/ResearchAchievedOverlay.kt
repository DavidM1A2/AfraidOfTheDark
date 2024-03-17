package com.davidm1a2.afraidofthedark.client.gui

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.research.Research
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiComponent
import net.minecraft.resources.ResourceLocation
import org.apache.logging.log4j.LogManager
import java.util.*

/**
 * Class used to show what researches were unlocked. This code is copied from the achievement UI from MC 1.8.9
 *
 * @property mc MC reference
 * @property width The width of the overlay
 * @property height The height of hte overlay
 * @property researchDescription The description of the research
 * @property notificationTime The time of the last research notification
 * @property toDisplay A queue of researches to display
 */
class ResearchAchievedOverlay : GuiComponent() {
    private val mc = Minecraft.getInstance()
    private var width = 0
    private var height = 0
    private var researchDescription: String? = null
    private var notificationTime: Long = 0
    private val toDisplay = LinkedList<Research>()

    /**
     * Displays a given research in the overlay
     *
     * @param research The research to display
     */
    fun displayResearch(research: Research) {
        // Only allow our queue to store 3 elements, so we don't flood the player with unlock messages
        if (toDisplay.size < 3) {
            toDisplay.push(research)
        } else {
            LOG.info("Skipping displaying research ${research.getName().string}, we already have three others in queue")
        }
    }

    /**
     * Function provided by MC's achievement window to setup the viewport, copied and unmodified
     */
    private fun updateResearchAchievedWindowScale() {
        // TODO: Replace with toast (Use AdvancementToast.java as example)
    }

    /**
     * Function provided by MC's achievement window to setup the window, copied and slightly modified to work with a queue of researches to show
     */
    fun updateResearchAchievedWindow(poseStack: PoseStack) {
        // TODO: Replace with toast (Use AdvancementToast.java as example)
    }

    /**
     * Clears any researches in the queue to be displayed
     */
    fun clearResearches() {
        toDisplay.clear()
        notificationTime = 0L
    }

    companion object {
        private val LOG = LogManager.getLogger()

        // The texture of the achievement background
        private val ACHIEVEMENT_BACKGROUND = ResourceLocation(Constants.MOD_ID, "textures/gui/research_achieved.png")
    }
}