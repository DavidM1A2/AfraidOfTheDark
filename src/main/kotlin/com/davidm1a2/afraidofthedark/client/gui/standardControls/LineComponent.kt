package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility
import com.davidm1a2.afraidofthedark.client.gui.layout.*
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import java.awt.Color
import kotlin.math.min
import kotlin.math.sqrt

/**
 * Class representing a line drawn on a GUI. The provided weight will be scaled to match the size of the window.
 */
open class LineComponent(lineFrom: Position, lineTo: Position, offset: Position, private val weight: Double, color: Color, gravity: Gravity = Gravity.CENTER):
        AOTDGuiComponent(color = color, gravity = gravity, offset = offset) {

    init {
        this.prefSize = lineFrom.toDimension(lineTo)
    }

    override fun draw() {
        if (this.isVisible) {
            GlStateManager.pushMatrix()
            GlStateManager.enableBlend()
            GlStateManager.disableTexture()
            GlStateManager.color4f(
                    this.color.red / 255f,
                    this.color.green / 255f,
                    this.color.blue / 255f,
                    this.color.alpha / 255f
            )

            val tes = Tessellator.getInstance()
            val bufferBuffer = tes.buffer

            bufferBuffer.begin(7, DefaultVertexFormats.POSITION)
            val deltaX = width.toDouble()
            val deltaY = height.toDouble()
            val mag = sqrt(deltaX * deltaX + deltaY * deltaY)

            var newX = 0.0
            var newY = 0.0

            val size = min(AOTDGuiUtility.getWindowWidthInMCCoords(), AOTDGuiUtility.getWindowHeightInMCCoords())

            if (mag != 0.0) {
                newX = deltaY / mag * weight * (size / Constants.REFERENCE_SIZE)
                newY = -deltaX / mag * weight * (size / Constants.REFERENCE_SIZE)
            }

            val x1 = x - (newX/2)
            val y1 = y - (newY/2)
            val x2 = x + width - (newX/2)
            val y2 = y + height - (newY/2)
            val x3 = x + width + (newX/2)
            val y3 = y + height + (newY/2)
            val x4 = x + (newX/2)
            val y4 = y + (newY/2)

            bufferBuffer.pos(x1, y1, 0.0).endVertex()
            bufferBuffer.pos(x2, y2, 0.0).endVertex()
            bufferBuffer.pos(x3, y3, 0.0).endVertex()
            bufferBuffer.pos(x4, y4, 0.0).endVertex()
            tes.draw()

            GlStateManager.enableTexture()
            GlStateManager.disableBlend()
            GlStateManager.popMatrix()
        }
    }
}