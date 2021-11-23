package com.davidm1a2.afraidofthedark.client.entity.spell.projectile

import com.davidm1a2.afraidofthedark.client.entity.LateEntityRenderer
import com.davidm1a2.afraidofthedark.common.entity.spell.projectile.SpellProjectileEntity
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
import net.minecraft.util.math.vector.Vector3f
import org.lwjgl.opengl.GL11C
import java.util.Random

/**
 * Renderer class for the spell projectile entity
 *
 * @constructor just passes down fields and the render manager
 * @param renderManager The render manager to pass down
 */
class SpellProjectileRenderer(renderManager: EntityRendererManager) : EntityRenderer<SpellProjectileEntity>(renderManager), LateEntityRenderer {
    private val random = Random()

    override fun render(
        spellProjectile: SpellProjectileEntity,
        entityYaw: Float,
        partialTicks: Float,
        matrixStack: MatrixStack,
        renderTypeBuffer: IRenderTypeBuffer,
        packedLight: Int
    ) {
        matrixStack.pushPose()

        matrixStack.translate(0.0, spellProjectile.boundingBox.ysize / 2, 0.0)

        val buffer = renderTypeBuffer.getBuffer(RENDER_TYPE)

        random.setSeed(spellProjectile.id.toLong())
        val tickCount = spellProjectile.tickCount + partialTicks
        val color = spellProjectile.getColor()
        val red = color.red
        val green = color.green
        val blue = color.blue

        setupBaseRotations(matrixStack, tickCount)

        // Setup in-plane rotation
        matrixStack.pushPose()
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(tickCount * IN_PLANE_ROTATION_SPEED))
        // Rotate the plane
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(90f))
        drawOneSprite(matrixStack, buffer, red, green, blue)
        matrixStack.popPose()

        // Setup in-plane rotation
        matrixStack.pushPose()
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(tickCount * IN_PLANE_ROTATION_SPEED))
        // Rotate the plane
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(90f))
        drawOneSprite(matrixStack, buffer, red, green, blue)
        matrixStack.popPose()

        // Setup in-plane rotation
        matrixStack.pushPose()
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(tickCount * IN_PLANE_ROTATION_SPEED))
        // Rotate the plane
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(90f))
        drawOneSprite(matrixStack, buffer, red, green, blue)
        matrixStack.popPose()

        matrixStack.popPose()
    }

    private fun setupBaseRotations(matrixStack: MatrixStack, tickCount: Float) {
        val xRotation = (tickCount + (random.nextGaussian().toFloat() + 1)) * PLANE_ROTATION_SPEED
        val yRotation = (tickCount + (random.nextGaussian().toFloat() + 1)) * PLANE_ROTATION_SPEED
        val zRotation = (tickCount + (random.nextGaussian().toFloat() + 1)) * PLANE_ROTATION_SPEED

        matrixStack.mulPose((if (random.nextBoolean()) Vector3f.XP else Vector3f.XN).rotationDegrees(xRotation))
        matrixStack.mulPose((if (random.nextBoolean()) Vector3f.YP else Vector3f.YN).rotationDegrees(yRotation))
        matrixStack.mulPose((if (random.nextBoolean()) Vector3f.ZP else Vector3f.ZN).rotationDegrees(zRotation))
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

    override fun getRenderOffset(entity: SpellProjectileEntity, partialTicks: Float): Vector3d {
        return Vector3d.ZERO
    }

    override fun getTextureLocation(entity: SpellProjectileEntity): ResourceLocation {
        return SPELL_PROJECTILE_TEXTURE
    }

    companion object {
        private const val IN_PLANE_ROTATION_SPEED = 3
        private const val PLANE_ROTATION_SPEED = 2

        // Ignores block and sky light levels and always renders the same
        private val FULLBRIGHT = LightTexture.pack(15, 15)

        // The texture used by the model
        private val SPELL_PROJECTILE_TEXTURE = ResourceLocation("afraidofthedark:textures/entity/spell/projectile.png")

        private val RENDER_TYPE: RenderType = @Suppress("INACCESSIBLE_TYPE") RenderType.create(
            "spell_projectile",
            DefaultVertexFormats.NEW_ENTITY,
            7,
            1024,
            false,
            true,
            RenderType.State.builder()
                .setTextureState(RenderState.TextureState(SPELL_PROJECTILE_TEXTURE, false, false))
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
