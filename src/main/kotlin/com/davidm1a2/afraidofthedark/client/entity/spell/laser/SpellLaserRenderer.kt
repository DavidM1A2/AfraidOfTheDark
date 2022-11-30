package com.davidm1a2.afraidofthedark.client.entity.spell.laser

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.computeRotationTo
import com.davidm1a2.afraidofthedark.common.entity.spell.laser.SpellLaserEntity
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.IVertexBuilder
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.RenderState
import net.minecraft.client.renderer.RenderState.TransparencyState
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Matrix3f
import net.minecraft.util.math.vector.Matrix4f
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.util.math.vector.Vector3f
import org.lwjgl.opengl.GL11
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

        val color = spellLaser.getColor()
        val red = color.red
        val green = color.green
        val blue = color.blue

        val distance = startPos.distanceTo(endPos)
        val numSegments = (distance / RENDER_WIDTH).roundToInt().coerceAtLeast(1)
        val segmentLength = distance / numSegments

        // Point the laser in the right direction
        val direction = endPos.subtract(startPos).normalize()
        val rotation = BASE_RENDER_DIRECTION.computeRotationTo(direction)
        matrixStack.mulPose(rotation)

        // Rotate/Spin the laser
        val lengthScale = segmentLength / RENDER_WIDTH
        matrixStack.mulPose(Vector3f.XP.rotationDegrees((spellLaser.tickCount + partialTicks) * SPIN_SPEED))
        for (segment in 0 until numSegments) {
            drawSegment(matrixStack, buffer, lengthScale, red, green, blue)
            matrixStack.translate(segmentLength, 0.0, 0.0)
        }

        matrixStack.popPose()
    }

    private fun drawSegment(matrixStack: MatrixStack, buffer: IVertexBuilder, lengthScale: Double, red: Int, green: Int, blue: Int) {
        val rotationMatrix = matrixStack.last().pose()
        val normalMatrix = matrixStack.last().normal()
        val rotationPerSprite = 180f / SPRITE_COUNT
        for (ignored in 0 until SPRITE_COUNT) {
            drawVertex(rotationMatrix, normalMatrix, buffer, 0.0, -RENDER_HEIGHT, 0.0, 0f, 0f, red, green, blue)
            drawVertex(rotationMatrix, normalMatrix, buffer, RENDER_WIDTH * lengthScale, -RENDER_HEIGHT, 0.0, 1f, 0f, red, green, blue)
            drawVertex(rotationMatrix, normalMatrix, buffer, RENDER_WIDTH * lengthScale, RENDER_HEIGHT, 0.0, 1f, 1f, red, green, blue)
            drawVertex(rotationMatrix, normalMatrix, buffer, 0.0, RENDER_HEIGHT, 0.0, 0f, 1f, red, green, blue)
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(rotationPerSprite))
        }
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(180f))
    }

    private fun drawVertex(
        rotationMatrix: Matrix4f,
        normalMatrix: Matrix3f,
        vertexBuilder: IVertexBuilder,
        x: Double,
        y: Double,
        z: Double,
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
        private const val RENDER_WIDTH = 2.0
        private const val RENDER_HEIGHT = 0.03
        private const val SPRITE_COUNT = 2
        private const val SPIN_SPEED = 20.0f

        // Ignores block and sky light levels and always renders the same
        private val FULLBRIGHT = LightTexture.pack(15, 15)

        private val BASE_RENDER_DIRECTION = Vector3d(1.0, 0.0, 0.0)

        // The texture used by the model
        private val SPELL_LASER_TEXTURE = ResourceLocation("afraidofthedark:textures/entity/spell/laser.png")

        private val RENDER_TYPE: RenderType = @Suppress("INACCESSIBLE_TYPE") RenderType.create(
            "spell_laser",
            DefaultVertexFormats.NEW_ENTITY,
            GL11.GL_QUADS,
            1024,
            false,
            true,
            RenderType.State.builder()
                .setTextureState(RenderState.TextureState(SPELL_LASER_TEXTURE, false, false))
                .setTransparencyState(TransparencyState("no_transparency", { RenderSystem.disableBlend() }) {})
                .setCullState(RenderState.CullState(false))
                .setLightmapState(RenderState.LightmapState(false))
                .createCompositeState(true)
        )
    }
}
