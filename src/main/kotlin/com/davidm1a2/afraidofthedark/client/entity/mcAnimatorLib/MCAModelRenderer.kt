package com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Quaternion
import com.mojang.math.Vector3f
import com.mojang.math.Vector4f
import net.minecraft.core.Direction

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


    private class Cube(
        texOffX: Int,
        texOffY: Int,
        private var x: Float,
        private var y: Float,
        private var z: Float,
        width: Float,
        height: Float,
        depth: Float,
        deltaX: Float,
        deltaY: Float,
        deltaZ: Float,
        mirorIn: Boolean,
        texWidth: Float,
        texHeight: Float
    ) {
        private val quads: Array<TexturedQuad>

        init {
            var endX = x + width
            var endY = y + height
            var endZ = z + depth
            x -= deltaX
            y -= deltaY
            z -= deltaZ
            endX += deltaX
            endY += deltaY
            endZ += deltaZ
            if (mirorIn) {
                val temp = endX
                endX = x
                x = temp
            }
            val ptvBLB = PositionTextureVertex(x, y, z, 0.0f, 0.0f)
            val ptvBRB = PositionTextureVertex(endX, y, z, 0.0f, 8.0f)
            val ptvBRT = PositionTextureVertex(endX, endY, z, 8.0f, 8.0f)
            val ptvBLT = PositionTextureVertex(x, endY, z, 8.0f, 0.0f)
            val ptvFLB = PositionTextureVertex(x, y, endZ, 0.0f, 0.0f)
            val ptvFRB = PositionTextureVertex(endX, y, endZ, 0.0f, 8.0f)
            val ptvFRT = PositionTextureVertex(endX, endY, endZ, 8.0f, 8.0f)
            val ptvFLT = PositionTextureVertex(x, endY, endZ, 8.0f, 0.0f)

            quads = arrayOf(
                TexturedQuad(
                    arrayOf(
                        ptvBLT,
                        ptvBRT,
                        ptvBRB,
                        ptvBLB
                    ),
                    texOffX + depth + width + depth,
                    texOffY + depth,
                    texOffX + depth + width + depth + width,
                    texOffY + depth + height,
                    texWidth,
                    texHeight,
                    mirorIn,
                    Direction.NORTH
                ),
                TexturedQuad(
                    arrayOf(
                        ptvFRB,
                        ptvFLB,
                        ptvBLB,
                        ptvBRB
                    ),
                    texOffX + depth + width,
                    texOffY.toFloat(),
                    texOffX + depth + width + width,
                    texOffY + depth,
                    texWidth,
                    texHeight,
                    mirorIn,
                    Direction.DOWN
                ),
                TexturedQuad(
                    arrayOf(
                        ptvBRT,
                        ptvFRT,
                        ptvFRB,
                        ptvBRB
                    ),
                    texOffX + depth + width,
                    texOffY + depth,
                    texOffX + depth + width + depth,
                    texOffY + depth + height,
                    texWidth,
                    texHeight,
                    mirorIn,
                    Direction.EAST
                ),
                TexturedQuad(
                    arrayOf(
                        ptvFLT,
                        ptvBLT,
                        ptvBLB,
                        ptvFLB
                    ),
                    texOffX.toFloat(),
                    texOffY + depth,
                    texOffX + depth,
                    texOffY + depth + height,
                    texWidth,
                    texHeight,
                    mirorIn,
                    Direction.WEST
                ),
                TexturedQuad(
                    arrayOf(
                        ptvBRT,
                        ptvBLT,
                        ptvFLT,
                        ptvFRT
                    ),
                    texOffX + depth,
                    texOffY.toFloat(),
                    texOffX + depth + width,
                    texOffY + depth,
                    texWidth,
                    texHeight,
                    mirorIn,
                    Direction.UP
                ),
                TexturedQuad(
                    arrayOf(
                        ptvFRT,
                        ptvFLT,
                        ptvFLB,
                        ptvFRB
                    ),
                    texOffX + depth,
                    texOffY + depth,
                    texOffX + depth + width,
                    texOffY + depth + height,
                    texWidth,
                    texHeight,
                    mirorIn,
                    Direction.SOUTH
                )
            )
        }

        fun compile(stack: PoseStack.Pose, consumer: VertexConsumer, packedLight: Int, packedOverlay: Int, red: Float, green: Float, blue: Float, alpha: Float) {
            val pose = stack.pose()
            val normal = stack.normal()

            for (quad in this.quads) {
                val copiedNormal = quad.normal.copy()
                copiedNormal.transform(normal)
                val normalX = copiedNormal.x()
                val normalY = copiedNormal.y()
                val normalZ = copiedNormal.z()

                for (vertex in quad.vertexPositions) {
                    val x = vertex.position.x() / 16.0f
                    val y = vertex.position.y() / 16.0f
                    val z = vertex.position.z() / 16.0f
                    val position = Vector4f(x, y, z, 1.0f)
                    position.transform(pose)
                    consumer.vertex(position.x(), position.y(), position.z(), red, green, blue, alpha, vertex.textureU, vertex.textureV, packedOverlay, packedLight, normalX, normalY, normalZ)
                }
            }
        }
    }

    private class PositionTextureVertex(val position: Vector3f, val textureU: Float, val textureV: Float) {
        constructor(x: Float, y: Float, z: Float, texU: Float, texV: Float) : this(Vector3f(x, y, z), texU, texV)

        fun setTextureUV(texU: Float, texV: Float): PositionTextureVertex {
            return PositionTextureVertex(position, texU, texV)
        }
    }

    private class TexturedQuad(
        val vertexPositions: Array<PositionTextureVertex>,
        u1: Float,
        v1: Float,
        u2: Float,
        v2: Float,
        texWidth: Float,
        texHeight: Float,
        mirrorIn: Boolean,
        directionIn: Direction
    ) {
        val normal: Vector3f

        init {
            vertexPositions[0] = vertexPositions[0].setTextureUV(u2 / texWidth, v1 / texHeight)
            vertexPositions[1] = vertexPositions[1].setTextureUV(u1 / texWidth, v1 / texHeight)
            vertexPositions[2] = vertexPositions[2].setTextureUV(u1 / texWidth, v2 / texHeight)
            vertexPositions[3] = vertexPositions[3].setTextureUV(u2 / texWidth, v2 / texHeight)
            if (mirrorIn) {
                val i = vertexPositions.size
                for (j in 0 until i / 2) {
                    val vert = vertexPositions[j]
                    vertexPositions[j] = vertexPositions[i - 1 - j]
                    vertexPositions[i - 1 - j] = vert
                }
            }
            normal = directionIn.step()
            if (mirrorIn) {
                normal.mul(-1.0f, 1.0f, 1.0f)
            }
        }
    }
}