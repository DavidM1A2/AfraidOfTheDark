package com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.Utils.getQuat4fFromMatrix
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.Utils.makeFloatBuffer
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.intoArray
import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.isEmptyRotationMatrix
import net.minecraft.client.renderer.GLAllocation
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.model.ModelBase
import net.minecraft.client.renderer.entity.model.ModelRenderer
import org.lwjgl.opengl.GL11
import java.nio.FloatBuffer
import javax.vecmath.Matrix4f
import javax.vecmath.Quat4f
import javax.vecmath.Vector3f

/**
 * This class was provided by the MC animator library and updated to Kotlin
 *
 * @param model The model to render
 * @param boxName The name of the box
 * @param xTextureOffset The x texture offset
 * @param yTextureOffset The y texture offset
 * @property textureOffsetX copy of the private base field
 * @property textureOffsetY copy of the private base field
 * @property compiled copy of the private base field
 * @property displayList copy of the private base field
 * @property rotationMatrix The rotation matrix to use
 * @property prevRotationMatrix The rotation matrix last update
 * @property defaultRotationPointX The default x rotation
 * @property defaultRotationPointY The default y rotation
 * @property defaultRotationPointZ The default z rotation
 * @property defaultRotationMatrix The default rotation matrix
 * @property defaultRotationAsQuat4f The default rotation matrix as a quaternion
 */
