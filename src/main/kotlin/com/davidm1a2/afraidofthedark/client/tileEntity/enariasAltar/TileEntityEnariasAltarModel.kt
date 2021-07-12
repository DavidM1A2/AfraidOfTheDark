package com.davidm1a2.afraidofthedark.client.tileEntity.enariasAltar

import com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib.MCAModelRenderer
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.IMCAnimatedModel
import net.minecraft.client.renderer.model.Model
import javax.vecmath.Matrix4f
import javax.vecmath.Quat4f


/**
 * Model for the enaria's altar
 *
 * @constructor initializes the model parts, this is created from MCAnimator
 * @property parts A map of part name to part
 * @property altar The different parts of the model
 */
class TileEntityEnariasAltarModel internal constructor() : Model() {
    private val parts = mutableMapOf<String, MCAModelRenderer>()
    private val altar: MCAModelRenderer

    init {
        textureWidth = 64
        textureHeight = 64

        altar = MCAModelRenderer(this, "Altar", 0, 0)
        altar.mirror = false
        altar.addBox(-8.0f, 0.0f, -8.0f, 16, 4, 16)
        altar.setInitialRotationPoint(0.0f, 0.0f, 0.0f)
        altar.setInitialRotationQuaternion(Matrix4f().setAndReturn(Quat4f(0.0f, 0.0f, 0.0f, 1.0f)).transposeAndReturn())
        altar.setTextureSize(64, 64)
        parts[altar.boxName] = altar

        val spike1 = MCAModelRenderer(this, "spike1", 48, 51)
        spike1.mirror = false
        spike1.addBox(2.5f, 0.0f, 2.5f, 1, 1, 1)
        spike1.setInitialRotationPoint(0.0f, 6.5f, 0.0f)
        spike1.setInitialRotationQuaternion(Matrix4f().setAndReturn(Quat4f(0.0f, 0.0f, 0.0f, 1.0f)).transposeAndReturn())
        spike1.setTextureSize(64, 64)
        parts[spike1.boxName] = spike1
        altar.addChild(spike1)

        val spike2 = MCAModelRenderer(this, "spike2", 52, 53)
        spike2.mirror = false
        spike2.addBox(-3.5f, 0.0f, 2.5f, 1, 1, 1)
        spike2.setInitialRotationPoint(0.0f, 7.0f, 0.0f)
        spike2.setInitialRotationQuaternion(Matrix4f().setAndReturn(Quat4f(0.0f, 0.0f, 0.0f, 1.0f)).transposeAndReturn())
        spike2.setTextureSize(64, 64)
        parts[spike2.boxName] = spike2
        altar.addChild(spike2)

        val spike3 = MCAModelRenderer(this, "spike3", 48, 53)
        spike3.mirror = false
        spike3.addBox(-3.5f, 0.0f, -3.5f, 1, 1, 1)
        spike3.setInitialRotationPoint(0.0f, 7.0f, 0.0f)
        spike3.setInitialRotationQuaternion(Matrix4f().setAndReturn(Quat4f(0.0f, 0.0f, 0.0f, 1.0f)).transposeAndReturn())
        spike3.setTextureSize(64, 64)
        parts[spike3.boxName] = spike3
        altar.addChild(spike3)

        val spike4 = MCAModelRenderer(this, "spike4", 52, 51)
        spike4.mirror = false
        spike4.addBox(2.5f, 0.0f, -3.5f, 1, 1, 1)
        spike4.setInitialRotationPoint(0.0f, 6.5f, 0.0f)
        spike4.setInitialRotationQuaternion(Matrix4f().setAndReturn(Quat4f(0.0f, 0.0f, 0.0f, 1.0f)).transposeAndReturn())
        spike4.setTextureSize(64, 64)
        parts[spike4.boxName] = spike4
        altar.addChild(spike4)

        val layer1 = MCAModelRenderer(this, "layer1", 0, 20)
        layer1.mirror = false
        layer1.addBox(-7.5f, 0.0f, -7.5f, 15, 1, 15)
        layer1.setInitialRotationPoint(0.0f, 4.0f, 0.0f)
        layer1.setInitialRotationQuaternion(Matrix4f().setAndReturn(Quat4f(0.0f, 0.0f, 0.0f, 1.0f)).transposeAndReturn())
        layer1.setTextureSize(64, 64)
        parts[layer1.boxName] = layer1
        altar.addChild(layer1)

        val layer2 = MCAModelRenderer(this, "layer2", 0, 36)
        layer2.mirror = false
        layer2.addBox(-7.0f, 0.0f, -7.0f, 14, 1, 14)
        layer2.setInitialRotationPoint(0.0f, 5.0f, 0.0f)
        layer2.setInitialRotationQuaternion(Matrix4f().setAndReturn(Quat4f(0.0f, 0.0f, 0.0f, 1.0f)).transposeAndReturn())
        layer2.setTextureSize(64, 64)
        parts[layer2.boxName] = layer2
        altar.addChild(layer2)

        val layer3 = MCAModelRenderer(this, "layer3", 0, 51)
        layer3.mirror = false
        layer3.addBox(-6.0f, 0.0f, -6.0f, 12, 1, 12)
        layer3.setInitialRotationPoint(0.0f, 5.5f, 0.0f)
        layer3.setInitialRotationQuaternion(Matrix4f().setAndReturn(Quat4f(0.0f, 0.0f, 0.0f, 1.0f)).transposeAndReturn())
        layer3.setTextureSize(64, 64)
        parts[layer3.boxName] = layer3
        altar.addChild(layer3)

        val crystal = MCAModelRenderer(this, "crystal", 48, 56)
        crystal.mirror = false
        crystal.addBox(-0.5f, 0.0f, -0.5f, 1, 7, 1)
        crystal.setInitialRotationPoint(0.0f, 9.0f, 0.0f)
        crystal.setInitialRotationQuaternion(Matrix4f().setAndReturn(Quat4f(0.0f, 0.0f, 0.0f, 1.0f)).transposeAndReturn())
        crystal.setTextureSize(64, 64)
        parts[crystal.boxName] = crystal
        altar.addChild(crystal)

        val crystalBig = MCAModelRenderer(this, "crystalBig", 52, 57)
        crystalBig.mirror = false
        crystalBig.addBox(-1.0f, 0.0f, -1.0f, 2, 5, 2)
        crystalBig.setInitialRotationPoint(0.0f, 1.0f, 0.0f)
        crystalBig.setInitialRotationQuaternion(Matrix4f().setAndReturn(Quat4f(0.0f, 0.38268346f, 0.0f, 0.9238795f)).transposeAndReturn())
        crystalBig.setTextureSize(64, 64)
        parts[crystalBig.boxName] = crystalBig
        crystal.addChild(crystalBig)
    }

    /**
     * Renders a tick of the animation for a model
     */
    fun render(model: IMCAnimatedModel, scale: Float) {
        // Perform the animation
        model.getAnimationHandler().performAnimationInModel(parts)

        // Render the model in its current state
        altar.render(scale)
    }
}

