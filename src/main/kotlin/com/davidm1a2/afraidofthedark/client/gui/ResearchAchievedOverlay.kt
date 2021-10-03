package com.davidm1a2.afraidofthedark.client.gui

import com.davidm1a2.afraidofthedark.client.sound.ResearchUnlockedSound
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.research.Research
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.AbstractGui
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import java.util.LinkedList

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
class ResearchAchievedOverlay : AbstractGui() {
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
        toDisplay.push(research)
    }

    /**
     * Function provided by MC's achievement window to setup the viewport, copied and unmodified
     */
    private fun updateResearchAchievedWindowScale() {
        RenderSystem.viewport(0, 0, mc.window.width, mc.window.height)
        RenderSystem.matrixMode(5889)
        RenderSystem.loadIdentity()
        RenderSystem.matrixMode(5888)
        RenderSystem.loadIdentity()
        width = mc.window.guiScaledWidth
        height = mc.window.guiScaledHeight
        RenderSystem.clear(GL11.GL_DEPTH_BUFFER_BIT, Minecraft.ON_OSX)
        RenderSystem.matrixMode(5889)
        RenderSystem.loadIdentity()
        RenderSystem.ortho(0.0, width.toDouble(), height.toDouble(), 0.0, 1000.0, 3000.0)
        RenderSystem.matrixMode(5888)
        RenderSystem.loadIdentity()
        RenderSystem.translatef(0.0f, 0.0f, -2000.0f)
    }

    /**
     * Function provided by MC's achievement window to setup the window, copied and slightly modified to work with a queue of researches to show
     */
    fun updateResearchAchievedWindow(matrixStack: MatrixStack) {
        // If there is no notification showing and the queue has another research to display display the next research
        if (notificationTime == 0L && !toDisplay.isEmpty()) {
            // The research to display
            val research = toDisplay.pop()
            // The new research description
            researchDescription = research.getName().string
            // Update the notification time to be the current system time
            notificationTime = System.currentTimeMillis()
            // Play the achievement sound and display the research
            Minecraft.getInstance().soundManager.play(ResearchUnlockedSound())
        }

        if (notificationTime != 0L && Minecraft.getInstance().player != null) {
            val percentFinished = (System.currentTimeMillis() - notificationTime) / 6000.0
            if (percentFinished < 0.0 || percentFinished > 1.0) {
                notificationTime = 0L
                return
            }

            updateResearchAchievedWindowScale()
            RenderSystem.disableDepthTest()
            RenderSystem.depthMask(false)

            var currentOffset = percentFinished * 2.0
            if (currentOffset > 1.0) {
                currentOffset = 2.0 - currentOffset
            }
            currentOffset *= 4.0
            currentOffset = 1.0 - currentOffset
            if (currentOffset < 0.0) {
                currentOffset = 0.0
            }
            currentOffset = currentOffset * currentOffset * currentOffset * currentOffset

            val x = width - 160
            val y = 0 - (currentOffset * 36.0).toInt()

            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f)
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            mc.textureManager.bind(ACHIEVEMENT_BACKGROUND)
            RenderSystem.disableLighting()
            blit(matrixStack, x, y, 0f, 0f, 160, 32, 160, 32)
            mc.font.draw(matrixStack, I18n.get("researchbanner.title"), x + 10f, y + 5f, -256)
            mc.font.draw(matrixStack, researchDescription!!, x + 10f, y + 18f, -1)
            RenderHelper.setupFor3DItems()
            RenderSystem.disableLighting()
            RenderSystem.enableRescaleNormal()
            RenderSystem.enableColorMaterial()
            RenderSystem.enableLighting()
            RenderSystem.disableLighting()
            RenderSystem.depthMask(true)
            RenderSystem.enableDepthTest()
        }
    }

    /**
     * Clears any researches in the queue to be displayed
     */
    fun clearResearches() {
        toDisplay.clear()
        notificationTime = 0L
    }

    companion object {
        // The texture of the achievement background
        private val ACHIEVEMENT_BACKGROUND = ResourceLocation(Constants.MOD_ID, "textures/gui/research_achieved.png")
    }
}