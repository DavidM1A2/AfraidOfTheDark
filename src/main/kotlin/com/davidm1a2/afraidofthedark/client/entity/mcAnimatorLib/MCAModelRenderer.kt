package com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.vertex.IVertexBuilder
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import it.unimi.dsi.fastutil.objects.ObjectList
import net.minecraft.client.renderer.Quaternion
import net.minecraft.client.renderer.Vector3f
import net.minecraft.client.renderer.Vector4f
import net.minecraft.client.renderer.model.Model
import net.minecraft.client.renderer.model.ModelRenderer
import net.minecraft.util.Direction
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import kotlin.math.asin
import kotlin.math.atan2

class MCAModelRenderer(
    private val model: Model,
    private var textureOffsetX: Int,
    private var textureOffsetY: Int
) : ModelRenderer(0, 0, 0, 0) {

    private var textureWidth = model.textureWidth.toFloat()
    private var textureHeight = model.textureHeight.toFloat()
    private var defaultRotationPointX = 0f
    private var defaultRotationPointY = 0f
    private var defaultRotationPointZ = 0f
    private val cubeList: ObjectList<ModelBox> = ObjectArrayList()
    private val childModels: ObjectList<MCAModelRenderer> = ObjectArrayList()

    override fun copyModelAngles(modelRendererIn: ModelRenderer) {
        this.rotateAngleX = modelRendererIn.rotateAngleX
        this.rotateAngleY = modelRendererIn.rotateAngleY
        this.rotateAngleZ = modelRendererIn.rotateAngleZ
        this.rotationPointX = modelRendererIn.rotationPointX
        this.rotationPointY = modelRendererIn.rotationPointY
        this.rotationPointZ = modelRendererIn.rotationPointZ
    }

    /**
     * Sets the current box's rotation points and rotation angles to another box.
     */
    override fun addChild(renderer: ModelRenderer) {
        if (renderer is MCAModelRenderer) this.childModels.add(renderer)
    }

    override fun setTextureOffset(x: Int, y: Int): MCAModelRenderer {
        this.textureOffsetX = x
        this.textureOffsetY = y
        return this
    }

    override fun addBox(partName: String?, x: Float, y: Float, z: Float, width: Int, height: Int, depth: Int, delta: Float, texX: Int, texY: Int): MCAModelRenderer {
        setTextureOffset(texX, texY)
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width.toFloat(), height.toFloat(), depth.toFloat(), delta, delta, delta, this.mirror)
        return this
    }

    override fun addBox(x: Float, y: Float, z: Float, width: Float, height: Float, depth: Float): MCAModelRenderer {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, 0.0f, 0.0f, 0.0f, this.mirror)
        return this
    }

    override fun addBox(x: Float, y: Float, z: Float, width: Float, height: Float, depth: Float, mirrorIn: Boolean): MCAModelRenderer {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, 0.0f, 0.0f, 0.0f, mirrorIn)
        return this
    }

    override fun addBox(x: Float, y: Float, z: Float, width: Float, height: Float, depth: Float, delta: Float) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, delta, delta, delta, this.mirror)
    }

    override fun addBox(x: Float, y: Float, z: Float, width: Float, height: Float, depth: Float, deltaX: Float, deltaY: Float, deltaZ: Float) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, deltaX, deltaY, deltaZ, this.mirror)
    }

    override fun addBox(x: Float, y: Float, z: Float, width: Float, height: Float, depth: Float, delta: Float, mirrorIn: Boolean) {
        this.addBox(this.textureOffsetX, this.textureOffsetY, x, y, z, width, height, depth, delta, delta, delta, mirrorIn)
    }

    private fun addBox(texOffX: Int, texOffY: Int, x: Float, y: Float, z: Float, width: Float, height: Float, depth: Float, deltaX: Float, deltaY: Float, deltaZ: Float, mirrorIn: Boolean) {
        this.cubeList.add(
            ModelBox(texOffX, texOffY, x, y, z, width, height, depth, deltaX, deltaY, deltaZ, mirrorIn, this.textureWidth, this.textureHeight)
        )
    }

    override fun setRotationPoint(rotationPointXIn: Float, rotationPointYIn: Float, rotationPointZIn: Float) {
        this.rotationPointX = rotationPointXIn
        this.rotationPointY = rotationPointYIn
        this.rotationPointZ = rotationPointZIn
    }

    override fun render(matrixStackIn: MatrixStack, bufferIn: IVertexBuilder, packedLightIn: Int, packedOverlayIn: Int) {
        this.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, 1.0f, 1.0f, 1.0f, 1.0f)
    }

    override fun render(matrixStackIn: MatrixStack, bufferIn: IVertexBuilder, packedLightIn: Int, packedOverlayIn: Int, red: Float, green: Float, blue: Float, alpha: Float) {
        if (this.showModel) {
            if (!this.cubeList.isEmpty() || !this.childModels.isEmpty()) {
                matrixStackIn.push()
                translateRotate(matrixStackIn)
                doRender(matrixStackIn.last, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha)
                for (modelrenderer in this.childModels) {
                    modelrenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha)
                }
                matrixStackIn.pop()
            }
        }
    }

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

    private fun doRender(matrixEntryIn: MatrixStack.Entry, bufferIn: IVertexBuilder, packedLightIn: Int, packedOverlayIn: Int, red: Float, green: Float, blue: Float, alpha: Float) {
        val matrix4f = matrixEntryIn.matrix
        val matrix3f = matrixEntryIn.normal
        for (modelBox in this.cubeList) {
            for (quad in modelBox.quads) {
                val vector3f = quad!!.normal.copy()
                vector3f.transform(matrix3f)
                val f = vector3f.x
                val f1 = vector3f.y
                val f2 = vector3f.z
                for (i in 0..3) {
                    val vert = quad.vertexPositions[i]
                    val f3 = vert.position.x / 16.0f
                    val f4 = vert.position.y / 16.0f
                    val f5 = vert.position.z / 16.0f
                    val vector4f = Vector4f(f3, f4, f5, 1.0f)
                    vector4f.transform(matrix4f)
                    bufferIn.addVertex(vector4f.x, vector4f.y, vector4f.z, red, green, blue, alpha, vert.textureU, vert.textureV, packedOverlayIn, packedLightIn, f, f1, f2)
                }
            }
        }
    }

    /**
     * Returns the model renderer with the new texture parameters.
     */
    override fun setTextureSize(textureWidthIn: Int, textureHeightIn: Int): MCAModelRenderer {
        this.textureWidth = textureWidthIn.toFloat()
        this.textureHeight = textureHeightIn.toFloat()
        return this
    }

    fun getRotationPointAsVector(): Vector3f {
        return Vector3f(rotationPointX, rotationPointY, rotationPointZ)
    }

    @OnlyIn(Dist.CLIENT)
    class ModelBox(texOffX: Int, texOffY: Int, private var x: Float, private var y: Float, private var z: Float, width: Float, height: Float, depth: Float, deltaX: Float, deltaY: Float, deltaZ: Float, mirorIn: Boolean, texWidth: Float, texHeight: Float) {
        internal val quads: Array<TexturedQuad?>
        private val posX1: Float
        private val posY1: Float
        private val posZ1: Float
        private val posX2: Float
        private val posY2: Float
        private val posZ2: Float

        init {
            posX1 = x
            posY1 = y
            posZ1 = z
            posX2 = x + width
            posY2 = y + height
            posZ2 = z + depth
            quads = arrayOfNulls(6)
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

            quads[2] = TexturedQuad(
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
            )
            quads[3] = TexturedQuad(
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
            )
            quads[1] = TexturedQuad(
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
            )
            quads[4] = TexturedQuad(
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
            )
            quads[0] = TexturedQuad(
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
            )
            quads[5] = TexturedQuad(
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
        }
    }

    @OnlyIn(Dist.CLIENT)
    internal class PositionTextureVertex(val position: Vector3f, val textureU: Float, val textureV: Float) {
        constructor(x: Float, y: Float, z: Float, texU: Float, texV: Float) : this(Vector3f(x, y, z), texU, texV) {}

        fun setTextureUV(texU: Float, texV: Float): PositionTextureVertex {
            return PositionTextureVertex(position, texU, texV)
        }
    }

    @OnlyIn(Dist.CLIENT)
    internal class TexturedQuad(
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
            val f = 0.0f / texWidth
            val f1 = 0.0f / texHeight
            vertexPositions[0] = vertexPositions[0].setTextureUV(u2 / texWidth - f, v1 / texHeight + f1)
            vertexPositions[1] = vertexPositions[1].setTextureUV(u1 / texWidth + f, v1 / texHeight + f1)
            vertexPositions[2] = vertexPositions[2].setTextureUV(u1 / texWidth + f, v2 / texHeight - f1)
            vertexPositions[3] = vertexPositions[3].setTextureUV(u2 / texWidth - f, v2 / texHeight - f1)
            if (mirrorIn) {
                val i = vertexPositions.size
                for (j in 0 until i / 2) {
                    val vert = vertexPositions[j]
                    vertexPositions[j] = vertexPositions[i - 1 - j]
                    vertexPositions[i - 1 - j] = vert
                }
            }
            normal = directionIn.toVector3f()
            if (mirrorIn) {
                normal.mul(-1.0f, 1.0f, 1.0f)
            }
        }
    }

}