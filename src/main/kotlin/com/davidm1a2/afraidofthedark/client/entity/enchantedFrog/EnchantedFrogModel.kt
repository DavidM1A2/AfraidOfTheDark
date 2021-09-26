package com.davidm1a2.afraidofthedark.client.entity.enchantedFrog

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.davidm1a2.afraidofthedark.common.entity.enchantedFrog.EnchantedFrogEntity
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.entity.model.EntityModel
import net.minecraft.util.math.vector.Quaternion


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

        texWidth = 32
        texHeight = 32

        frogBody = MCAModelRenderer(this, 0, 0)
        frogBody.mirror = false
        frogBody.addBox(-4.0f, 0.0f, -3.0f, 8f, 5f, 6f)
        frogBody.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        frogBody.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        frogBody.setTexSize(32, 32)
        parts["frogBody"] = frogBody

        val frogLLeg = MCAModelRenderer(this, 0, 11)
        frogLLeg.mirror = false
        frogLLeg.addBox(0.0f, 0.0f, 0.0f, 2f, 2f, 5f)
        frogLLeg.setInitialRotationPoint(4.0f, 0.0f, -3.0f)
        frogLLeg.setInitialRotationQuaternion(Quaternion(0.27059805f, 0.27059805f, 0.65328145f, 0.65328145f))
        frogLLeg.setTexSize(32, 32)
        parts["frogLLeg"] = frogLLeg
        frogBody.addChild(frogLLeg)

        val frogRLeg = MCAModelRenderer(this, 0, 11)
        frogRLeg.mirror = false
        frogRLeg.addBox(0.0f, 0.0f, 0.0f, 2f, 2f, 5f)
        frogRLeg.setInitialRotationPoint(-4.0f, 0.0f, -3.0f)
        frogRLeg.setInitialRotationQuaternion(Quaternion(0.0f, -0.38268346f, 0.0f, 0.9238795f))
        frogRLeg.setTexSize(32, 32)
        parts["frogRLeg"] = frogRLeg
        frogBody.addChild(frogRLeg)

        val frogMouth = MCAModelRenderer(this, 0, 18)
        frogMouth.mirror = false
        frogMouth.addBox(0.0f, 0.0f, 0.0f, 8f, 2f, 5f)
        frogMouth.setInitialRotationPoint(-4.0f, 0.0f, 3.0f)
        frogMouth.setInitialRotationQuaternion(Quaternion(-0.08715574f, 0.0f, 0.0f, 0.9961947f))
        frogMouth.setTexSize(32, 32)
        parts["frogMouth"] = frogMouth
        frogBody.addChild(frogMouth)

        val frogHead = MCAModelRenderer(this, 0, 25)
        frogHead.mirror = false
        frogHead.addBox(0.0f, 0.0f, 0.0f, 8f, 5f, 3f)
        frogHead.setInitialRotationPoint(-4.0f, 5.0f, 2.0f)
        frogHead.setInitialRotationQuaternion(Quaternion(0.6427876f, 0.0f, 0.0f, 0.76604444f))
        frogHead.setTexSize(32, 32)
        parts["frogHead"] = frogHead
        frogBody.addChild(frogHead)
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