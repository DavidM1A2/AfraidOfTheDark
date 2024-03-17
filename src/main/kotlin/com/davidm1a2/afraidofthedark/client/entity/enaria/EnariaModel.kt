package com.davidm1a2.afraidofthedark.client.entity.enaria

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Quaternion
import net.minecraft.client.model.EntityModel
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity

class EnariaModel<T : Entity> internal constructor(
    private val isTransparent: Boolean,
    renderTypeFactory: (ResourceLocation) -> RenderType
) : EntityModel<T>(renderTypeFactory) {
    private val parts = mutableMapOf<String, MCAModelRenderer>()
    private val body: MCAModelRenderer

    init {
        body = MCAModelRenderer(0, 16, 64f, 64f)
        body.addBox(-4.0f, -12.0f, -2.0f, 8f, 12f, 4f)
        body.setInitialRotationPoint(0.0f, 2.0f, 2.0f)
        body.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["body"] = body

        val rightarm = MCAModelRenderer(41, 16, 64f, 64f)
        rightarm.addBox(-3.0f, -10.0f, -2.0f, 3f, 12f, 4f)
        rightarm.setInitialRotationPoint(-4.0f, -2.0f, 0.0f)
        rightarm.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["rightarm"] = rightarm
        body.addChild(rightarm)

        val leftarm = MCAModelRenderer(25, 16, 64f, 64f)
        leftarm.addBox(0.0f, -10.0f, -2.0f, 3f, 12f, 4f)
        leftarm.setInitialRotationPoint(4.0f, -2.0f, 0.0f)
        leftarm.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["leftarm"] = leftarm
        body.addChild(leftarm)

        val rightleg = MCAModelRenderer(16, 32, 64f, 64f)
        rightleg.addBox(-2.0f, -12.0f, -2.0f, 4f, 12f, 4f)
        rightleg.setInitialRotationPoint(-2.0f, -12.0f, 0.0f)
        rightleg.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["rightleg"] = rightleg
        body.addChild(rightleg)

        val leftleg = MCAModelRenderer(0, 32, 64f, 64f)
        leftleg.addBox(-2.0f, -12.0f, -2.0f, 4f, 12f, 4f)
        leftleg.setInitialRotationPoint(2.0f, -12.0f, 0.0f)
        leftleg.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["leftleg"] = leftleg
        body.addChild(leftleg)

        val head = MCAModelRenderer(0, 0, 64f, 64f)
        head.addBox(-4.0f, 0.0f, -4.0f, 8f, 8f, 8f)
        head.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        head.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["head"] = head
        body.addChild(head)
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
        body.render(
            matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, if (isTransparent) {
                0.3f
            } else {
                1.0f
            }
        )
    }

    override fun setupAnim(
        entity: T,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    ) {
        (entity as IMCAnimatedModel).getAnimationHandler().performAnimationInModel(parts)
    }
}
