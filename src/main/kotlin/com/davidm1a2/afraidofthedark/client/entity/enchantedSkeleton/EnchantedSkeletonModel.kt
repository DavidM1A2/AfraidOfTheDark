package com.davidm1a2.afraidofthedark.client.entity.enchantedSkeleton

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.davidm1a2.afraidofthedark.common.entity.enchantedSkeleton.EnchantedSkeletonEntity
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.Quaternion
import net.minecraft.client.renderer.entity.model.EntityModel

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

        textureWidth = 64
        textureHeight = 32

        body = MCAModelRenderer(this, 16, 16)
        body.mirror = false
        body.addBox(-4.0f, -12.0f, -2.0f, 8f, 12f, 4f)
        body.setInitialRotationPoint(0.0f, 2.0f, 2.0f)
        body.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        body.setTextureSize(64, 32)
        parts["body"] = body

        val head = MCAModelRenderer(this, 0, 0)
        head.mirror = false
        head.addBox(-4.0f, 0.0f, -4.0f, 8f, 8f, 8f)
        head.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        head.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        head.setTextureSize(64, 32)
        parts["head"] = head
        body.addChild(head)

        val rightarm = MCAModelRenderer(this, 40, 16)
        rightarm.mirror = false
        rightarm.addBox(-2.0f, -10.0f, -1.0f, 2f, 12f, 2f)
        rightarm.setInitialRotationPoint(-4.0f, -2.0f, 0.0f)
        rightarm.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        rightarm.setTextureSize(64, 32)
        parts["rightarm"] = rightarm
        body.addChild(rightarm)

        val leftarm = MCAModelRenderer(this, 40, 16)
        leftarm.mirror = false
        leftarm.addBox(0.0f, -10.0f, -1.0f, 2f, 12f, 2f)
        leftarm.setInitialRotationPoint(4.0f, -2.0f, 0.0f)
        leftarm.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        leftarm.setTextureSize(64, 32)
        parts["leftarm"] = leftarm
        body.addChild(leftarm)

        val rightleg = MCAModelRenderer(this, 0, 16)
        rightleg.mirror = false
        rightleg.addBox(-1.0f, -12.0f, -1.0f, 2f, 12f, 2f)
        rightleg.setInitialRotationPoint(-2.0f, -12.0f, 0.0f)
        rightleg.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        rightleg.setTextureSize(64, 32)
        parts["rightleg"] = rightleg
        body.addChild(rightleg)

        val leftleg = MCAModelRenderer(this, 0, 16)
        leftleg.mirror = false
        leftleg.addBox(-1.0f, -12.0f, -1.0f, 2f, 12f, 2f)
        leftleg.setInitialRotationPoint(2.0f, -12.0f, 0.0f)
        leftleg.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        leftleg.setTextureSize(64, 32)
        parts["leftleg"] = leftleg
        body.addChild(leftleg)

        val heart = MCAModelRenderer(this, 48, 4)
        heart.mirror = false
        heart.addBox(-1.5f, -2.0f, -1.0f, 3f, 3f, 2f)
        heart.setInitialRotationPoint(0.0f, -3.0f, 0.0f)
        heart.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        heart.setTextureSize(64, 32)
        parts["heart"] = heart
        body.addChild(heart)
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

    override fun setRotationAngles(
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