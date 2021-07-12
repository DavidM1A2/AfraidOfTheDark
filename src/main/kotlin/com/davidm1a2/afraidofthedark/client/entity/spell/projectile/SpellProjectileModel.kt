package com.davidm1a2.afraidofthedark.client.entity.spell.projectile

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.spell.projectile.SpellProjectileEntity
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.Quaternion
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.model.EntityModel

/**
 * Class representing the spell projectile entity model
 *
 * @constructor initializes the model
 * @property parts A map of part name to part
 * @property center The different parts of the model
 */
class SpellProjectileModel internal constructor() : EntityModel<SpellProjectileEntity>(RenderType::getEntityTranslucent) {
    private var parts = mutableMapOf<String, MCAModelRenderer>()
    private val center: MCAModelRenderer

    init {
        // Auto-generated from the MCAnimator software

        textureWidth = 32
        textureHeight = 32

        center = MCAModelRenderer(this, 0, 10)
        center.mirror = false
        center.addBox(-3.0f, -3.0f, -3.0f, 6f, 6f, 6f)
        center.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        center.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        center.setTextureSize(32, 32)
        parts["Center"] = center

        val one = MCAModelRenderer(this, 0, 0)
        one.mirror = false
        one.addBox(-2.0f, -2.0f, 0.0f, 4f, 4f, 4f)
        one.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        one.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        one.setTextureSize(32, 32)
        parts["one"] = one
        center.addChild(one)

        val two = MCAModelRenderer(this, 0, 0)
        two.mirror = false
        two.addBox(0.0f, -2.0f, -2.0f, 4f, 4f, 4f)
        two.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        two.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        two.setTextureSize(32, 32)
        parts["two"] = two
        center.addChild(two)

        val three = MCAModelRenderer(this, 0, 0)
        three.mirror = false
        three.addBox(-2.0f, -2.0f, -4.0f, 4f, 4f, 4f)
        three.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        three.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        three.setTextureSize(32, 32)
        parts["three"] = three
        center.addChild(three)

        val four = MCAModelRenderer(this, 0, 0)
        four.mirror = false
        four.addBox(-4.0f, -2.0f, -2.0f, 4f, 4f, 4f)
        four.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        four.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        four.setTextureSize(32, 32)
        parts["four"] = four
        center.addChild(four)

        val five = MCAModelRenderer(this, 0, 0)
        five.mirror = false
        five.addBox(-2.0f, -4.0f, -2.0f, 4f, 4f, 4f)
        five.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        five.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        five.setTextureSize(32, 32)
        parts["five"] = five
        center.addChild(five)

        val six = MCAModelRenderer(this, 0, 0)
        six.mirror = false
        six.addBox(-2.0f, 0.0f, -2.0f, 4f, 4f, 4f)
        six.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        six.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        six.setTextureSize(32, 32)
        parts["six"] = six
        center.addChild(six)
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
        center.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha)
    }

    override fun setRotationAngles(
        entity: SpellProjectileEntity,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    ) {
        (entity as IMCAnimatedModel).getAnimationHandler().performAnimationInModel(parts)
    }
}
