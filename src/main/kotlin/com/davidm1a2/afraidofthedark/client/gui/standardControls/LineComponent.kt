package com.davidm1a2.afraidofthedark.client.gui.standardControls

import com.davidm1a2.afraidofthedark.client.gui.base.*
import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import java.awt.Color
import kotlin.math.sqrt

open class LineComponent(private val lineFrom: Position<Double>, private val lineTo: Position<Double>, private val weight: Double, color: Color, gravity: AOTDGuiGravity = AOTDGuiGravity.CENTER):
        AOTDGuiComponent(color = color, gravity = gravity) {

    init {
        this.prefSize = if (lineFrom is AbsolutePosition && lineTo is AbsolutePosition) lineTo.sub(lineFrom).toDimensions()
            else if (lineFrom is RelativePosition && lineTo is RelativePosition) lineTo.sub(lineFrom).toDimensions()
            else Dimensions(0.0, 0.0)
        this.offset = if (lineFrom is AbsolutePosition && lineTo is AbsolutePosition) lineTo.avg(lineFrom)
            else if (lineFrom is RelativePosition && lineTo is RelativePosition) lineTo.avg(lineFrom)
            else Position(0.0, 0.0)
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

            if (mag != 0.0) {
                newX = deltaY * weight
                newY = -deltaX * weight
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