package com.davidm1a2.afraidofthedark.client.tileEntity.vitaeExtractor

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.model.Model
import net.minecraft.util.math.vector.Quaternion


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
        texWidth = 64
        texHeight = 32

        base = MCAModelRenderer(this, 0, 9)
        base.mirror = false
        base.addBox(-3.0f, 0.0f, -3.0f, 6f, 1f, 6f)
        base.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        base.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        base.setTexSize(64, 32)
        parts["Base"] = base

        val edge1 = MCAModelRenderer(this, 12, 0)
        edge1.mirror = false
        edge1.addBox(0.0f, 0.0f, 0.0f, 1f, 5f, 1f)
        edge1.setInitialRotationPoint(-3.0f, 1.0f, -3.0f)
        edge1.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        edge1.setTexSize(64, 32)
        parts["Edge1"] = edge1
        base.addChild(edge1)

        val edge2 = MCAModelRenderer(this, 4, 0)
        edge2.mirror = false
        edge2.addBox(0.0f, 0.0f, 0.0f, 1f, 5f, 1f)
        edge2.setInitialRotationPoint(2.0f, 1.0f, -3.0f)
        edge2.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        edge2.setTexSize(64, 32)
        parts["Edge2"] = edge2
        base.addChild(edge2)

        val edge3 = MCAModelRenderer(this, 8, 0)
        edge3.mirror = false
        edge3.addBox(0.0f, 0.0f, 0.0f, 1f, 5f, 1f)
        edge3.setInitialRotationPoint(2.0f, 1.0f, 2.0f)
        edge3.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        edge3.setTexSize(64, 32)
        parts["Edge3"] = edge3
        base.addChild(edge3)

        val edge4 = MCAModelRenderer(this, 0, 0)
        edge4.mirror = false
        edge4.addBox(0.0f, 0.0f, 0.0f, 1f, 5f, 1f)
        edge4.setInitialRotationPoint(-3.0f, 1.0f, 2.0f)
        edge4.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        edge4.setTexSize(64, 32)
        parts["Edge4"] = edge4
        base.addChild(edge4)

        val upper = MCAModelRenderer(this, 0, 16)
        upper.mirror = false
        upper.addBox(0.0f, 0.0f, 0.0f, 6f, 1f, 6f)
        upper.setInitialRotationPoint(-3.0f, 6.0f, -3.0f)
        upper.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        upper.setTexSize(64, 32)
        parts["Upper"] = upper
        base.addChild(upper)

        val lid = MCAModelRenderer(this, 16, 26)
        lid.mirror = false
        lid.addBox(0.0f, 0.0f, 0.0f, 4f, 1f, 4f)
        lid.setInitialRotationPoint(-2.0f, 7.0f, -2.0f)
        lid.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        lid.setTexSize(64, 32)
        parts["Lid"] = lid
        base.addChild(lid)

        val contents = MCAModelRenderer(this, 0, 23)
        contents.mirror = false
        contents.addBox(-2.0f, 0.0f, -2.0f, 4f, 5f, 4f)
        contents.setInitialRotationPoint(0.0f, 1.0f, 0.0f)
        contents.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        contents.setTexSize(64, 32)
        parts["Contents"] = contents
        base.addChild(contents)
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