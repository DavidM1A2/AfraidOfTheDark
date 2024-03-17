package com.davidm1a2.afraidofthedark.client.entity.frostPhoenix

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.davidm1a2.afraidofthedark.common.entity.frostPhoenix.FrostPhoenixProjectileEntity
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Quaternion
import net.minecraft.client.model.EntityModel


/**
 * Model class that defines the frost phoenix projectile model
 *
 * @constructor initializes the model
 * @property parts A map of part name to part
 * @property center The different parts of the model
 */
class FrostPhoenixProjectileModel internal constructor() : EntityModel<FrostPhoenixProjectileEntity>() {
    private val parts = mutableMapOf<String, MCAModelRenderer>()
    private val center: MCAModelRenderer

    init {
        // Auto-generated from the MCAnimator software

        center = MCAModelRenderer(0, 0, 32f, 32f)
        center.addBox(-1.0f, -2.0f, -1.0f, 2f, 4f, 2f)
        center.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        center.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["Center"] = center

        val cross1 = MCAModelRenderer(11, 4, 32f, 32f)
        cross1.addBox(-3.0f, 0.0f, -1.0f, 6f, 1f, 2f)
        cross1.setInitialRotationPoint(0.0f, -1.0f, 0.0f)
        cross1.setInitialRotationQuaternion(Quaternion(0.34343532f, -0.27070254f, 0.1541134f, 0.88601434f))
        parts["Cross1"] = cross1
        center.addChild(cross1)

        val cross2 = MCAModelRenderer(0, 8, 32f, 32f)
        cross2.addBox(-1.0f, 0.0f, -3.0f, 2f, 1f, 6f)
        cross2.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        cross2.setInitialRotationQuaternion(Quaternion(0.15145284f, -0.39454812f, -0.32479164f, 0.8461112f))
        parts["Cross2"] = cross2
        center.addChild(cross2)

        val cross3 = MCAModelRenderer(0, 16, 32f, 32f)
        cross3.addBox(-1.0f, 0.0f, -3.0f, 2f, 1f, 6f)
        cross3.setInitialRotationPoint(0.0f, -1.0f, 0.0f)
        cross3.setInitialRotationQuaternion(Quaternion(-0.21406221f, -0.96557224f, 0.1443057f, 0.03199177f))
        parts["Cross3"] = cross3
        center.addChild(cross3)

        val cross4 = MCAModelRenderer(11, 0, 32f, 32f)
        cross4.addBox(-3.0f, 0.0f, -1.0f, 6f, 1f, 2f)
        cross4.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        cross4.setInitialRotationQuaternion(Quaternion(-0.8103247f, 0.36077982f, -0.18781008f, 0.42182833f))
        parts["Cross4"] = cross4
        center.addChild(cross4)
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
        center.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha)
    }

    override fun setupAnim(
        entity: FrostPhoenixProjectileEntity,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    ) {
        (entity as IMCAnimatedModel).getAnimationHandler().performAnimationInModel(parts)
    }
}
