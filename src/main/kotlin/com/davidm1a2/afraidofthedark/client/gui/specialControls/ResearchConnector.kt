package com.davidm1a2.afraidofthedark.client.gui.specialControls

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiComponent
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiContainer
import com.davidm1a2.afraidofthedark.client.gui.base.AOTDGuiGravity
import com.davidm1a2.afraidofthedark.client.gui.base.Position
import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.platform.GlStateManager.DestFactor
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import java.awt.Color
import kotlin.math.roundToInt
import kotlin.math.sqrt

class ResearchConnector(offset: Position<Int>, val toOffset: Position<Int>): AOTDGuiContainer(0, 0, offset.x, offset.y, gravity = AOTDGuiGravity.CENTER) {
    init {
        this.color = Color.CYAN
    }

    override fun draw() {
        if (this.isVisible) {
            println("test")

            GlStateManager.pushMatrix()
            GlStateManager.enableBlend()
            GlStateManager.disableTexture()
            GlStateManager.color4f(
                    this.color.red / 255f,
                    this.color.green / 255f,
                    this.color.blue / 255f,
                    this.color.alpha / 255f
            )

            val tessellator = Tessellator.getInstance()
            val bufferBuffer = tessellator.buffer

            bufferBuffer.begin(7, DefaultVertexFormats.POSITION)
            val deltaX = toOffset.x.toDouble()
            val deltaY = toOffset.y.toDouble()
            val mag = sqrt(deltaX * deltaX + deltaY * deltaY)

            val newX = deltaY / mag * CONNECTOR_WIDTH
            val newY = -deltaX / mag * CONNECTOR_WIDTH

            val x1 = x
            val y1 = y
            val x2 = x + toOffset.x
            val y2 = y + toOffset.y
            val x3 = x + toOffset.x + newX.roundToInt()
            val y3 = y + toOffset.y + newY.roundToInt()
            val x4 = x + newX.roundToInt()
            val y4 = y + newY.roundToInt()

            bufferBuffer.pos(x1.toDouble(), y1.toDouble(), 0.0).endVertex()
            bufferBuffer.pos(x2.toDouble(), y2.toDouble(), 0.0).endVertex()
            bufferBuffer.pos(x3.toDouble(), y3.toDouble(), 0.0).endVertex()
            bufferBuffer.pos(x4.toDouble(), y4.toDouble(), 0.0).endVertex()
            tessellator.draw()

            GlStateManager.enableTexture()
            GlStateManager.disableBlend()
            GlStateManager.popMatrix()
        }
    }

    companion object {
        const val CONNECTOR_WIDTH = 4.0
    }
}