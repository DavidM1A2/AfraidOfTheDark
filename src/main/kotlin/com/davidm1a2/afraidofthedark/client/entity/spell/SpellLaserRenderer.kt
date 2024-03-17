package com.davidm1a2.afraidofthedark.client.entity.spell

import com.davidm1a2.afraidofthedark.common.entity.mcAnimatorLib.computeRotationTo
import com.davidm1a2.afraidofthedark.common.entity.spell.SpellLaserEntity
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.*
import com.mojang.math.Matrix3f
import com.mojang.math.Matrix4f
import com.mojang.math.Vector3f
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec3
import kotlin.math.roundToInt

/**
 * Renderer class for the spell laser entity
 *
 * @constructor just passes down fields and the render manager
 * @param renderManager The render manager to pass down
 */
class SpellLaserRenderer(renderManager: EntityRendererProvider.Context) : EntityRenderer<SpellLaserEntity>(renderManager) {
    override fun render(
        spellLaser: SpellLaserEntity,
        entityYaw: Float,
        partialTicks: Float,
        matrixStack: PoseStack,
        renderTypeBuffer: MultiBufferSource,
        packedLight: Int
    ) {
        val buffer = renderTypeBuffer.getBuffer(RENDER_TYPE)
        val startPos = spellLaser.position()
        val endPos = spellLaser.endPos
        if (startPos == endPos) {
            return
        }

        val color = spellLaser.color
        val red = color.red
        val green = color.green
        val blue = color.blue

        val distance = startPos.distanceTo(endPos)
        val numSegments = (distance / RENDER_WIDTH).roundToInt().coerceAtLeast(1)
        val segmentLength = distance / numSegments

        // Point the laser in the right direction
        val direction = endPos.subtract(startPos).normalize()
        val rotation = BASE_RENDER_DIRECTION.computeRotationTo(direction)
        matrixStack.pushPose()
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

    private fun drawSegment(matrixStack: PoseStack, buffer: VertexConsumer, lengthScale: Double, red: Int, green: Int, blue: Int) {
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
    }

    private fun drawVertex(
        rotationMatrix: Matrix4f,
        normalMatrix: Matrix3f,
        vertexBuilder: VertexConsumer,
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

    override fun getRenderOffset(entity: SpellLaserEntity, partialTicks: Float): Vec3 {
        return Vec3.ZERO
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

        private val BASE_RENDER_DIRECTION = Vec3(1.0, 0.0, 0.0)

        // The texture used by the model
        private val SPELL_LASER_TEXTURE = ResourceLocation("afraidofthedark:textures/entity/spell/laser.png")

        private val RENDER_TYPE: RenderType = @Suppress("INACCESSIBLE_TYPE") RenderType.create(
            "spell_laser",
            DefaultVertexFormat.NEW_ENTITY,
            VertexFormat.Mode.QUADS,
            1024,
            false,
            true,
            RenderType.CompositeState.builder()
                .setTextureState(RenderStateShard.TextureStateShard(SPELL_LASER_TEXTURE, false, false))
                .setTransparencyState(RenderStateShard.TransparencyStateShard("no_transparency", { RenderSystem.disableBlend() }) {})
                .setCullState(RenderStateShard.CullStateShard(false))
                .setLightmapState(RenderStateShard.LightmapStateShard(false))
                .createCompositeState(true)
        )
    }
}
