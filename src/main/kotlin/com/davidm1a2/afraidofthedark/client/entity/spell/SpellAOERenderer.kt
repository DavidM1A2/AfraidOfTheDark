package com.davidm1a2.afraidofthedark.client.entity.spell

import com.davidm1a2.afraidofthedark.client.entity.LateEntityRenderer
import com.davidm1a2.afraidofthedark.common.entity.spell.SpellAOEEntity
import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.platform.GlStateManager
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
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.vector.Matrix3f
import net.minecraft.util.math.vector.Matrix4f
import net.minecraft.util.math.vector.Vector3d
import org.lwjgl.opengl.GL11
import kotlin.math.PI
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class SpellAOERenderer(renderManager: EntityRendererManager) : EntityRenderer<SpellAOEEntity>(renderManager), LateEntityRenderer {
    override fun render(
        spellAOE: SpellAOEEntity,
        entityYaw: Float,
        partialTicks: Float,
        matrixStack: MatrixStack,
        renderTypeBuffer: IRenderTypeBuffer,
        packedLight: Int
    ) {
        val buffer = renderTypeBuffer.getBuffer(RENDER_TYPE)

        val radius = spellAOE.radius
        val color = spellAOE.color
        val red = color.red
        val green = color.green
        val blue = color.blue

        val latitudes = ceil(7 + radius / 3).toInt()
        val longitudes = ceil(7 + radius / 3).toInt()

        val rotationMatrix = matrixStack.last().pose()
        val normalMatrix = matrixStack.last().normal()

        val lifespan = spellAOE.lifespanTicks.toFloat()
        if (lifespan <= 0) {
            return
        }
        val ticksRemaining = spellAOE.tickCount
        val alpha = MathHelper.lerp((ticksRemaining / lifespan).coerceIn(0f..1f), 100f, 0f).roundToInt()

        val textureVStep = SPRITE_HEIGHT.toFloat() / TEXTURE_HEIGHT.toFloat()
        val step = ticksRemaining % SPRITE_COUNT
        val startV = step * textureVStep
        val endV = (step + 1) * textureVStep

        matrixStack.pushPose()
        // Algorithm from https://stackoverflow.com/questions/43412525/algorithm-to-draw-a-sphere-using-quadrilaterals
        for (latitude in 1..latitudes) {
            val lat0 = PI * (((latitude - 1) / latitudes.toFloat()) - 0.5f)
            val z0 = radius * sin(lat0)
            val zr0 = cos(lat0)

            val lat1 = PI * ((latitude / latitudes.toFloat()) - 0.5f)
            val z1 = radius * sin(lat1)
            val zr1 = cos(lat1)

            for (longitude in 1..longitudes) {
                val long0 = 2 * PI * ((longitude - 1) / longitudes.toFloat())
                val x0 = radius * cos(long0)
                val y0 = radius * sin(long0)

                val long1 = 2 * PI * (longitude / longitudes.toFloat())
                val x1 = radius * cos(long1)
                val y1 = radius * sin(long1)

                drawVertex(rotationMatrix, normalMatrix, buffer, x0 * zr0, z0, y0 * zr0, 0f, startV, red, green, blue, alpha)
                drawVertex(rotationMatrix, normalMatrix, buffer, x1 * zr0, z0, y1 * zr0, 1f, startV, red, green, blue, alpha)
                drawVertex(rotationMatrix, normalMatrix, buffer, x1 * zr1, z1, y1 * zr1, 1f, endV, red, green, blue, alpha)
                drawVertex(rotationMatrix, normalMatrix, buffer, x0 * zr1, z1, y0 * zr1, 0f, endV, red, green, blue, alpha)
            }
        }

        matrixStack.popPose()
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

    override fun getRenderOffset(entity: SpellAOEEntity, partialTicks: Float): Vector3d {
        return Vector3d.ZERO
    }

    override fun getTextureLocation(entity: SpellAOEEntity): ResourceLocation {
        return SPELL_AOE_TEXTURE
    }

    companion object {
        private const val TEXTURE_HEIGHT = 128
        private const val SPRITE_HEIGHT = 16
        private const val SPRITE_COUNT = TEXTURE_HEIGHT / SPRITE_HEIGHT

        // Ignores block and sky light levels and always renders the same
        private val FULLBRIGHT = LightTexture.pack(15, 15)

        // The texture used by the model
        private val SPELL_AOE_TEXTURE = ResourceLocation("afraidofthedark:textures/entity/spell/aoe.png")

        private val RENDER_TYPE: RenderType = @Suppress("INACCESSIBLE_TYPE") RenderType.create(
            "spell_aoe",
            DefaultVertexFormats.NEW_ENTITY,
            GL11.GL_QUADS,
            256,
            false,
            true,
            RenderType.State.builder()
                .setTextureState(RenderState.TextureState(SPELL_AOE_TEXTURE, false, false))
                .setTransparencyState(TransparencyState("translucent_transparency", {
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
                .setCullState(RenderState.CullState(false))
                .createCompositeState(false)
        )
    }
}