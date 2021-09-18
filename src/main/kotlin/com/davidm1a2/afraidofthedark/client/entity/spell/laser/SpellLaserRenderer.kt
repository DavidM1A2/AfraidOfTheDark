package com.davidm1a2.afraidofthedark.client.entity.spell.laser

import com.davidm1a2.afraidofthedark.common.entity.spell.laser.SpellLaserEntity
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.RenderState
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Matrix3f
import net.minecraft.util.math.vector.Matrix4f
import net.minecraft.util.math.vector.Vector3d
import org.lwjgl.opengl.GL11C
import kotlin.math.roundToInt

/**
 * Renderer class for the spell laser entity
 *
 * @constructor just passes down fields and the render manager
 * @param renderManager The render manager to pass down
 */
class SpellLaserRenderer(renderManager: EntityRendererManager) : EntityRenderer<SpellLaserEntity>(renderManager) {
    override fun render(
        spellLaser: SpellLaserEntity,
        entityYaw: Float,
        partialTicks: Float,
        matrixStack: MatrixStack,
        renderTypeBuffer: IRenderTypeBuffer,
        packedLight: Int
    ) {
        matrixStack.pushPose()

        val buffer = renderTypeBuffer.getBuffer(RENDER_TYPE)
        val startPos = spellLaser.position()
        val endPos = spellLaser.getEndPos()
        if (startPos == endPos) {
            return
        }
        val segmentLength = getSegmentLength(startPos, endPos)

        matrixStack.popPose()
    }

    private fun getSegmentLength(startPos: Vector3d, endPos: Vector3d): Double {
        val length = startPos.distanceToSqr(endPos)
        val numSegments = (length / STANDARD_TEXTURE_WIDTH_BLOCKS).roundToInt().coerceAtLeast(1)
        return length / numSegments
    }

    private fun drawOneSprite(matrixStack: MatrixStack, buffer: IVertexBuilder, red: Int, green: Int, blue: Int) {
        val rotationMatrix = matrixStack.last().pose()
        val normalMatrix = matrixStack.last().normal()
        drawVertex(rotationMatrix, normalMatrix, buffer, -1, -1, 0, 0f, 0f, red, green, blue)
        drawVertex(rotationMatrix, normalMatrix, buffer, 1, -1, 0, 1f, 0f, red, green, blue)
        drawVertex(rotationMatrix, normalMatrix, buffer, 1, 1, 0, 1f, 1f, red, green, blue)
        drawVertex(rotationMatrix, normalMatrix, buffer, -1, 1, 0, 0f, 1f, red, green, blue)
    }

    private fun drawVertex(
        rotationMatrix: Matrix4f,
        normalMatrix: Matrix3f,
        vertexBuilder: IVertexBuilder,
        x: Int,
        y: Int,
        z: Int,
        u: Float,
        v: Float,
        r: Int,
        g: Int,
        b: Int
    ) {
        vertexBuilder
            .vertex(rotationMatrix, x.toFloat(), y.toFloat(), z.toFloat())
            .color(r, g, b, 255)
            .uv(u, v)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(FULLBRIGHT)
            .normal(normalMatrix, 0f, 0f, 1f)
            .endVertex()
    }

    override fun getRenderOffset(entity: SpellLaserEntity, partialTicks: Float): Vector3d {
        return Vector3d.ZERO
    }

    override fun getTextureLocation(entity: SpellLaserEntity): ResourceLocation {
        return SPELL_LASER_TEXTURE
    }

    companion object {
        private const val STANDARD_TEXTURE_WIDTH_BLOCKS = 2

        // Ignores block and sky light levels and always renders the same
        private val FULLBRIGHT = LightTexture.pack(15, 15)

        // The texture used by the model
        private val SPELL_LASER_TEXTURE = ResourceLocation("afraidofthedark:textures/entity/spell/laser.png")

        private val RENDER_TYPE: RenderType = @Suppress("INACCESSIBLE_TYPE") RenderType.create(
            "spell_laser",
            DefaultVertexFormats.NEW_ENTITY,
            7,
            1024,
            false,
            true,
            RenderType.State.builder()
                .setTextureState(RenderState.TextureState(SPELL_LASER_TEXTURE, false, false))
                .setTransparencyState(RenderState.TransparencyState("translucent_transparency", {
                    RenderSystem.enableBlend()
                    RenderSystem.blendFuncSeparate(
                        GlStateManager.SourceFactor.SRC_ALPHA,
                        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                        GlStateManager.SourceFactor.ONE,
                        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA
                    )
                }) {
                    RenderSystem.disableBlend()
                    RenderSystem.defaultBlendFunc()
                })
                .setWriteMaskState(RenderState.WriteMaskState(true, false))
                .setDepthTestState(RenderState.DepthTestState("<", GL11C.GL_LESS))
                .setCullState(RenderState.CullState(false))
                .createCompositeState(false)
        )
    }
}
