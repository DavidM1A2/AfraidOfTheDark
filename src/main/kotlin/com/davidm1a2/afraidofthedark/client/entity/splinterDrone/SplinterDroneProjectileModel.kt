package com.davidm1a2.afraidofthedark.client.entity.splinterDrone

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.SplinterDroneProjectileEntity
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.entity.model.EntityModel
import net.minecraft.util.math.vector.Quaternion

/**
 * Model class that defines the splinter drone projectile model
 *
 * @constructor initializes the model
 * @property parts A map of part name to part
 * @property body The different parts of the model
 */
class SplinterDroneProjectileModel internal constructor() : EntityModel<SplinterDroneProjectileEntity>() {
    private val parts = mutableMapOf<String, MCAModelRenderer>()
    private val body: MCAModelRenderer

    init {
        // Auto-generated from the MCAnimator software

        texWidth = 32
        texHeight = 32

        body = MCAModelRenderer(this, 0, 0)
        body.mirror = false
        body.addBox(-3.0f, -3.0f, -3.0f, 6f, 6f, 6f)
        body.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        body.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        body.setTexSize(32, 32)
        parts["body"] = body

        val part1 = MCAModelRenderer(this, 0, 16)
        part1.mirror = false
        part1.addBox(-2.0f, -2.0f, 0.0f, 4f, 4f, 4f)
        part1.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        part1.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        part1.setTexSize(32, 32)
        parts["part1"] = part1
        body.addChild(part1)

        val part2 = MCAModelRenderer(this, 0, 16)
        part2.mirror = false
        part2.addBox(0.0f, -2.0f, -2.0f, 4f, 4f, 4f)
        part2.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        part2.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        part2.setTexSize(32, 32)
        parts["part2"] = part2
        body.addChild(part2)

        val part3 = MCAModelRenderer(this, 0, 16)
        part3.mirror = false
        part3.addBox(-2.0f, -2.0f, -4.0f, 4f, 4f, 4f)
        part3.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        part3.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        part3.setTexSize(32, 32)
        parts["part3"] = part3
        body.addChild(part3)

        val part4 = MCAModelRenderer(this, 0, 16)
        part4.mirror = false
        part4.addBox(-4.0f, -2.0f, -2.0f, 4f, 4f, 4f)
        part4.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        part4.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        part4.setTexSize(32, 32)
        parts["part4"] = part4
        body.addChild(part4)

        val part5 = MCAModelRenderer(this, 0, 16)
        part5.mirror = false
        part5.addBox(-2.0f, 0.0f, -2.0f, 4f, 4f, 4f)
        part5.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        part5.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        part5.setTexSize(32, 32)
        parts["part5"] = part5
        body.addChild(part5)

        val part6 = MCAModelRenderer(this, 0, 16)
        part6.mirror = false
        part6.addBox(-2.0f, -4.0f, -2.0f, 4f, 4f, 4f)
        part6.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        part6.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        part6.setTexSize(32, 32)
        parts["part6"] = part6
        body.addChild(part6)
    }

    override fun renderToBuffer(
        matrixStack: MatrixStack,
        vertexBuilder: IVertexBuilder,
        packedLight: Int,
        packedOverlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        body.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha)
    }

    override fun setupAnim(
        entity: SplinterDroneProjectileEntity,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    ) {
        (entity as IMCAnimatedModel).getAnimationHandler().performAnimationInModel(parts)
    }
}
