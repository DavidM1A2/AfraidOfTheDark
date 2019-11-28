package com.davidm1a2.afraidofthedark.client.entity.enaria

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedEntity
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.animation.AnimationHandler
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Matrix4f
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion
import net.minecraft.client.model.ModelBase
import net.minecraft.entity.Entity

/**
 * Model for the enaria and ghastly enaria entity
 *
 * @constructor initializes the model parts, this is created from MCAnimator
 * @property parts A map of part name to part
 * @property body The different parts of the model
 */
class ModelEnaria internal constructor() : ModelBase()
{
    private val parts = mutableMapOf<String, MCAModelRenderer>()
    private val body: MCAModelRenderer

    init
    {
        textureWidth = 64
        textureHeight = 64

        body = MCAModelRenderer(this, "body", 0, 16)
        body.mirror = false
        body.addBox(-4.0f, -12.0f, -2.0f, 8, 12, 4)
        body.setInitialRotationPoint(0.0f, 2.0f, 2.0f)
        body.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        body.setTextureSize(64, 64)
        parts[body.boxName] = body

        val rightarm = MCAModelRenderer(this, "rightarm", 41, 16)
        rightarm.mirror = false
        rightarm.addBox(-3.0f, -10.0f, -2.0f, 3, 12, 4)
        rightarm.setInitialRotationPoint(-4.0f, -2.0f, 0.0f)
        rightarm.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        rightarm.setTextureSize(64, 64)
        parts[rightarm.boxName] = rightarm
        body.addChild(rightarm)

        val leftarm = MCAModelRenderer(this, "leftarm", 25, 16)
        leftarm.mirror = false
        leftarm.addBox(0.0f, -10.0f, -2.0f, 3, 12, 4)
        leftarm.setInitialRotationPoint(4.0f, -2.0f, 0.0f)
        leftarm.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        leftarm.setTextureSize(64, 64)
        parts[leftarm.boxName] = leftarm
        body.addChild(leftarm)

        val rightleg = MCAModelRenderer(this, "rightleg", 16, 32)
        rightleg.mirror = false
        rightleg.addBox(-2.0f, -12.0f, -2.0f, 4, 12, 4)
        rightleg.setInitialRotationPoint(-2.0f, -12.0f, 0.0f)
        rightleg.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        rightleg.setTextureSize(64, 64)
        parts[rightleg.boxName] = rightleg
        body.addChild(rightleg)

        val leftleg = MCAModelRenderer(this, "leftleg", 0, 32)
        leftleg.mirror = false
        leftleg.addBox(-2.0f, -12.0f, -2.0f, 4, 12, 4)
        leftleg.setInitialRotationPoint(2.0f, -12.0f, 0.0f)
        leftleg.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        leftleg.setTextureSize(64, 64)
        parts[leftleg.boxName] = leftleg
        body.addChild(leftleg)

        val head = MCAModelRenderer(this, "head", 0, 0)
        head.mirror = false
        head.addBox(-4.0f, 0.0f, -4.0f, 8, 8, 8)
        head.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        head.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        head.setTextureSize(64, 64)
        parts[head.boxName] = head
        body.addChild(head)
    }

    /**
     * Called every game tick to render the enaria model
     *
     * @param entityIn        The entity to render, this must be an enaria entity
     * @param limbSwing       ignored, used only by default MC
     * @param limbSwingAmount ignored, used only by default MC
     * @param ageInTicks      ignored, used only by default MC
     * @param netHeadYaw      ignored, used only by default MC
     * @param headPitch       ignored, used only by default MC
     * @param scale           The scale to render the model at
     */
    override fun render(entityIn: Entity?, limbSwing: Float, limbSwingAmount: Float, ageInTicks: Float, netHeadYaw: Float, headPitch: Float, scale: Float)
    {
        // Grab the enaria entity
        val enariaEntity = entityIn as IMCAnimatedEntity?

        // Perform the animation
        AnimationHandler.performAnimationInModel(parts, enariaEntity)

        // Render every non-child part
        body.render(scale)
    }
}
