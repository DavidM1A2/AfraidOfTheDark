package com.davidm1a2.afraidofthedark.client.entity.enchantedFrog

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Matrix4f
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion
import net.minecraft.client.model.ModelBase
import net.minecraft.entity.Entity


/**
 * Model class that defines the enchanted frog model
 *
 * @constructor initializes the model
 * @property parts A map of part name to part
 * @property frogBody The different parts of the model
 */
class ModelEnchantedFrog internal constructor() : ModelBase() {
    private val parts = mutableMapOf<String, MCAModelRenderer>()
    private val frogBody: MCAModelRenderer

    init {
        // Auto-generated from the MCAnimator software

        textureWidth = 32
        textureHeight = 32

        frogBody = MCAModelRenderer(this, "frogBody", 0, 0)
        frogBody.mirror = false
        frogBody.addBox(-4.0f, 0.0f, -3.0f, 8, 5, 6)
        frogBody.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        frogBody.setInitialRotationMatrix(
            Matrix4f().set(
                Quaternion(0.0f, 0.0f, 0.0f, 1.0f)
            ).transpose()
        )
        frogBody.setTextureSize(32, 32)
        parts[frogBody.boxName] = frogBody

        val frogLLeg = MCAModelRenderer(this, "frogLLeg", 0, 11)
        frogLLeg.mirror = false
        frogLLeg.addBox(0.0f, 0.0f, 0.0f, 2, 2, 5)
        frogLLeg.setInitialRotationPoint(4.0f, 0.0f, -3.0f)
        frogLLeg.setInitialRotationMatrix(
            Matrix4f().set(
                Quaternion(
                    0.27059805f,
                    0.27059805f,
                    0.65328145f,
                    0.65328145f
                )
            ).transpose()
        )
        frogLLeg.setTextureSize(32, 32)
        parts[frogLLeg.boxName] = frogLLeg
        frogBody.addChild(frogLLeg)

        val frogRLeg = MCAModelRenderer(this, "frogRLeg", 0, 11)
        frogRLeg.mirror = false
        frogRLeg.addBox(0.0f, 0.0f, 0.0f, 2, 2, 5)
        frogRLeg.setInitialRotationPoint(-4.0f, 0.0f, -3.0f)
        frogRLeg.setInitialRotationMatrix(
            Matrix4f().set(
                Quaternion(
                    0.0f,
                    -0.38268346f,
                    0.0f,
                    0.9238795f
                )
            ).transpose()
        )
        frogRLeg.setTextureSize(32, 32)
        parts[frogRLeg.boxName] = frogRLeg
        frogBody.addChild(frogRLeg)

        val frogMouth = MCAModelRenderer(this, "frogMouth", 0, 18)
        frogMouth.mirror = false
        frogMouth.addBox(0.0f, 0.0f, 0.0f, 8, 2, 5)
        frogMouth.setInitialRotationPoint(-4.0f, 0.0f, 3.0f)
        frogMouth.setInitialRotationMatrix(
            Matrix4f().set(
                Quaternion(
                    -0.08715574f,
                    0.0f,
                    0.0f,
                    0.9961947f
                )
            ).transpose()
        )
        frogMouth.setTextureSize(32, 32)
        parts[frogMouth.boxName] = frogMouth
        frogBody.addChild(frogMouth)

        val frogHead = MCAModelRenderer(this, "frogHead", 0, 25)
        frogHead.mirror = false
        frogHead.addBox(0.0f, 0.0f, 0.0f, 8, 5, 3)
        frogHead.setInitialRotationPoint(-4.0f, 5.0f, 2.0f)
        frogHead.setInitialRotationMatrix(
            Matrix4f().set(
                Quaternion(
                    0.6427876f,
                    0.0f,
                    0.0f,
                    0.76604444f
                )
            ).transpose()
        )
        frogHead.setTextureSize(32, 32)
        parts[frogHead.boxName] = frogHead
        frogBody.addChild(frogHead)
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
        frogBody.render(scale)
    }
}