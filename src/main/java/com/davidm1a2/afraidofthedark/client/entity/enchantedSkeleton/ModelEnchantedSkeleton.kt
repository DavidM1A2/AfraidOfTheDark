/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.davidm1a2.afraidofthedark.client.entity.enchantedSkeleton

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Matrix4f
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion
import net.minecraft.client.model.ModelBase
import net.minecraft.entity.Entity

/**
 * Model class that defines the enchanted skeleton model
 *
 * @constructor initializes the model
 * @property parts A map of part name to part
 * @property body The different parts of the model
 */
class ModelEnchantedSkeleton internal constructor() : ModelBase() {
    private val parts = mutableMapOf<String, MCAModelRenderer>()
    private val body: MCAModelRenderer

    init {
        // Auto-generated from the MCAnimator software

        textureWidth = 64
        textureHeight = 32

        body = MCAModelRenderer(this, "body", 16, 16)
        body.mirror = false
        body.addBox(-4.0f, -12.0f, -2.0f, 8, 12, 4)
        body.setInitialRotationPoint(0.0f, 2.0f, 2.0f)
        body.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        body.setTextureSize(64, 32)
        parts[body.boxName] = body

        val head = MCAModelRenderer(this, "head", 0, 0)
        head.mirror = false
        head.addBox(-4.0f, 0.0f, -4.0f, 8, 8, 8)
        head.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        head.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        head.setTextureSize(64, 32)
        parts[head.boxName] = head
        body.addChild(head)

        val rightarm = MCAModelRenderer(this, "rightarm", 40, 16)
        rightarm.mirror = false
        rightarm.addBox(-2.0f, -10.0f, -1.0f, 2, 12, 2)
        rightarm.setInitialRotationPoint(-4.0f, -2.0f, 0.0f)
        rightarm.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        rightarm.setTextureSize(64, 32)
        parts[rightarm.boxName] = rightarm
        body.addChild(rightarm)

        val leftarm = MCAModelRenderer(this, "leftarm", 40, 16)
        leftarm.mirror = false
        leftarm.addBox(0.0f, -10.0f, -1.0f, 2, 12, 2)
        leftarm.setInitialRotationPoint(4.0f, -2.0f, 0.0f)
        leftarm.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        leftarm.setTextureSize(64, 32)
        parts[leftarm.boxName] = leftarm
        body.addChild(leftarm)

        val rightleg = MCAModelRenderer(this, "rightleg", 0, 16)
        rightleg.mirror = false
        rightleg.addBox(-1.0f, -12.0f, -1.0f, 2, 12, 2)
        rightleg.setInitialRotationPoint(-2.0f, -12.0f, 0.0f)
        rightleg.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        rightleg.setTextureSize(64, 32)
        parts[rightleg.boxName] = rightleg
        body.addChild(rightleg)

        val leftleg = MCAModelRenderer(this, "leftleg", 0, 16)
        leftleg.mirror = false
        leftleg.addBox(-1.0f, -12.0f, -1.0f, 2, 12, 2)
        leftleg.setInitialRotationPoint(2.0f, -12.0f, 0.0f)
        leftleg.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        leftleg.setTextureSize(64, 32)
        parts[leftleg.boxName] = leftleg
        body.addChild(leftleg)

        val heart = MCAModelRenderer(this, "heart", 48, 4)
        heart.mirror = false
        heart.addBox(-1.5f, -2.0f, -1.0f, 3, 3, 2)
        heart.setInitialRotationPoint(0.0f, -3.0f, 0.0f)
        heart.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        heart.setTextureSize(64, 32)
        parts[heart.boxName] = heart
        body.addChild(heart)
    }

    /**
     * Called every game tick to render the skeleton model
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
        entityIn: Entity,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float,
        scale: Float
    ) {
        // Perform the animation
        (entityIn as IMCAnimatedModel).getAnimationHandler().performAnimationInModel(parts)

        // Render the model in its current state
        body.render(scale)
    }
}