package com.davidm1a2.afraidofthedark.client.tileEntity.enariasAltar

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Quaternion
import net.minecraft.client.model.Model
import net.minecraft.client.renderer.RenderType


/**
 * Model for the enaria's altar
 *
 * @constructor initializes the model parts, this is created from MCAnimator
 * @property parts A map of part name to part
 * @property altar The different parts of the model
 */
class EnariasAltarTileEntityModel internal constructor() : Model(RenderType::entityCutoutNoCull) {
    private val parts = mutableMapOf<String, MCAModelRenderer>()
    private val altar: MCAModelRenderer

    init {
        altar = MCAModelRenderer(0, 0, 64f, 64f)
        altar.addBox(-8.0f, 0.0f, -8.0f, 16f, 4f, 16f)
        altar.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        altar.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["Altar"] = altar

        val spike1 = MCAModelRenderer(48, 51, 64f, 64f)
        spike1.addBox(2.5f, 0.0f, 2.5f, 1f, 1f, 1f)
        spike1.setInitialRotationPoint(0.0f, 6.5f, 0.0f)
        spike1.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["spike1"] = spike1
        altar.addChild(spike1)

        val spike2 = MCAModelRenderer(52, 53, 64f, 64f)
        spike2.addBox(-3.5f, 0.0f, 2.5f, 1f, 1f, 1f)
        spike2.setInitialRotationPoint(0.0f, 7.0f, 0.0f)
        spike2.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["spike2"] = spike2
        altar.addChild(spike2)

        val spike3 = MCAModelRenderer(48, 53, 64f, 64f)
        spike3.addBox(-3.5f, 0.0f, -3.5f, 1f, 1f, 1f)
        spike3.setInitialRotationPoint(0.0f, 7.0f, 0.0f)
        spike3.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["spike3"] = spike3
        altar.addChild(spike3)

        val spike4 = MCAModelRenderer(52, 51, 64f, 64f)
        spike4.addBox(2.5f, 0.0f, -3.5f, 1f, 1f, 1f)
        spike4.setInitialRotationPoint(0.0f, 6.5f, 0.0f)
        spike4.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["spike4"] = spike4
        altar.addChild(spike4)

        val layer1 = MCAModelRenderer(0, 20, 64f, 64f)
        layer1.addBox(-7.5f, 0.0f, -7.5f, 15f, 1f, 15f)
        layer1.setInitialRotationPoint(0.0f, 4.0f, 0.0f)
        layer1.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["layer1"] = layer1
        altar.addChild(layer1)

        val layer2 = MCAModelRenderer(0, 36, 64f, 64f)
        layer2.addBox(-7.0f, 0.0f, -7.0f, 14f, 1f, 14f)
        layer2.setInitialRotationPoint(0.0f, 5.0f, 0.0f)
        layer2.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["layer2"] = layer2
        altar.addChild(layer2)

        val layer3 = MCAModelRenderer(0, 51, 64f, 64f)
        layer3.addBox(-6.0f, 0.0f, -6.0f, 12f, 1f, 12f)
        layer3.setInitialRotationPoint(0.0f, 5.5f, 0.0f)
        layer3.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["layer3"] = layer3
        altar.addChild(layer3)

        val crystal = MCAModelRenderer(48, 56, 64f, 64f)
        crystal.addBox(-0.5f, 0.0f, -0.5f, 1f, 7f, 1f)
        crystal.setInitialRotationPoint(0.0f, 9.0f, 0.0f)
        crystal.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        parts["crystal"] = crystal
        altar.addChild(crystal)

        val crystalBig = MCAModelRenderer(52, 57, 64f, 64f)
        crystalBig.addBox(-1.0f, 0.0f, -1.0f, 2f, 5f, 2f)
        crystalBig.setInitialRotationPoint(0.0f, 1.0f, 0.0f)
        crystalBig.setInitialRotationQuaternion(Quaternion(0.0f, 0.38268346f, 0.0f, 0.9238795f))
        parts["crystalBig"] = crystalBig
        crystal.addChild(crystalBig)
    }

    override fun renderToBuffer(
        matrixStack: PoseStack,
        vertexBuilder: VertexConsumer,
        packedLight: Int,
        packedOverlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        altar.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha)
    }

    fun performAnimations(model: IMCAnimatedModel) {
        model.getAnimationHandler().performAnimationInModel(parts)
    }
}

