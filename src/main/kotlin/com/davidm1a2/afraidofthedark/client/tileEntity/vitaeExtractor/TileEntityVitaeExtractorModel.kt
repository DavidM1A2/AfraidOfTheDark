package com.davidm1a2.afraidofthedark.client.tileEntity.vitaeExtractor

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.model.Model
import net.minecraft.util.math.vector.Quaternion


/**
 * Model for the vitae extractor
 *
 * @constructor initializes the model parts, this is created from MCAnimator
 * @property parts A map of part name to part
 * @property base The different parts of the model
 */
class TileEntityVitaeExtractorModel internal constructor() : Model(RenderType::entityCutoutNoCull) {
    private val parts = mutableMapOf<String, MCAModelRenderer>()
    private val base: MCAModelRenderer

    init {
        texWidth = 128
        texHeight = 64

        base = MCAModelRenderer(this, 0, 41)
        base.mirror = false
        base.addBox(-8.0f, 0.0f, -8.0f, 14f, 7f, 16f)
        base.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        base.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        base.setTexSize(128, 64)
        parts["Base"] = base

        val edge1 = MCAModelRenderer(this, 0, 23)
        edge1.mirror = false
        edge1.addBox(0.0f, 0.0f, 0.0f, 16f, 3f, 4f)
        edge1.setInitialRotationPoint(-8.0f, 7.0f, -8.0f)
        edge1.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        edge1.setTexSize(128, 64)
        parts["Edge1"] = edge1
        base.addChild(edge1)

        val edge2 = MCAModelRenderer(this, 0, 30)
        edge2.mirror = false
        edge2.addBox(0.0f, 0.0f, 0.0f, 4f, 3f, 8f)
        edge2.setInitialRotationPoint(4.0f, 7.0f, -4.0f)
        edge2.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        edge2.setTexSize(128, 64)
        parts["Edge2"] = edge2
        base.addChild(edge2)

        val edge3 = MCAModelRenderer(this, 0, 16)
        edge3.mirror = false
        edge3.addBox(0.0f, 0.0f, 0.0f, 16f, 3f, 4f)
        edge3.setInitialRotationPoint(-8.0f, 7.0f, 4.0f)
        edge3.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        edge3.setTexSize(128, 64)
        parts["Edge3"] = edge3
        base.addChild(edge3)

        val edge4 = MCAModelRenderer(this, 24, 30)
        edge4.mirror = false
        edge4.addBox(0.0f, 0.0f, 0.0f, 4f, 3f, 8f)
        edge4.setInitialRotationPoint(-8.0f, 7.0f, -4.0f)
        edge4.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        edge4.setTexSize(128, 64)
        parts["Edge4"] = edge4
        base.addChild(edge4)

        val openingBottom = MCAModelRenderer(this, 40, 13)
        openingBottom.mirror = false
        openingBottom.addBox(0.0f, 0.0f, 0.0f, 2f, 1f, 16f)
        openingBottom.setInitialRotationPoint(6.0f, 0.0f, -8.0f)
        openingBottom.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        openingBottom.setTexSize(128, 64)
        parts["OpeningBottom"] = openingBottom
        base.addChild(openingBottom)

        val openingSide1 = MCAModelRenderer(this, 12, 5)
        openingSide1.mirror = false
        openingSide1.addBox(0.0f, 0.0f, 0.0f, 2f, 7f, 4f)
        openingSide1.setInitialRotationPoint(6.0f, 1.0f, 4.0f)
        openingSide1.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        openingSide1.setTexSize(128, 64)
        parts["OpeningSide1"] = openingSide1
        base.addChild(openingSide1)

        val openingSide2 = MCAModelRenderer(this, 0, 5)
        openingSide2.mirror = false
        openingSide2.addBox(0.0f, 0.0f, 0.0f, 2f, 7f, 4f)
        openingSide2.setInitialRotationPoint(6.0f, 1.0f, -8.0f)
        openingSide2.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        openingSide2.setTexSize(128, 64)
        parts["OpeningSide2"] = openingSide2
        base.addChild(openingSide2)

        val openingCorner1 = MCAModelRenderer(this, 8, 1)
        openingCorner1.mirror = false
        openingCorner1.addBox(0.0f, 0.0f, 0.0f, 2f, 2f, 2f)
        openingCorner1.setInitialRotationPoint(6.0f, 5.0f, 2.0f)
        openingCorner1.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        openingCorner1.setTexSize(128, 64)
        parts["OpeningCorner1"] = openingCorner1
        base.addChild(openingCorner1)

        val openingCorner2 = MCAModelRenderer(this, 0, 1)
        openingCorner2.mirror = false
        openingCorner2.addBox(0.0f, 0.0f, 0.0f, 2f, 2f, 2f)
        openingCorner2.setInitialRotationPoint(6.0f, 5.0f, -4.0f)
        openingCorner2.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        openingCorner2.setTexSize(128, 64)
        parts["OpeningCorner2"] = openingCorner2
        base.addChild(openingCorner2)
    }

    override fun renderToBuffer(
        matrixStack: MatrixStack,
        vertexBuilder: IVertexBuilder,
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