class MCAModelRenderer(
    model: ModelBase,
    boxName: String,
    xTextureOffset: Int,
    yTextureOffset: Int
) : ModelRenderer(model, boxName) {
    private var textureOffsetX = 0
    private var textureOffsetY = 0
    private var compiled = false
    private var displayList = 0

    val rotationMatrix = Matrix4f()
    private var prevRotationMatrix = Matrix4f()

    private var defaultRotationPointX = 0f
    private var defaultRotationPointY = 0f
    private var defaultRotationPointZ = 0f

    private var defaultRotationMatrix = Matrix4f()
    var defaultRotationAsQuat4f: Quat4f? = null
        private set

    init {
        setTextureSize(model.textureWidth, model.textureHeight)
        setTextureOffset(xTextureOffset, yTextureOffset)
    }

    /**
     * Sets the texture offset x and y
     *
     * @param x The texture offset x
     * @param y The texture offset y
     * @return The model renderer instance
     */
    override fun setTextureOffset(x: Int, y: Int): ModelRenderer {
        textureOffsetX = x
        textureOffsetY = y
        return this
    }

    /**
     * Adds a box to render
     *
     * @param partName The name of the box
     * @param offsetX The x offset the box is at
     * @param offsetY The y offset the box is at
     * @param offsetZ The z offset the box is at
     * @param width The width of the box
     * @param height The height of the box
     * @param depth The depth of the box
     */
    override fun addBox(
        partName: String,
        offsetX: Float,
        offsetY: Float,
        offsetZ: Float,
        width: Int,
        height: Int,
        depth: Int
    ): ModelRenderer {
        cubeList.add(
            MCAModelBox(
                this, textureOffsetX, textureOffsetY, offsetX, offsetY, offsetZ, width,
                height, depth, 0.0f
            ).setBoxName("$boxName.$partName")
        )
        return this
    }

    /**
     * Adds a box to render
     *
     * @param offsetX The x offset the box is at
     * @param offsetY The y offset the box is at
     * @param offsetZ The z offset the box is at
     * @param width The width of the box
     * @param height The height of the box
     * @param depth The depth of the box
     */
    override fun addBox(
        offsetX: Float,
        offsetY: Float,
        offsetZ: Float,
        width: Int,
        height: Int,
        depth: Int
    ): ModelRenderer {
        cubeList.add(
            MCAModelBox(
                this, textureOffsetX, textureOffsetY, offsetX, offsetY, offsetZ, width,
                height, depth, 0.0f
            )
        )
        return this
    }

    /**
     * Adds a box to render
     *
     * @param offsetX The x offset the box is at
     * @param offsetY The y offset the box is at
     * @param offsetZ The z offset the box is at
     * @param width The width of the box
     * @param height The height of the box
     * @param depth The depth of the box
     * @param scale The scale multiplier of the box
     */
    override fun addBox(
        offsetX: Float,
        offsetY: Float,
        offsetZ: Float,
        width: Int,
        height: Int,
        depth: Int,
        scale: Float
    ) {
        cubeList.add(
            MCAModelBox(
                this, textureOffsetX, textureOffsetY, offsetX, offsetY, offsetZ, width,
                height, depth, scale
            )
        )
    }

    /**
     * Renders this model
     *
     * @param scale The scale to render at
     */
    override fun render(scale: Float) {
        if (!isHidden && showModel) {
            if (!compiled) {
                compileDisplayList(scale)
            }

            GL11.glTranslatef(offsetX, offsetY, offsetZ)
            var i: Int
            if (rotationMatrix.isEmptyRotationMatrix()) {
                if (rotationPointX == 0.0f && rotationPointY == 0.0f && rotationPointZ == 0.0f) {
                    GL11.glCallList(displayList)
                    if (childModels != null) {
                        i = 0
                        while (i < childModels.size) {
                            childModels[i].render(scale)
                            ++i
                        }
                    }
                } else {
                    GL11.glTranslatef(rotationPointX * scale, rotationPointY * scale, rotationPointZ * scale)
                    GL11.glCallList(displayList)
                    if (childModels != null) {
                        i = 0
                        while (i < childModels.size) {
                            childModels[i].render(scale)
                            ++i
                        }
                    }
                    GL11.glTranslatef(-rotationPointX * scale, -rotationPointY * scale, -rotationPointZ * scale)
                }
            } else {
                GL11.glPushMatrix()
                GL11.glTranslatef(rotationPointX * scale, rotationPointY * scale, rotationPointZ * scale)
                val buf = makeFloatBuffer(rotationMatrix.intoArray())
                GL11.glMultMatrixf(buf)
                GL11.glCallList(displayList)
                if (childModels != null) {
                    i = 0
                    while (i < childModels.size) {
                        childModels[i].render(scale)
                        ++i
                    }
                }
                GL11.glPopMatrix()
            }
            GL11.glTranslatef(-offsetX, -offsetY, -offsetZ)
            prevRotationMatrix = rotationMatrix
        }
    }

    /**
     * Unused
     *
     * @param scale The scale to render with
     */
    override fun renderWithRotation(scale: Float) {
        // NOTHING AS WE MUSTN'T USE GL ROTATIONS METHODS
    }

    /**
     * Called after render
     *
     * @param scale The scale to render with
     */
    override fun postRender(scale: Float) {
        if (!isHidden) {
            if (showModel) {
                if (!compiled) {
                    compileDisplayList(scale)
                }
                if (rotationMatrix.equals(prevRotationMatrix)) {
                    if (rotationPointX != 0.0f || rotationPointY != 0.0f || rotationPointZ != 0.0f) {
                        GL11.glTranslatef(rotationPointX * scale, rotationPointY * scale, rotationPointZ * scale)
                    }
                } else {
                    GL11.glTranslatef(rotationPointX * scale, rotationPointY * scale, rotationPointZ * scale)
                    GL11.glMultMatrixf(FloatBuffer.wrap(rotationMatrix.intoArray()))
                }
            }
        }
    }

    /**
     * Set default rotation point (model with no animations) and set the current rotation point.
     *
     * @param x The x coordinate of the rotation point
     * @param y The y coordinate of the rotation point
     * @param z The z coordinate of the rotation point
     */
    fun setInitialRotationPoint(x: Float, y: Float, z: Float) {
        defaultRotationPointX = x
        defaultRotationPointY = y
        defaultRotationPointZ = z
        setRotationPoint(x, y, z)
    }

    /**
     * Set the rotation point
     *
     * @param x The x coordinate of the rotation point
     * @param y The y coordinate of the rotation point
     * @param z The z coordinate of the rotation point
     */
    override fun setRotationPoint(x: Float, y: Float, z: Float) {
        rotationPointX = x
        rotationPointY = y
        rotationPointZ = z
    }

    /**
     * Reset the rotation point to the default values
     */
    fun resetRotationPoint() {
        rotationPointX = defaultRotationPointX
        rotationPointY = defaultRotationPointY
        rotationPointZ = defaultRotationPointZ
    }

    /**
     * Converts the rotation point to a 3D vector
     */
    fun getRotationPointAsVector(): Vector3f {
        return Vector3f(rotationPointX, rotationPointY, rotationPointZ)
    }

    /**
     * Set rotation matrix setting also an initial default value (model with no animations)
     *
     * @param matrix The matrix to use as the initial rotation matrix
     */
    fun setInitialRotationMatrix(matrix: Matrix4f) {
        defaultRotationMatrix = matrix
        setRotationMatrix(matrix)
        defaultRotationAsQuat4f = getQuat4fFromMatrix(rotationMatrix)
    }

    /**
     * Reset the rotation matrix to the default one.
     */
    fun resetRotationMatrix() {
        setRotationMatrix(defaultRotationMatrix)
    }

    /**
     * Set the rotation matrix values based on the given matrix
     *
     * @param matrix The matrix to set
     */
    private fun setRotationMatrix(matrix: Matrix4f) {
        rotationMatrix.set(matrix)
    }

    /**
     * Compiles a GL display list for this model
     *
     * @param scale The scale to render at
     */
    private fun compileDisplayList(scale: Float) {
        displayList = GLAllocation.generateDisplayLists(1)
        GL11.glNewList(displayList, GL11.GL_COMPILE)
        val tessellator = Tessellator.getInstance()
        for (i in cubeList.indices) {
            cubeList[i].render(tessellator.buffer, scale)
        }
        GL11.glEndList()
        compiled = true
    }
}