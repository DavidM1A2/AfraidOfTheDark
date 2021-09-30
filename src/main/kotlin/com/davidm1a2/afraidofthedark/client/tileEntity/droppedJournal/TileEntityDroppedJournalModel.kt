package com.davidm1a2.afraidofthedark.client.tileEntity.droppedJournal

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.model.Model
import net.minecraft.util.math.vector.Quaternion


class TileEntityDroppedJournalModel internal constructor() : Model(RenderType::entityCutoutNoCull) {
    private val parts = mutableMapOf<String, MCAModelRenderer>()
    private val pages: MCAModelRenderer

    init {
        texWidth = 256
        texHeight = 128

        pages = MCAModelRenderer(this, 0, 0)
        pages.mirror = false
        pages.addBox(-10.0f, -3.0f, -16.0f, 20f, 5f, 32f)
        pages.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        pages.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        pages.setTexSize(256, 128)
        parts["Pages"] = pages

        val frontCover = MCAModelRenderer(this, 0, 93)
        frontCover.mirror = false
        frontCover.addBox(0.0f, 0.0f, 0.0f, 22f, 1f, 34f)
        frontCover.setInitialRotationPoint(-12.0f, 2.0f, -17.0f)
        frontCover.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        frontCover.setTexSize(256, 128)
        parts["FrontCover"] = frontCover
        pages.addChild(frontCover)

        val backCover = MCAModelRenderer(this, 113, 93)
        backCover.mirror = false
        backCover.addBox(0.0f, 0.0f, 0.0f, 22f, 1f, 34f)
        backCover.setInitialRotationPoint(-12.0f, -4.0f, -17.0f)
        backCover.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        backCover.setTexSize(256, 128)
        parts["BackCover"] = backCover
        pages.addChild(backCover)

        val spine = MCAModelRenderer(this, 105, 0)
        spine.mirror = false
        spine.addBox(0.0f, -4.0f, 0.0f, 1f, 7f, 34f)
        spine.setInitialRotationPoint(10.0f, 0.0f, -17.0f)
        spine.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        spine.setTexSize(256, 128)
        parts["Spine"] = spine
        pages.addChild(spine)
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
        pages.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha)
    }
}

