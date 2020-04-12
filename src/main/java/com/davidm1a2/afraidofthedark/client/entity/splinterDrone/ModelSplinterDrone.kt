package com.davidm1a2.afraidofthedark.client.entity.splinterDrone

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Matrix4f
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion
import net.minecraft.client.model.ModelBase
import net.minecraft.entity.Entity

/**
 * Model class that defines the splinter drone model
 *
 * @constructor initializes the model
 * @property parts A map of part name to part
 * @property body The different parts of the model
 */
class ModelSplinterDrone internal constructor() : ModelBase() {
    private val parts = mutableMapOf<String, MCAModelRenderer>()
    private val body: MCAModelRenderer

    init {
        // Auto-generated from the MCAnimator software

        textureWidth = 64
        textureHeight = 64

        body = MCAModelRenderer(this, "Body", 18, 0)
        body.mirror = false
        body.addBox(-4.0f, -12.0f, -4.0f, 8, 24, 8)
        body.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        body.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        body.setTextureSize(128, 128)
        parts[body.boxName] = body

        val bodyPlate1 = MCAModelRenderer(this, "BodyPlate1", 0, 34)
        bodyPlate1.mirror = false
        bodyPlate1.addBox(-3.0f, -11.0f, -1.0f, 6, 22, 2)
        bodyPlate1.setInitialRotationPoint(0.0f, 0.0f, 4.0f)
        bodyPlate1.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        bodyPlate1.setTextureSize(128, 128)
        parts[bodyPlate1.boxName] = bodyPlate1
        body.addChild(bodyPlate1)

        val bodyPlate3 = MCAModelRenderer(this, "BodyPlate3", 0, 34)
        bodyPlate3.mirror = false
        bodyPlate3.addBox(-3.0f, -11.0f, -1.0f, 6, 22, 2)
        bodyPlate3.setInitialRotationPoint(-4.0f, 0.0f, 0.0f)
        bodyPlate3.setInitialRotationMatrix(
            Matrix4f().set(
                Quaternion(
                    0.0f,
                    0.70710677f,
                    0.0f,
                    0.70710677f
                )
            ).transpose()
        )
        bodyPlate3.setTextureSize(128, 128)
        parts[bodyPlate3.boxName] = bodyPlate3
        body.addChild(bodyPlate3)

        val bodyPlate4 = MCAModelRenderer(this, "BodyPlate4", 0, 34)
        bodyPlate4.mirror = false
        bodyPlate4.addBox(-3.0f, -11.0f, -1.0f, 6, 22, 2)
        bodyPlate4.setInitialRotationPoint(0.0f, 0.0f, -4.0f)
        bodyPlate4.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        bodyPlate4.setTextureSize(128, 128)
        parts[bodyPlate4.boxName] = bodyPlate4
        body.addChild(bodyPlate4)

        val bodyPlate2 = MCAModelRenderer(this, "BodyPlate2", 0, 34)
        bodyPlate2.mirror = false
        bodyPlate2.addBox(-3.0f, -11.0f, -1.0f, 6, 22, 2)
        bodyPlate2.setInitialRotationPoint(4.0f, 0.0f, 0.0f)
        bodyPlate2.setInitialRotationMatrix(
            Matrix4f().set(
                Quaternion(
                    0.0f,
                    -0.70710677f,
                    0.0f,
                    0.70710677f
                )
            ).transpose()
        )
        bodyPlate2.setTextureSize(128, 128)
        parts[bodyPlate2.boxName] = bodyPlate2
        body.addChild(bodyPlate2)

        val bottomPlate = MCAModelRenderer(this, "BottomPlate", 18, 50)
        bottomPlate.mirror = false
        bottomPlate.addBox(-3.0f, -1.0f, -3.0f, 6, 2, 6)
        bottomPlate.setInitialRotationPoint(0.0f, -12.0f, 0.0f)
        bottomPlate.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        bottomPlate.setTextureSize(128, 128)
        parts[bottomPlate.boxName] = bottomPlate
        body.addChild(bottomPlate)

        val topPlate = MCAModelRenderer(this, "TopPlate", 18, 50)
        topPlate.mirror = false
        topPlate.addBox(-3.0f, -1.0f, -3.0f, 6, 2, 6)
        topPlate.setInitialRotationPoint(0.0f, 12.0f, 0.0f)
        topPlate.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        topPlate.setTextureSize(128, 128)
        parts[topPlate.boxName] = topPlate
        body.addChild(topPlate)

        val sphere1 = MCAModelRenderer(this, "Sphere1", 18, 40)
        sphere1.mirror = false
        sphere1.addBox(-2.0f, 21.0f, -2.0f, 4, 4, 4)
        sphere1.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        sphere1.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        sphere1.setTextureSize(128, 128)
        parts[sphere1.boxName] = sphere1
        body.addChild(sphere1)

        val sphere2 = MCAModelRenderer(this, "Sphere2", 18, 40)
        sphere2.mirror = false
        sphere2.addBox(-2.0f, -25.0f, -2.0f, 4, 4, 4)
        sphere2.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        sphere2.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        sphere2.setTextureSize(128, 128)
        parts[sphere2.boxName] = sphere2
        body.addChild(sphere2)

        val pillar1 = MCAModelRenderer(this, "Pillar1", 6, 0)
        pillar1.mirror = false
        pillar1.addBox(-0.5f, -10.0f, 8.0f, 1, 20, 1)
        pillar1.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        pillar1.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        pillar1.setTextureSize(128, 128)
        parts[pillar1.boxName] = pillar1
        body.addChild(pillar1)

        val pillar2 = MCAModelRenderer(this, "Pillar2", 0, 0)
        pillar2.mirror = false
        pillar2.addBox(-0.5f, -9.0f, 10.0f, 1, 18, 1)
        pillar2.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        pillar2.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.38268346f, 0.0f, 0.9238795f)).transpose())
        pillar2.setTextureSize(128, 128)
        parts[pillar2.boxName] = pillar2
        body.addChild(pillar2)

        val pillar3 = MCAModelRenderer(this, "Pillar3", 6, 0)
        pillar3.mirror = false
        pillar3.addBox(-0.5f, -10.0f, 8.0f, 1, 20, 1)
        pillar3.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        pillar3.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.70710677f, 0.0f, 0.70710677f)).transpose())
        pillar3.setTextureSize(128, 128)
        parts[pillar3.boxName] = pillar3
        body.addChild(pillar3)

        val pillar4 = MCAModelRenderer(this, "Pillar4", 0, 0)
        pillar4.mirror = false
        pillar4.addBox(-0.5f, -9.0f, 10.0f, 1, 18, 1)
        pillar4.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        pillar4.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.9238795f, 0.0f, 0.38268343f)).transpose())
        pillar4.setTextureSize(128, 128)
        parts[pillar4.boxName] = pillar4
        body.addChild(pillar4)

        val pillar5 = MCAModelRenderer(this, "Pillar5", 6, 0)
        pillar5.mirror = false
        pillar5.addBox(-0.5f, -10.0f, 8.0f, 1, 20, 1)
        pillar5.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        pillar5.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, -1.0f, 0.0f, -4.371139E-8f)).transpose())
        pillar5.setTextureSize(128, 128)
        parts[pillar5.boxName] = pillar5
        body.addChild(pillar5)

        val pillar6 = MCAModelRenderer(this, "Pillar6", 0, 0)
        pillar6.mirror = false
        pillar6.addBox(-0.5f, -9.0f, 10.0f, 1, 18, 1)
        pillar6.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        pillar6.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, -0.9238795f, 0.0f, 0.38268343f)).transpose())
        pillar6.setTextureSize(128, 128)
        parts[pillar6.boxName] = pillar6
        body.addChild(pillar6)

        val pillar8 = MCAModelRenderer(this, "Pillar8", 0, 0)
        pillar8.mirror = false
        pillar8.addBox(-0.5f, -9.0f, 10.0f, 1, 18, 1)
        pillar8.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        pillar8.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, -0.38268346f, 0.0f, 0.9238795f)).transpose())
        pillar8.setTextureSize(128, 128)
        parts[pillar8.boxName] = pillar8
        body.addChild(pillar8)

        val pillar7 = MCAModelRenderer(this, "Pillar7", 6, 0)
        pillar7.mirror = false
        pillar7.addBox(-0.5f, -10.0f, 8.0f, 1, 20, 1)
        pillar7.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        pillar7.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, -0.70710677f, 0.0f, 0.70710677f)).transpose())
        pillar7.setTextureSize(128, 128)
        parts[pillar7.boxName] = pillar7
        body.addChild(pillar7)

    }

    /**
     * Called every game tick to render the splinter drone model
     *
     * @param entityIn        The entity to render, this must be a splinter drone
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