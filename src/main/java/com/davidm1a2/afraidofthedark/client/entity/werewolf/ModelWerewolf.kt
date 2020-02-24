package com.davidm1a2.afraidofthedark.client.entity.werewolf

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Matrix4f
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion
import com.davidm1a2.afraidofthedark.common.entity.werewolf.EntityWerewolf
import net.minecraft.client.model.ModelBase
import net.minecraft.entity.Entity

/**
 * Class representing the model of a werewolf
 *
 * @constructor initializes the model
 * @property parts A map of part name to part
 * @property bodyUpper The different parts of the model
 */
class ModelWerewolf internal constructor() : ModelBase() {
    private val parts = mutableMapOf<String, MCAModelRenderer>()
    private val bodyUpper: MCAModelRenderer

    init {
        // Auto-generated from the MCAnimator software

        textureWidth = 128
        textureHeight = 128

        bodyUpper = MCAModelRenderer(this, "BodyUpper", 47, 1)
        bodyUpper.mirror = false
        bodyUpper.addBox(-5.0f, -5.0f, 0.0f, 10, 10, 11)
        bodyUpper.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        bodyUpper.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        bodyUpper.setTextureSize(128, 128)
        parts[bodyUpper.boxName] = bodyUpper

        val bodyLower = MCAModelRenderer(this, "BodyLower", 0, 0)
        bodyLower.mirror = false
        bodyLower.addBox(-4.0f, -4.0f, -14.0f, 8, 8, 14)
        bodyLower.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        bodyLower.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        bodyLower.setTextureSize(128, 128)
        parts[bodyLower.boxName] = bodyLower
        bodyUpper.addChild(bodyLower)

        val head = MCAModelRenderer(this, "Head", 0, 51)
        head.mirror = false
        head.addBox(-3.0f, -4.0f, 0.0f, 6, 7, 8)
        head.setInitialRotationPoint(0.0f, 3.0f, 10.0f)
        head.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0784591f, 0.0f, 0.0f, 0.9969173f)).transpose())
        head.setTextureSize(128, 128)
        parts[head.boxName] = head
        bodyUpper.addChild(head)

        val rightFrontLeg = MCAModelRenderer(this, "RightFrontLeg", 0, 24)
        rightFrontLeg.mirror = false
        rightFrontLeg.addBox(-1.5f, -7.0f, -1.5f, 3, 7, 3)
        rightFrontLeg.setInitialRotationPoint(-4.5f, -4.0f, 9.0f)
        rightFrontLeg.setInitialRotationMatrix(
            Matrix4f().set(
                Quaternion(
                    0.17364818f,
                    0.0f,
                    0.0f,
                    0.9848077f
                )
            ).transpose()
        )
        rightFrontLeg.setTextureSize(128, 128)
        parts[rightFrontLeg.boxName] = rightFrontLeg
        bodyUpper.addChild(rightFrontLeg)

        val leftFrontLeg = MCAModelRenderer(this, "LeftFrontLeg", 0, 24)
        leftFrontLeg.mirror = false
        leftFrontLeg.addBox(-1.5f, -7.0f, -1.5f, 3, 7, 3)
        leftFrontLeg.setInitialRotationPoint(4.5f, -4.0f, 9.0f)
        leftFrontLeg.setInitialRotationMatrix(
            Matrix4f().set(
                Quaternion(
                    0.17364818f,
                    0.0f,
                    0.0f,
                    0.9848077f
                )
            ).transpose()
        )
        leftFrontLeg.setTextureSize(128, 128)
        parts[leftFrontLeg.boxName] = leftFrontLeg
        bodyUpper.addChild(leftFrontLeg)

        val tail = MCAModelRenderer(this, "Tail", 58, 33)
        tail.mirror = false
        tail.addBox(-1.0f, -1.0f, -14.0f, 3, 3, 15)
        tail.setInitialRotationPoint(0.0f, 2.0f, -14.0f)
        tail.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.08715574f, 0.0f, 0.0f, 0.9961947f)).transpose())
        tail.setTextureSize(128, 128)
        parts[tail.boxName] = tail
        bodyLower.addChild(tail)

        val leftBackLeg = MCAModelRenderer(this, "LeftBackLeg", 0, 36)
        leftBackLeg.mirror = false
        leftBackLeg.addBox(-2.0f, -5.0f, -2.0f, 4, 6, 4)
        leftBackLeg.setInitialRotationPoint(3.0f, -3.5f, -11.0f)
        leftBackLeg.setInitialRotationMatrix(
            Matrix4f().set(
                Quaternion(
                    -0.25881904f,
                    0.0f,
                    0.0f,
                    0.9659258f
                )
            ).transpose()
        )
        leftBackLeg.setTextureSize(128, 128)
        parts[leftBackLeg.boxName] = leftBackLeg
        bodyLower.addChild(leftBackLeg)

        val rightBackLeg = MCAModelRenderer(this, "RightBackLeg", 0, 36)
        rightBackLeg.mirror = false
        rightBackLeg.addBox(-2.0f, -5.0f, -2.0f, 4, 6, 4)
        rightBackLeg.setInitialRotationPoint(-3.0f, -3.5f, -11.0f)
        rightBackLeg.setInitialRotationMatrix(
            Matrix4f().set(
                Quaternion(
                    -0.25881904f,
                    0.0f,
                    0.0f,
                    0.9659258f
                )
            ).transpose()
        )
        rightBackLeg.setTextureSize(128, 128)
        parts[rightBackLeg.boxName] = rightBackLeg
        bodyLower.addChild(rightBackLeg)

        val snoutUpper = MCAModelRenderer(this, "SnoutUpper", 34, 58)
        snoutUpper.mirror = false
        snoutUpper.addBox(-2.0f, 0.0f, -1.0f, 4, 2, 6)
        snoutUpper.setInitialRotationPoint(0.0f, -1.0f, 8.0f)
        snoutUpper.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        snoutUpper.setTextureSize(128, 128)
        parts[snoutUpper.boxName] = snoutUpper
        head.addChild(snoutUpper)

        val snoutLower = MCAModelRenderer(this, "SnoutLower", 58, 59)
        snoutLower.mirror = false
        snoutLower.addBox(-2.0f, -2.0f, -1.0f, 4, 2, 5)
        snoutLower.setInitialRotationPoint(0.0f, -1.0f, 8.0f)
        snoutLower.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.17364818f, 0.0f, 0.0f, 0.9848077f)).transpose())
        snoutLower.setTextureSize(128, 128)
        parts[snoutLower.boxName] = snoutLower
        head.addChild(snoutLower)

        val rightEar = MCAModelRenderer(this, "RightEar", 0, 0)
        rightEar.mirror = false
        rightEar.addBox(-1.0f, 0.0f, 0.0f, 2, 2, 1)
        rightEar.setInitialRotationPoint(-2.0f, 3.0f, 1.0f)
        rightEar.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        rightEar.setTextureSize(128, 128)
        parts[rightEar.boxName] = rightEar
        head.addChild(rightEar)

        val leftEar = MCAModelRenderer(this, "LeftEar", 0, 0)
        leftEar.mirror = false
        leftEar.addBox(-1.0f, 0.0f, 0.0f, 2, 2, 1)
        leftEar.setInitialRotationPoint(2.0f, 3.0f, 1.0f)
        leftEar.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        leftEar.setTextureSize(128, 128)
        parts[leftEar.boxName] = leftEar
        head.addChild(leftEar)

        val rightFrontFoot = MCAModelRenderer(this, "RightFrontFoot", 19, 24)
        rightFrontFoot.mirror = false
        rightFrontFoot.addBox(-1.5f, -6.0f, -1.0f, 3, 7, 3)
        rightFrontFoot.setInitialRotationPoint(0.0f, -7.0f, 0.0f)
        rightFrontFoot.setInitialRotationMatrix(
            Matrix4f().set(
                Quaternion(
                    -0.25881904f,
                    0.0f,
                    0.0f,
                    0.9659258f
                )
            ).transpose()
        )
        rightFrontFoot.setTextureSize(128, 128)
        parts[rightFrontFoot.boxName] = rightFrontFoot
        rightFrontLeg.addChild(rightFrontFoot)

        val lefttFrontFoot = MCAModelRenderer(this, "LefttFrontFoot", 19, 24)
        lefttFrontFoot.mirror = false
        lefttFrontFoot.addBox(-1.5f, -6.0f, -1.0f, 3, 7, 3)
        lefttFrontFoot.setInitialRotationPoint(0.0f, -7.0f, 0.0f)
        lefttFrontFoot.setInitialRotationMatrix(
            Matrix4f().set(
                Quaternion(
                    -0.25881904f,
                    0.0f,
                    0.0f,
                    0.9659258f
                )
            ).transpose()
        )
        lefttFrontFoot.setTextureSize(128, 128)
        parts[lefttFrontFoot.boxName] = lefttFrontFoot
        leftFrontLeg.addChild(lefttFrontFoot)

        val leftBackLowerLeg = MCAModelRenderer(this, "LeftBackLowerLeg", 19, 37)
        leftBackLowerLeg.mirror = false
        leftBackLowerLeg.addBox(-2.0f, -4.5f, -1.5f, 3, 6, 3)
        leftBackLowerLeg.setInitialRotationPoint(0.5f, -5.0f, 0.0f)
        leftBackLowerLeg.setInitialRotationMatrix(
            Matrix4f().set(
                Quaternion(
                    0.47715878f,
                    0.0f,
                    0.0f,
                    0.8788171f
                )
            ).transpose()
        )
        leftBackLowerLeg.setTextureSize(128, 128)
        parts[leftBackLowerLeg.boxName] = leftBackLowerLeg
        leftBackLeg.addChild(leftBackLowerLeg)

        val rightBackLowerLeg = MCAModelRenderer(this, "RightBackLowerLeg", 19, 37)
        rightBackLowerLeg.mirror = false
        rightBackLowerLeg.addBox(-2.0f, -4.5f, -1.5f, 3, 6, 3)
        rightBackLowerLeg.setInitialRotationPoint(0.5f, -5.0f, 0.0f)
        rightBackLowerLeg.setInitialRotationMatrix(
            Matrix4f().set(
                Quaternion(
                    0.47715878f,
                    0.0f,
                    0.0f,
                    0.8788171f
                )
            ).transpose()
        )
        rightBackLowerLeg.setTextureSize(128, 128)
        parts[rightBackLowerLeg.boxName] = rightBackLowerLeg
        rightBackLeg.addChild(rightBackLowerLeg)

        val leftBackFoot = MCAModelRenderer(this, "LeftBackFoot", 35, 38)
        leftBackFoot.mirror = false
        leftBackFoot.addBox(-1.0f, -5.0f, -1.5f, 3, 5, 3)
        leftBackFoot.setInitialRotationPoint(-1.0f, -4.0f, 0.0f)
        leftBackFoot.setInitialRotationMatrix(
            Matrix4f().set(
                Quaternion(
                    -0.2079117f,
                    0.0f,
                    0.0f,
                    0.9781476f
                )
            ).transpose()
        )
        leftBackFoot.setTextureSize(128, 128)
        parts[leftBackFoot.boxName] = leftBackFoot
        leftBackLowerLeg.addChild(leftBackFoot)

        val rightBackFoot = MCAModelRenderer(this, "RightBackFoot", 35, 38)
        rightBackFoot.mirror = false
        rightBackFoot.addBox(-1.0f, -5.0f, -1.5f, 3, 5, 3)
        rightBackFoot.setInitialRotationPoint(-1.0f, -4.0f, 0.0f)
        rightBackFoot.setInitialRotationMatrix(
            Matrix4f().set(
                Quaternion(
                    -0.2079117f,
                    0.0f,
                    0.0f,
                    0.9781476f
                )
            ).transpose()
        )
        rightBackFoot.setTextureSize(128, 128)
        parts[rightBackFoot.boxName] = rightBackFoot
        rightBackLowerLeg.addChild(rightBackFoot)

    }

    /**
     * Called every game tick to render the werewolf model
     *
     * @param entityIn        The entity to render, this must be an enchanted skeleton
     * @param limbSwing       ignored, used only by default MC
     * @param limbSwingAmount ignored, used only by default MC
     * @param ageInTicks      ignored, used only by default MC
     * @param netHeadYaw      ignored, used only by default MC
     * @param headPitch       ignored, used only by default MC
     * @param scale           The scale to render the model at
     */
    override fun render(
        entityIn: Entity?,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float,
        scale: Float
    ) {
        // Cast the entity to a werewolf model
        val entity = entityIn as EntityWerewolf?

        // Animate the model (moves all pieces from time t to t+1)
        AnimationHandler.performAnimationInModel(parts, entity)

        // Render the model
        bodyUpper.render(scale)
    }
}