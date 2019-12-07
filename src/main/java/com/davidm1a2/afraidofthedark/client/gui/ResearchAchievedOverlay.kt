package com.davidm1a2.afraidofthedark.client.gui

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.registry.research.Research
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation
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
class ResearchAchievedOverlay : Gui()
{
    private val mc = Minecraft.getMinecraft()
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
    fun displayResearch(research: Research)
    {
        toDisplay.push(research)
    }

    /**
     * Function provided by MC's achievement window to setup the viewport, copied and unmodified
     */
    private fun updateResearchAchievedWindowScale()
    {
        GlStateManager.viewport(0, 0, mc.displayWidth, mc.displayHeight)
        GlStateManager.matrixMode(5889)
        GlStateManager.loadIdentity()
        GlStateManager.matrixMode(5888)
        GlStateManager.loadIdentity()
        val scaledresolution = ScaledResolution(mc)
        width = scaledresolution.scaledWidth
        height = scaledresolution.scaledHeight
        GlStateManager.clear(256)
        GlStateManager.matrixMode(5889)
        GlStateManager.loadIdentity()
        GlStateManager.ortho(0.0, width.toDouble(), height.toDouble(), 0.0, 1000.0, 3000.0)
        GlStateManager.matrixMode(5888)
        GlStateManager.loadIdentity()
        GlStateManager.translate(0.0f, 0.0f, -2000.0f)
    }

    /**
     * Function provided by MC's achievement window to setup the window, copied and slightly modified to work with a queue of researches to show
     */
    fun updateResearchAchievedWindow()
    {
        // If there is no notification showing and the queue has another research to display display the next research
        if (notificationTime == 0L && !toDisplay.isEmpty())
        {
            // The research to display
            val research = toDisplay.pop()
            // The new research description
            researchDescription = I18n.format(research.getUnlocalizedName())
            // Update the notification time to be the current system time
            notificationTime = Minecraft.getSystemTime()
        }
        // After this everything is copied from the default MC achievement class
        if (notificationTime != 0L && Minecraft.getMinecraft().player != null)
        {
            val d0 = (Minecraft.getSystemTime() - notificationTime) / 3000.0
            if (d0 < 0.0 || d0 > 1.0)
            {
                notificationTime = 0L
                return
            }
            updateResearchAchievedWindowScale()
            GlStateManager.disableDepth()
            GlStateManager.depthMask(false)
            var d1 = d0 * 2.0
            if (d1 > 1.0)
            {
                d1 = 2.0 - d1
            }
            d1 *= 4.0
            d1 = 1.0 - d1
            if (d1 < 0.0)
            {
                d1 = 0.0
            }
            d1 = d1 * d1 * d1 * d1
            val i = width - 160
            val j = 0 - (d1 * 36.0).toInt()
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
            GlStateManager.enableTexture2D()
            mc.textureManager.bindTexture(ACHIEVEMENT_BACKGROUND)
            GlStateManager.disableLighting()
            this.drawTexturedModalRect(i, j, 96, 202, 160, 32)
            mc.fontRenderer.drawString(ACHIEVEMENT_TITLE, i + 10, j + 5, -256)
            mc.fontRenderer.drawString(researchDescription!!, i + 10, j + 18, -1)
            RenderHelper.enableGUIStandardItemLighting()
            GlStateManager.disableLighting()
            GlStateManager.enableRescaleNormal()
            GlStateManager.enableColorMaterial()
            GlStateManager.enableLighting()
            GlStateManager.disableLighting()
            GlStateManager.depthMask(true)
            GlStateManager.enableDepth()
        }
    }

    /**
     * Clears any researches in the queue to be displayed
     */
    fun clearResearches()
    {
        toDisplay.clear()
        notificationTime = 0L
    }

    companion object
    {
        // The texture of the achievement background
        private val ACHIEVEMENT_BACKGROUND = ResourceLocation(Constants.MOD_ID, "textures/gui/research_achieved.png")
        // The title of the research achieved window
        private const val ACHIEVEMENT_TITLE = "New Research!"
    }
}