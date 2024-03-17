package com.davidm1a2.afraidofthedark.client.entity.spell

import com.davidm1a2.afraidofthedark.client.entity.LateEntityRenderer
import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.entity.spell.SpellWallEntity
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
import kotlin.math.floor
import kotlin.math.roundToInt

class SpellWallRenderer(renderManager: EntityRendererProvider.Context) : EntityRenderer<SpellWallEntity>(renderManager), LateEntityRenderer {
    override fun render(
        spellWall: SpellWallEntity,
        entityYaw: Float,
        partialTicks: Float,
        matrixStack: PoseStack,
        renderTypeBuffer: MultiBufferSource,
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
        val innerAlpha = Mth.lerp(percentRemaining, 140f, 0f).roundToInt()
        val outerAlpha = Mth.lerp(percentRemaining, 255f, 0f).roundToInt()

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
            .normal(normalMatrix, 1f, 0f, 0f)
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

    override fun getRenderOffset(entity: SpellWallEntity, partialTicks: Float): Vec3 {
        return Vec3.ZERO
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
            DefaultVertexFormat.NEW_ENTITY,
            VertexFormat.Mode.QUADS,
            256,
            false,
            true,
            RenderType.CompositeState.builder()
                .setTextureState(RenderStateShard.TextureStateShard(SPELL_WALL_TEXTURE, false, false))
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

        private val WALL_BORDER_RENDER_TYPE: RenderType = @Suppress("INACCESSIBLE_TYPE") RenderType.create(
            "spell_wall_border",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.LINE_STRIP,
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
                // TODO: Might break wall rendering
                // .setLineState(RenderStateShard.LineStateShard(OptionalDouble.of(3.0)))
                .setCullState(RenderStateShard.CullStateShard(true))
                .createCompositeState(false)
        )
    }
}