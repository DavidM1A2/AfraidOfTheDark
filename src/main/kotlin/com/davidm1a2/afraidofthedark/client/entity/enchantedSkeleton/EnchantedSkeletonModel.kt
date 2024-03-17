package com.davidm1a2.afraidofthedark.client.entity.enchantedSkeleton

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.davidm1a2.afraidofthedark.common.entity.enchantedSkeleton.EnchantedSkeletonEntity
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Quaternion
import net.minecraft.client.model.EntityModel

/**
 * Model class that defines the enchanted skeleton model
 *
 * @constructor initializes the model
 * @property parts A map of part name to part
 * @property body The different parts of the model
 */
class EnchantedSkeletonModel internal constructor() : EntityModel<EnchantedSkeletonEntity>() {
    private val parts = mutableMapOf<String, MCAModelRenderer>()
    private val body: MCAModelRenderer

    init {
        // Auto-generated from the MCAnimator software

        body = MCAModelRenderer(16, 16, 64f, 32f)
        body.addBox(-4.0f, -12.0f, -2.0f, 8f, 12f, 4f)
        body.setInitialRotationPoint(0.0f, 2.0f, 2.0f)
        body.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["body"] = body

        val head = MCAModelRenderer(0, 0, 64f, 32f)
        head.addBox(-4.0f, 0.0f, -4.0f, 8f, 8f, 8f)
        head.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        head.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["head"] = head
        body.addChild(head)

        val rightarm = MCAModelRenderer(40, 16, 64f, 32f)
        rightarm.addBox(-2.0f, -10.0f, -1.0f, 2f, 12f, 2f)
        rightarm.setInitialRotationPoint(-4.0f, -2.0f, 0.0f)
        rightarm.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["rightarm"] = rightarm
        body.addChild(rightarm)

        val leftarm = MCAModelRenderer(40, 16, 64f, 32f)
        leftarm.addBox(0.0f, -10.0f, -1.0f, 2f, 12f, 2f)
        leftarm.setInitialRotationPoint(4.0f, -2.0f, 0.0f)
        leftarm.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["leftarm"] = leftarm
        body.addChild(leftarm)

        val rightleg = MCAModelRenderer(0, 16, 64f, 32f)
        rightleg.addBox(-1.0f, -12.0f, -1.0f, 2f, 12f, 2f)
        rightleg.setInitialRotationPoint(-2.0f, -12.0f, 0.0f)
        rightleg.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["rightleg"] = rightleg
        body.addChild(rightleg)

        val leftleg = MCAModelRenderer(0, 16, 64f, 32f)
        leftleg.addBox(-1.0f, -12.0f, -1.0f, 2f, 12f, 2f)
        leftleg.setInitialRotationPoint(2.0f, -12.0f, 0.0f)
        leftleg.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["leftleg"] = leftleg
        body.addChild(leftleg)

        val heart = MCAModelRenderer(48, 4, 64f, 32f)
        heart.addBox(-1.5f, -2.0f, -1.0f, 3f, 3f, 2f)
        heart.setInitialRotationPoint(0.0f, -3.0f, 0.0f)
        heart.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["heart"] = heart
        body.addChild(heart)
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
        body.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha)
    }

    override fun setupAnim(
        entity: EnchantedSkeletonEntity,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    ) {
        (entity as IMCAnimatedModel).getAnimationHandler().performAnimationInModel(parts)
    }
}