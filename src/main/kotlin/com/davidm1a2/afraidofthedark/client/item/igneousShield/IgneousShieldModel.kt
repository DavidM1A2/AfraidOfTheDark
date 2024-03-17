package com.davidm1a2.afraidofthedark.client.item.igneousShield

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.model.Model
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.renderer.RenderType


class IgneousShieldModel : Model(RenderType::entitySolid) {
    private val bbMain: ModelPart
    private val handleR1: ModelPart
    private val rightEdgeR1: ModelPart
    private val leftEdgeR1: ModelPart
    private val rightCenterR1: ModelPart
    private val leftCenterR1: ModelPart

    init {
        handleR1 = ModelPart(listOf(makeDefaultCube(0, 0, -3.0f, -2.0f, 1.0f, 6.0f, 4.0f, 6.0f)), emptyMap())
        handleR1.setPos(0.0f, -24.0f, -2.0f)
        setRotationAngle(handleR1, 0.0f, 0.0f, 1.5708f)
        rightEdgeR1 = ModelPart(listOf(makeDefaultCube(8, 10, -4.0f, -10.0f, 0.0f, 2.0f, 20.0f, 1.0f)), emptyMap())
        rightEdgeR1.setPos(0.0f, -24.0f, -2.0f)
        setRotationAngle(rightEdgeR1, 0.0f, 0.0f, -0.0873f)
        leftEdgeR1 = ModelPart(listOf(makeDefaultCube(14, 10, 2.0f, -10.0f, 0.0f, 2.0f, 20.0f, 1.0f)), emptyMap())
        leftEdgeR1.setPos(0.0f, -24.0f, -2.0f)
        setRotationAngle(leftEdgeR1, 0.0f, 0.0f, 0.0873f)
        rightCenterR1 = ModelPart(listOf(makeDefaultCube(20, 10, -3.0f, -9.0f, -0.5f, 3.0f, 18.0f, 1.0f)), emptyMap())
        rightCenterR1.setPos(0.0f, -24.0f, -2.0f)
        setRotationAngle(rightCenterR1, 0.0f, 0.0f, -0.0436f)
        leftCenterR1 = ModelPart(listOf(makeDefaultCube(28, 0, 0.0f, -9.0f, -0.5f, 3.0f, 18.0f, 1.0f)), emptyMap())
        leftCenterR1.setPos(0.0f, -24.0f, -2.0f)
        setRotationAngle(leftCenterR1, 0.0f, 0.0f, 0.0436f)

        bbMain = ModelPart(
            listOf(makeDefaultCube(0, 10, -1.0f, -34.0f, -3.0f, 2.0f, 20.0f, 2.0f)), mapOf(
                "handleR1" to handleR1,
                "rightEdgeR1" to rightEdgeR1,
                "leftEdgeR1" to leftEdgeR1,
                "rightCenterR1" to rightCenterR1,
                "leftCenterR1" to leftCenterR1
            )
        )
    }

    private fun makeDefaultCube(texOffsX: Int, texOffsY: Int, x: Float, y: Float, z: Float, width: Float, height: Float, depth: Float): ModelPart.Cube {
        return ModelPart.Cube(texOffsX, texOffsY, x, y, z, width, height, depth, 0f, 0f, 0f, false, 64f, 64f)
    }

    override fun renderToBuffer(
        poseStack: PoseStack,
        buffer: VertexConsumer,
        packedLight: Int,
        packedOverlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        bbMain.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha)
    }

    private fun setRotationAngle(modelPart: ModelPart, x: Float, y: Float, z: Float) {
        modelPart.xRot = x
        modelPart.yRot = y
        modelPart.zRot = z
    }
}