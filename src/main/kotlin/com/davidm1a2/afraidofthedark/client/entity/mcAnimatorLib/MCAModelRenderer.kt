package com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib

import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.Quaternion
import net.minecraft.client.renderer.Vector3f
import net.minecraft.client.renderer.model.Model
import net.minecraft.client.renderer.model.ModelRenderer
import kotlin.math.asin
import kotlin.math.atan2

class MCAModelRenderer(
    model: Model,
    private val xTextureOffset: Int,
    private val yTextureOffset: Int
) : ModelRenderer(model, xTextureOffset, yTextureOffset) {

    private var defaultRotationPointX = 0f
    private var defaultRotationPointY = 0f
    private var defaultRotationPointZ = 0f

    private val boxQueue: MutableList<Runnable> = mutableListOf()

    val rotation: Quaternion = Quaternion.ONE.copy()
    private val prevRotation: Quaternion = Quaternion.ONE.copy()
    var defaultRotation: Quaternion = Quaternion.ONE.copy()
        private set

    override fun translateRotate(matrixStack: MatrixStack) {
        if (rotation != prevRotation) {
            // See: https://automaticaddison.com/how-to-convert-a-quaternion-into-euler-angles-in-python/
            val term0 = 2.0f * (rotation.w * rotation.x + rotation.y * rotation.z)
            val term1 = 1.0f - 2.0f * (rotation.x * rotation.x + rotation.y * rotation.y)
            rotateAngleX = atan2(term0, term1)

            val term2 = (2.0f * (rotation.w * rotation.y - rotation.z * rotation.x)).coerceIn(-1.0f, 1.0f)
            rotateAngleY = asin(term2)

            val term3 = 2.0f * (rotation.w * rotation.z + rotation.x * rotation.y)
            val term4 = 1.0f - 2.0f * (rotation.y * rotation.y + rotation.z * rotation.z)
            rotateAngleZ = atan2(term3, term4)

            prevRotation.set(rotation.x, rotation.y, rotation.z, rotation.w)
        }
        super.translateRotate(matrixStack)
    }

    fun setInitialRotationPoint(x: Float, y: Float, z: Float) {
        defaultRotationPointX = x
        defaultRotationPointY = y
        defaultRotationPointZ = z
        setRotationPoint(x, y, z)
    }

    fun setInitialRotationQuaternion(quaternion: Quaternion) {
        this.defaultRotation = quaternion
        this.rotation.set(quaternion.x, quaternion.y, quaternion.z, quaternion.w)
    }

    fun resetRotationPoint() {
        setRotationPoint(defaultRotationPointX, defaultRotationPointY, defaultRotationPointZ)
    }

    fun resetRotationQuaternion() {
        this.rotation.set(defaultRotation.x, defaultRotation.y, defaultRotation.z, defaultRotation.w)
    }

    fun getRotationPointAsVector(): Vector3f {
        return Vector3f(rotationPointX, rotationPointY, rotationPointZ)
    }

    override fun setTextureSize(textureWidthIn: Int, textureHeightIn: Int): ModelRenderer {
        setTextureOffset(textureWidthIn - xTextureOffset, textureHeightIn - yTextureOffset)
        val ret = super.setTextureSize(-textureWidthIn, -textureHeightIn)
        boxQueue?.forEach { it.run() }
        boxQueue?.clear()
        return ret
    }

    override fun addBox(x: Float, y: Float, z: Float, width: Float, height: Float, depth: Float): ModelRenderer {
        boxQueue.add {
            super.addBox(x + width, y + height, z + depth, -width, -height, -depth)
        }
        return this
    }
}