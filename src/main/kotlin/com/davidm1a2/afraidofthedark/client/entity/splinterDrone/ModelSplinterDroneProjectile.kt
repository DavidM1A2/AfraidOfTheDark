package com.davidm1a2.afraidofthedark.client.entity.splinterDrone

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.setAndReturn
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.transposeAndReturn
import net.minecraft.client.renderer.entity.model.ModelBase
import net.minecraft.entity.Entity
import javax.vecmath.Matrix4f
import javax.vecmath.Quat4f

/**
 * Model class that defines the splinter drone projectile model
 *
 * @constructor initializes the model
 * @property parts A map of part name to part
 * @property body The different parts of the model
 */
class ModelSplinterDroneProjectile internal constructor() : ModelBase() {
    private val parts = mutableMapOf<String, MCAModelRenderer>()
    private val body: MCAModelRenderer

    init {
        // Auto-generated from the MCAnimator software

        textureWidth = 32
        textureHeight = 32

        body = MCAModelRenderer(this, "body", 0, 0)
        body.mirror = false
        body.addBox(-3.0f, -3.0f, -3.0f, 6, 6, 6)
        body.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        body.setInitialRotationMatrix(Matrix4f().setAndReturn(Quat4f(0.0f, 0.0f, 0.0f, 1.0f)).transposeAndReturn())
        body.setTextureSize(32, 32)
        parts[body.boxName] = body

        val part1 = MCAModelRenderer(this, "part1", 0, 16)
        part1.mirror = false
        part1.addBox(-2.0f, -2.0f, 0.0f, 4, 4, 4)
        part1.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        part1.setInitialRotationMatrix(Matrix4f().setAndReturn(Quat4f(0.0f, 0.0f, 0.0f, 1.0f)).transposeAndReturn())
        part1.setTextureSize(32, 32)
        parts[part1.boxName] = part1
        body.addChild(part1)

        val part2 = MCAModelRenderer(this, "part2", 0, 16)
        part2.mirror = false
        part2.addBox(0.0f, -2.0f, -2.0f, 4, 4, 4)
        part2.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        part2.setInitialRotationMatrix(Matrix4f().setAndReturn(Quat4f(0.0f, 0.0f, 0.0f, 1.0f)).transposeAndReturn())
        part2.setTextureSize(32, 32)
        parts[part2.boxName] = part2
        body.addChild(part2)

        val part3 = MCAModelRenderer(this, "part3", 0, 16)
        part3.mirror = false
        part3.addBox(-2.0f, -2.0f, -4.0f, 4, 4, 4)
        part3.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        part3.setInitialRotationMatrix(Matrix4f().setAndReturn(Quat4f(0.0f, 0.0f, 0.0f, 1.0f)).transposeAndReturn())
        part3.setTextureSize(32, 32)
        parts[part3.boxName] = part3
        body.addChild(part3)

        val part4 = MCAModelRenderer(this, "part4", 0, 16)
        part4.mirror = false
        part4.addBox(-4.0f, -2.0f, -2.0f, 4, 4, 4)
        part4.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        part4.setInitialRotationMatrix(Matrix4f().setAndReturn(Quat4f(0.0f, 0.0f, 0.0f, 1.0f)).transposeAndReturn())
        part4.setTextureSize(32, 32)
        parts[part4.boxName] = part4
        body.addChild(part4)

        val part5 = MCAModelRenderer(this, "part5", 0, 16)
        part5.mirror = false
        part5.addBox(-2.0f, 0.0f, -2.0f, 4, 4, 4)
        part5.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        part5.setInitialRotationMatrix(Matrix4f().setAndReturn(Quat4f(0.0f, 0.0f, 0.0f, 1.0f)).transposeAndReturn())
        part5.setTextureSize(32, 32)
        parts[part5.boxName] = part5
        body.addChild(part5)

        val part6 = MCAModelRenderer(this, "part6", 0, 16)
        part6.mirror = false
        part6.addBox(-2.0f, -4.0f, -2.0f, 4, 4, 4)
        part6.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        part6.setInitialRotationMatrix(Matrix4f().setAndReturn(Quat4f(0.0f, 0.0f, 0.0f, 1.0f)).transposeAndReturn())
        part6.setTextureSize(32, 32)
        parts[part6.boxName] = part6
        body.addChild(part6)
    }

    /**
     * Called every game tick to render the splinter drone projectile model
     *
     * @param entityIn        The entity to render, this must be a splinter drone projectile
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
