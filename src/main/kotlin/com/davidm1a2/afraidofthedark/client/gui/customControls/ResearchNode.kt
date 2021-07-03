package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.client.gui.events.MouseEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.client.gui.screens.BloodStainedJournalPageScreen
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ButtonPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.registry.research.Research
import com.mojang.realmsclient.gui.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.I18n

/**
 * Button that represents a research in the research GUI
 */
class ResearchNode(prefSize: Dimensions, offset: Position, val research: Research, isCheatSheet: Boolean) : ButtonPane(
    icon = ImagePane("afraidofthedark:textures/gui/journal_tech_tree/research_background.png", ImagePane.DispMode.FIT_TO_PARENT),
    iconHovered = ImagePane("afraidofthedark:textures/gui/journal_tech_tree/research_background_hovered.png", ImagePane.DispMode.FIT_TO_PARENT),
    silent = true,
    gravity = Gravity.CENTER,
    prefSize = prefSize,
    offset = offset
) {
    // The player's research for fast querying
    private val playerResearch = entityPlayer.getResearch()

    private val questionIcon = ImagePane("afraidofthedark:textures/gui/research_icons/question_mark.png")
    private val researchIcon = ImagePane(this.research.icon, ImagePane.DispMode.FIT_TO_PARENT)

    override var isVisible: Boolean
        get() {
            // Whenever visibility is checked, also update visibility of the icon displayed
            this.questionIcon.isVisible = !playerResearch.isResearched(this.research)
            return playerResearch.isResearched(this.research) || playerResearch.canResearch(this.research)
        }
        set(value) {}

    init {
        this.add(researchIcon)
        this.add(questionIcon)

        // Create a node listener that controls the behavior of this research node
        this.addMouseListener {
            if (it.eventType == MouseEvent.EventType.Click) {
                if (it.clickedButton == MouseEvent.LEFT_MOUSE_BUTTON && this.isVisible && this.isHovered && this.inBounds) {
                    if (isCheatSheet) {
                        // Cheat Sheet - If this research can be researched unlock it
                        if (playerResearch.canResearch(research)) {
                            playerResearch.setResearchAndAlert(research, true, entityPlayer)
                            playerResearch.sync(entityPlayer, false)
                            refreshHoverTexts()
                        } else if (playerResearch.isResearched(research)) {
                            // Show the research if it's already researched
                            Minecraft.getInstance().displayGuiScreen(
                                BloodStainedJournalPageScreen(
                                    I18n.format(research.getUnlocalizedText()),
                                    I18n.format(research.getUnlocalizedName()),
                                    research.researchedRecipes
                                )
                            )
                        }
                    } else {
                        // If this isn't a cheat sheet open the research page
                        if (playerResearch.isResearched(research)) {
                            Minecraft.getInstance().displayGuiScreen(
                                BloodStainedJournalPageScreen(
                                    I18n.format(research.getUnlocalizedText()),
                                    I18n.format(research.getUnlocalizedName()),
                                    research.researchedRecipes
                                )
                            )
                        } else if (research.preRequisite != null && playerResearch.isResearched(research.preRequisite)) {   // Pre-Page UI
                            Minecraft.getInstance().displayGuiScreen(
                                BloodStainedJournalPageScreen(
                                    I18n.format(research.getUnlocalizedPreText()),
                                    "???",
                                    research.preResearchedRecipes
                                )
                            )
                        }
                    }
                }
            }
        }
        refreshHoverTexts()
    }

    private fun refreshHoverTexts() {
        hoverTexts = if (playerResearch.isResearched(this.research)) {
            arrayOf(I18n.format(research.getUnlocalizedName()), "${ChatFormatting.ITALIC}${I18n.format(this.research.getUnlocalizedTooltip())}")
        } else {
            arrayOf("?", "${ChatFormatting.ITALIC}Unknown Research")
        }
    }
}
