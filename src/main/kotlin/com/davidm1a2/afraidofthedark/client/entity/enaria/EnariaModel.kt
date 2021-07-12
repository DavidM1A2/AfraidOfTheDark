package com.davidm1a2.afraidofthedark.client.entity.enaria

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.Quaternion
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.model.EntityModel
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation

class EnariaModel<T : Entity> internal constructor(renderTypeFactory: (ResourceLocation) -> RenderType) : EntityModel<T>(renderTypeFactory) {
    private val parts = mutableMapOf<String, MCAModelRenderer>()
    private val body: MCAModelRenderer

    init {
        textureWidth = 64
        textureHeight = 64

        body = MCAModelRenderer(this, 0, 16)
        body.mirror = false
        body.addBox(-4.0f, -12.0f, -2.0f, 8f, 12f, 4f)
        body.setInitialRotationPoint(0.0f, 2.0f, 2.0f)
        body.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        body.setTextureSize(64, 64)
        parts["body"] = body

        val rightarm = MCAModelRenderer(this, 41, 16)
        rightarm.mirror = false
        rightarm.addBox(-3.0f, -10.0f, -2.0f, 3f, 12f, 4f)
        rightarm.setInitialRotationPoint(-4.0f, -2.0f, 0.0f)
        rightarm.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        rightarm.setTextureSize(64, 64)
        parts["rightarm"] = rightarm
        body.addChild(rightarm)

        val leftarm = MCAModelRenderer(this, 25, 16)
        leftarm.mirror = false
        leftarm.addBox(0.0f, -10.0f, -2.0f, 3f, 12f, 4f)
        leftarm.setInitialRotationPoint(4.0f, -2.0f, 0.0f)
        leftarm.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        leftarm.setTextureSize(64, 64)
        parts["leftarm"] = leftarm
        body.addChild(leftarm)

        val rightleg = MCAModelRenderer(this, 16, 32)
        rightleg.mirror = false
        rightleg.addBox(-2.0f, -12.0f, -2.0f, 4f, 12f, 4f)
        rightleg.setInitialRotationPoint(-2.0f, -12.0f, 0.0f)
        rightleg.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        rightleg.setTextureSize(64, 64)
        parts["rightleg"] = rightleg
        body.addChild(rightleg)

        val leftleg = MCAModelRenderer(this, 0, 32)
        leftleg.mirror = false
        leftleg.addBox(-2.0f, -12.0f, -2.0f, 4f, 12f, 4f)
        leftleg.setInitialRotationPoint(2.0f, -12.0f, 0.0f)
        leftleg.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        leftleg.setTextureSize(64, 64)
        parts["leftleg"] = leftleg
        body.addChild(leftleg)

        val head = MCAModelRenderer(this, 0, 0)
        head.mirror = false
        head.addBox(-4.0f, 0.0f, -4.0f, 8f, 8f, 8f)
        head.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        head.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        head.setTextureSize(64, 64)
        parts["head"] = head
        body.addChild(head)
    }

    override fun render(
        matrixStack: MatrixStack,
        vertexBuilder: IVertexBuilder,
        packedLight: Int,
        packedOverlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        body.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha)
    }

    override fun setRotationAngles(entity: T, limbSwing: Float, limbSwingAmount: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float) {
        (entity as IMCAnimatedModel).getAnimationHandler().performAnimationInModel(parts)
    }
}
