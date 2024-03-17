package com.davidm1a2.afraidofthedark.client.tileEntity.vitaeExtractor

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Quaternion
import net.minecraft.client.model.Model
import net.minecraft.client.renderer.RenderType


/**
 * Model for the vitae lantern
 *
 * @constructor initializes the model parts, this is created from MCAnimator
 * @property parts A map of part name to part
 * @property base The different parts of the model
 */
class VitaeLanternModel internal constructor() : Model(RenderType::entityTranslucent) {
    private val parts = mutableMapOf<String, MCAModelRenderer>()
    private val base: MCAModelRenderer

    init {
        base = MCAModelRenderer(0, 9, 64f, 32f)
        base.addBox(-3.0f, 0.0f, -3.0f, 6f, 1f, 6f)
        base.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        base.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["Base"] = base

        val edge1 = MCAModelRenderer(12, 0, 64f, 32f)
        edge1.addBox(0.0f, 0.0f, 0.0f, 1f, 5f, 1f)
        edge1.setInitialRotationPoint(-3.0f, 1.0f, -3.0f)
        edge1.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["Edge1"] = edge1
        base.addChild(edge1)

        val edge2 = MCAModelRenderer(4, 0, 64f, 32f)
        edge2.addBox(0.0f, 0.0f, 0.0f, 1f, 5f, 1f)
        edge2.setInitialRotationPoint(2.0f, 1.0f, -3.0f)
        edge2.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["Edge2"] = edge2
        base.addChild(edge2)

        val edge3 = MCAModelRenderer(8, 0, 64f, 32f)
        edge3.addBox(0.0f, 0.0f, 0.0f, 1f, 5f, 1f)
        edge3.setInitialRotationPoint(2.0f, 1.0f, 2.0f)
        edge3.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["Edge3"] = edge3
        base.addChild(edge3)

        val edge4 = MCAModelRenderer(0, 0, 64f, 32f)
        edge4.addBox(0.0f, 0.0f, 0.0f, 1f, 5f, 1f)
        edge4.setInitialRotationPoint(-3.0f, 1.0f, 2.0f)
        edge4.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["Edge4"] = edge4
        base.addChild(edge4)

        val upper = MCAModelRenderer(0, 16, 64f, 32f)
        upper.addBox(0.0f, 0.0f, 0.0f, 6f, 1f, 6f)
        upper.setInitialRotationPoint(-3.0f, 6.0f, -3.0f)
        upper.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["Upper"] = upper
        base.addChild(upper)

        val lid = MCAModelRenderer(16, 26, 64f, 32f)
        lid.addBox(0.0f, 0.0f, 0.0f, 4f, 1f, 4f)
        lid.setInitialRotationPoint(-2.0f, 7.0f, -2.0f)
        lid.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["Lid"] = lid
        base.addChild(lid)

        val contents = MCAModelRenderer(0, 23, 64f, 32f)
        contents.addBox(-2.0f, 0.0f, -2.0f, 4f, 5f, 4f)
        contents.setInitialRotationPoint(0.0f, 1.0f, 0.0f)
        contents.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["Contents"] = contents
        base.addChild(contents)
    }

    override fun renderToBuffer(
        matrixStack: PoseStack,
        vertexBuilder: VertexConsumer,
        packedLight: Int,
        packedOverlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        base.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha)
    }
}