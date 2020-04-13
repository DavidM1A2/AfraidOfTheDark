package com.davidm1a2.afraidofthedark.client.entity.mcAnimatorLib

import net.minecraft.client.model.ModelBox
import net.minecraft.client.model.ModelRenderer
import net.minecraft.client.model.PositionTextureVertex
import net.minecraft.client.model.TexturedQuad
import net.minecraft.client.renderer.BufferBuilder
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

/**
 * This class was provided by the MC animator library and updated to Kotlin
 *
 * @constructor Coords values are set following a right-handed system with no transformations(ie. a default one, with +Z which goes out of the screen)
 * @param renderer The model renderer
 * @param textureX The x coordinate on the texture sheet
 * @param textureY The y coordinate on the texture sheet
 * @param x The x position of the box
 * @param y The y position of the box
 * @param z The z position of the box
 * @param sizeX The x size of the box
 * @param sizeY The y size of the box
 * @param sizeZ The z size of the box
 * @param scale The scale of the box
 * @property quadList Same as superclass, but it was PRIVATE.
 */
class MCAModelBox(
    renderer: ModelRenderer,
    textureX: Int,
    textureY: Int,
    x: Float,
    y: Float,
    z: Float,
    sizeX: Int,
    sizeY: Int,
    sizeZ: Int,
    scale: Float
) : ModelBox(renderer, textureX, textureY, x, y, z, sizeX, sizeY, sizeZ, scale) {
    private val quadList: Array<TexturedQuad>

    init {
        var posX = x
        var posY = y
        var posZ = z

        var endX = posX + sizeX
        var endY = posY + sizeY
        var endZ = posZ + sizeZ

        posX -= scale
        posY -= scale
        posZ -= scale
        endX += scale
        endY += scale
        endZ += scale

        if (renderer.mirror) {
            val tempValueForSwitch = endX
            endX = posX
            posX = tempValueForSwitch
        }

        // Create PTV with RANDOM UV values (0 and 8). They will be set correctly later.
        val ptvBackLeftBottom = PositionTextureVertex(posX, posY, posZ, 0.0f, 0.0f)
        val ptvBackRightBottom = PositionTextureVertex(endX, posY, posZ, 0.0f, 8.0f)
        val ptvBackRightTop = PositionTextureVertex(endX, endY, posZ, 8.0f, 8.0f)
        val ptvBackLeftTop = PositionTextureVertex(posX, endY, posZ, 8.0f, 0.0f)
        val ptvFrontLeftBottom = PositionTextureVertex(posX, posY, endZ, 0.0f, 0.0f)
        val ptvFrontRightBottom = PositionTextureVertex(endX, posY, endZ, 0.0f, 8.0f)
        val ptvFrontRightTop = PositionTextureVertex(endX, endY, endZ, 8.0f, 8.0f)
        val ptvFrontLeftTop = PositionTextureVertex(posX, endY, endZ, 8.0f, 0.0f)

        // Create the TexturedQuads. The constructor of each quad defines the order of the PTV (counterclockwise) and fixes their UV.
        this.quadList = arrayOf(
            // Right quad
            TexturedQuad(
                arrayOf(
                    ptvBackRightTop,
                    ptvFrontRightTop,
                    ptvFrontRightBottom,
                    ptvBackRightBottom
                ),
                textureX + sizeZ + sizeX,
                textureY + sizeZ,
                textureX + sizeZ + sizeX + sizeZ,
                textureY + sizeZ + sizeY,
                renderer.textureWidth,
                renderer.textureHeight
            ),
            // Left quad
            TexturedQuad(
                arrayOf(
                    ptvFrontLeftTop,
                    ptvBackLeftTop,
                    ptvBackLeftBottom,
                    ptvFrontLeftBottom
                ),
                textureX,
                textureY + sizeZ,
                textureX + sizeZ,
                textureY + sizeZ + sizeY,
                renderer.textureWidth,
                renderer.textureHeight
            ),
            // Bottom quad
            TexturedQuad(
                arrayOf(
                    ptvFrontRightBottom,
                    ptvFrontLeftBottom,
                    ptvBackLeftBottom,
                    ptvBackRightBottom
                ),
                textureX + sizeZ + sizeX,
                textureY,
                textureX + sizeZ + sizeX + sizeX,
                textureY + sizeZ,
                renderer.textureWidth,
                renderer.textureHeight
            ),
            // Top quad
            TexturedQuad(
                arrayOf(
                    ptvBackRightTop,
                    ptvBackLeftTop,
                    ptvFrontLeftTop,
                    ptvFrontRightTop
                ),
                textureX + sizeZ,
                textureY,
                textureX + sizeZ + sizeX,
                textureY + sizeZ,
                renderer.textureWidth,
                renderer.textureHeight
            ),
            // Back quad
            TexturedQuad(
                arrayOf(
                    ptvBackLeftTop,
                    ptvBackRightTop,
                    ptvBackRightBottom,
                    ptvBackLeftBottom
                ),
                textureX + sizeZ + sizeX + sizeZ,
                textureY + sizeZ,
                textureX + sizeZ + sizeX + sizeZ + sizeX,
                textureY + sizeZ + sizeY,
                renderer.textureWidth,
                renderer.textureHeight
            ),
            // Front quad
            TexturedQuad(
                arrayOf(
                    ptvFrontRightTop,
                    ptvFrontLeftTop,
                    ptvFrontLeftBottom,
                    ptvFrontRightBottom
                ),
                textureX + sizeZ,
                textureY + sizeZ,
                textureX + sizeZ + sizeX,
                textureY + sizeZ + sizeY,
                renderer.textureWidth,
                renderer.textureHeight
            )
        )

        if (renderer.mirror) {
            this.quadList.forEach { it.flipFace() }
        }
    }

    /**
     * Renders the box
     *
     * @param bufferBuilder The buffer builder to draw with
     * @param scale The scale to draw at
     */
    @SideOnly(Side.CLIENT)
    override fun render(bufferBuilder: BufferBuilder, scale: Float) {
        this.quadList.forEach { it.draw(bufferBuilder, scale) }
    }
}