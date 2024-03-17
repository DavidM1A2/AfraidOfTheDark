package com.davidm1a2.afraidofthedark.client.entity.werewolf

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.werewolf.WerewolfEntity
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Quaternion
import net.minecraft.client.model.EntityModel

/**
 * Class representing the model of a werewolf
 *
 * @constructor initializes the model
 * @property parts A map of part name to part
 * @property bodyUpper The different parts of the model
 */
class WerewolfModel internal constructor() : EntityModel<WerewolfEntity>() {
    private val parts = mutableMapOf<String, MCAModelRenderer>()
    private val bodyUpper: MCAModelRenderer

    init {
        // Auto-generated from the MCAnimator software

        bodyUpper = MCAModelRenderer(47, 1, 128f, 128f)
        bodyUpper.addBox(-5.0f, -5.0f, 0.0f, 10f, 10f, 11f)
        bodyUpper.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        bodyUpper.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["BodyUpper"] = bodyUpper

        val bodyLower = MCAModelRenderer(0, 0, 128f, 128f)
        bodyLower.addBox(-4.0f, -4.0f, -14.0f, 8f, 8f, 14f)
        bodyLower.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        bodyLower.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["BodyLower"] = bodyLower
        bodyUpper.addChild(bodyLower)

        val head = MCAModelRenderer(0, 51, 128f, 128f)
        head.addBox(-3.0f, -4.0f, 0.0f, 6f, 7f, 8f)
        head.setInitialRotationPoint(0.0f, 3.0f, 10.0f)
        head.setInitialRotationQuaternion(Quaternion(0.0784591f, 0.0f, 0.0f, 0.9969173f))
        parts["Head"] = head
        bodyUpper.addChild(head)

        val rightFrontLeg = MCAModelRenderer(0, 24, 128f, 128f)
        rightFrontLeg.addBox(-1.5f, -7.0f, -1.5f, 3f, 7f, 3f)
        rightFrontLeg.setInitialRotationPoint(-4.5f, -4.0f, 9.0f)
        rightFrontLeg.setInitialRotationQuaternion(Quaternion(0.17364818f, 0.0f, 0.0f, 0.9848077f))
        parts["RightFrontLeg"] = rightFrontLeg
        bodyUpper.addChild(rightFrontLeg)

        val leftFrontLeg = MCAModelRenderer(0, 24, 128f, 128f)
        leftFrontLeg.addBox(-1.5f, -7.0f, -1.5f, 3f, 7f, 3f)
        leftFrontLeg.setInitialRotationPoint(4.5f, -4.0f, 9.0f)
        leftFrontLeg.setInitialRotationQuaternion(Quaternion(0.17364818f, 0.0f, 0.0f, 0.9848077f))
        parts["LeftFrontLeg"] = leftFrontLeg
        bodyUpper.addChild(leftFrontLeg)

        val tail = MCAModelRenderer(58, 33, 128f, 128f)
        tail.addBox(-1.0f, -1.0f, -14.0f, 3f, 3f, 15f)
        tail.setInitialRotationPoint(0.0f, 2.0f, -14.0f)
        tail.setInitialRotationQuaternion(Quaternion(0.08715574f, 0.0f, 0.0f, 0.9961947f))
        parts["Tail"] = tail
        bodyLower.addChild(tail)

        val leftBackLeg = MCAModelRenderer(0, 36, 128f, 128f)
        leftBackLeg.addBox(-2.0f, -5.0f, -2.0f, 4f, 6f, 4f)
        leftBackLeg.setInitialRotationPoint(3.0f, -3.5f, -11.0f)
        leftBackLeg.setInitialRotationQuaternion(Quaternion(-0.25881904f, 0.0f, 0.0f, 0.9659258f))
        parts["LeftBackLeg"] = leftBackLeg
        bodyLower.addChild(leftBackLeg)

        val rightBackLeg = MCAModelRenderer(0, 36, 128f, 128f)
        rightBackLeg.addBox(-2.0f, -5.0f, -2.0f, 4f, 6f, 4f)
        rightBackLeg.setInitialRotationPoint(-3.0f, -3.5f, -11.0f)
        rightBackLeg.setInitialRotationQuaternion(Quaternion(-0.25881904f, 0.0f, 0.0f, 0.9659258f))
        parts["RightBackLeg"] = rightBackLeg
        bodyLower.addChild(rightBackLeg)

        val snoutUpper = MCAModelRenderer(34, 58, 128f, 128f)
        snoutUpper.addBox(-2.0f, 0.0f, -1.0f, 4f, 2f, 6f)
        snoutUpper.setInitialRotationPoint(0.0f, -1.0f, 8.0f)
        snoutUpper.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["SnoutUpper"] = snoutUpper
        head.addChild(snoutUpper)

        val snoutLower = MCAModelRenderer(58, 59, 128f, 128f)
        snoutLower.addBox(-2.0f, -2.0f, -1.0f, 4f, 2f, 5f)
        snoutLower.setInitialRotationPoint(0.0f, -1.0f, 8.0f)
        snoutLower.setInitialRotationQuaternion(Quaternion(0.17364818f, 0.0f, 0.0f, 0.9848077f))
        parts["SnoutLower"] = snoutLower
        head.addChild(snoutLower)

        val rightEar = MCAModelRenderer(0, 0, 128f, 128f)
        rightEar.addBox(-1.0f, 0.0f, 0.0f, 2f, 2f, 1f)
        rightEar.setInitialRotationPoint(-2.0f, 3.0f, 1.0f)
        rightEar.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["RightEar"] = rightEar
        head.addChild(rightEar)

        val leftEar = MCAModelRenderer(0, 0, 128f, 128f)
        leftEar.addBox(-1.0f, 0.0f, 0.0f, 2f, 2f, 1f)
        leftEar.setInitialRotationPoint(2.0f, 3.0f, 1.0f)
        leftEar.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["LeftEar"] = leftEar
        head.addChild(leftEar)

        val rightFrontFoot = MCAModelRenderer(19, 24, 128f, 128f)
        rightFrontFoot.addBox(-1.5f, -6.0f, -1.0f, 3f, 7f, 3f)
        rightFrontFoot.setInitialRotationPoint(0.0f, -7.0f, 0.0f)
        rightFrontFoot.setInitialRotationQuaternion(Quaternion(-0.25881904f, 0.0f, 0.0f, 0.9659258f))
        parts["RightFrontFoot"] = rightFrontFoot
        rightFrontLeg.addChild(rightFrontFoot)

        val lefttFrontFoot = MCAModelRenderer(19, 24, 128f, 128f)
        lefttFrontFoot.addBox(-1.5f, -6.0f, -1.0f, 3f, 7f, 3f)
        lefttFrontFoot.setInitialRotationPoint(0.0f, -7.0f, 0.0f)
        lefttFrontFoot.setInitialRotationQuaternion(Quaternion(-0.25881904f, 0.0f, 0.0f, 0.9659258f))
        parts["LefttFrontFoot"] = lefttFrontFoot
        leftFrontLeg.addChild(lefttFrontFoot)

        val leftBackLowerLeg = MCAModelRenderer(19, 37, 128f, 128f)
        leftBackLowerLeg.addBox(-2.0f, -4.5f, -1.5f, 3f, 6f, 3f)
        leftBackLowerLeg.setInitialRotationPoint(0.5f, -5.0f, 0.0f)
        leftBackLowerLeg.setInitialRotationQuaternion(Quaternion(0.47715878f, 0.0f, 0.0f, 0.8788171f))
        parts["LeftBackLowerLeg"] = leftBackLowerLeg
        leftBackLeg.addChild(leftBackLowerLeg)

        val rightBackLowerLeg = MCAModelRenderer(19, 37, 128f, 128f)
        rightBackLowerLeg.addBox(-2.0f, -4.5f, -1.5f, 3f, 6f, 3f)
        rightBackLowerLeg.setInitialRotationPoint(0.5f, -5.0f, 0.0f)
        rightBackLowerLeg.setInitialRotationQuaternion(Quaternion(0.47715878f, 0.0f, 0.0f, 0.8788171f))
        parts["RightBackLowerLeg"] = rightBackLowerLeg
        rightBackLeg.addChild(rightBackLowerLeg)

        val leftBackFoot = MCAModelRenderer(35, 38, 128f, 128f)
        leftBackFoot.addBox(-1.0f, -5.0f, -1.5f, 3f, 5f, 3f)
        leftBackFoot.setInitialRotationPoint(-1.0f, -4.0f, 0.0f)
        leftBackFoot.setInitialRotationQuaternion(Quaternion(-0.2079117f, 0.0f, 0.0f, 0.9781476f))
        parts["LeftBackFoot"] = leftBackFoot
        leftBackLowerLeg.addChild(leftBackFoot)

        val rightBackFoot = MCAModelRenderer(35, 38, 128f, 128f)
        rightBackFoot.addBox(-1.0f, -5.0f, -1.5f, 3f, 5f, 3f)
        rightBackFoot.setInitialRotationPoint(-1.0f, -4.0f, 0.0f)
        rightBackFoot.setInitialRotationQuaternion(Quaternion(-0.2079117f, 0.0f, 0.0f, 0.9781476f))
        parts["RightBackFoot"] = rightBackFoot
        rightBackLowerLeg.addChild(rightBackFoot)

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
        bodyUpper.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha)
    }

    override fun setupAnim(entity: WerewolfEntity, limbSwing: Float, limbSwingAmount: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float) {
        (entity as IMCAnimatedModel).getAnimationHandler().performAnimationInModel(parts)
    }
}