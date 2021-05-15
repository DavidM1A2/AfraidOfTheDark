package com.davidm1a2.afraidofthedark.client.gui.specialControls

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiGravity
import com.davidm1a2.afraidofthedark.client.gui.base.Dimensions
import com.davidm1a2.afraidofthedark.client.gui.base.Position
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiButton
import com.davidm1a2.afraidofthedark.client.gui.standardControls.ImagePane
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.registry.research.Research
import com.mojang.realmsclient.gui.ChatFormatting
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation

/**
 * Button that represents a research in the research GUI
 *
 * @constructor initializes the button based on the selected research
 * @param x        The x coordinate of the button
 * @param y        The y coordinate of the button
 * @param research The research that this button represents
 */
class AOTDGuiResearchNodeButton(prefSize: Dimensions<Double>, offset: Position<Double>, val research: Research) : AOTDGuiButton(
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
    }

    /**
     * Called to draw the name of the research when the button is hovered
     */
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

    companion object {
        // Icon used by buttons that have unknown icons
        private val UNKNOWN_RESEARCH = ResourceLocation("afraidofthedark:textures/gui/research_icons/question_mark.png")
    }
}
