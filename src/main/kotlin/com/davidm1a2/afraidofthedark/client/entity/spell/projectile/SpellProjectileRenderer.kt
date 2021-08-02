package com.davidm1a2.afraidofthedark.client.entity.spell.projectile

import com.davidm1a2.afraidofthedark.common.entity.spell.projectile.SpellProjectileEntity
import com.mojang.blaze3d.matrix.MatrixStack
import net.minecraft.client.renderer.IRenderTypeBuffer
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererManager
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.vector.Vector3d

/**
 * Renderer class for the spell projectile entity
 *
 * @constructor just passes down fields and the render manager
 * @param renderManager The render manager to pass down
 */
class SpellProjectileRenderer(renderManager: EntityRendererManager) : EntityRenderer<SpellProjectileEntity>(renderManager) {
    override fun render(
        spellProjectile: SpellProjectileEntity,
        entityYaw: Float,
        partialTicks: Float,
        matrixStack: MatrixStack,
        renderTypeBuffer: IRenderTypeBuffer,
        packedLight: Int
    ) {
        spellProjectile.getAnimationHandler().update()
        val rgb = spellProjectile.getColor()
        SPELL_PROJECTILE_MODEL.setupAnim(spellProjectile, 0f, 0f, 0f, 0f, 0f)
        SPELL_PROJECTILE_MODEL.renderToBuffer(
            matrixStack,
            renderTypeBuffer.getBuffer(RENDER_TYPE),
            packedLight,
            OverlayTexture.NO_OVERLAY,
            rgb.red / 255f,
            rgb.green / 255f,
            rgb.blue / 255f,
            1.0f
        )
    }

    override fun getRenderOffset(entity: SpellProjectileEntity, partialTicks: Float): Vector3d {
        return Vector3d(0.0, 0.2, 0.0)
    }

    override fun getTextureLocation(entity: SpellProjectileEntity): ResourceLocation {
        return SPELL_PROJECTILE_TEXTURE
    }

    companion object {
        // The texture used by the model
        private val SPELL_PROJECTILE_TEXTURE = ResourceLocation("afraidofthedark:textures/entity/spell/projectile.png")

        // The spell projectile model
        private val SPELL_PROJECTILE_MODEL = SpellProjectileModel()

        private val RENDER_TYPE = RenderType.entityCutoutNoCull(SPELL_PROJECTILE_TEXTURE)
    }
}
