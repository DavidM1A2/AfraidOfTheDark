package com.davidm1a2.afraidofthedark.client.tileEntity.magicCrystal

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.model.Model
import net.minecraft.util.math.vector.Quaternion


class MagicCrystalTileEntityModel internal constructor() : Model(RenderType::entityCutout) {
    private val parts = mutableMapOf<String, MCAModelRenderer>()
    private val base: MCAModelRenderer

    init {
        texWidth = 128
        texHeight = 64


        base = MCAModelRenderer(this, 0, 28)
        base.mirror = false
        base.addBox(-6.0f, 14.0f, -6.0f, 12f, 24f, 12f)
        base.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        base.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        base.setTexSize(128, 64)
        parts["Base"] = base

        val topLevel1 = MCAModelRenderer(this, 48, 48)
        topLevel1.mirror = false
        topLevel1.addBox(-5.0f, 0.0f, -5.0f, 10f, 6f, 10f)
        topLevel1.setInitialRotationPoint(0.0f, 38.0f, 0.0f)
        topLevel1.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        topLevel1.setTexSize(128, 64)
        parts["TopLevel1"] = topLevel1
        base.addChild(topLevel1)

        val bottomLevel1 = MCAModelRenderer(this, 88, 46)
        bottomLevel1.mirror = false
        bottomLevel1.addBox(-5.0f, 0.0f, -5.0f, 10f, 8f, 10f)
        bottomLevel1.setInitialRotationPoint(0.0f, 6.0f, 0.0f)
        bottomLevel1.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        bottomLevel1.setTexSize(128, 64)
        parts["BottomLevel1"] = bottomLevel1
        base.addChild(bottomLevel1)

        val topLevel2 = MCAModelRenderer(this, 48, 34)
        topLevel2.mirror = false
        topLevel2.addBox(-4.0f, 0.0f, -4.0f, 8f, 6f, 8f)
        topLevel2.setInitialRotationPoint(0.0f, 6.0f, 0.0f)
        topLevel2.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        topLevel2.setTexSize(128, 64)
        parts["TopLevel2"] = topLevel2
        topLevel1.addChild(topLevel2)

        val bottomLevel2 = MCAModelRenderer(this, 88, 35)
        bottomLevel2.mirror = false
        bottomLevel2.addBox(-4.0f, 0.0f, -4.0f, 8f, 3f, 8f)
        bottomLevel2.setInitialRotationPoint(0.0f, -3.0f, 0.0f)
        bottomLevel2.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        bottomLevel2.setTexSize(128, 64)
        parts["BottomLevel2"] = bottomLevel2
        bottomLevel1.addChild(bottomLevel2)

        val topLevel3 = MCAModelRenderer(this, 48, 22)
        topLevel3.mirror = false
        topLevel3.addBox(-3.0f, 0.0f, -3.0f, 6f, 6f, 6f)
        topLevel3.setInitialRotationPoint(0.0f, 6.0f, 0.0f)
        topLevel3.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        topLevel3.setTexSize(128, 64)
        parts["TopLevel3"] = topLevel3
        topLevel2.addChild(topLevel3)

        val bottomLevel3 = MCAModelRenderer(this, 88, 27)
        bottomLevel3.mirror = false
        bottomLevel3.addBox(-3.0f, 0.0f, -3.0f, 6f, 2f, 6f)
        bottomLevel3.setInitialRotationPoint(0.0f, -2.0f, 0.0f)
        bottomLevel3.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        bottomLevel3.setTexSize(128, 64)
        parts["BottomLevel3"] = bottomLevel3
        bottomLevel2.addChild(bottomLevel3)

        val topLevel4 = MCAModelRenderer(this, 48, 12)
        topLevel4.mirror = false
        topLevel4.addBox(-2.0f, 0.0f, -2.0f, 4f, 6f, 4f)
        topLevel4.setInitialRotationPoint(0.0f, 6.0f, 0.0f)
        topLevel4.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        topLevel4.setTexSize(128, 64)
        parts["TopLevel4"] = topLevel4
        topLevel3.addChild(topLevel4)

        val bottomLevel4 = MCAModelRenderer(this, 88, 22)
        bottomLevel4.mirror = false
        bottomLevel4.addBox(-2.0f, 0.0f, -2.0f, 4f, 1f, 4f)
        bottomLevel4.setInitialRotationPoint(0.0f, -1.0f, 0.0f)
        bottomLevel4.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        bottomLevel4.setTexSize(128, 64)
        parts["BottomLevel4"] = bottomLevel4
        bottomLevel3.addChild(bottomLevel4)
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