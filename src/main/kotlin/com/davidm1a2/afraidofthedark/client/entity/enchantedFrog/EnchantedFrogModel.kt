package com.davidm1a2.afraidofthedark.client.entity.enchantedFrog

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.davidm1a2.afraidofthedark.common.entity.enchantedFrog.EnchantedFrogEntity
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Quaternion
import net.minecraft.client.model.EntityModel


/**
 * Model class that defines the enchanted frog model
 *
 * @constructor initializes the model
 * @property parts A map of part name to part
 * @property frogBody The different parts of the model
 */
class EnchantedFrogModel internal constructor() : EntityModel<EnchantedFrogEntity>() {
    private val parts = mutableMapOf<String, MCAModelRenderer>()
    private val frogBody: MCAModelRenderer

    init {
        // Auto-generated from the MCAnimator software

        frogBody = MCAModelRenderer(0, 0, 32f, 32f)
        frogBody.addBox(-4.0f, 0.0f, -3.0f, 8f, 5f, 6f)
        frogBody.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        frogBody.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["frogBody"] = frogBody

        val frogLLeg = MCAModelRenderer(0, 11, 32f, 32f)
        frogLLeg.addBox(0.0f, 0.0f, 0.0f, 2f, 2f, 5f)
        frogLLeg.setInitialRotationPoint(4.0f, 0.0f, -3.0f)
        frogLLeg.setInitialRotationQuaternion(Quaternion(0.27059805f, 0.27059805f, 0.65328145f, 0.65328145f))
        parts["frogLLeg"] = frogLLeg
        frogBody.addChild(frogLLeg)

        val frogRLeg = MCAModelRenderer(0, 11, 32f, 32f)
        frogRLeg.addBox(0.0f, 0.0f, 0.0f, 2f, 2f, 5f)
        frogRLeg.setInitialRotationPoint(-4.0f, 0.0f, -3.0f)
        frogRLeg.setInitialRotationQuaternion(Quaternion(0.0f, -0.38268346f, 0.0f, 0.9238795f))
        parts["frogRLeg"] = frogRLeg
        frogBody.addChild(frogRLeg)

        val frogMouth = MCAModelRenderer(0, 18, 32f, 32f)
        frogMouth.addBox(0.0f, 0.0f, 0.0f, 8f, 2f, 5f)
        frogMouth.setInitialRotationPoint(-4.0f, 0.0f, 3.0f)
        frogMouth.setInitialRotationQuaternion(Quaternion(-0.08715574f, 0.0f, 0.0f, 0.9961947f))
        parts["frogMouth"] = frogMouth
        frogBody.addChild(frogMouth)

        val frogHead = MCAModelRenderer(0, 25, 32f, 32f)
        frogHead.addBox(0.0f, 0.0f, 0.0f, 8f, 5f, 3f)
        frogHead.setInitialRotationPoint(-4.0f, 5.0f, 2.0f)
        frogHead.setInitialRotationQuaternion(Quaternion(0.6427876f, 0.0f, 0.0f, 0.76604444f))
        parts["frogHead"] = frogHead
        frogBody.addChild(frogHead)
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
        frogBody.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha)
    }

    override fun setupAnim(
        entity: EnchantedFrogEntity,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    ) {
        (entity as IMCAnimatedModel).getAnimationHandler().performAnimationInModel(parts)
    }
}