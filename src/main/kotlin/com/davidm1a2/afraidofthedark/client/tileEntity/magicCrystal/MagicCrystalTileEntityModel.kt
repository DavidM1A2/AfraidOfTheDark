package com.davidm1a2.afraidofthedark.client.tileEntity.magicCrystal

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Quaternion
import net.minecraft.client.model.Model
import net.minecraft.client.renderer.RenderType


class MagicCrystalTileEntityModel internal constructor() : Model(RenderType::entityCutout) {
    private val parts = mutableMapOf<String, MCAModelRenderer>()
    private val base: MCAModelRenderer

    init {
        base = MCAModelRenderer(0, 28, 128f, 64f)
        base.addBox(-6.0f, 14.0f, -6.0f, 12f, 24f, 12f)
        base.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        base.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["Base"] = base

        val topLevel1 = MCAModelRenderer(48, 48, 128f, 64f)
        topLevel1.addBox(-5.0f, 0.0f, -5.0f, 10f, 6f, 10f)
        topLevel1.setInitialRotationPoint(0.0f, 38.0f, 0.0f)
        topLevel1.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["TopLevel1"] = topLevel1
        base.addChild(topLevel1)

        val bottomLevel1 = MCAModelRenderer(88, 46, 128f, 64f)
        bottomLevel1.addBox(-5.0f, 0.0f, -5.0f, 10f, 8f, 10f)
        bottomLevel1.setInitialRotationPoint(0.0f, 6.0f, 0.0f)
        bottomLevel1.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["BottomLevel1"] = bottomLevel1
        base.addChild(bottomLevel1)

        val topLevel2 = MCAModelRenderer(48, 34, 128f, 64f)
        topLevel2.addBox(-4.0f, 0.0f, -4.0f, 8f, 6f, 8f)
        topLevel2.setInitialRotationPoint(0.0f, 6.0f, 0.0f)
        topLevel2.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["TopLevel2"] = topLevel2
        topLevel1.addChild(topLevel2)

        val bottomLevel2 = MCAModelRenderer(88, 35, 128f, 64f)
        bottomLevel2.addBox(-4.0f, 0.0f, -4.0f, 8f, 3f, 8f)
        bottomLevel2.setInitialRotationPoint(0.0f, -3.0f, 0.0f)
        bottomLevel2.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["BottomLevel2"] = bottomLevel2
        bottomLevel1.addChild(bottomLevel2)

        val topLevel3 = MCAModelRenderer(48, 22, 128f, 64f)
        topLevel3.addBox(-3.0f, 0.0f, -3.0f, 6f, 6f, 6f)
        topLevel3.setInitialRotationPoint(0.0f, 6.0f, 0.0f)
        topLevel3.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["TopLevel3"] = topLevel3
        topLevel2.addChild(topLevel3)

        val bottomLevel3 = MCAModelRenderer(88, 27, 128f, 64f)
        bottomLevel3.addBox(-3.0f, 0.0f, -3.0f, 6f, 2f, 6f)
        bottomLevel3.setInitialRotationPoint(0.0f, -2.0f, 0.0f)
        bottomLevel3.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["BottomLevel3"] = bottomLevel3
        bottomLevel2.addChild(bottomLevel3)

        val topLevel4 = MCAModelRenderer(48, 12, 128f, 64f)
        topLevel4.addBox(-2.0f, 0.0f, -2.0f, 4f, 6f, 4f)
        topLevel4.setInitialRotationPoint(0.0f, 6.0f, 0.0f)
        topLevel4.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["TopLevel4"] = topLevel4
        topLevel3.addChild(topLevel4)

        val bottomLevel4 = MCAModelRenderer(88, 22, 128f, 64f)
        bottomLevel4.addBox(-2.0f, 0.0f, -2.0f, 4f, 1f, 4f)
        bottomLevel4.setInitialRotationPoint(0.0f, -1.0f, 0.0f)
        bottomLevel4.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["BottomLevel4"] = bottomLevel4
        bottomLevel3.addChild(bottomLevel4)
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