package com.davidm1a2.afraidofthedark.client.entity.werewolf

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.werewolf.WerewolfEntity
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.Quaternion
import net.minecraft.client.renderer.entity.model.EntityModel

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

        textureWidth = 128
        textureHeight = 128

        bodyUpper = MCAModelRenderer(this, 47, 1)
        bodyUpper.mirror = false
        bodyUpper.addBox(-5.0f, -5.0f, 0.0f, 10f, 10f, 11f)
        bodyUpper.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        bodyUpper.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        bodyUpper.setTextureSize(128, 128)
        parts["BodyUpper"] = bodyUpper

        val bodyLower = MCAModelRenderer(this, 0, 0)
        bodyLower.mirror = false
        bodyLower.addBox(-4.0f, -4.0f, -14.0f, 8f, 8f, 14f)
        bodyLower.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        bodyLower.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        bodyLower.setTextureSize(128, 128)
        parts["BodyLower"] = bodyLower
        bodyUpper.addChild(bodyLower)

        val head = MCAModelRenderer(this, 0, 51)
        head.mirror = false
        head.addBox(-3.0f, -4.0f, 0.0f, 6f, 7f, 8f)
        head.setInitialRotationPoint(0.0f, 3.0f, 10.0f)
        head.setInitialRotationQuaternion(Quaternion(0.0784591f, 0.0f, 0.0f, 0.9969173f))
        head.setTextureSize(128, 128)
        parts["Head"] = head
        bodyUpper.addChild(head)

        val rightFrontLeg = MCAModelRenderer(this, 0, 24)
        rightFrontLeg.mirror = false
        rightFrontLeg.addBox(-1.5f, -7.0f, -1.5f, 3f, 7f, 3f)
        rightFrontLeg.setInitialRotationPoint(-4.5f, -4.0f, 9.0f)
        rightFrontLeg.setInitialRotationQuaternion(Quaternion(0.17364818f, 0.0f, 0.0f, 0.9848077f))
        rightFrontLeg.setTextureSize(128, 128)
        parts["RightFrontLeg"] = rightFrontLeg
        bodyUpper.addChild(rightFrontLeg)

        val leftFrontLeg = MCAModelRenderer(this, 0, 24)
        leftFrontLeg.mirror = false
        leftFrontLeg.addBox(-1.5f, -7.0f, -1.5f, 3f, 7f, 3f)
        leftFrontLeg.setInitialRotationPoint(4.5f, -4.0f, 9.0f)
        leftFrontLeg.setInitialRotationQuaternion(Quaternion(0.17364818f, 0.0f, 0.0f, 0.9848077f))
        leftFrontLeg.setTextureSize(128, 128)
        parts["LeftFrontLeg"] = leftFrontLeg
        bodyUpper.addChild(leftFrontLeg)

        val tail = MCAModelRenderer(this, 58, 33)
        tail.mirror = false
        tail.addBox(-1.0f, -1.0f, -14.0f, 3f, 3f, 15f)
        tail.setInitialRotationPoint(0.0f, 2.0f, -14.0f)
        tail.setInitialRotationQuaternion(Quaternion(0.08715574f, 0.0f, 0.0f, 0.9961947f))
        tail.setTextureSize(128, 128)
        parts["Tail"] = tail
        bodyLower.addChild(tail)

        val leftBackLeg = MCAModelRenderer(this, 0, 36)
        leftBackLeg.mirror = false
        leftBackLeg.addBox(-2.0f, -5.0f, -2.0f, 4f, 6f, 4f)
        leftBackLeg.setInitialRotationPoint(3.0f, -3.5f, -11.0f)
        leftBackLeg.setInitialRotationQuaternion(Quaternion(0.25881904f, 0.0f, 0.0f, 0.9659258f))
        leftBackLeg.setTextureSize(128, 128)
        parts["LeftBackLeg"] = leftBackLeg
        bodyLower.addChild(leftBackLeg)

        val rightBackLeg = MCAModelRenderer(this, 0, 36)
        rightBackLeg.mirror = false
        rightBackLeg.addBox(-2.0f, -5.0f, -2.0f, 4f, 6f, 4f)
        rightBackLeg.setInitialRotationPoint(-3.0f, -3.5f, -11.0f)
        rightBackLeg.setInitialRotationQuaternion(Quaternion(-0.25881904f, 0.0f, 0.0f, 0.9659258f))
        rightBackLeg.setTextureSize(128, 128)
        parts["RightBackLeg"] = rightBackLeg
        bodyLower.addChild(rightBackLeg)

        val snoutUpper = MCAModelRenderer(this, 34, 58)
        snoutUpper.mirror = false
        snoutUpper.addBox(-2.0f, 0.0f, -1.0f, 4f, 2f, 6f)
        snoutUpper.setInitialRotationPoint(0.0f, -1.0f, 8.0f)
        snoutUpper.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        snoutUpper.setTextureSize(128, 128)
        parts["SnoutUpper"] = snoutUpper
        head.addChild(snoutUpper)

        val snoutLower = MCAModelRenderer(this, 58, 59)
        snoutLower.mirror = false
        snoutLower.addBox(-2.0f, -2.0f, -1.0f, 4f, 2f, 5f)
        snoutLower.setInitialRotationPoint(0.0f, -1.0f, 8.0f)
        snoutLower.setInitialRotationQuaternion(Quaternion(0.17364818f, 0.0f, 0.0f, 0.9848077f))
        snoutLower.setTextureSize(128, 128)
        parts["SnoutLower"] = snoutLower
        head.addChild(snoutLower)

        val rightEar = MCAModelRenderer(this, 0, 0)
        rightEar.mirror = false
        rightEar.addBox(-1.0f, 0.0f, 0.0f, 2f, 2f, 1f)
        rightEar.setInitialRotationPoint(-2.0f, 3.0f, 1.0f)
        rightEar.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        rightEar.setTextureSize(128, 128)
        parts["RightEar"] = rightEar
        head.addChild(rightEar)

        val leftEar = MCAModelRenderer(this, 0, 0)
        leftEar.mirror = false
        leftEar.addBox(-1.0f, 0.0f, 0.0f, 2f, 2f, 1f)
        leftEar.setInitialRotationPoint(2.0f, 3.0f, 1.0f)
        leftEar.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        leftEar.setTextureSize(128, 128)
        parts["LeftEar"] = leftEar
        head.addChild(leftEar)

        val rightFrontFoot = MCAModelRenderer(this, 19, 24)
        rightFrontFoot.mirror = false
        rightFrontFoot.addBox(-1.5f, -6.0f, -1.0f, 3f, 7f, 3f)
        rightFrontFoot.setInitialRotationPoint(0.0f, -7.0f, 0.0f)
        rightFrontFoot.setInitialRotationQuaternion(Quaternion(-0.25881904f, 0.0f, 0.0f, 0.9659258f))
        rightFrontFoot.setTextureSize(128, 128)
        parts["RightFrontFoot"] = rightFrontFoot
        rightFrontLeg.addChild(rightFrontFoot)

        val lefttFrontFoot = MCAModelRenderer(this, 19, 24)
        lefttFrontFoot.mirror = false
        lefttFrontFoot.addBox(-1.5f, -6.0f, -1.0f, 3f, 7f, 3f)
        lefttFrontFoot.setInitialRotationPoint(0.0f, -7.0f, 0.0f)
        lefttFrontFoot.setInitialRotationQuaternion(Quaternion(-0.25881904f, 0.0f, 0.0f, 0.9659258f))
        lefttFrontFoot.setTextureSize(128, 128)
        parts["LefttFrontFoot"] = lefttFrontFoot
        leftFrontLeg.addChild(lefttFrontFoot)

        val leftBackLowerLeg = MCAModelRenderer(this, 19, 37)
        leftBackLowerLeg.mirror = false
        leftBackLowerLeg.addBox(-2.0f, -4.5f, -1.5f, 3f, 6f, 3f)
        leftBackLowerLeg.setInitialRotationPoint(0.5f, -5.0f, 0.0f)
        leftBackLowerLeg.setInitialRotationQuaternion(Quaternion(0.47715878f, 0.0f, 0.0f, 0.8788171f))
        leftBackLowerLeg.setTextureSize(128, 128)
        parts["LeftBackLowerLeg"] = leftBackLowerLeg
        leftBackLeg.addChild(leftBackLowerLeg)

        val rightBackLowerLeg = MCAModelRenderer(this, 19, 37)
        rightBackLowerLeg.mirror = false
        rightBackLowerLeg.addBox(-2.0f, -4.5f, -1.5f, 3f, 6f, 3f)
        rightBackLowerLeg.setInitialRotationPoint(0.5f, -5.0f, 0.0f)
        rightBackLowerLeg.setInitialRotationQuaternion(Quaternion(0.47715878f, 0.0f, 0.0f, 0.8788171f))
        rightBackLowerLeg.setTextureSize(128, 128)
        parts["RightBackLowerLeg"] = rightBackLowerLeg
        rightBackLeg.addChild(rightBackLowerLeg)

        val leftBackFoot = MCAModelRenderer(this, 35, 38)
        leftBackFoot.mirror = false
        leftBackFoot.addBox(-1.0f, -5.0f, -1.5f, 3f, 5f, 3f)
        leftBackFoot.setInitialRotationPoint(-1.0f, -4.0f, 0.0f)
        leftBackFoot.setInitialRotationQuaternion(Quaternion(-0.2079117f, 0.0f, 0.0f, 0.9781476f))
        leftBackFoot.setTextureSize(128, 128)
        parts["LeftBackFoot"] = leftBackFoot
        leftBackLowerLeg.addChild(leftBackFoot)

        val rightBackFoot = MCAModelRenderer(this, 35, 38)
        rightBackFoot.mirror = false
        rightBackFoot.addBox(-1.0f, -5.0f, -1.5f, 3f, 5f, 3f)
        rightBackFoot.setInitialRotationPoint(-1.0f, -4.0f, 0.0f)
        rightBackFoot.setInitialRotationQuaternion(Quaternion(-0.2079117f, 0.0f, 0.0f, 0.9781476f))
        rightBackFoot.setTextureSize(128, 128)
        parts["RightBackFoot"] = rightBackFoot
        rightBackLowerLeg.addChild(rightBackFoot)

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
        bodyUpper.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha)
    }

    override fun setRotationAngles(entity: WerewolfEntity, limbSwing: Float, limbSwingAmount: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float) {
        (entity as IMCAnimatedModel).getAnimationHandler().performAnimationInModel(parts)
    }
}