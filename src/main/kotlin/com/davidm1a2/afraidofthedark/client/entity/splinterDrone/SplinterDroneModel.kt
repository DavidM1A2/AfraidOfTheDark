package com.davidm1a2.afraidofthedark.client.entity.splinterDrone

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.SplinterDroneEntity
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.Quaternion
import net.minecraft.client.renderer.entity.model.EntityModel

/**
 * Model class that defines the splinter drone model
 *
 * @constructor initializes the model
 * @property parts A map of part name to part
 * @property body The different parts of the model
 */
class SplinterDroneModel internal constructor() : EntityModel<SplinterDroneEntity>() {
    private val parts = mutableMapOf<String, MCAModelRenderer>()
    private val body: MCAModelRenderer

    init {
        // Auto-generated from the MCAnimator software

        textureWidth = 64
        textureHeight = 64

        body = MCAModelRenderer(this, 18, 0)
        body.mirror = false
        body.addBox(-4.0f, -12.0f, -4.0f, 8f, 24f, 8f)
        body.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        body.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        body.setTextureSize(64, 64)
        parts["Body"] = body

        val bodyPlate1 = MCAModelRenderer(this, 0, 34)
        bodyPlate1.mirror = false
        bodyPlate1.addBox(-3.0f, -11.0f, -1.0f, 6f, 22f, 2f)
        bodyPlate1.setInitialRotationPoint(0.0f, 0.0f, 4.0f)
        bodyPlate1.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        bodyPlate1.setTextureSize(64, 64)
        parts["BodyPlate1"] = bodyPlate1
        body.addChild(bodyPlate1)

        val bodyPlate3 = MCAModelRenderer(this, 0, 34)
        bodyPlate3.mirror = false
        bodyPlate3.addBox(-3.0f, -11.0f, -1.0f, 6f, 22f, 2f)
        bodyPlate3.setInitialRotationPoint(-4.0f, 0.0f, 0.0f)
        bodyPlate3.setInitialRotationQuaternion(Quaternion(0.0f, 0.70710677f, 0.0f, 0.70710677f))
        bodyPlate3.setTextureSize(64, 64)
        parts["BodyPlate3"] = bodyPlate3
        body.addChild(bodyPlate3)

        val bodyPlate4 = MCAModelRenderer(this, 0, 34)
        bodyPlate4.mirror = false
        bodyPlate4.addBox(-3.0f, -11.0f, -1.0f, 6f, 22f, 2f)
        bodyPlate4.setInitialRotationPoint(0.0f, 0.0f, -4.0f)
        bodyPlate4.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        bodyPlate4.setTextureSize(64, 64)
        parts["BodyPlate4"] = bodyPlate4
        body.addChild(bodyPlate4)

        val bodyPlate2 = MCAModelRenderer(this, 0, 34)
        bodyPlate2.mirror = false
        bodyPlate2.addBox(-3.0f, -11.0f, -1.0f, 6f, 22f, 2f)
        bodyPlate2.setInitialRotationPoint(4.0f, 0.0f, 0.0f)
        bodyPlate2.setInitialRotationQuaternion(Quaternion(0.0f, -0.70710677f, 0.0f, 0.70710677f))
        bodyPlate2.setTextureSize(64, 64)
        parts["BodyPlate2"] = bodyPlate2
        body.addChild(bodyPlate2)

        val bottomPlate = MCAModelRenderer(this, 18, 50)
        bottomPlate.mirror = false
        bottomPlate.addBox(-3.0f, -1.0f, -3.0f, 6f, 2f, 6f)
        bottomPlate.setInitialRotationPoint(0.0f, -12.0f, 0.0f)
        bottomPlate.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        bottomPlate.setTextureSize(64, 64)
        parts["BottomPlate"] = bottomPlate
        body.addChild(bottomPlate)

        val topPlate = MCAModelRenderer(this, 18, 50)
        topPlate.mirror = false
        topPlate.addBox(-3.0f, -1.0f, -3.0f, 6f, 2f, 6f)
        topPlate.setInitialRotationPoint(0.0f, 12.0f, 0.0f)
        topPlate.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        topPlate.setTextureSize(64, 64)
        parts["TopPlate"] = topPlate
        body.addChild(topPlate)

        val sphere1 = MCAModelRenderer(this, 18, 40)
        sphere1.mirror = false
        sphere1.addBox(-2.0f, 21.0f, -2.0f, 4f, 4f, 4f)
        sphere1.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        sphere1.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        sphere1.setTextureSize(64, 64)
        parts["Sphere1"] = sphere1
        body.addChild(sphere1)

        val sphere2 = MCAModelRenderer(this, 18, 40)
        sphere2.mirror = false
        sphere2.addBox(-2.0f, -25.0f, -2.0f, 4f, 4f, 4f)
        sphere2.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        sphere2.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        sphere2.setTextureSize(64, 64)
        parts["Sphere2"] = sphere2
        body.addChild(sphere2)

        val pillar1 = MCAModelRenderer(this, 6, 0)
        pillar1.mirror = false
        pillar1.addBox(-0.5f, -10.0f, 8.0f, 1f, 20f, 1f)
        pillar1.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        pillar1.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        pillar1.setTextureSize(64, 64)
        parts["Pillar1"] = pillar1
        body.addChild(pillar1)

        val pillar2 = MCAModelRenderer(this, 0, 0)
        pillar2.mirror = false
        pillar2.addBox(-0.5f, -9.0f, 10.0f, 1f, 18f, 1f)
        pillar2.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        pillar2.setInitialRotationQuaternion(Quaternion(0.0f, 0.38268346f, 0.0f, 0.9238795f))
        pillar2.setTextureSize(64, 64)
        parts["Pillar2"] = pillar2
        body.addChild(pillar2)

        val pillar3 = MCAModelRenderer(this, 6, 0)
        pillar3.mirror = false
        pillar3.addBox(-0.5f, -10.0f, 8.0f, 1f, 20f, 1f)
        pillar3.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        pillar3.setInitialRotationQuaternion(Quaternion(0.0f, 0.70710677f, 0.0f, 0.70710677f))
        pillar3.setTextureSize(64, 64)
        parts["Pillar3"] = pillar3
        body.addChild(pillar3)

        val pillar4 = MCAModelRenderer(this, 0, 0)
        pillar4.mirror = false
        pillar4.addBox(-0.5f, -9.0f, 10.0f, 1f, 18f, 1f)
        pillar4.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        pillar4.setInitialRotationQuaternion(Quaternion(0.0f, 0.9238795f, 0.0f, 0.38268343f))
        pillar4.setTextureSize(64, 64)
        parts["Pillar4"] = pillar4
        body.addChild(pillar4)

        val pillar5 = MCAModelRenderer(this, 6, 0)
        pillar5.mirror = false
        pillar5.addBox(-0.5f, -10.0f, 8.0f, 1f, 20f, 1f)
        pillar5.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        pillar5.setInitialRotationQuaternion(Quaternion(0.0f, -1.0f, 0.0f, -4.371139E-8f))
        pillar5.setTextureSize(64, 64)
        parts["Pillar5"] = pillar5
        body.addChild(pillar5)

        val pillar6 = MCAModelRenderer(this, 0, 0)
        pillar6.mirror = false
        pillar6.addBox(-0.5f, -9.0f, 10.0f, 1f, 18f, 1f)
        pillar6.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        pillar6.setInitialRotationQuaternion(Quaternion(0.0f, -0.9238795f, 0.0f, 0.38268343f))
        pillar6.setTextureSize(64, 64)
        parts["Pillar6"] = pillar6
        body.addChild(pillar6)

        val pillar8 = MCAModelRenderer(this, 0, 0)
        pillar8.mirror = false
        pillar8.addBox(-0.5f, -9.0f, 10.0f, 1f, 18f, 1f)
        pillar8.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        pillar8.setInitialRotationQuaternion(Quaternion(0.0f, -0.38268346f, 0.0f, 0.9238795f))
        pillar8.setTextureSize(64, 64)
        parts["Pillar8"] = pillar8
        body.addChild(pillar8)

        val pillar7 = MCAModelRenderer(this, 6, 0)
        pillar7.mirror = false
        pillar7.addBox(-0.5f, -10.0f, 8.0f, 1f, 20f, 1f)
        pillar7.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        pillar7.setInitialRotationQuaternion(Quaternion(0.0f, -0.70710677f, 0.0f, 0.70710677f))
        pillar7.setTextureSize(64, 64)
        parts["Pillar7"] = pillar7
        body.addChild(pillar7)

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
        body.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha)
    }

    override fun setRotationAngles(
        entity: SplinterDroneEntity,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    ) {
        (entity as IMCAnimatedModel).getAnimationHandler().performAnimationInModel(parts)
    }
}