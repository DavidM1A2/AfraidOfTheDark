package com.davidm1a2.afraidofthedark.client.tileEntity.enariasAltar

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.model.Model
import net.minecraft.util.math.vector.Quaternion


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
        texWidth = 64
        texHeight = 64

        altar = MCAModelRenderer(this, 0, 0)
        altar.mirror = false
        altar.addBox(-8.0f, 0.0f, -8.0f, 16f, 4f, 16f)
        altar.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        altar.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        altar.setTexSize(64, 64)
        parts["Altar"] = altar

        val spike1 = MCAModelRenderer(this, 48, 51)
        spike1.mirror = false
        spike1.addBox(2.5f, 0.0f, 2.5f, 1f, 1f, 1f)
        spike1.setInitialRotationPoint(0.0f, 6.5f, 0.0f)
        spike1.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        spike1.setTexSize(64, 64)
        parts["spike1"] = spike1
        altar.addChild(spike1)

        val spike2 = MCAModelRenderer(this, 52, 53)
        spike2.mirror = false
        spike2.addBox(-3.5f, 0.0f, 2.5f, 1f, 1f, 1f)
        spike2.setInitialRotationPoint(0.0f, 7.0f, 0.0f)
        spike2.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        spike2.setTexSize(64, 64)
        parts["spike2"] = spike2
        altar.addChild(spike2)

        val spike3 = MCAModelRenderer(this, 48, 53)
        spike3.mirror = false
        spike3.addBox(-3.5f, 0.0f, -3.5f, 1f, 1f, 1f)
        spike3.setInitialRotationPoint(0.0f, 7.0f, 0.0f)
        spike3.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        spike3.setTexSize(64, 64)
        parts["spike3"] = spike3
        altar.addChild(spike3)

        val spike4 = MCAModelRenderer(this, 52, 51)
        spike4.mirror = false
        spike4.addBox(2.5f, 0.0f, -3.5f, 1f, 1f, 1f)
        spike4.setInitialRotationPoint(0.0f, 6.5f, 0.0f)
        spike4.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        spike4.setTexSize(64, 64)
        parts["spike4"] = spike4
        altar.addChild(spike4)

        val layer1 = MCAModelRenderer(this, 0, 20)
        layer1.mirror = false
        layer1.addBox(-7.5f, 0.0f, -7.5f, 15f, 1f, 15f)
        layer1.setInitialRotationPoint(0.0f, 4.0f, 0.0f)
        layer1.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        layer1.setTexSize(64, 64)
        parts["layer1"] = layer1
        altar.addChild(layer1)

        val layer2 = MCAModelRenderer(this, 0, 36)
        layer2.mirror = false
        layer2.addBox(-7.0f, 0.0f, -7.0f, 14f, 1f, 14f)
        layer2.setInitialRotationPoint(0.0f, 5.0f, 0.0f)
        layer2.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        layer2.setTexSize(64, 64)
        parts["layer2"] = layer2
        altar.addChild(layer2)

        val layer3 = MCAModelRenderer(this, 0, 51)
        layer3.mirror = false
        layer3.addBox(-6.0f, 0.0f, -6.0f, 12f, 1f, 12f)
        layer3.setInitialRotationPoint(0.0f, 5.5f, 0.0f)
        layer3.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        layer3.setTexSize(64, 64)
        parts["layer3"] = layer3
        altar.addChild(layer3)

        val crystal = MCAModelRenderer(this, 48, 56)
        crystal.mirror = false
        crystal.addBox(-0.5f, 0.0f, -0.5f, 1f, 7f, 1f)
        crystal.setInitialRotationPoint(0.0f, 9.0f, 0.0f)
        crystal.setInitialRotationQuaternion(Quaternion(0.0f, 0.0f, 0.0f, 1.0f))
        crystal.setTexSize(64, 64)
        parts["crystal"] = crystal
        altar.addChild(crystal)

        val crystalBig = MCAModelRenderer(this, 52, 57)
        crystalBig.mirror = false
        crystalBig.addBox(-1.0f, 0.0f, -1.0f, 2f, 5f, 2f)
        crystalBig.setInitialRotationPoint(0.0f, 1.0f, 0.0f)
        crystalBig.setInitialRotationQuaternion(Quaternion(0.0f, 0.38268346f, 0.0f, 0.9238795f))
        crystalBig.setTexSize(64, 64)
        parts["crystalBig"] = crystalBig
        crystal.addChild(crystalBig)
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
        altar.render(matrixStack, vertexBuilder, packedLight, packedOverlay, red, green, blue, alpha)
    }

    fun performAnimations(model: IMCAnimatedModel) {
        model.getAnimationHandler().performAnimationInModel(parts)
    }
}

