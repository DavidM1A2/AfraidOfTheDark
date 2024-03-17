package com.davidm1a2.afraidofthedark.client.entity.spell

import com.davidm1a2.afraidofthedark.client.entity.LateEntityRenderer
import com.davidm1a2.afraidofthedark.common.entity.spell.SpellConeEntity
import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.math.Matrix3f
import com.mojang.math.Matrix4f
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3
import java.util.*
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class SpellConeRenderer(renderManager: EntityRendererProvider.Context) : EntityRenderer<SpellConeEntity>(renderManager), LateEntityRenderer {
    override fun render(
        spellCone: SpellConeEntity,
        entityYaw: Float,
        partialTicks: Float,
        matrixStack: PoseStack,
        renderTypeBuffer: MultiBufferSource,
        packedLight: Int
    ) {
        val direction = spellCone.direction
        val length = spellCone.length
        val radius = spellCone.radius
        val color = spellCone.color
        val red = color.red
        val green = color.green
        val blue = color.blue

        val lifespan = spellCone.lifespanTicks.toFloat()
        if (lifespan <= 0) {
            return
        }
        val ticksRemaining = spellCone.tickCount
        val percentRemaining = (ticksRemaining / lifespan).coerceIn(0f..1f)
        val coneAlpha = Mth.lerp(percentRemaining, 120f, 0f).roundToInt()
        val borderAlpha = Mth.lerp(percentRemaining, 255f, 0f).roundToInt()

        // Must be a multiple of two, so that drawing quads works
        val numTriangles = 2 * ((10 + radius).roundToInt() / 2)
        val angleIncrement = Math.toRadians(360.0 / numTriangles)
        val baseCenter = direction.scale(length.toDouble())
        var baseLeftRightDir = UP_VECTOR.cross(direction)
        // This means we're looking straight up or down
        if (baseLeftRightDir.length() < 0.00001) {
            baseLeftRightDir = LEFT_VECTOR
        }
        val baseUpDownDir = baseLeftRightDir.cross(direction)

        val rotationMatrix = matrixStack.last().pose()
        val normalMatrix = matrixStack.last().normal()

        val baseVertices = MutableList(numTriangles) {
            val rotation = angleIncrement * it
            baseCenter.add(baseLeftRightDir.scale(cos(rotation)).add(baseUpDownDir.scale(sin(rotation))).scale(radius.toDouble()))
        }
        // Duplicate the starting vertex, so the loop below will add a final quad ending on the starting vertex
        baseVertices.add(baseVertices[0])

        // Draw quads from the point of the cone to three vertices on the base. This will create a sort of diamond shape
        val coneBuffer = renderTypeBuffer.getBuffer(CONE_RENDER_TYPE)
        for (i in 2..numTriangles step 2) {
            val baseVertexOne = baseVertices[i]
            val baseVertexTwo = baseVertices[i - 1]
            val baseVertexThree = baseVertices[i - 2]

            // Draw the "point" vertex of the cone first
            drawConeVertex(rotationMatrix, normalMatrix, coneBuffer, 0.0, 0.0, 0.0, 0f, 0f, red, green, blue, coneAlpha)
            drawConeVertex(rotationMatrix, normalMatrix, coneBuffer, baseVertexOne.x, baseVertexOne.y, baseVertexOne.z, 1f, 0f, red, green, blue, coneAlpha)
            drawConeVertex(rotationMatrix, normalMatrix, coneBuffer, baseVertexTwo.x, baseVertexTwo.y, baseVertexTwo.z, 1f, 1f, red, green, blue, coneAlpha)
            drawConeVertex(rotationMatrix, normalMatrix, coneBuffer, baseVertexThree.x, baseVertexThree.y, baseVertexThree.z, 0f, 1f, red, green, blue, coneAlpha)
        }

        val coneBorderBuffer = renderTypeBuffer.getBuffer(CONE_BORDER_RENDER_TYPE)
        for (i in 0..numTriangles) {
            val baseVertex = baseVertices[i]
            // Draw a point from the tip of the cone to each vertex
            drawBorderVertex(rotationMatrix, coneBorderBuffer, 0.0, 0.0, 0.0, red, green, blue, borderAlpha)
            drawBorderVertex(rotationMatrix, coneBorderBuffer, baseVertex.x, baseVertex.y, baseVertex.z, red, green, blue, borderAlpha)
        }
    }

    private fun drawConeVertex(
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
        b: Int,
        a: Int
    ) {
        vertexBuilder
            .vertex(rotationMatrix, x.toFloat(), y.toFloat(), z.toFloat())
            .color(r, g, b, a)
            .uv(u, v)
            .overlayCoords(OverlayTexture.NO_OVERLAY)
            .uv2(FULLBRIGHT)
            .normal(normalMatrix, 0f, 0f, 1f)
            .endVertex()
    }

    private fun drawBorderVertex(
        rotationMatrix: Matrix4f,
        vertexBuilder: VertexConsumer,
        x: Double,
        y: Double,
        z: Double,
        r: Int,
        g: Int,
        b: Int,
        a: Int
    ) {
        vertexBuilder
            .vertex(rotationMatrix, x.toFloat(), y.toFloat(), z.toFloat())
            .color(r, g, b, a)
            .endVertex()
    }

    override fun getRenderOffset(entity: SpellConeEntity, partialTicks: Float): Vec3 {
        return Vec3.ZERO
    }

    override fun getTextureLocation(entity: SpellConeEntity): ResourceLocation {
        return SPELL_CONE_TEXTURE
    }

    companion object {
        private val UP_VECTOR = Vec3(0.0, 1.0, 0.0)
        private val LEFT_VECTOR = Vec3(1.0, 0.0, 0.0)

        // Ignores block and sky light levels and always renders the same
        private val FULLBRIGHT = LightTexture.pack(15, 15)

        // The texture used by the model
        private val SPELL_CONE_TEXTURE = ResourceLocation("afraidofthedark:textures/entity/spell/cone.png")

        private val CONE_RENDER_TYPE: RenderType = @Suppress("INACCESSIBLE_TYPE") RenderType.create(
            "spell_cone",
            DefaultVertexFormat.NEW_ENTITY,
            VertexFormat.Mode.QUADS,
            256,
            false,
            true,
            RenderType.CompositeState.builder()
                .setTextureState(RenderStateShard.TextureStateShard(SPELL_CONE_TEXTURE, false, false))
                .setTransparencyState(RenderStateShard.TransparencyStateShard("translucent_transparency", {
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
                .setWriteMaskState(RenderStateShard.WriteMaskStateShard(true, false))
                .setCullState(RenderStateShard.CullStateShard(false))
                .createCompositeState(false)
        )

        private val CONE_BORDER_RENDER_TYPE: RenderType = @Suppress("INACCESSIBLE_TYPE") RenderType.create(
            "spell_corner_border",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.LINES,
            256,
            false,
            true,
            RenderType.CompositeState.builder()
                .setTransparencyState(RenderStateShard.TransparencyStateShard("translucent_transparency", {
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
                // TODO: Might break cone rendering
                // .setLineState(RenderStateShard.LineStateShard(OptionalDouble.of(3.0)))
                .setCullState(RenderStateShard.CullStateShard(true))
                .createCompositeState(false)
        )
    }
}