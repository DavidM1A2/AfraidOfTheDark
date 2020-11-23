package com.davidm1a2.afraidofthedark.client.particle

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.platform.GlStateManager.DestFactor
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor
import net.minecraft.client.particle.IParticleRenderType
import net.minecraft.client.renderer.BufferBuilder
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.TextureManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ResourceLocation

class AOTDParticleRenderType(private val texture: ResourceLocation) : IParticleRenderType {
    override fun beginRender(bufferBuilder: BufferBuilder, textureManager: TextureManager) {
        RenderHelper.disableStandardItemLighting()
        GlStateManager.depthMask(false)
        textureManager.bindTexture(texture)
        GlStateManager.enableBlend()
        GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA)
        GlStateManager.alphaFunc(516, 0.003921569f)
        bufferBuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP)
    }

    override fun finishRender(tessellator: Tessellator) {
        tessellator.draw()
    }

    override fun toString(): String {
        return "AOTD_PARTICLE"
    }
}