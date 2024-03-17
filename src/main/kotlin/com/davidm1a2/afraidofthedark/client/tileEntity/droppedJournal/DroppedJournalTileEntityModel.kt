package com.davidm1a2.afraidofthedark.client.tileEntity.droppedJournal

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Quaternion
import net.minecraft.client.model.Model
import net.minecraft.client.renderer.RenderType


class DroppedJournalTileEntityModel internal constructor() : Model(RenderType::entityCutoutNoCull) {
    private val parts = mutableMapOf<String, MCAModelRenderer>()
    private val pages: MCAModelRenderer

    init {
        pages = MCAModelRenderer(0, 0, 256f, 128f)
        pages.addBox(-10.0f, -3.0f, -16.0f, 20f, 5f, 32f)
        pages.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        pages.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["Pages"] = pages

        val frontCover = MCAModelRenderer(0, 93, 256f, 128f)
        frontCover.addBox(0.0f, 0.0f, 0.0f, 22f, 1f, 34f)
        frontCover.setInitialRotationPoint(-12.0f, 2.0f, -17.0f)
        frontCover.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["FrontCover"] = frontCover
        pages.addChild(frontCover)

        val backCover = MCAModelRenderer(113, 93, 256f, 128f)
        backCover.addBox(0.0f, 0.0f, 0.0f, 22f, 1f, 34f)
        backCover.setInitialRotationPoint(-12.0f, -4.0f, -17.0f)
        backCover.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["BackCover"] = backCover
        pages.addChild(backCover)

        val spine = MCAModelRenderer(105, 0, 256f, 128f)
        spine.addBox(0.0f, -4.0f, 0.0f, 1f, 7f, 34f)
        spine.setInitialRotationPoint(10.0f, 0.0f, -17.0f)
        spine.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["Spine"] = spine
        pages.addChild(spine)
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
        pages.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha)
    }
}

