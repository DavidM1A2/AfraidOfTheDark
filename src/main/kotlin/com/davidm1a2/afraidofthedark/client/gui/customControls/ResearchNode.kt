package com.davidm1a2.afraidofthedark.client.gui.customControls

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.client.gui.events.MouseEvent
import com.davidm1a2.afraidofthedark.client.gui.layout.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.client.gui.screens.ArcaneJournalPageScreen
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ButtonPane
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.network.packets.other.CheatSheetUnlockPacket
import com.davidm1a2.afraidofthedark.common.research.Research
import net.minecraft.client.Minecraft
import net.minecraft.util.text.TextFormatting
import net.minecraft.util.text.TranslationTextComponent

/**
 * Button that represents a research in the research GUI
 */
class ResearchNode(prefSize: Dimensions, offset: Position, val research: Research, isCheatSheet: Boolean) : ButtonPane(
    icon = ImagePane("afraidofthedark:textures/gui/arcane_journal_tech_tree/research_background_new.png", ImagePane.DispMode.FIT_TO_PARENT),
    iconHovered = ImagePane("afraidofthedark:textures/gui/arcane_journal_tech_tree/research_background_hovered_new.png", ImagePane.DispMode.FIT_TO_PARENT),
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
                            AfraidOfTheDark.packetHandler.sendToServer(CheatSheetUnlockPacket(research))
                            refreshHoverTexts(true)
                        } else if (playerResearch.isResearched(research)) {
                            // Show the research if it's already researched
                            Minecraft.getInstance().setScreen(ArcaneJournalPageScreen(research))
                        }
                    } else {
                        Minecraft.getInstance().setScreen(ArcaneJournalPageScreen(research))
                    }
                }
            }
        }
        refreshHoverTexts(false)
    }

    private fun refreshHoverTexts(forceShowUnlocked: Boolean) {
        // forceShowUnlocked means we've requested that the server unlock a research for us, but we might not have a response yet. Just assume
        // the unlock was successful and show the real tooltip if it's 'true'.
        hoverTexts = if (forceShowUnlocked || playerResearch.isResearched(this.research)) {
            arrayOf(research.getName().string, "${TextFormatting.ITALIC}${research.getTooltip().string}")
        } else {
            arrayOf(
                TranslationTextComponent("tooltip.afraidofthedark.gui.journal_research.question_mark").string,
                TranslationTextComponent("tooltip.afraidofthedark.gui.journal_research.unknown_research").string,
            )
        }
    }
}
