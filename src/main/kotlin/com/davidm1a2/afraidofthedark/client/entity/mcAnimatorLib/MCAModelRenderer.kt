package com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Quaternion
import com.mojang.math.Vector3f
import net.minecraft.client.model.geom.ModelPart.Cube

class MCAModelRenderer(
    private val textureOffsetX: Int,
    private val textureOffsetY: Int,
    private val textureWidth: Float,
    private val textureHeight: Float
) {
    private var x: Float = 0f
    private var y: Float = 0f
    private var z: Float = 0f
    private val cubeList: MutableList<Cube> = mutableListOf()
    private val childModels: MutableList<MCAModelRenderer> = mutableListOf()

    private var defaultRotationPointX = 0f
    private var defaultRotationPointY = 0f
    private var defaultRotationPointZ = 0f

    val rotation: Quaternion = Quaternion.ONE.copy()
    val defaultRotation: Quaternion = Quaternion.ONE.copy()

    /**
     * Sets the current box's rotation points and rotation angles to another box.
     */
    fun addChild(renderer: MCAModelRenderer) {
        this.childModels.add(renderer)
    }

    fun addBox(x: Float, y: Float, z: Float, width: Float, height: Float, depth: Float): MCAModelRenderer {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth)
        return this
    }

    private fun addBox(
        texOffX: Int,
        texOffY: Int,
        x: Float,
        y: Float,
        z: Float,
        width: Float,
        height: Float,
        depth: Float
    ) {
        this.cubeList.add(Cube(texOffX, texOffY, x, y, z, width, height, depth, 0f, 0f, 0f, false, this.textureWidth, this.textureHeight))
    }

    fun render(matrixStackIn: PoseStack, bufferIn: VertexConsumer, packedLightIn: Int, packedOverlayIn: Int) {
        this.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, 1.0f, 1.0f, 1.0f, 1.0f)
    }

    fun render(
        matrixStackIn: PoseStack,
        bufferIn: VertexConsumer,
        packedLightIn: Int,
        packedOverlayIn: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        if (this.cubeList.isNotEmpty() || this.childModels.isNotEmpty()) {
            matrixStackIn.pushPose()
            translateAndRotate(matrixStackIn)
            for (modelBox in this.cubeList) {
                modelBox.compile(
                    matrixStackIn.last(),
                    bufferIn,
                    packedLightIn,
                    packedOverlayIn,
                    red,
                    green,
                    blue,
                    alpha
                )
            }
            for (modelrenderer in this.childModels) {
                modelrenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha)
            }
            matrixStackIn.popPose()
        }
    }

    private fun translateAndRotate(matrixStack: PoseStack) {
        matrixStack.translate((this.x / 16.0f).toDouble(), (this.y / 16.0f).toDouble(), (this.z / 16.0f).toDouble())
        matrixStack.mulPose(rotation)
    }

    fun setRotationPoint(rotationPointX: Float, rotationPointY: Float, rotationPointZ: Float) {
        this.x = rotationPointX
        this.y = rotationPointY
        this.z = rotationPointZ
    }

    fun getRotationPointAsVector(): Vector3f {
        return Vector3f(x, y, z)
    }

    fun setInitialRotationPoint(x: Float, y: Float, z: Float) {
        defaultRotationPointX = x
        defaultRotationPointY = y
        defaultRotationPointZ = z
        setRotationPoint(x, y, z)
    }

    fun setInitialRotationQuaternion(quaternion: Quaternion) {
        this.defaultRotation.set(quaternion.i(), quaternion.j(), quaternion.k(), quaternion.r())
        this.rotation.set(quaternion.i(), quaternion.j(), quaternion.k(), quaternion.r())
    }

    fun resetRotationPoint() {
        setRotationPoint(defaultRotationPointX, defaultRotationPointY, defaultRotationPointZ)
    }

    fun resetRotationQuaternion() {
        this.rotation.set(defaultRotation.i(), defaultRotation.j(), defaultRotation.k(), defaultRotation.r())
    }
}