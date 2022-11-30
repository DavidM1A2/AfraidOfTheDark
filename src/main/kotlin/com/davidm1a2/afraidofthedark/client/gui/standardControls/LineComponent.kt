package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.AOTDGuiUtility
import com.davidm1a2.afraidofthedark.client.gui.layout.Gravity
import com.davidm1a2.afraidofthedark.client.gui.layout.Position
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.min
import kotlin.math.sqrt

/**
 * Class representing a line drawn on a GUI. The provided weight will be scaled to match the size of the window.
 */
open class LineComponent(lineFrom: Position, lineTo: Position, offset: Position, private val weight: Double, color: Color, gravity: Gravity = Gravity.CENTER) :
    AOTDGuiComponent(color = color, gravity = gravity, offset = offset) {

    init {
        this.prefSize = lineFrom.dimensionsBetween(lineTo)
    }

    override fun draw(matrixStack: MatrixStack) {
        if (this.isVisible) {
            matrixStack.pushPose()
            RenderSystem.enableBlend()
            RenderSystem.disableTexture()

            val tes = Tessellator.getInstance()
            val bufferBuffer = tes.builder

            bufferBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR)
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

            val x1 = x - (newX / 2)
            val y1 = y - (newY / 2)
            val x2 = x + width - (newX / 2)
            val y2 = y + height - (newY / 2)
            val x3 = x + width + (newX / 2)
            val y3 = y + height + (newY / 2)
            val x4 = x + (newX / 2)
            val y4 = y + (newY / 2)

            bufferBuffer.vertex(x1, y1, 0.0)
                .color(this.color.red / 255f, this.color.green / 255f, this.color.blue / 255f, this.color.alpha / 255f)
                .endVertex()
            bufferBuffer.vertex(x2, y2, 0.0)
                .color(this.color.red / 255f, this.color.green / 255f, this.color.blue / 255f, this.color.alpha / 255f)
                .endVertex()
            bufferBuffer.vertex(x3, y3, 0.0)
                .color(this.color.red / 255f, this.color.green / 255f, this.color.blue / 255f, this.color.alpha / 255f)
                .endVertex()
            bufferBuffer.vertex(x4, y4, 0.0)
                .color(this.color.red / 255f, this.color.green / 255f, this.color.blue / 255f, this.color.alpha / 255f)
                .endVertex()
            tes.end()

            RenderSystem.enableTexture()
            RenderSystem.disableBlend()
            matrixStack.popPose()
        }
    }
}