package com.davidm1a2.afraidofthedark.client.tileEntity.vitaeExtractor

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Quaternion
import net.minecraft.client.model.Model
import net.minecraft.client.renderer.RenderType


/**
 * Model for the vitae extractor
 *
 * @constructor initializes the model parts, this is created from MCAnimator
 * @property parts A map of part name to part
 * @property base The different parts of the model
 */
class VitaeExtractorTileEntityModel internal constructor() : Model(RenderType::entityCutoutNoCull) {
    private val parts = mutableMapOf<String, MCAModelRenderer>()
    private val base: MCAModelRenderer

    init {
        base = MCAModelRenderer(0, 41, 128f, 64f)
        base.addBox(-8.0f, 0.0f, -8.0f, 14f, 7f, 16f)
        base.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        base.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["Base"] = base

        val edge1 = MCAModelRenderer(0, 23, 128f, 64f)
        edge1.addBox(0.0f, 0.0f, 0.0f, 16f, 3f, 4f)
        edge1.setInitialRotationPoint(-8.0f, 7.0f, -8.0f)
        edge1.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["Edge1"] = edge1
        base.addChild(edge1)

        val edge2 = MCAModelRenderer(0, 30, 128f, 64f)
        edge2.addBox(0.0f, 0.0f, 0.0f, 4f, 3f, 8f)
        edge2.setInitialRotationPoint(4.0f, 7.0f, -4.0f)
        edge2.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["Edge2"] = edge2
        base.addChild(edge2)

        val edge3 = MCAModelRenderer(0, 16, 128f, 64f)
        edge3.addBox(0.0f, 0.0f, 0.0f, 16f, 3f, 4f)
        edge3.setInitialRotationPoint(-8.0f, 7.0f, 4.0f)
        edge3.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["Edge3"] = edge3
        base.addChild(edge3)

        val edge4 = MCAModelRenderer(24, 30, 128f, 64f)
        edge4.addBox(0.0f, 0.0f, 0.0f, 4f, 3f, 8f)
        edge4.setInitialRotationPoint(-8.0f, 7.0f, -4.0f)
        edge4.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["Edge4"] = edge4
        base.addChild(edge4)

        val openingBottom = MCAModelRenderer(40, 13, 128f, 64f)
        openingBottom.addBox(0.0f, 0.0f, 0.0f, 2f, 1f, 16f)
        openingBottom.setInitialRotationPoint(6.0f, 0.0f, -8.0f)
        openingBottom.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["OpeningBottom"] = openingBottom
        base.addChild(openingBottom)

        val openingSide1 = MCAModelRenderer(12, 5, 128f, 64f)
        openingSide1.addBox(0.0f, 0.0f, 0.0f, 2f, 6f, 4f)
        openingSide1.setInitialRotationPoint(6.0f, 1.0f, 4.0f)
        openingSide1.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["OpeningSide1"] = openingSide1
        base.addChild(openingSide1)

        val openingSide2 = MCAModelRenderer(0, 5, 128f, 64f)
        openingSide2.addBox(0.0f, 0.0f, 0.0f, 2f, 6f, 4f)
        openingSide2.setInitialRotationPoint(6.0f, 1.0f, -8.0f)
        openingSide2.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["OpeningSide2"] = openingSide2
        base.addChild(openingSide2)

        val openingCorner1 = MCAModelRenderer(8, 1, 128f, 64f)
        openingCorner1.addBox(0.0f, 0.0f, 0.0f, 2f, 2f, 2f)
        openingCorner1.setInitialRotationPoint(6.0f, 5.0f, 2.0f)
        openingCorner1.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["OpeningCorner1"] = openingCorner1
        base.addChild(openingCorner1)

        val openingCorner2 = MCAModelRenderer(0, 1, 128f, 64f)
        openingCorner2.addBox(0.0f, 0.0f, 0.0f, 2f, 2f, 2f)
        openingCorner2.setInitialRotationPoint(6.0f, 5.0f, -4.0f)
        openingCorner2.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["OpeningCorner2"] = openingCorner2
        base.addChild(openingCorner2)
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