package com.davidm1a2.afraidofthedark.client.entity.spell

import com.davidm1a2.afraidofthedark.client.entity.LateEntityRenderer
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.entity.spell.SpellWallEntity
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
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.vector.Matrix3f
import net.minecraft.util.math.vector.Matrix4f
import net.minecraft.util.math.vector.Vector3d
import org.lwjgl.opengl.GL11
import java.util.OptionalDouble
import kotlin.math.floor
import kotlin.math.roundToInt

class SpellWallRenderer(renderManager: EntityRendererManager) : EntityRenderer<SpellWallEntity>(renderManager), LateEntityRenderer {
    override fun render(
        spellWall: SpellWallEntity,
        entityYaw: Float,
        partialTicks: Float,
        matrixStack: MatrixStack,
        renderTypeBuffer: IRenderTypeBuffer,
        packedLight: Int
    ) {
        val width = spellWall.width
        val height = spellWall.height
        val color = spellWall.color
        val red = color.red
        val green = color.green
        val blue = color.blue

        val lifespan = spellWall.lifespanTicks.toFloat()
        if (lifespan <= 0) {
            return
        }
        val ticksRemaining = spellWall.tickCount
        val percentRemaining = (ticksRemaining / lifespan).coerceIn(0f..1f)
        val innerAlpha = MathHelper.lerp(percentRemaining, 140f, 0f).roundToInt()
        val outerAlpha = MathHelper.lerp(percentRemaining, 255f, 0f).roundToInt()

        val rotationMatrix = matrixStack.last().pose()
        val normalMatrix = matrixStack.last().normal()

        val halfWidthX = width.x / 2
        val halfWidthY = width.y / 2
        val halfWidthZ = width.z / 2

        val halfHeightX = height.x / 2
        val halfHeightY = height.y / 2
        val halfHeightZ = height.z / 2

        val uTileCount = floor(width.length()).toFloat()
        val vTileCount = floor(height.length()).toFloat()

        val wallBuffer = renderTypeBuffer.getBuffer(WALL_RENDER_TYPE)
        drawQuadVertex(rotationMatrix, normalMatrix, wallBuffer, -halfWidthX + halfHeightX, -halfWidthY + halfHeightY, -halfWidthZ + halfHeightZ, 0f, 0f, red, green, blue, innerAlpha)
        drawQuadVertex(rotationMatrix, normalMatrix, wallBuffer, halfWidthX + halfHeightX, halfWidthY + halfHeightY, halfWidthZ + halfHeightZ, uTileCount, 0f, red, green, blue, innerAlpha)
        drawQuadVertex(rotationMatrix, normalMatrix, wallBuffer, halfWidthX - halfHeightX, halfWidthY - halfHeightY, halfWidthZ - halfHeightZ, uTileCount, vTileCount, red, green, blue, innerAlpha)
        drawQuadVertex(rotationMatrix, normalMatrix, wallBuffer, -halfWidthX - halfHeightX, -halfWidthY - halfHeightY, -halfWidthZ - halfHeightZ, 0f, vTileCount, red, green, blue, innerAlpha)

        val borderBuffer = renderTypeBuffer.getBuffer(WALL_BORDER_RENDER_TYPE)
        drawBorderVertex(rotationMatrix, borderBuffer, -halfWidthX + halfHeightX, -halfWidthY + halfHeightY, -halfWidthZ + halfHeightZ, red, green, blue, outerAlpha)
        drawBorderVertex(rotationMatrix, borderBuffer, halfWidthX + halfHeightX, halfWidthY + halfHeightY, halfWidthZ + halfHeightZ, red, green, blue, outerAlpha)
        drawBorderVertex(rotationMatrix, borderBuffer, halfWidthX - halfHeightX, halfWidthY - halfHeightY, halfWidthZ - halfHeightZ, red, green, blue, outerAlpha)
        drawBorderVertex(rotationMatrix, borderBuffer, -halfWidthX - halfHeightX, -halfWidthY - halfHeightY, -halfWidthZ - halfHeightZ, red, green, blue, outerAlpha)
    }

    private fun drawQuadVertex(
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
            .normal(normalMatrix, 1f, 0f, 0f)
            .endVertex()
    }

    private fun drawBorderVertex(
        rotationMatrix: Matrix4f,
        vertexBuilder: IVertexBuilder,
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

    override fun getRenderOffset(entity: SpellWallEntity, partialTicks: Float): Vector3d {
        return Vector3d.ZERO
    }

    override fun getTextureLocation(entity: SpellWallEntity): ResourceLocation {
        return SPELL_WALL_TEXTURE
    }

    companion object {
        // Ignores block and sky light levels and always renders the same
        private val FULLBRIGHT = LightTexture.pack(15, 15)

        // The texture used by the model
        private val SPELL_WALL_TEXTURE = ResourceLocation(Constants.MOD_ID, "textures/entity/spell/wall.png")

        private val WALL_RENDER_TYPE: RenderType = @Suppress("INACCESSIBLE_TYPE") RenderType.create(
            "spell_wall",
            DefaultVertexFormats.NEW_ENTITY,
            GL11.GL_QUADS,
            256,
            false,
            true,
            RenderType.State.builder()
                .setTextureState(RenderState.TextureState(SPELL_WALL_TEXTURE, false, false))
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
                .setCullState(RenderState.CullState(false))
                .createCompositeState(false)
        )

        private val WALL_BORDER_RENDER_TYPE: RenderType = @Suppress("INACCESSIBLE_TYPE") RenderType.create(
            "spell_wall_border",
            DefaultVertexFormats.POSITION_COLOR,
            GL11.GL_LINE_LOOP,
            256,
            false,
            true,
            RenderType.State.builder()
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
                .setLineState(RenderState.LineState(OptionalDouble.of(3.0)))
                .setCullState(RenderState.CullState(true))
                .createCompositeState(false)
        )
    }
}