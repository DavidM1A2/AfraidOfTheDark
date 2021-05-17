package com.davidm1a2.afraidofthedark.client.gui.specialControls

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiGravity
import com.davidm1a2.afraidofthedark.client.gui.base.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.base.Position
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseEvent
import com.davidm1a2.afraidofthedark.client.gui.events.AOTDMouseMoveEvent
import com.davidm1a2.afraidofthedark.client.gui.screens.BloodStainedJournalPageScreen
import com.davidm1a2.afraidofthedark.client.gui.screens.BloodStainedJournalResearchScreen
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiButton
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.registry.research.Research
import com.mojang.realmsclient.gui.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.resources.I18n

/**
 * Button that represents a research in the research GUI
 */
class AOTDGuiResearchNodeButton(prefSize: Dimensions<Double>, offset: Position<Double>, val research: Research, isCheatSheet: Boolean, parent: BloodStainedJournalResearchScreen) : AOTDGuiButton(
    prefSize,
    offset,
    icon = ImagePane("afraidofthedark:textures/gui/journal_tech_tree/research_background.png", ImagePane.DispMode.FIT_TO_PARENT),
    iconHovered = ImagePane("afraidofthedark:textures/gui/journal_tech_tree/research_background_hovered.png", ImagePane.DispMode.FIT_TO_PARENT),
    gravity = AOTDGuiGravity.CENTER
) {
    // The player's research for fast querying
    private val playerResearch = entityPlayer.getResearch()

    init {
        // Make the button visible if the research is either researched or can be researched show it
        this.isVisible = playerResearch.isResearched(this.research) || playerResearch.canResearch(this.research)
        this.add(ImagePane(this.research.icon, ImagePane.DispMode.FIT_TO_PARENT))

        // Create two node listeners that controls the behavior of this research node
        this.addMouseMoveListener{
            if (it.eventType == AOTDMouseMoveEvent.EventType.Enter) {
                // If the node is visible then play the hover sound since we moved our mouse over it
                if (it.source.isVisible && it.source.inBounds) {
                    entityPlayer.playSound(ModSounds.BUTTON_HOVER, 0.7f, 1.9f)
                }
            }
        }
        this.addMouseListener {
            if (it.eventType == AOTDMouseEvent.EventType.Click) {
                if (it.clickedButton == AOTDMouseEvent.LEFT_MOUSE_BUTTON && this.isVisible && this.isHovered && this.inBounds) {
                    if (isCheatSheet) { // Cheat Sheet - If this research can be researched unlock it
                        if (playerResearch.canResearch(research)) {
                            playerResearch.setResearchAndAlert(research, true, entityPlayer)
                            playerResearch.sync(entityPlayer, false)
                            parent.invalidate() // Redraw the screen with new research
                        }
                    } else {    // If this isn't a cheat sheet open the research page
                        if (playerResearch.isResearched(research)) {    // Page UI
                            Minecraft.getInstance().displayGuiScreen(
                                    BloodStainedJournalPageScreen(
                                            I18n.format(research.getUnlocalizedText()),
                                            I18n.format(research.getUnlocalizedName()),
                                            research.researchedRecipes)
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
    }

    override fun drawOverlay() {
        super.drawOverlay()

        // If the button intersects the pane it's in then allow for drawing hover text
        if (this.isVisible && this.inBounds && this.isHovered) {
            val mouseX = AOTDGuiUtility.getMouseXInMCCoord()
            val mouseY = AOTDGuiUtility.getMouseYInMCCoord()

            // If the research is researched show the name of the research when hovered
            if (playerResearch.isResearched(this.research)) {
                fontRenderer.drawString(I18n.format(research.getUnlocalizedName()), mouseX + 5f, mouseY.toFloat(), 0xFF3399)
                fontRenderer.drawString(
                    "${ChatFormatting.ITALIC}${I18n.format(this.research.getUnlocalizedTooltip())}",
                    mouseX + 7f,
                    mouseY + 10f,
                    0xE62E8A
                )
            }

            // If the research can be researched show a ? and unknown research when hovered
            else if (playerResearch.canResearch(this.research)) {
                fontRenderer.drawString("?", mouseX + 5f, mouseY.toFloat(), 0xFF3399)
                fontRenderer.drawString("${ChatFormatting.ITALIC}Unknown Research", mouseX + 7f, mouseY + 10f, 0xE62E8A)
            }
        }
    }
}
