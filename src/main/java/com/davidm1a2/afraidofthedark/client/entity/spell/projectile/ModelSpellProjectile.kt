package com.davidm1a2.afraidofthedark.client.entity.spell.projectile

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Matrix4f
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.math.Quaternion
import net.minecraft.client.model.ModelBase
import net.minecraft.entity.Entity

/**
 * Class representing the spell projectile entity model
 *
 * @constructor initializes the model
 * @property parts A map of part name to part
 * @property center The different parts of the model
 */
class ModelSpellProjectile internal constructor() : ModelBase() {
    private var parts = mutableMapOf<String, MCAModelRenderer>()
    private val center: MCAModelRenderer

    init {
        // Auto-generated from the MCAnimator software

        textureWidth = 32
        textureHeight = 32

        center = MCAModelRenderer(this, "Center", 0, 10)
        center.mirror = false
        center.addBox(-3.0f, -3.0f, -3.0f, 6, 6, 6)
        center.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        center.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        center.setTextureSize(32, 32)
        parts[center.boxName] = center

        val one = MCAModelRenderer(this, "one", 0, 0)
        one.mirror = false
        one.addBox(-2.0f, -2.0f, 0.0f, 4, 4, 4)
        one.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        one.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        one.setTextureSize(32, 32)
        parts[one.boxName] = one
        center.addChild(one)

        val two = MCAModelRenderer(this, "two", 0, 0)
        two.mirror = false
        two.addBox(0.0f, -2.0f, -2.0f, 4, 4, 4)
        two.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        two.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        two.setTextureSize(32, 32)
        parts[two.boxName] = two
        center.addChild(two)

        val three = MCAModelRenderer(this, "three", 0, 0)
        three.mirror = false
        three.addBox(-2.0f, -2.0f, -4.0f, 4, 4, 4)
        three.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        three.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        three.setTextureSize(32, 32)
        parts[three.boxName] = three
        center.addChild(three)

        val four = MCAModelRenderer(this, "four", 0, 0)
        four.mirror = false
        four.addBox(-4.0f, -2.0f, -2.0f, 4, 4, 4)
        four.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        four.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        four.setTextureSize(32, 32)
        parts[four.boxName] = four
        center.addChild(four)

        val five = MCAModelRenderer(this, "five", 0, 0)
        five.mirror = false
        five.addBox(-2.0f, -4.0f, -2.0f, 4, 4, 4)
        five.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        five.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        five.setTextureSize(32, 32)
        parts[five.boxName] = five
        center.addChild(five)

        val six = MCAModelRenderer(this, "six", 0, 0)
        six.mirror = false
        six.addBox(-2.0f, 0.0f, -2.0f, 4, 4, 4)
        six.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        six.setInitialRotationMatrix(Matrix4f().set(Quaternion(0.0f, 0.0f, 0.0f, 1.0f)).transpose())
        six.setTextureSize(32, 32)
        parts[six.boxName] = six
        center.addChild(six)
    }

    /**
     * Called every game tick to render the delivery method projectile model
     *
     * @param entityIn        The entity to render, this must be a delivery method projectile
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
        center.render(scale)
    }
}
