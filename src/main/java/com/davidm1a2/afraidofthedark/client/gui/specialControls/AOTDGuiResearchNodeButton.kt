package com.davidm1a2.afraidofthedark.client.gui.specialControls

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility
import com.davidm1a2.afraidofthedark.client.gui.standardControls.AOTDGuiButton
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.registry.research.Research
import com.mojang.realmsclient.gui.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
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
class AOTDGuiResearchNodeButton(x: Int, y: Int, val research: Research) : AOTDGuiButton(
        x,
        y,
        32,
        32,
        "afraidofthedark:textures/gui/journal_tech_tree/research_background.png",
        "afraidofthedark:textures/gui/journal_tech_tree/research_background_hovered.png"
)
{
    // The player's research for fast querying
    private val playerResearch = entityPlayer.getResearch()

    init
    {
        // Make the button visible if the research is either researched or can be researched show it
        this.isVisible = playerResearch.isResearched(this.research) || playerResearch.canResearch(this.research)
    }

    /**
     * Draws the node button
     */
    override fun draw()
    {
        if (this.isVisible)
        {
            super.draw()

            // Ensure our color is white to draw with
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
            GlStateManager.enableBlend()

            // Draw the researches icon on the button
            Minecraft.getMinecraft().textureManager.bindTexture(this.research.icon)
            Gui.drawScaledCustomSizeModalRect(this.getXScaled(), this.getYScaled(), 0f, 0f, 64, 64, this.getWidthScaled(), this.getHeightScaled(), 64f, 64f)

            // If the player has not researched the research then show the question mark over top
            if (!playerResearch.isResearched(this.research))
            {
                Minecraft.getMinecraft().textureManager.bindTexture(UNKNOWN_RESEARCH)
                Gui.drawScaledCustomSizeModalRect(this.getXScaled(), this.getYScaled(), 0f, 0f, 32, 32, this.getWidthScaled(), this.getHeightScaled(), 32f, 32f)
            }

            GlStateManager.disableBlend()
        }
    }

    /**
     * Called to draw the name of the research when the button is hovered
     */
    override fun drawOverlay()
    {
        super.drawOverlay()

        // If the button intersects the pane it's in then allow for drawing hover text
        if (this.parent!!.parent!!.intersects(this) && this.isHovered)
        {
            val mouseX = AOTDGuiUtility.getMouseXInMCCoord()
            val mouseY = AOTDGuiUtility.getMouseYInMCCoord()

            // If the research is researched show the name of the research when hovered
            if (playerResearch.isResearched(this.research))
            {
                fontRenderer.drawString(I18n.format(research.getUnlocalizedName()), mouseX + 5, mouseY, 0xFF3399)
                fontRenderer.drawString("${ChatFormatting.ITALIC}${I18n.format(this.research.getUnlocalizedTooltip())}", mouseX + 7, mouseY + 10, 0xE62E8A)
            }
            // If the research can be researched show a ? and unknown research when hovered
            else if (playerResearch.canResearch(this.research))
            {
                fontRenderer.drawString("?", mouseX + 5, mouseY, 0xFF3399)
                fontRenderer.drawString("${ChatFormatting.ITALIC}Unknown Research", mouseX + 7, mouseY + 10, 0xE62E8A)
            }
        }
    }

    companion object
    {
        // Icon used by buttons that have unknown icons
        private val UNKNOWN_RESEARCH = ResourceLocation("afraidofthedark:textures/gui/research_icons/question_mark.png")
    }
}
